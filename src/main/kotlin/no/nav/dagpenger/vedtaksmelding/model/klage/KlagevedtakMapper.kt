package no.nav.dagpenger.vedtaksmelding.model.klage

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import java.util.UUID

class KlagevedtakMapper(
    vedtakJson: String,
) {
    private val vedtak: JsonNode
    private val objectMapper: ObjectMapper =
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    init {
        vedtak = objectMapper.readTree(vedtakJson)
    }

    fun vedtak(): KlageVedtak =
        KlageVedtak(
            behandlingId = behandlingId,
            opplysninger = vedtakOpplysninger,
            fagsakId = fagsakId,
        )

    private val behandlingId =
        UUID.fromString(vedtak.get("behandlingId").asText())
            ?: throw IllegalArgumentException("behandlingId mangler")

    // TODO: FagsakId må hentes fra vedtakJson
    val fagsakId = "fagsakId"

    private val vedtakOpplysninger: Set<Opplysning> =
        setOf(
            Opplysning(
                opplysningTekstId = KlageOpplysningTyper.KlageMottatDato.opplysningTekstId,
                råVerdi = vedtak.verdi(KlageOpplysningTyper.KlageMottatDato.opplysningNavnId),
                datatype = Opplysning.Datatype.DATO,
            ),
            Opplysning(
                opplysningTekstId = KlageOpplysningTyper.KlageUtfall.opplysningTekstId,
                råVerdi = vedtak.verdi(KlageOpplysningTyper.KlageUtfall.opplysningNavnId, opplysningsset = "utfallOpplysninger"),
                datatype = Opplysning.Datatype.TEKST,
            ),
            Opplysning(
                opplysningTekstId = KlageOpplysningTyper.ErKlagenSkriftelig.opplysningTekstId,
                råVerdi = vedtak.verdi(KlageOpplysningTyper.ErKlagenSkriftelig.opplysningNavnId),
                datatype = Opplysning.Datatype.BOOLSK,
            ),
            Opplysning(
                opplysningTekstId = KlageOpplysningTyper.ErKlagenUnderskrevet.opplysningTekstId,
                råVerdi = vedtak.verdi(KlageOpplysningTyper.ErKlagenUnderskrevet.opplysningNavnId),
                datatype = Opplysning.Datatype.BOOLSK,
            ),
            Opplysning(
                opplysningTekstId = KlageOpplysningTyper.KlagenNevnerEndring.opplysningTekstId,
                råVerdi = vedtak.verdi(KlageOpplysningTyper.KlagenNevnerEndring.opplysningNavnId),
                datatype = Opplysning.Datatype.BOOLSK,
            ),
            Opplysning(
                opplysningTekstId = KlageOpplysningTyper.RettsligKlageinteresse.opplysningTekstId,
                råVerdi = vedtak.verdi(KlageOpplysningTyper.RettsligKlageinteresse.opplysningNavnId),
                datatype = Opplysning.Datatype.BOOLSK,
            ),
        )

    private fun JsonNode.verdi(
        opplysningNavnId: String,
        opplysningsset: String = "behandlingOpplysninger",
    ): String =
        this
            .get(opplysningsset)
            .find {
                it.get("opplysningNavnId").asText() == opplysningNavnId
            }?.get("verdi")
            ?.asText()
            ?: throw IllegalArgumentException("Opplysning med navnId $opplysningNavnId mangler for behandlingId $behandlingId")
}
