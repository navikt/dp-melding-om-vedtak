package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.mockk
import org.junit.jupiter.api.Test

class VedtakMeldingTest {
    @Test
    fun `Skal lage riktig vedtaksmelding`() {
        Vedtaksmelding.byggVedtaksmelding(
            vedtak =
                Vedtak(
                    vilkår =
                        setOf(
                            Vilkår(
                                navn = "Oppfyller kravet til minsteinntekt eller verneplikt",
                                status = Vilkår.Status.IKKE_OPPFYLT,
                            ),
                        ),
                    utfall = Utfall.AVSLÅTT,
                ),
            mediator = mockk(),
        ).shouldBeInstanceOf<Avslag>()

        Vedtaksmelding.byggVedtaksmelding(
            vedtak =
                Vedtak(
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                ),
            mediator = mockk(),
        ).shouldBeInstanceOf<Innvilgelse>()
    }

    @Test
    fun `Skal feile dersom man ikke kan bygge en og bare en melding om vedtak`() {
        shouldThrow<Vedtaksmelding.UkjentVedtakException> {
            Vedtaksmelding.byggVedtaksmelding(
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
