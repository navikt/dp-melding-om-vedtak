package no.nav.dagpenger.vedtaksmelding.model.innvilgelse

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.AntallStønadsukerSomGisVedOrdinæreDagpenger
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.UKER
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
                råVerdi = true.toString(),
                datatype = BOOLSK,
            )
        vedtakVernepliktOgInntekt.finnOpplysning("opplysning.antall-stonadsuker") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.antall-stonadsuker",
                råVerdi = "26",
                datatype = HELTALL,
                enhet = UKER,
            )
        vedtakVernepliktOgInntekt.finnOpplysning(AntallStønadsukerSomGisVedOrdinæreDagpenger.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = AntallStønadsukerSomGisVedOrdinæreDagpenger.opplysningTekstId,
                råVerdi = "52",
                datatype = HELTALL,
                enhet = UKER,
            )
    }
}
