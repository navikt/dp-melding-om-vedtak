package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeInstanceOf
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall.AVSLÅTT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall.INNVILGET
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.MINSTEINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseMelding
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class VedtakMeldingTest {
    private val behandlingId = UUIDv7.ny()

    @Test
    fun `Skal lage riktig vedtaksmelding`() {
        VedtakMelding
            .byggVedtaksmelding(
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
                    ),
                alleBrevblokker = emptyList(),
            ).shouldBeInstanceOf<AvslagMelding>()

        VedtakMelding
            .byggVedtaksmelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        vilkår = emptySet(),
                        utfall = INNVILGET,
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
                    ),
                alleBrevblokker = emptyList(),
            )
        }
    }
}
