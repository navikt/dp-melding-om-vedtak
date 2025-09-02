package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import java.time.LocalDate
import java.util.UUID

private val logger = KotlinLogging.logger {}

open class OpplysningDataException(message: String) : RuntimeException(message)

class BehandlingResultatData(json: String) {
    companion object {
        private val objectMapper: ObjectMapper =
            jacksonObjectMapper()
                .registerModule(JavaTimeModule())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    private data class RettighetPeriode(
        val fraOgMed: LocalDate,
        val tilOgMed: LocalDate? = null,
        val harRett: Boolean,
    )

    private val jsonNode = objectMapper.readTree(json)

    val opplysningNoder = jsonNode["opplysninger"]

    private val rettighetsPerioder: Set<RettighetPeriode> = hentRettighetsPerioder()

    fun provingsDato(): LocalDate {
        return rettighetsPerioder.singleOrNull()?.fraOgMed
            ?: throw OpplysningDataException("Kunne ikke finne en og bare en rettighetsperiode")
    }

    private fun hentRettighetsPerioder(): Set<RettighetPeriode> {
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
                verdi["verdi"].also { require(it.isNumber) { "Forventet at desimaltall har  number verdi, men var $it" } }
                    .asDouble()
            }

            false -> throw IllegalArgumentException("Ugyldig verdinode: $verdi")
        }
    }

    fun heltall(id: UUID): Int {
        val verdi = verdiNode(id)
        return when (verdi["datatype"].asText() == "heltall") {
            true -> {
                verdi["verdi"].also { require(it.isInt) { "Forventet at heltall har int verdi, men var $it" } }
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
                verdi["verdi"].also { require(it.isTextual) { "Forventet at tekst  verdi, men var $it" } }.asText()
            }

            false -> throw IllegalArgumentException("Ugyldig verdinode: $verdi")
        }
    }

    fun boolsk(id: UUID): Boolean {
        return verdiNode(id).let { verdi ->
            when (verdi.get("datatype").asText() == "boolsk") {
                true -> {
                    verdi.get("verdi")
                        .also { require(it.isBoolean) { "Forventet at boolsk har  boolsk verdi, men var $it" } }
                        ?.asBoolean()
                }

                false -> throw IllegalArgumentException("Ugyldig verdinode: $verdi")
            }
        } ?: throw IllegalArgumentException("Fant ingen opplysning med id $id")
    }

    fun dato(id: UUID): LocalDate {
        val verdi = verdiNode(id)
        return when (verdi["datatype"].asText() == "dato") {
            true -> {
                verdi["verdi"].also { require(it.isDato()) { "Forventet at dato har riktig  dato verdi, men var $it" } }
                    .asDato()
            }

            false -> throw IllegalArgumentException("Ugyldig verdinode: $verdi")
        }
    }

    private fun JsonNode.isDato(): Boolean = toDateOrNull() != null

    private fun JsonNode.asDato(): LocalDate =
        toDateOrNull() ?: throw IllegalArgumentException("Kan ikke konvertere $this til LocalDate")

    private fun JsonNode.toDateOrNull(): LocalDate? {
        return try {
            LocalDate.parse(asText())
        } catch (e: Exception) {
            logger.error(e) { "Kan ikke konvertere $this til LocalDate" }
            null
        }
    }

    private fun verdiNode(id: UUID): JsonNode =
        opplysningNoder.firstOrNull { it["opplysningTypeId"].asText() == id.toString() }?.let { opplysningNode ->
            opplysningNode["perioder"].singleOrNull {
                it["status"].asText() == "Ny"
            }?.let { it["verdi"] } ?: throw OpplysningDataException("Fant ingen ny periode for opplysning med id $id")
        }
            ?: throw OpplysningIkkeFunnet(id)

    fun behandlingId(): UUID = jsonNode["behandlingId"].let { UUID.fromString(it.asText()) }

    fun harRett(): Boolean {
        return jsonNode["rettighetsperioder"].first()["harRett"].asBoolean()
    }

    data class OpplysningIkkeFunnet(val opplysningId: UUID) :
        OpplysningDataException("Fant ikke opplysning med id $opplysningId") :
}
