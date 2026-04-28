package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeInstanceOf
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Opprinnelse
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Periode
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.gjenopptak.GjenopptakMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansMelding
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class VedtaksmeldingTest {
    private val behandlingId = UUIDv7.ny()

    @Test
    fun `Skal lage riktig vedtaksmelding`() {
        Vedtaksmelding
            .byggVedtaksmelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        opplysninger =
                            setOf(
                                DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(false, listOf(Periode(false, Opprinnelse.NY))),
                            ),
                        utfall = Vedtak.Utfall.AVSLÅTT,
                        automatiskBehandling = false,
                    ),
                alleBrevblokker = emptyList(),
            ).shouldBeInstanceOf<AvslagMelding>()

        Vedtaksmelding
            .byggVedtaksmelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        opplysninger = emptySet(),
                        utfall = Vedtak.Utfall.INNVILGET,
                        automatiskBehandling = false,
                    ),
                alleBrevblokker = emptyList(),
            ).shouldBeInstanceOf<InnvilgelseMelding>()

        Vedtaksmelding
            .byggVedtaksmelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        opplysninger = emptySet(),
                        utfall = Vedtak.Utfall.GJENOPPTAK,
                        automatiskBehandling = false,
                    ),
                alleBrevblokker = emptyList(),
            ).shouldBeInstanceOf<GjenopptakMelding>()

        Vedtaksmelding
            .byggVedtaksmelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        opplysninger =
                            setOf(
                                DagpengerOpplysning.OppfyllerVilkåretOmTapAvArbeidstid(false, listOf(Periode(false, Opprinnelse.NY))),
                            ),
                        utfall = Vedtak.Utfall.STANS,
                        automatiskBehandling = true,
                    ),
                alleBrevblokker = emptyList(),
            ).shouldBeInstanceOf<StansMelding>()
    }

    @Test
    fun `Skal feile dersom man ikke kan bygge en og bare en melding om vedtak`() {
        shouldThrow<Vedtaksmelding.UkjentVedtakException> {
            Vedtaksmelding.byggVedtaksmelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        opplysninger = emptySet(),
                        utfall = Vedtak.Utfall.AVSLÅTT,
                        automatiskBehandling = false,
                    ),
                alleBrevblokker = emptyList(),
            )
        }
    }
}
