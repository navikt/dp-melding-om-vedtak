package no.nav.dagpenger.vedtaksmelding.model.dagpenger.omgjøring

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test
import java.time.LocalDate

class OmgjøringUtenKlageTest {
    private val behandlingId = UUIDv7.ny()

    @Test
    fun `Kriterier for å lage omgjøring-brev`() {
        shouldThrow<Vedtaksmelding.ManglerBrevstøtte> {
            OmgjøringMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.AVSLÅTT,
                        opplysninger = emptySet(),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldThrow<Vedtaksmelding.ManglerBrevstøtte> {
            OmgjøringMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.INNVILGET,
                        opplysninger = emptySet(),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldThrow<Vedtaksmelding.ManglerBrevstøtte> {
            OmgjøringMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.GJENOPPTAK,
                        opplysninger = emptySet(),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldThrow<Vedtaksmelding.ManglerBrevstøtte> {
            OmgjøringMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                        opplysninger =
                            setOf(
                                DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = false),
                                DagpengerOpplysning.VedtaketMåAnsesUgyldig(verdi = false),
                                DagpengerOpplysning.VedtaketErIkkeTilSkade(verdi = false),
                            ),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldNotThrow<Vedtaksmelding.ManglerBrevstøtte> {
            OmgjøringMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                        opplysninger =
                            setOf(
                                DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                                DagpengerOpplysning.VedtaketMåAnsesUgyldig(verdi = true),
                                DagpengerOpplysning.VedtaketErIkkeTilSkade(verdi = true),
                            ),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldNotThrow<Vedtaksmelding.ManglerBrevstøtte> {
            OmgjøringMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                        opplysninger =
                            setOf(
                                DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            ),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldNotThrow<Vedtaksmelding.ManglerBrevstøtte> {
            OmgjøringMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                        opplysninger =
                            setOf(
                                DagpengerOpplysning.VedtaketMåAnsesUgyldig(verdi = true),
                            ),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldNotThrow<Vedtaksmelding.ManglerBrevstøtte> {
            OmgjøringMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                        opplysninger =
                            setOf(
                                DagpengerOpplysning.VedtaketErIkkeTilSkade(verdi = true),
                            ),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
    }

    @Test
    fun `Riktige brevblokker når dagpengeperiode er endret og det ikke er til-og-med-dato`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.AntallStønadsukerErEndret(verdi = true),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }

    @Test
    fun `Riktige brevblokker når dagpengeperiode er endret og til-og-med-dato er gitt`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_1.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_2.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.AntallStønadsukerErEndret(verdi = true),
                            DagpengerOpplysning.SisteDagMedRett(verdi = LocalDate.now().plusWeeks(4)),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }

    @Test
    fun `Riktige brevblokker når til-og-med-dato er endret`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_1.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_2.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.SisteDagMedRett(verdi = LocalDate.now().plusWeeks(4)),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }

    @Test
    fun `Riktige brevblokker når dagsats er endret og det ikke gjenstår egenandel`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.DagsatsErEndret(verdi = true),
                            DagpengerOpplysning.Egenandel(verdi = 3000),
                            DagpengerOpplysning.EgenandelGjenstående(verdi = 0),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }

    @Test
    fun `Riktige brevblokker når dagsats er endret og det gjenstår egenandel`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_EGENANDEL.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.DagsatsErEndret(verdi = true),
                            DagpengerOpplysning.Egenandel(verdi = 3000),
                            DagpengerOpplysning.EgenandelGjenstående(verdi = 1000),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }

    @Test
    fun `Riktige brevblokker når dagsats er endret og det er samordning med sykepenger`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_SYKEPENGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.DagsatsErEndret(verdi = true),
                            DagpengerOpplysning.HarSamordnet(verdi = true),
                            DagpengerOpplysning.SykepengerDagsats(verdi = 500),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }

    @Test
    fun `Riktige brevblokker når dagsats er endret og det er samordning med pleiepenger`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_PLEIEPENGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.DagsatsErEndret(verdi = true),
                            DagpengerOpplysning.HarSamordnet(verdi = true),
                            DagpengerOpplysning.PleiepengerDagsats(verdi = 500),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }

    @Test
    fun `Riktige brevblokker når dagsats er endret og det er samordning med omsorgspenger`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_OMSORGSPENGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.DagsatsErEndret(verdi = true),
                            DagpengerOpplysning.HarSamordnet(verdi = true),
                            DagpengerOpplysning.OmsorgspengerDagsats(verdi = 500),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }

    @Test
    fun `Riktige brevblokker når dagsats er endret og det er samordning med opplæringspenger`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_OPPLÆRINGSPENGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.DagsatsErEndret(verdi = true),
                            DagpengerOpplysning.HarSamordnet(verdi = true),
                            DagpengerOpplysning.OpplæringspengerDagsats(verdi = 500),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }

    @Test
    fun `Riktige brevblokker når dagsats er endret og det er samordning med uføre`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_UFØRE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.DagsatsErEndret(verdi = true),
                            DagpengerOpplysning.HarSamordnet(verdi = true),
                            DagpengerOpplysning.UføreDagsats(verdi = 500),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }

    @Test
    fun `Riktige brevblokker når dagsats er endret og det er samordning med foreldrepenger`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_FORELDREPENGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.DagsatsErEndret(verdi = true),
                            DagpengerOpplysning.HarSamordnet(verdi = true),
                            DagpengerOpplysning.ForeldrepengerDagsats(verdi = 500),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }

    @Test
    fun `Riktige brevblokker når dagsats er endret og det er samordning med svangerskapspenger`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_SVANGERSKAPSPENGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.DagsatsErEndret(verdi = true),
                            DagpengerOpplysning.HarSamordnet(verdi = true),
                            DagpengerOpplysning.SvangerskapspengerDagsats(verdi = 500),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }

    @Test
    fun `Riktige brevblokker når dagsats er endret og det er samordning med flere ytelser`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_GENERISK.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.DagsatsErEndret(verdi = true),
                            DagpengerOpplysning.HarSamordnet(verdi = true),
                            DagpengerOpplysning.SvangerskapspengerDagsats(verdi = 200),
                            DagpengerOpplysning.SykepengerDagsats(verdi = 250),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }

    @Test
    fun `Riktige brevblokker når det er godkjent lokal arbeidssøker`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_1.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_2.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.GodkjentLokalArbeidssøker(verdi = true),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }

    @Test
    fun `Riktige brevblokker når det er godkjent kun deltidssøker`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_1.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_2.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.GodkjentKunDeltidssøker(verdi = true),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }

    @Test
    fun `Riktige brevblokker når dagsats er endret og det gis barnetillegg`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_BARNETILLEGG.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.DagsatsErEndret(verdi = true),
                            DagpengerOpplysning.AntallBarnSomGirRettTilBarnetillegg(verdi = 2),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }

    @Test
    fun `Riktige brevblokker når dagsats er endret og det gis barnetillegg og 90-prosentregel er brukt`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_BARNETILLEGG.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_NITTI_PROSENT_REGEL.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.DagsatsErEndret(verdi = true),
                            DagpengerOpplysning.AntallBarnSomGirRettTilBarnetillegg(verdi = 2),
                            DagpengerOpplysning.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget(
                                verdi = 10,
                            ),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }

    @Test
    fun `Riktige brevblokker når fastsatt vanlig arbeidstid er endret`() {
        val forventedeBrevblokker =
            listOf(
                InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT.brevblokkId,
                InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_UTBETALING.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                Vedtaksmelding.FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
        OmgjøringMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.OMGJORT_MED_INNVILGELSE,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram(verdi = true),
                            DagpengerOpplysning.FastsattVanligArbeidstidPerUkeErEndret(verdi = true),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokker
    }
}
