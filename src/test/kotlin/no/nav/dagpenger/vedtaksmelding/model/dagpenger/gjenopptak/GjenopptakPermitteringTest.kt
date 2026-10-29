package no.nav.dagpenger.vedtaksmelding.model.dagpenger.gjenopptak

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Opprinnelse
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Periode
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.FasteBrevblokker.SPØRSMÅL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_ARBEIDSTIDEN_DIN
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_DAGPENGEPERIODE_PERMITTERT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_DAGPENGEPERIODE_PERMITTERT_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_DAGPENGEPERIODE_PERMITTERT_DEL_3
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_DAGPENGEPERIODE_PERMITTERT_FISK_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_DAGPENGEPERIODE_PERMITTERT_FISK_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_DAGPENGEPERIODE_PERMITTERT_FISK_DEL_3
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_EGENANDEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_INNLEDNING_EGENANDEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_INNLEDNING_SAMME_PERIODE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_REBEREGNING_IKKE_RETT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_PERMITTERT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_PERMITTERT_FISK
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_UTBETALING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT_FISK
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class GjenopptakPermitteringTest {
    private val behandlingId = UUIDv7.ny()

    @Test
    fun `Riktige brevblokker gjenopptak permittering når det ikke er reberegning, har forbruk, lokal arbeidssøker`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_PERMITTERT.brevblokkId,
                GJENOPPTAK_INNLEDNING_SAMME_PERIODE.brevblokkId,
                GJENOPPTAK_INNLEDNING_EGENANDEL.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE_PERMITTERT_DEL_1.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE_PERMITTERT_DEL_2.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE_PERMITTERT_DEL_3.brevblokkId,
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
                RETT_TIL_Å_KLAGE.brevBlokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
        GjenopptakMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.GJENOPPTAK,
                    automatiskBehandling = false,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.Egenandel(verdi = 3000, listOf(Periode(verdi = 3000, Opprinnelse.NY))),
                            DagpengerOpplysning.EgenandelGjenstående(verdi = 2000, listOf(Periode(verdi = 2000, Opprinnelse.NY))),
                            DagpengerOpplysning.GrunnlagErReberegnet(verdi = false),
                            DagpengerOpplysning.AntallStønadsdagerSomGjenstår(verdi = 259, listOf(Periode(verdi = 259, Opprinnelse.NY))),
                            DagpengerOpplysning.GodkjentLokalArbeidssøker(true, listOf(Periode(true, Opprinnelse.NY))),
                            DagpengerOpplysning.SkalPermitteringVurderes(true, listOf(Periode(true, Opprinnelse.NY))),
                            DagpengerOpplysning.SkalPermitteringFiskeindustriVurderes(false, listOf(Periode(false, Opprinnelse.NY))),
                            DagpengerOpplysning.OppfyllerKravetTilPermittering(true, listOf(Periode(true, Opprinnelse.NY))),
                        ),
                    behandletHendelseType = "SØKNAD",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Riktige brevblokker gjenopptak permittert fisk når ikke reberegning, har forbruk`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_PERMITTERT_FISK.brevblokkId,
                GJENOPPTAK_INNLEDNING_SAMME_PERIODE.brevblokkId,
                GJENOPPTAK_INNLEDNING_EGENANDEL.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT_FISK.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE_PERMITTERT_FISK_DEL_1.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE_PERMITTERT_FISK_DEL_2.brevblokkId,
                GJENOPPTAK_DAGPENGEPERIODE_PERMITTERT_FISK_DEL_3.brevblokkId,
                GJENOPPTAK_REBEREGNING_IKKE_RETT.brevblokkId,
                GJENOPPTAK_ARBEIDSTIDEN_DIN.brevblokkId,
                GJENOPPTAK_EGENANDEL.brevblokkId,
                INNVILGELSE_MELDEKORT.brevblokkId,
                INNVILGELSE_UTBETALING.brevblokkId,
                INNVILGELSE_SKATTEKORT.brevblokkId,
                INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
        GjenopptakMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.GJENOPPTAK,
                    automatiskBehandling = false,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.Egenandel(verdi = 3000, listOf(Periode(verdi = 3000, Opprinnelse.NY))),
                            DagpengerOpplysning.EgenandelGjenstående(verdi = 2000, listOf(Periode(verdi = 2000, Opprinnelse.NY))),
                            DagpengerOpplysning.GrunnlagErReberegnet(verdi = false),
                            DagpengerOpplysning.AntallStønadsdagerSomGjenstår(verdi = 259, listOf(Periode(verdi = 259, Opprinnelse.NY))),
                            DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri(
                                verdi = true,
                                listOf(Periode(verdi = true, Opprinnelse.NY)),
                            ),
                        ),
                    behandletHendelseType = "SØKNAD",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }
}
