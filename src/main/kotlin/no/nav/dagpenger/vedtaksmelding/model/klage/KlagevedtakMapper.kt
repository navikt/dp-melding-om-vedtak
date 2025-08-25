package no.nav.dagpenger.vedtaksmelding.model.klage

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import java.time.LocalDate
import java.util.UUID

private val logger = KotlinLogging.logger {}

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
            fagsakId = fagsakId,
            opplysninger = vedtakOpplysninger,
        )
    }

    private val behandlingId =
        UUID.fromString(vedtak.get("behandlingId").asText())
            ?: throw IllegalArgumentException("behandlingId mangler")

    // TODO: FagsakId må hentes fra vedtakJson
    val fagsakId = "fagsakId"

    private val vedtakOpplysninger: Set<KlageOpplysning<*, *>> =
        setOf(
            KlageOpplysning.KlageMottattDato(
                verdi = vedtak.datoVerdi(KlageOpplysning.KlageMottattDato.opplysningNavnId),
            ),
            KlageOpplysning.PåklagetVedtakDato(
                verdi = vedtak.datoVerdi(KlageOpplysning.PåklagetVedtakDato.opplysningNavnId),
            ),
            KlageOpplysning.KlageUtfall(
                verdi = vedtak.utfallsverdi(KlageOpplysning.KlageUtfall.opplysningNavnId),
            ),
            KlageOpplysning.ErKlagenSkriftelig(
                verdi = vedtak.boolskVerdi(KlageOpplysning.ErKlagenSkriftelig.opplysningNavnId),
            ),
            KlageOpplysning.ErKlagenUnderskrevet(
                verdi = vedtak.boolskVerdi(KlageOpplysning.ErKlagenUnderskrevet.opplysningNavnId),
            ),
            KlageOpplysning.KlagenNevnerEndring(
                verdi = vedtak.boolskVerdi(KlageOpplysning.KlagenNevnerEndring.opplysningNavnId),
            ),
            KlageOpplysning.KlagefristOppfylt(
                verdi = vedtak.boolskVerdi(KlageOpplysning.KlagefristOppfylt.opplysningNavnId),
            ),
            KlageOpplysning.RettsligKlageinteresse(
                verdi = vedtak.boolskVerdi(KlageOpplysning.RettsligKlageinteresse.opplysningNavnId),
            ),
        )

    private fun JsonNode.utfallsverdi(opplysningNavnId: String): String {
        return this.get("utfallOpplysninger").find {
            it.get("opplysningNavnId").asText() == opplysningNavnId
        }?.get("verdi")?.asText()
            ?: throw IllegalArgumentException("Opplysning med navnId $opplysningNavnId mangler for behandlingId $behandlingId")
    }

    private fun JsonNode.datoVerdi(opplysningNavnId: String): LocalDate {
        this.hentVerdiNode(opplysningNavnId).let {
            try {
                return LocalDate.parse(it.get("verdi").asText())
            } catch (e: Exception) {
                val verdi = it.get("verdi")
                logger.error(e) { "Kan ikke parse $verdi til LocalDate" }
                throw IllegalArgumentException("Kan ikke konvertere $verdi til LocalDate for behandlingId $behandlingId")
            }
        }
    }

    private fun JsonNode.boolskVerdi(opplysningNavnId: String): Boolean {
        this.hentVerdiNode(opplysningNavnId).let {
            try {
                return it.get("verdi").asBoolean()
            } catch (e: Exception) {
                val verdi = it.get("verdi")
                logger.error(e) { "Kan ikke parse $verdi til boolean" }
                throw IllegalArgumentException("Kan ikke konvertere $verdi til boolean for behandlingId $behandlingId")
            }
        }
    }

    private fun JsonNode.hentVerdiNode(opplysningNavnId: String): JsonNode {
        return this.get("behandlingOpplysninger").find {
            it.get("opplysningNavnId").asText() == opplysningNavnId
        }
            ?: throw IllegalArgumentException("Opplysning med navnId $opplysningNavnId mangler for behandlingId $behandlingId")
    }
}
