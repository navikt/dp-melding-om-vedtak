package no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans

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
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_ARBEID_OVER_TERSKEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_IKKE_MELDT_SEG_I_TIDE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_SVART_NEI_TIL_Å_STÅ_TILMELDT
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

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
                        opplysninger = setOf(DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(false)),
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
                        opplysninger = setOf(DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(false)),
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
                        opplysninger = setOf(DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(false)),
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
                        automatiskBehandling = false,
                        opplysninger = setOf(DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(false)),
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
                        opplysninger = setOf(DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(true)),
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
                        opplysninger = setOf(DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(false)),
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
                        opplysninger = setOf(DagpengerOpplysning.OppfyllerVilkåretOmTapAvArbeidstid(false)),
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
                        opplysninger = setOf(DagpengerOpplysning.OppyllerMeldeplikt(false)),
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
                            DagpengerOpplysning.OppfyllerVilkåretOmTapAvArbeidstid(verdi = false),
                            DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid(verdi = 50.0),
                            DagpengerOpplysning.FastsattVanligArbeidstidPerUke(verdi = 35.5),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                STANS_INNLEDNING.brevblokkId,
                STANS_ARBEID_OVER_TERSKEL.brevblokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
    }

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
                            DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(verdi = false),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                STANS_INNLEDNING.brevblokkId,
                STANS_SVART_NEI_TIL_Å_STÅ_TILMELDT.brevblokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
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
                            DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(verdi = false),
                            DagpengerOpplysning.OppyllerMeldeplikt(verdi = false),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                STANS_INNLEDNING.brevblokkId,
                STANS_IKKE_MELDT_SEG_I_TIDE.brevblokkId,
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
    }
}
