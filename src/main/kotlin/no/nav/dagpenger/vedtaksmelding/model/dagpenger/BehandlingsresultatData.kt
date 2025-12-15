package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.OpplysningDataException
import java.time.LocalDate
import java.util.UUID

private val logger = KotlinLogging.logger {}

class BehandlingsresultatData(
    json: String,
) {
    companion object {
        private val objectMapper: ObjectMapper =
            jacksonObjectMapper()
                .registerModule(JavaTimeModule())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    private data class Rettighetsperiode(
        val fraOgMed: LocalDate,
        val tilOgMed: LocalDate? = null,
        val harRett: Boolean,
        val opprinnelse: String,
    )

    private val jsonNode = objectMapper.readTree(json)

    val opplysningNoder = jsonNode["opplysninger"]

    private val rettighetsperioder: List<Rettighetsperiode> = rettighetsperioder().sortedBy { it.fraOgMed }

    fun virkningsdato(): LocalDate {
        val nyeRettighetsperioder = rettighetsperioder.filter { it.opprinnelse == "Ny" }
        return when (utfall()) {
            Vedtak.Utfall.AVSLÅTT -> {
                nyeRettighetsperioder
                    .firstOrNull { !it.harRett }
                    ?.fraOgMed
                    ?: throw ManglendeVirkningsdato("Fant ingen rettighetsperiode med harRett = false for avslått vedtak")
            }

            Vedtak.Utfall.INNVILGET -> {
                nyeRettighetsperioder
                    .firstOrNull { it.harRett }
                    ?.fraOgMed
                    ?: throw ManglendeVirkningsdato("Fant ingen rettighetsperiode med harRett = true for innvilget vedtak")
            }
        }
    }

    fun sisteDagMedRett(): LocalDate? {
        val nyeRettighetsperioder = rettighetsperioder.filter { it.opprinnelse == "Ny" }
        return when (utfall()) {
            Vedtak.Utfall.INNVILGET -> {
                nyeRettighetsperioder
                    .firstOrNull { it.harRett }
                    ?.tilOgMed
            }
            else -> null
        }
    }

    private fun rettighetsperioder(): List<Rettighetsperiode> {
        val nodes = jsonNode["rettighetsperioder"]
        return try {
            objectMapper.convertValue(
                nodes,
                object : TypeReference<List<Rettighetsperiode>>() {},
            )
        } catch (e: Exception) {
            logger.error(e) { "Fant ikke rettighetsperioder" }
            throw OpplysningDataException("Fant ikke rettighetsperioder")
        }
    }

    fun flyttall(id: UUID): Double {
        val verdi = verdiNode(id)

        return when (verdi["datatype"].asText() == "desimaltall") {
            true -> {
                verdi["verdi"]
                    .also { require(it.isNumber) { "Forventet at desimaltall har number verdi, men var $it" } }
                    .asDouble()
            }

            false -> throw IllegalArgumentException("Ugyldig verdinode: $verdi")
        }
    }

    fun heltall(id: UUID): Int {
        val verdi = verdiNode(id)
        return when (verdi["datatype"].asText() == "heltall") {
            true -> {
                verdi["verdi"]
                    .also { require(it.isInt) { "Forventet at heltall har int verdi, men var $it" } }
                    .asInt()
            }

            false -> throw IllegalArgumentException("Ugyldig verdinode: $verdi")
        }
    }

    fun penger(id: UUID): Number {
        val verdi = verdiNode(id)

        return when (verdi["datatype"].asText() == "penger") {
            true -> {
                val node = verdi["verdi"]
                require(node.isNumber) { "Forventet at penger har number verdi, men var $node" }
                if (node.isInt) node.asInt() else node.asDouble()
            }

            false -> throw IllegalArgumentException("Ugyldig verdinode: $verdi")
        }
    }

    fun tekst(id: UUID): String {
        val verdi = verdiNode(id)

        return when (verdi["datatype"].asText() == "tekst") {
            true -> {
                verdi["verdi"].also { require(it.isTextual) { "Forventet tekstlig verdi, men var $it" } }.asText()
            }

            false -> throw IllegalArgumentException("Ugyldig verdinode: $verdi")
        }
    }

    fun boolsk(id: UUID): Boolean {
        val verdiNode = verdiNode(id)

        return verdiNode.let { verdi ->
            when (verdi["datatype"].asText() == "boolsk") {
                true -> {
                    verdi["verdi"]
                        .also {
                            require(it.isBoolean) { "Forventet at boolsk har boolsk verdi, men var $it" }
                        }.asBoolean()
                }

                false -> throw IllegalArgumentException("Ugyldig verdinode: $verdi")
            }
        }
    }

    fun dato(id: UUID): LocalDate {
        val verdi = verdiNode(id)
        return when (verdi["datatype"].asText() == "dato") {
            true -> {
                verdi["verdi"]
                    .also { require(it.isDato()) { "Forventet at dato har riktig dato verdi, men var $it" } }
                    .asDato()
            }

            false -> throw IllegalArgumentException("Ugyldig verdinode: $verdi")
        }
    }

    private fun JsonNode.isDato(): Boolean = toDateOrNull() != null

    private fun JsonNode.asDato(): LocalDate = toDateOrNull() ?: throw IllegalArgumentException("Kan ikke konvertere $this til LocalDate")

    private fun JsonNode.toDateOrNull(): LocalDate? =
        try {
            LocalDate.parse(asText())
        } catch (e: Exception) {
            logger.error(e) { "Kan ikke konvertere $this til LocalDate" }
            null
        }

    private fun verdiNode(id: UUID): JsonNode =
        opplysningNoder
            .filter {
                it["opplysningTypeId"].asText() == id.toString()
            }.also {
                if (it.isEmpty()) {
                    throw BehandlingResultatOpplysningIkkeFunnet(id)
                }

                if (it.size > 1) {
                    throw OpplysningDataException("Fant flere enn èn opplysningstype med id $id")
                }
            }.single()
            .let { opplysningNode ->

                opplysningNode["perioder"]
                    .filter {
                        it.periodeInkludererVirkningsdato(virkningsdato())
                    }.also {
                        if (it.isEmpty()) {
                            throw NyPeriodeIkkeFunnet(id)
                        }

                        if (it.size > 1) {
                            throw OpplysningDataException("Fant flere enn èn periode for opplysning med id $id")
                        }
                    }.single()["verdi"]
            }

    private fun JsonNode.periodeInkludererVirkningsdato(virkningsdato: LocalDate): Boolean {
        val fraOgMedNode = this.get("gyldigFraOgMed")
        val tilOgMedNode = this.get("gyldigTilOgMed")

        if (fraOgMedNode == null || fraOgMedNode.isNull) {
            return true
        }

        val fraOgMed = fraOgMedNode.asDato()
        val tilOgMed = if (tilOgMedNode == null || tilOgMedNode.isNull) null else tilOgMedNode.asDato()

        return virkningsdato >= fraOgMed && (tilOgMed == null || virkningsdato <= tilOgMed)
    }

    fun behandlingId(): UUID = jsonNode["behandlingId"].let { UUID.fromString(it.asText()) }

    fun utfall(): Vedtak.Utfall {
        val førteTil = jsonNode["førteTil"].asText()
        return when (førteTil) {
            "Innvilgelse" -> Vedtak.Utfall.INNVILGET
            "Avslag" -> Vedtak.Utfall.AVSLÅTT
            else -> {
                throw UtfallIkkeStøttet(førteTil)
            }
        }
    }

    data class BehandlingResultatOpplysningIkkeFunnet(
        val opplysningId: UUID,
    ) : OpplysningDataException("Fant ikke behandling resultat opplysning med id $opplysningId")

    data class NyPeriodeIkkeFunnet(
        val opplysningId: UUID,
    ) : OpplysningDataException("Fant ikke ny periode for behandling resultat opplysning med id $opplysningId")

    data class UtfallIkkeStøttet(
        val førteTil: String,
    ) : OpplysningDataException("førteTil '$førteTil' er ikke støttet")

    class ManglendeVirkningsdato(
        message: String,
    ) : OpplysningDataException(message)
}
