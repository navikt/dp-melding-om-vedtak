package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeInstanceOf
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagMelding
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.MINSTEINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseMelding
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak.Utfall.AVSLÅTT
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak.Utfall.INNVILGET
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vilkår
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class VedtakMeldingTest {
    private val behandlingId = UUIDv7.ny()

    @Test
    fun `Skal lage riktig vedtaksmelding`() {
        VedtakMelding.byggVedtaksmelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår =
                        setOf(
                            Vilkår(
                                navn = MINSTEINNTEKT.vilkårNavn,
                                status = Vilkår.Status.IKKE_OPPFYLT,
                            ),
                        ),
                    utfall = AVSLÅTT,
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).shouldBeInstanceOf<AvslagMelding>()

        VedtakMelding.byggVedtaksmelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = INNVILGET,
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).shouldBeInstanceOf<InnvilgelseMelding>()
    }

    @Test
    fun `Skal feile dersom man ikke kan bygge en og bare en melding om vedtak`() {
        shouldThrow<VedtakMelding.UkjentVedtakException> {
            VedtakMelding.byggVedtaksmelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        vilkår = emptySet(),
                        utfall = AVSLÅTT,
                        fagsakId = "fagsakId test",
                    ),
                alleBrevblokker = emptyList(),
            )
        }
    }
}
