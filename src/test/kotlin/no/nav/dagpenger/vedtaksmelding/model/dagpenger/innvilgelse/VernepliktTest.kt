package no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AntallGSomGisSomGrunnlagVedVerneplikt
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AntallStønadsuker
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AntallStønadsukerSomGisVedOrdinæreDagpenger
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.Egenandel
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.ErInnvilgetMedVerneplikt
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ARBEIDSTIDEN_DIN_VERNEPLIKT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE_VERNEPLIKT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_EGENANDEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG_VERNEPLIKT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MED_EGENANDEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ORDINÆR
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_UTBETALING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.KRONER
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.UKER
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class VernepliktTest {
    private val resourceRetriever = object {}.javaClass

    private val vedtakVerneplikt =
        resourceRetriever.getResource("/json/vedtak-verneplikt.json")?.let { VedtakMapper(it.readText()).vedtak() }
            ?: throw RuntimeException("Fant ikke ressurs")

    @Test
    fun `Hent opplysning om antall G som gis som grunnlag ved bruk av vernepliktregel`() {
        vedtakVerneplikt.finnOpplysning(AntallGSomGisSomGrunnlagVedVerneplikt.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = AntallGSomGisSomGrunnlagVedVerneplikt.opplysningTekstId,
                råVerdi = "3.0",
                datatype = FLYTTALL,
            ).also {
                it.formatertVerdi shouldBe "3"
            }
    }

    @Test
    fun `Hent opplysning om antall stønadsuker som gis ved ordinære dagpenger`() {
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
    fun `Hent opplysning som forteller om vedtak er innvilget med vernepliktregel`() {
        vedtakVerneplikt.finnOpplysning(ErInnvilgetMedVerneplikt.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = ErInnvilgetMedVerneplikt.opplysningTekstId,
                råVerdi = true.toString(),
                datatype = BOOLSK,
            )
    }

    @Test
    fun `Hent opplysning om antall stønadsuker`() {
        vedtakVerneplikt.finnOpplysning(AntallStønadsuker.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = AntallStønadsuker.opplysningTekstId,
                råVerdi = "26",
                datatype = HELTALL,
                enhet = UKER,
            ).also {
                it.formatertVerdi shouldBe "26"
            }
    }

    @Test
    fun `Rikig brevblokker for innvilgelse av dagpenger etter verneplikt`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_ORDINÆR.brevblokkId,
                INNVILGELSE_MED_EGENANDEL.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                INNVILGELSE_DAGPENGEPERIODE_VERNEPLIKT.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_GRUNNLAG_VERNEPLIKT.brevblokkId,
                INNVILGELSE_ARBEIDSTIDEN_DIN_VERNEPLIKT.brevblokkId,
                INNVILGELSE_EGENANDEL.brevblokkId,
                INNVILGELSE_MELDEKORT.brevblokkId,
                INNVILGELSE_UTBETALING.brevblokkId,
                INNVILGELSE_SKATTEKORT.brevblokkId,
                INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker

        InnvilgelseMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            Opplysning(ErInnvilgetMedVerneplikt.opplysningTekstId, "true", BOOLSK),
                            Opplysning(Egenandel.opplysningTekstId, "1000.0", HELTALL, KRONER),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }
}
