package no.nav.dagpenger.vedtaksmelding.model.innvilgelse

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.ForeldrepengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.HarSamordnet
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.OmsorgspengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.OpplæringspengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.PleiepengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.SvangerskapspengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.SykepengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.UføreDagsats
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ARBEIDSTIDEN_DIN
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_EGENANDEL
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_FORELDREPENGER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_GENERISK
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_OMSORGSPENGER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_OPPLÆRINGSPENGER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_PLEIEPENGER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_SVANGERSKAPSPENGER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_SYKEPENGER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_UFØRE
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_UTBETALING
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak.Utfall
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class SamordningTest {
    private fun samordnetOpplysning(verdi: String = "true") =
        Opplysning(
            opplysningTekstId = HarSamordnet.opplysningTekstId,
            råVerdi = verdi,
            datatype = BOOLSK,
        )

    private fun samordnetYtelseDagsats(
        opplysningTekstId: String,
        dagsats: Int = 100,
    ) = Opplysning(
        opplysningTekstId = opplysningTekstId,
        råVerdi = dagsats.toString(),
        datatype = BOOLSK,
    )

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak med samordning av sykepenger`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_INNLEDNING.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                INNVILGELSE_DAGPENGEPERIODE.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_SAMORDNET_SYKEPENGER.brevblokkId,
                INNVILGELSE_GRUNNLAG.brevblokkId,
                INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId,
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
                            samordnetOpplysning(), samordnetYtelseDagsats(SykepengerDagsats.opplysningTekstId),
                        ),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak med samordning av pleiepenger`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_INNLEDNING.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                INNVILGELSE_DAGPENGEPERIODE.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_SAMORDNET_PLEIEPENGER.brevblokkId,
                INNVILGELSE_GRUNNLAG.brevblokkId,
                INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId,
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
                            samordnetOpplysning(), samordnetYtelseDagsats(PleiepengerDagsats.opplysningTekstId),
                        ),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak med samordning av omsorgspenger`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_INNLEDNING.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                INNVILGELSE_DAGPENGEPERIODE.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_SAMORDNET_OMSORGSPENGER.brevblokkId,
                INNVILGELSE_GRUNNLAG.brevblokkId,
                INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId,
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
                            samordnetOpplysning(), samordnetYtelseDagsats(OmsorgspengerDagsats.opplysningTekstId),
                        ),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak med samordning av opplæringspenger`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_INNLEDNING.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                INNVILGELSE_DAGPENGEPERIODE.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_SAMORDNET_OPPLÆRINGSPENGER.brevblokkId,
                INNVILGELSE_GRUNNLAG.brevblokkId,
                INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId,
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
                            samordnetOpplysning(), samordnetYtelseDagsats(OpplæringspengerDagsats.opplysningTekstId),
                        ),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak med samordning av uføre`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_INNLEDNING.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                INNVILGELSE_DAGPENGEPERIODE.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_SAMORDNET_UFØRE.brevblokkId,
                INNVILGELSE_GRUNNLAG.brevblokkId,
                INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId,
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
                            samordnetOpplysning(), samordnetYtelseDagsats(UføreDagsats.opplysningTekstId),
                        ),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak med samordning av foreldrepenger`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_INNLEDNING.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                INNVILGELSE_DAGPENGEPERIODE.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_SAMORDNET_FORELDREPENGER.brevblokkId,
                INNVILGELSE_GRUNNLAG.brevblokkId,
                INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId,
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
                            samordnetOpplysning(), samordnetYtelseDagsats(ForeldrepengerDagsats.opplysningTekstId),
                        ),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak som har samordning av svangerskapspenger`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_INNLEDNING.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                INNVILGELSE_DAGPENGEPERIODE.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_SAMORDNET_SVANGERSKAPSPENGER.brevblokkId,
                INNVILGELSE_GRUNNLAG.brevblokkId,
                INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId,
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
                            samordnetOpplysning(),
                            samordnetYtelseDagsats(SvangerskapspengerDagsats.opplysningTekstId),
                        ),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak som har samordning med flere ytelser`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_INNLEDNING.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                INNVILGELSE_DAGPENGEPERIODE.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_SAMORDNET_GENERISK.brevblokkId,
                INNVILGELSE_GRUNNLAG.brevblokkId,
                INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId,
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
                            samordnetOpplysning(),
                            samordnetYtelseDagsats(SykepengerDagsats.opplysningTekstId),
                            samordnetYtelseDagsats(ForeldrepengerDagsats.opplysningTekstId),
                            samordnetYtelseDagsats(SvangerskapspengerDagsats.opplysningTekstId),
                        ),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak som har samordning med ukjent ytelse`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_INNLEDNING.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                INNVILGELSE_DAGPENGEPERIODE.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_SAMORDNET_GENERISK.brevblokkId,
                INNVILGELSE_GRUNNLAG.brevblokkId,
                INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId,
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
                    opplysninger = setOf(samordnetOpplysning()),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }
}
