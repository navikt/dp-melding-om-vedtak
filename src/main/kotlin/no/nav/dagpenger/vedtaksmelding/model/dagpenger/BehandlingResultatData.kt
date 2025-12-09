package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.Enhet
import no.nav.dagpenger.vedtaksmelding.model.OpplysningDataException
import java.time.LocalDate
import java.util.UUID

private val logger = KotlinLogging.logger {}

class BehandlingResultatData(
    json: String,
) {
    companion object {
        private val objectMapper: ObjectMapper =
            jacksonObjectMapper()
                .registerModule(JavaTimeModule())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    data class RettighetPeriode(
        val fraOgMed: LocalDate,
        val tilOgMed: LocalDate? = null,
        val harRett: Boolean,
    )

    private val jsonNode = objectMapper.readTree(json)

    val opplysningNoder = jsonNode["opplysninger"]

    private val rettighetsPerioder: Set<RettighetPeriode> = hentRettighetsPerioder()

    fun provingsDato(): LocalDate =
        periodeNode(UUID.fromString("0194881f-91d1-7df2-ba1d-4533f37fcc76"))
            .last {
                it["opprinnelse"].asText() == "Ny"
            }["verdi"]["verdi"]
            .asDato()

    fun hentRettighetsPerioder(): Set<RettighetPeriode> {
        val nodes = jsonNode["rettighetsperioder"]
        return try {
            objectMapper.convertValue(
                nodes,
                object : TypeReference<Set<RettighetPeriode>>() {},
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

    fun pengePerioder(id: UUID): List<PeriodisertDagpengerOpplysning.Periode<Enhet.KRONER, Number>> {
        val periodeNode = periodeNode(id)
        val nyePerioder = periodeNode.filter { it["opprinnelse"].asText() == "Ny" }
        return nyePerioder.map {
            PeriodisertDagpengerOpplysning.Periode<Enhet.KRONER, Number>(
                fom = it["gyldigFraOgMed"].asDato(),
                tom = it["gyldigTilOgMed"].toDateOrNull(),
                verdi = it.requireVerdiAvType(Enhet.KRONER),
                enhet = Enhet.KRONER,
            )
        }
    }

    private fun JsonNode.asNumber(): Number =
        when {
            isInt -> asInt()
            isDouble -> asDouble()
            else -> throw IllegalArgumentException("Kan ikke konvertere $this til Number")
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

    fun periodeNode(id: UUID): JsonNode =
        opplysningNoder
            .filter {
                it["opplysningTypeId"].asText() == id.toString()
            }.also {
                if (it.isEmpty()) {
                    throw BehandlingResultatOpplysningIkkeFunnet(id)
                }

                if (it.size > 1) {
                    throw OpplysningDataException("Fant flere enn èn opplysning med id $id")
                }
            }.single()["perioder"]

    private fun verdiNode(id: UUID): JsonNode =
        periodeNode(id)
            .filter {
                it["opprinnelse"].asText() == "Ny"
            }.also {
                if (it.isEmpty()) {
                    throw NyPeriodeIkkeFunnet(id)
                }

                if (it.size > 1) {
                    logger.warn { "Fant flere enn èn ny periode for opplysning med id $id" }
                }
            }.last()["verdi"]

    fun behandlingId(): UUID = jsonNode["behandlingId"].let { UUID.fromString(it.asText()) }

    fun harRett(): Boolean = jsonNode["rettighetsperioder"].first()["harRett"].asBoolean()

    fun utfall(): Vedtak.Utfall {
        val node = jsonNode["førteTil"]
        return when (node.asText()) {
            "Innvilgelse" -> Vedtak.Utfall.INNVILGET
            else -> {
                throw IllegalArgumentException("Kan ikke konvertere $node til Utfall")
            }
        }
    }

    data class BehandlingResultatOpplysningIkkeFunnet(
        val opplysningId: UUID,
    ) : OpplysningDataException("Fant ikke behandling resultat opplysning med id $opplysningId")

    data class NyPeriodeIkkeFunnet(
        val opplysningId: UUID,
    ) : OpplysningDataException("Fant ikke ny periode for behandling resultat opplysning med id $opplysningId")

    @Suppress("UNCHECKED_CAST")
    private fun <V : Any> JsonNode.requireVerdiAvType(enhet: Enhet): V =
        this["verdi"]
            .also {
                require(it.isObject) { "Forventet verdi objekt men var $it" }
            }.let { verdi ->
                val dataType = verdi["datatype"].asText()
                when (enhet) {
                    Enhet.BARN -> TODO()
                    Enhet.ENHETSLØS -> TODO()
                    Enhet.HELTALL -> TODO()
                    Enhet.KRONER -> {
                        require(dataType == "penger") {
                            "Forventet datatype penger men var $verdi"
                        }
                        verdi["verdi"].let {
                            it.asNumber() as V
                        }
                    }

                    Enhet.TIMER -> TODO()
                    Enhet.UKER -> TODO()
                }
            }
}
