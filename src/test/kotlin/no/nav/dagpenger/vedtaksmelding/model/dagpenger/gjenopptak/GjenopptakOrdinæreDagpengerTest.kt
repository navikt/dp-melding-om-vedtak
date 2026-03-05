package no.nav.dagpenger.vedtaksmelding.model.dagpenger.gjenopptak

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.FasteBrevblokker.SPØRSMÅL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.ManglerBrevstøtte
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_ARBEIDSTIDEN_DIN
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_DAGPENGEPERIODE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_3
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_DAGPENGEPERIODE_UTEN_FORBRUK
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_EGENANDEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_EGENANDEL_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_REBEREGNING_IKKE_RETT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_REBEREGNING_UTFØRT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_BARNETILLEGG
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MED_EGENANDEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_NITTI_PROSENT_REGEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ORDINÆR
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ORDINÆR_FOM_TOM
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_PERMITTERT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_PERMITTERT_FISK
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_UTBETALING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT_FISK
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test
import java.time.LocalDate

class GjenopptakOrdinæreDagpengerTest {
    private val behandlingId = UUIDv7.ny()

    @Test
    fun `Kriterier for å lage gjenopptak-brev`() {
        shouldThrow<ManglerBrevstøtte> {
            GjenopptakMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.AVSLÅTT,
                        opplysninger = emptySet(),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldThrow<ManglerBrevstøtte> {
            GjenopptakMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.INNVILGET,
                        opplysninger = emptySet(),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldNotThrowAny {
            GjenopptakMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.GJENOPPTAK,
                        opplysninger = emptySet(),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
    }

    @Test
    fun `Riktige brevblokker når det ikke er reberegnet grunnlag og ikke forbrukt av dagpengeperioden`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_ORDINÆR.brevblokkId,
                GJENOPPTAK_INNLEDNING.brevblokkId,
                INNVILGELSE_MED_EGENANDEL.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE_UTEN_FORBRUK.brevblokkId,
                GJENOPPTAK_REBEREGNING_IKKE_RETT.brevblokkId,
                GJENOPPTAK_ARBEIDSTIDEN_DIN.brevblokkId,
                INNVILGELSE_MELDEKORT.brevblokkId,
                INNVILGELSE_UTBETALING.brevblokkId,
                INNVILGELSE_SKATTEKORT.brevblokkId,
                INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
        GjenopptakMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.GJENOPPTAK,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.Egenandel(3000),
                            DagpengerOpplysning.GrunnlagErReberegnet(verdi = false),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Riktige brevblokker når det ikke er reberegnet grunnlag og ikke forbrukt av dagpengeperioden og til-dato har verdi`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_ORDINÆR_FOM_TOM.brevblokkId,
                GJENOPPTAK_INNLEDNING.brevblokkId,
                INNVILGELSE_MED_EGENANDEL.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_1.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_2.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_3.brevblokkId,
                GJENOPPTAK_REBEREGNING_IKKE_RETT.brevblokkId,
                GJENOPPTAK_ARBEIDSTIDEN_DIN.brevblokkId,
                INNVILGELSE_MELDEKORT.brevblokkId,
                INNVILGELSE_UTBETALING.brevblokkId,
                INNVILGELSE_SKATTEKORT.brevblokkId,
                INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
        GjenopptakMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.GJENOPPTAK,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.Egenandel(3000),
                            DagpengerOpplysning.GrunnlagErReberegnet(verdi = false),
                            DagpengerOpplysning.SisteDagMedRett(LocalDate.of(2026, 7, 10)),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Riktige brevblokker når det ikke er reberegnet grunnlag og forbruk av periode`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_ORDINÆR.brevblokkId,
                GJENOPPTAK_INNLEDNING.brevblokkId,
                GJENOPPTAK_EGENANDEL_INNLEDNING.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE.brevblokkId,
                GJENOPPTAK_REBEREGNING_IKKE_RETT.brevblokkId,
                GJENOPPTAK_ARBEIDSTIDEN_DIN.brevblokkId,
                GJENOPPTAK_EGENANDEL.brevblokkId,
                INNVILGELSE_MELDEKORT.brevblokkId,
                INNVILGELSE_UTBETALING.brevblokkId,
                INNVILGELSE_SKATTEKORT.brevblokkId,
                INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
        GjenopptakMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.GJENOPPTAK,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.Egenandel(verdi = 3000),
                            DagpengerOpplysning.EgenandelGjenstående(verdi = 2000),
                            DagpengerOpplysning.GrunnlagErReberegnet(verdi = false),
                            DagpengerOpplysning.AntallStønadsdagerSomGjenstår(verdi = 259),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Riktige brevblokker når det ikke er reberegnet grunnlag og forbruk av periode - permittert`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_PERMITTERT.brevblokkId,
                GJENOPPTAK_INNLEDNING.brevblokkId,
                GJENOPPTAK_EGENANDEL_INNLEDNING.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE.brevblokkId,
                GJENOPPTAK_REBEREGNING_IKKE_RETT.brevblokkId,
                GJENOPPTAK_ARBEIDSTIDEN_DIN.brevblokkId,
                GJENOPPTAK_EGENANDEL.brevblokkId,
                INNVILGELSE_MELDEKORT.brevblokkId,
                INNVILGELSE_UTBETALING.brevblokkId,
                INNVILGELSE_SKATTEKORT.brevblokkId,
                INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
        GjenopptakMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.GJENOPPTAK,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.Egenandel(verdi = 3000),
                            DagpengerOpplysning.EgenandelGjenstående(verdi = 2000),
                            DagpengerOpplysning.GrunnlagErReberegnet(verdi = false),
                            DagpengerOpplysning.AntallStønadsdagerSomGjenstår(verdi = 259),
                            DagpengerOpplysning.OppfyllerKravetTilPermittering(verdi = true),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Riktige brevblokker når det ikke er reberegnet grunnlag og forbruk av periode - permittert fisk`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_PERMITTERT_FISK.brevblokkId,
                GJENOPPTAK_INNLEDNING.brevblokkId,
                GJENOPPTAK_EGENANDEL_INNLEDNING.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT_FISK.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE.brevblokkId,
                GJENOPPTAK_REBEREGNING_IKKE_RETT.brevblokkId,
                GJENOPPTAK_ARBEIDSTIDEN_DIN.brevblokkId,
                GJENOPPTAK_EGENANDEL.brevblokkId,
                INNVILGELSE_MELDEKORT.brevblokkId,
                INNVILGELSE_UTBETALING.brevblokkId,
                INNVILGELSE_SKATTEKORT.brevblokkId,
                INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
        GjenopptakMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.GJENOPPTAK,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.Egenandel(verdi = 3000),
                            DagpengerOpplysning.EgenandelGjenstående(verdi = 2000),
                            DagpengerOpplysning.GrunnlagErReberegnet(verdi = false),
                            DagpengerOpplysning.AntallStønadsdagerSomGjenstår(verdi = 259),
                            DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri(verdi = true),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Riktige brevblokker ved reberegnet grunnlag og forbruk av periode`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_ORDINÆR.brevblokkId,
                GJENOPPTAK_INNLEDNING.brevblokkId,
                GJENOPPTAK_EGENANDEL_INNLEDNING.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE.brevblokkId,
                GJENOPPTAK_REBEREGNING_UTFØRT.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_GRUNNLAG.brevblokkId,
                GJENOPPTAK_ARBEIDSTIDEN_DIN.brevblokkId,
                GJENOPPTAK_EGENANDEL.brevblokkId,
                INNVILGELSE_MELDEKORT.brevblokkId,
                INNVILGELSE_UTBETALING.brevblokkId,
                INNVILGELSE_SKATTEKORT.brevblokkId,
                INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
        GjenopptakMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.GJENOPPTAK,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.Egenandel(verdi = 3000),
                            DagpengerOpplysning.EgenandelGjenstående(verdi = 2000),
                            DagpengerOpplysning.GrunnlagErReberegnet(verdi = true),
                            DagpengerOpplysning.AntallStønadsdagerSomGjenstår(verdi = 259),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Riktige brevblokker ved reberegnet grunnlag, forbruk av periode og antall barn er endret`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_ORDINÆR.brevblokkId,
                GJENOPPTAK_INNLEDNING.brevblokkId,
                GJENOPPTAK_EGENANDEL_INNLEDNING.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE.brevblokkId,
                GJENOPPTAK_REBEREGNING_UTFØRT.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_BARNETILLEGG.brevblokkId,
                INNVILGELSE_GRUNNLAG.brevblokkId,
                GJENOPPTAK_ARBEIDSTIDEN_DIN.brevblokkId,
                GJENOPPTAK_EGENANDEL.brevblokkId,
                INNVILGELSE_MELDEKORT.brevblokkId,
                INNVILGELSE_UTBETALING.brevblokkId,
                INNVILGELSE_SKATTEKORT.brevblokkId,
                INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
        GjenopptakMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.GJENOPPTAK,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.Egenandel(verdi = 3000),
                            DagpengerOpplysning.EgenandelGjenstående(verdi = 2000),
                            DagpengerOpplysning.GrunnlagErReberegnet(verdi = true),
                            DagpengerOpplysning.AntallStønadsdagerSomGjenstår(verdi = 259),
                            DagpengerOpplysning.AntallBarnSomGirRettTilBarnetillegg(verdi = 3),
                            DagpengerOpplysning.AntallBarnSomGirRettTilBarnetilleggErEndret(verdi = true),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Riktige brevblokker ved reberegnet grunnlag, forbruk av periode og bruk av nittiprosentregel er endret`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_ORDINÆR.brevblokkId,
                GJENOPPTAK_INNLEDNING.brevblokkId,
                GJENOPPTAK_EGENANDEL_INNLEDNING.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE.brevblokkId,
                GJENOPPTAK_REBEREGNING_UTFØRT.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_NITTI_PROSENT_REGEL.brevblokkId,
                INNVILGELSE_GRUNNLAG.brevblokkId,
                GJENOPPTAK_ARBEIDSTIDEN_DIN.brevblokkId,
                GJENOPPTAK_EGENANDEL.brevblokkId,
                INNVILGELSE_MELDEKORT.brevblokkId,
                INNVILGELSE_UTBETALING.brevblokkId,
                INNVILGELSE_SKATTEKORT.brevblokkId,
                INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
        GjenopptakMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.GJENOPPTAK,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.Egenandel(verdi = 3000),
                            DagpengerOpplysning.EgenandelGjenstående(verdi = 2000),
                            DagpengerOpplysning.GrunnlagErReberegnet(verdi = true),
                            DagpengerOpplysning.AntallStønadsdagerSomGjenstår(verdi = 259),
                            DagpengerOpplysning.AntallBarnSomGirRettTilBarnetillegg(verdi = 3),
                            DagpengerOpplysning.NittiProsentregelErEndret(verdi = true),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Riktige brevblokker når det ikke er reberegnet grunnlag, forbruk av periode og godkjent lokal arbeidssøker`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_ORDINÆR.brevblokkId,
                GJENOPPTAK_INNLEDNING.brevblokkId,
                GJENOPPTAK_EGENANDEL_INNLEDNING.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE.brevblokkId,
                GJENOPPTAK_REBEREGNING_IKKE_RETT.brevblokkId,
                GJENOPPTAK_ARBEIDSTIDEN_DIN.brevblokkId,
                INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_1.brevblokkId,
                INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_2.brevblokkId,
                GJENOPPTAK_EGENANDEL.brevblokkId,
                INNVILGELSE_MELDEKORT.brevblokkId,
                INNVILGELSE_UTBETALING.brevblokkId,
                INNVILGELSE_SKATTEKORT.brevblokkId,
                INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
        GjenopptakMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.GJENOPPTAK,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.Egenandel(verdi = 3000),
                            DagpengerOpplysning.EgenandelGjenstående(verdi = 2000),
                            DagpengerOpplysning.GrunnlagErReberegnet(verdi = false),
                            DagpengerOpplysning.AntallStønadsdagerSomGjenstår(verdi = 259),
                            DagpengerOpplysning.GodkjentLokalArbeidssøker(true),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Riktige brevblokker når det ikke er reberegnet grunnlag, forbruk av periode og godkjent deltidssøker`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_ORDINÆR.brevblokkId,
                GJENOPPTAK_INNLEDNING.brevblokkId,
                GJENOPPTAK_EGENANDEL_INNLEDNING.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE.brevblokkId,
                GJENOPPTAK_REBEREGNING_IKKE_RETT.brevblokkId,
                GJENOPPTAK_ARBEIDSTIDEN_DIN.brevblokkId,
                INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_1.brevblokkId,
                INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_2.brevblokkId,
                GJENOPPTAK_EGENANDEL.brevblokkId,
                INNVILGELSE_MELDEKORT.brevblokkId,
                INNVILGELSE_UTBETALING.brevblokkId,
                INNVILGELSE_SKATTEKORT.brevblokkId,
                INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
        GjenopptakMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.GJENOPPTAK,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.Egenandel(verdi = 3000),
                            DagpengerOpplysning.EgenandelGjenstående(verdi = 2000),
                            DagpengerOpplysning.GrunnlagErReberegnet(verdi = false),
                            DagpengerOpplysning.AntallStønadsdagerSomGjenstår(verdi = 259),
                            DagpengerOpplysning.GodkjentKunDeltidssøker(true),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }
}
