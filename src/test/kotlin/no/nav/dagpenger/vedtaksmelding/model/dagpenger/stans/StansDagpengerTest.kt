package no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
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
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.ManglerBrevstøtte
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_ALDER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_ARBEID_OVER_TERSKEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_IKKE_MELDT_SEG_I_TIDE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_REELL_ARBEIDSSØKER_GENERELL_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_REELL_ARBEIDSSØKER_GENERELL_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_REELL_ARBEIDSSØKER_SVART_NEI_TIL_Å_STÅ_TILMELDT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_TRENGER_DU_FORTSATT_DAGPENGER
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.LocalDate

class StansDagpengerTest {
    private val behandlingId = UUIDv7.ny()

    @Test
    fun `Kriterier for å lage stans-brev`() {
        shouldThrow<ManglerBrevstøtte> {
            StansMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.AVSLÅTT,
                        automatiskBehandling = true,
                        opplysninger =
                            setOf(
                                DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(
                                    verdi = false,
                                    perioder =
                                        listOf(
                                            Periode(
                                                verdi = false,
                                                opprinnelse = Opprinnelse.NY,
                                                gyldigFraOgMed = LocalDate.MIN,
                                            ),
                                        ),
                                ),
                            ),
                        behandletHendelseType = "ARBEIDSSØKERPERIODE",
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldThrow<ManglerBrevstøtte> {
            StansMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.INNVILGET,
                        automatiskBehandling = true,
                        opplysninger =
                            setOf(
                                DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(
                                    verdi = false,
                                    perioder =
                                        listOf(
                                            Periode(
                                                verdi = false,
                                                opprinnelse = Opprinnelse.NY,
                                                gyldigFraOgMed = LocalDate.MIN,
                                            ),
                                        ),
                                ),
                            ),
                        behandletHendelseType = "ARBEIDSSØKERPERIODE",
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldThrow<ManglerBrevstøtte> {
            StansMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.GJENOPPTAK,
                        automatiskBehandling = true,
                        opplysninger =
                            setOf(
                                DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(
                                    verdi = false,
                                    perioder =
                                        listOf(
                                            Periode(
                                                verdi = false,
                                                opprinnelse = Opprinnelse.NY,
                                                gyldigFraOgMed = LocalDate.MIN,
                                            ),
                                        ),
                                ),
                            ),
                        behandletHendelseType = "ARBEIDSSØKERPERIODE",
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldThrow<ManglerBrevstøtte> {
            StansMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.STANS,
                        automatiskBehandling = true,
                        opplysninger =
                            setOf(
                                DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(
                                    verdi = true,
                                    perioder =
                                        listOf(
                                            Periode(
                                                verdi = true,
                                                opprinnelse = Opprinnelse.NY,
                                                gyldigFraOgMed = LocalDate.MIN,
                                            ),
                                        ),
                                ),
                            ),
                        behandletHendelseType = "ARBEIDSSØKERPERIODE",
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldNotThrowAny {
            StansMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.STANS,
                        automatiskBehandling = true,
                        opplysninger =
                            setOf(
                                DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(
                                    verdi = false,
                                    perioder =
                                        listOf(
                                            Periode(
                                                verdi = false,
                                                opprinnelse = Opprinnelse.NY,
                                                gyldigFraOgMed = LocalDate.MIN,
                                            ),
                                        ),
                                ),
                            ),
                        behandletHendelseType = "ARBEIDSSØKERPERIODE",
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldNotThrowAny {
            StansMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.STANS,
                        automatiskBehandling = false,
                        opplysninger =
                            setOf(
                                DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(
                                    verdi = false,
                                    perioder =
                                        listOf(
                                            Periode(
                                                verdi = false,
                                                opprinnelse = Opprinnelse.NY,
                                                gyldigFraOgMed = LocalDate.MIN,
                                            ),
                                        ),
                                ),
                            ),
                        behandletHendelseType = "ARBEIDSSØKERPERIODE",
                    ),
                alleBrevblokker = emptyList(),
            )
        }
    }

    @Test
    fun `Riktige brevblokker når bruker har arbeidet over terskel i tre meldeperioder`() {
        StansMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.STANS,
                    automatiskBehandling = true,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.OppfyllerVilkåretOmTapAvArbeidstid(
                                verdi = false,
                                perioder =
                                    listOf(
                                        Periode(
                                            verdi = false,
                                            opprinnelse = Opprinnelse.NY,
                                            gyldigFraOgMed = LocalDate.MIN,
                                        ),
                                    ),
                            ),
                            DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid(
                                verdi = 50.0,
                                perioder =
                                    listOf(
                                        Periode(
                                            verdi = 50.0,
                                            opprinnelse = Opprinnelse.NY,
                                            gyldigFraOgMed = LocalDate.MIN,
                                        ),
                                    ),
                            ),
                            DagpengerOpplysning.FastsattVanligArbeidstidPerUke(
                                verdi = 35.5,
                                perioder =
                                    listOf(
                                        Periode(
                                            verdi = 35.5,
                                            opprinnelse = Opprinnelse.NY,
                                            gyldigFraOgMed = LocalDate.MIN,
                                        ),
                                    ),
                            ),
                        ),
                    behandletHendelseType = "MELDEKORT",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                STANS_INNLEDNING.brevblokkId,
                STANS_ARBEID_OVER_TERSKEL.brevblokkId,
                STANS_TRENGER_DU_FORTSATT_DAGPENGER.brevblokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
    }

    // TODO: Må få mer kontekst fra PJ's for å avdekke dette spesialtilfellet
    @Disabled
    @Test
    fun `Riktige brevblokker når bruker har svart Nei på å stå tilmeldt`() {
        StansMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.STANS,
                    automatiskBehandling = true,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(
                                verdi = false,
                                perioder = listOf(Periode(verdi = false, Opprinnelse.NY, gyldigFraOgMed = LocalDate.MIN)),
                            ),
                        ),
                    behandletHendelseType = "ARBEIDSSØKERPERIODE",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                STANS_INNLEDNING.brevblokkId,
                STANS_REELL_ARBEIDSSØKER_SVART_NEI_TIL_Å_STÅ_TILMELDT.brevblokkId,
                STANS_TRENGER_DU_FORTSATT_DAGPENGER.brevblokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
    }

    @Test
    fun `Riktige brevblokker når bruker har blitt utmeldt av arbeidssøkerregisteret - ikke pga manglende meldeplikt`() {
        StansMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.STANS,
                    automatiskBehandling = true,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(
                                verdi = false,
                                perioder = listOf(Periode(verdi = false, Opprinnelse.NY, gyldigFraOgMed = LocalDate.MIN)),
                            ),
                        ),
                    behandletHendelseType = "ARBEIDSSØKERPERIODE",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                STANS_INNLEDNING.brevblokkId,
                STANS_REELL_ARBEIDSSØKER_GENERELL_DEL_1.brevblokkId,
                STANS_REELL_ARBEIDSSØKER_GENERELL_DEL_2.brevblokkId,
                STANS_TRENGER_DU_FORTSATT_DAGPENGER.brevblokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
    }

    @Test
    fun `Riktige brevblokker når bruker ikke har meldt seg i tilde`() {
        StansMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.STANS,
                    automatiskBehandling = true,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(
                                verdi = false,
                                perioder = listOf(Periode(verdi = false, Opprinnelse.NY, gyldigFraOgMed = LocalDate.MIN)),
                            ),
                            DagpengerOpplysning.OppyllerMeldeplikt(
                                verdi = false,
                                perioder = listOf(Periode(verdi = false, Opprinnelse.NY, gyldigFraOgMed = LocalDate.MIN)),
                            ),
                        ),
                    behandletHendelseType = "ARBEIDSSØKERPERIODE",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                STANS_INNLEDNING.brevblokkId,
                STANS_IKKE_MELDT_SEG_I_TIDE.brevblokkId,
                STANS_TRENGER_DU_FORTSATT_DAGPENGER.brevblokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
    }

    @Test
    fun `Riktige brevblokker når bruker har fylt 67 år`() {
        StansMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.STANS,
                    automatiskBehandling = true,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.KravTilAlder(
                                verdi = false,
                                perioder = listOf(Periode(verdi = false, Opprinnelse.NY, gyldigFraOgMed = LocalDate.now().minusDays(2))),
                            ),
                        ),
                    behandletHendelseType = "MELDEKORT",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                STANS_INNLEDNING.brevblokkId,
                STANS_ALDER.brevblokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
    }
}
