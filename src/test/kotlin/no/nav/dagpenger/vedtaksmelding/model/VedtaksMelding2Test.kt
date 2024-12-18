package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.mockk
import org.junit.jupiter.api.Test

class VedtaksMelding2Test {
    @Test
    fun `Skal lage riktig vedtaks melding`() {
        VedtaksMelding2.byggVedtaksMelding(
            vedtak =
                Vedtak(
                    vilkår =
                        setOf(
                            Vilkår(
                                navn = "Krav til minsteinntekt",
                                status = Vilkår.Status.IKKE_OPPFYLT,
                            ),
                        ),
                    utfall = Utfall.AVSLÅTT,
                ),
            mediator = mockk(),
        ).shouldBeInstanceOf<AvslagMinsteInntekt>()

        VedtaksMelding2.byggVedtaksMelding(
            vedtak =
                Vedtak(
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                ),
            mediator = mockk(),
        ).shouldBeInstanceOf<Innvilgelse>()
    }

    @Test
    fun `Skal feile dersom man ikke kan bygge et og bare et melding om vedtak`() {
        shouldThrow<VedtaksMelding2.UkjentVedtakException> {
            VedtaksMelding2.byggVedtaksMelding(
                vedtak =
                    Vedtak(
                        vilkår = emptySet(),
                        utfall = Utfall.AVSLÅTT,
                    ),
                mediator = mockk(),
            )
        }
    }
}
