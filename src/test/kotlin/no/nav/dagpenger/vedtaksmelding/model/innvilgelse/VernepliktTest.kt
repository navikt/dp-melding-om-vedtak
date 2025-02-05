package no.nav.dagpenger.vedtaksmelding.model.innvilgelse

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.AntallGSomGisSomGrunnlagVedVerneplikt
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.AntallStønadsuker
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.AntallStønadsukerSomGisVedOrdinæreDagpenger
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.ErInnvilgetMedVerneplikt
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.KRONER
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.UKER
import org.junit.jupiter.api.Test

class VernepliktTest {
    private val resourceRetriever = object {}.javaClass

    private val vedtakVerneplikt =
        resourceRetriever.getResource("/json/vedtak-verneplikt.json")?.let { VedtakMapper(it.readText()).vedtak() }
            ?: throw RuntimeException("Fant ikke ressurs")

    @Test
    fun `skal hente antall G som gis som grunnlag ved bruk av vernepliktregel`() {
        vedtakVerneplikt.finnOpplysning(AntallGSomGisSomGrunnlagVedVerneplikt.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = AntallGSomGisSomGrunnlagVedVerneplikt.opplysningTekstId,
                råVerdi = "3.0",
                datatype = FLYTTALL,
                enhet = KRONER,
            ).also {
                it.formatertVerdi shouldBe "3 kroner"
            }
    }

    @Test
    fun `Hent opplysning Antall stønadsuker som gis ved ordinære dagpenger`() {
        vedtakVerneplikt.finnOpplysning(AntallStønadsukerSomGisVedOrdinæreDagpenger.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = AntallStønadsukerSomGisVedOrdinæreDagpenger.opplysningTekstId,
                råVerdi = "0",
                datatype = HELTALL,
                enhet = UKER,
            ).also {
                it.formatertVerdi shouldBe "0"
            }
    }

    @Test
    fun `skal hente opplysning om innvilgelse av verneplikt og riktig antall stonadsuker`() {
        vedtakVerneplikt.finnOpplysning(AntallStønadsuker.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = AntallStønadsuker.opplysningTekstId,
                råVerdi = "26",
                datatype = HELTALL,
                enhet = UKER,
            )
        vedtakVerneplikt.finnOpplysning(ErInnvilgetMedVerneplikt.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = ErInnvilgetMedVerneplikt.opplysningTekstId,
                råVerdi = true.toString(),
                datatype = BOOLSK,
            )
    }
}
