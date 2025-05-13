package no.nav.dagpenger.vedtaksmelding.model

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.dagpenger.vedtaksmelding.model.vedtak.KlageVedtak
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import java.util.UUID

class KlagevedtakMapper(vedtakJson: String) {
    private val vedtak: JsonNode
    private val objectMapper: ObjectMapper =
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    init {
        vedtak = objectMapper.readTree(vedtakJson)
    }

    fun vedtak(): KlageVedtak {
        return KlageVedtak(
            behandlingId = behandlingId,
            opplysninger = vedtakOpplysninger,
            fagsakId = fagsakId,
        )
    }

    private val behandlingId =
        UUID.fromString(vedtak.get("behandlingId").asText())
            ?: throw IllegalArgumentException("behandlingId mangler")

    // TODO: FagsakId må hentes fra vedtakJson
    val fagsakId = "fagsakId"

    private val vedtakOpplysninger: Set<Opplysning> =
        setOf(
            Opplysning(
                opplysningTekstId = "KLAGEFRIST",
                råVerdi = vedtak.råVerdi("KLAGEFRIST"),
                datatype = Opplysning.Datatype.DATO,
            ),
        )

    private fun JsonNode.råVerdi(opplysningTekstId: String): String {
        return this.get("behandlingOpplysninger").find {
            it.get("opplysningNavnId").asText() == opplysningTekstId
        }?.get("verdi")?.asText()
            ?: throw IllegalArgumentException("Opplysning med tekstId $opplysningTekstId mangler for behandlingId $behandlingId")
    }
}
