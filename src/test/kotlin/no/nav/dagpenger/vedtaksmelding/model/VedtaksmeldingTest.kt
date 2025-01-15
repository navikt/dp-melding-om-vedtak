package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.mockk
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class VedtaksmeldingTest {
    private val behandlingId = UUIDv7.ny()

    @Test
    fun `Skal lage riktig vedtaksmelding`() {
        Vedtaksmelding.byggVedtaksmelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
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
                    behandlingId = behandlingId,
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
                        behandlingId = behandlingId,
                        vilkår = emptySet(),
                        utfall = Utfall.AVSLÅTT,
                    ),
                mediator = mockk(),
            )
        }
    }
}
