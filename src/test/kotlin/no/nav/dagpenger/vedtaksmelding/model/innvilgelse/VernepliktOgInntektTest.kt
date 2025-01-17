package no.nav.dagpenger.vedtaksmelding.model.innvilgelse

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.UKER
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper
import org.junit.jupiter.api.Test

class VernepliktOgInntektTest {
    private val resourceRetriever = object {}.javaClass

    private val vedtakVernepliktOgInntekt =
        resourceRetriever.getResource("/json/vedtak-verneplikt-og-inntekt.json")?.let { VedtakMapper(it.readText()).vedtak() }
            ?: throw RuntimeException("Fant ikke ressurs")

    @Test
    fun `Hent relevate opplysninger når både verneplikt og inntektskravet er oppfylt`() {
        vedtakVernepliktOgInntekt.finnOpplysning("opplysning.er-innvilget-med-verneplikt") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.er-innvilget-med-verneplikt",
                verdi = true.toString(),
                datatype = BOOLSK,
            )
        vedtakVernepliktOgInntekt.finnOpplysning("opplysning.antall-stonadsuker") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.antall-stonadsuker",
                verdi = "26",
                datatype = HELTALL,
                enhet = UKER,
            )
        vedtakVernepliktOgInntekt.finnOpplysning("opplysning.antall-stonadsuker-som-gis-ved-ordinare-dagpenger") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.antall-stonadsuker-som-gis-ved-ordinare-dagpenger",
                verdi = "104",
                datatype = HELTALL,
                enhet = UKER,
            )
    }
}
