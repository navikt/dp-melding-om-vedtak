package no.nav.dagpenger.vedtaksmelding.model.innvilgelse

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.AntallStønadsuker
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.AntallStønadsukerSomGisVedOrdinæreDagpenger
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.ErInnvilgetMedVerneplikt
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
        vedtakVernepliktOgInntekt.finnOpplysning(ErInnvilgetMedVerneplikt.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = ErInnvilgetMedVerneplikt.opplysningTekstId,
                råVerdi = true.toString(),
                datatype = BOOLSK,
            )
        vedtakVernepliktOgInntekt.finnOpplysning(AntallStønadsuker.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = AntallStønadsuker.opplysningTekstId,
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
