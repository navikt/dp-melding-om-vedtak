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
private val sikkerlogger = KotlinLogging.logger("tjenestekall")

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
        return runCatching {
            KlageVedtak(
                behandlingId = behandlingId,
                fagsakId = fagsakId,
                opplysninger = vedtakOpplysninger,
            )
        }.onFailure {
            sikkerlogger.error(it) {
                "Feil ved mapping av klagevedtak for behandlingId $behandlingId. VedtakJson: $vedtak"
            }
        }.getOrThrow()
    }

    private val behandlingId by lazy {
        UUID.fromString(vedtak.get("behandlingId").asText())
            ?: throw IllegalArgumentException("behandlingId mangler")
    }

    // TODO: FagsakId må hentes fra vedtakJson
    val fagsakId = "fagsakId"

    private val vedtakOpplysninger: Set<KlageOpplysning<*, *>> by lazy {
        buildSet {
            vedtak.utfallsverdi(KlageOpplysning.KlageUtfall.opplysningNavnId)?.let {
                add(KlageOpplysning.KlageUtfall(it))
            }

            vedtak.datoVerdi(KlageOpplysning.KlageMottattDato.opplysningNavnId)?.let {
                add(KlageOpplysning.KlageMottattDato(it))
            }

            vedtak.datoVerdi(KlageOpplysning.PåklagetVedtakDato.opplysningNavnId)?.let {
                add(KlageOpplysning.PåklagetVedtakDato(it))
            }

            vedtak.boolskVerdi(KlageOpplysning.ErKlagenSkriftelig.opplysningNavnId)?.let {
                add(KlageOpplysning.ErKlagenSkriftelig(it))
            }
            vedtak.boolskVerdi(KlageOpplysning.ErKlagenUnderskrevet.opplysningNavnId)?.let {
                add(KlageOpplysning.ErKlagenUnderskrevet(it))
            }

            vedtak.boolskVerdi(KlageOpplysning.KlagenNevnerEndring.opplysningNavnId)?.let {
                add(KlageOpplysning.KlagenNevnerEndring(it))
            }
            vedtak.boolskVerdi(KlageOpplysning.KlagefristOppfylt.opplysningNavnId)?.let {
                add(KlageOpplysning.KlagefristOppfylt(it))
            }
            vedtak.boolskVerdi(KlageOpplysning.RettsligKlageinteresse.opplysningNavnId)?.let {
                add(KlageOpplysning.RettsligKlageinteresse(it))
            }
        }
    }

    private fun JsonNode.utfallsverdi(opplysningNavnId: String): String? {
        return this.get("utfallOpplysninger").find {
            it.get("opplysningNavnId").asText() == opplysningNavnId
        }?.get("verdi")?.asText()
    }

    private fun JsonNode.datoVerdi(opplysningNavnId: String): LocalDate? {
        return this.hentVerdiNode(opplysningNavnId)?.let {
            try {
                LocalDate.parse(it.asText())
            } catch (e: Exception) {
                val verdi = it.get("verdi")
                logger.error(e) { "Kan ikke parse $verdi til LocalDate" }
                throw IllegalArgumentException("Kan ikke konvertere $verdi til LocalDate for behandlingId $behandlingId")
            }
        }
    }

    private fun JsonNode.boolskVerdi(opplysningNavnId: String): Boolean? {
        return this.hentVerdiNode(opplysningNavnId)?.let {
            when (it.isBoolean) {
                true -> it.asBoolean()
                false -> {
                    val errorMessage = "Kan ikke konvertere $it til boolean for behandlingId $behandlingId"
                    logger.error { "Kan ikke parse $it til boolean for behandlingId: $behandlingId" }
                    throw IllegalArgumentException(errorMessage)
                }
            }
        }
    }

    private fun JsonNode.hentVerdiNode(opplysningNavnId: String): JsonNode? {
        return this.get("behandlingOpplysninger").find {
            it.get("opplysningNavnId").asText() == opplysningNavnId
        }?.get("verdi")
    }
}
