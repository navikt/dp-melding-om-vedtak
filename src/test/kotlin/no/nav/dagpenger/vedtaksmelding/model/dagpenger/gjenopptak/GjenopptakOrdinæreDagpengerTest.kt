package no.nav.dagpenger.vedtaksmelding.model.dagpenger.gjenopptak

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.ManglerBrevstøtte
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class GjenopptakOrdinæreDagpengerTest {
    private val behandlingId = UUIDv7.ny()

    @Test
    fun `Kriterier for å lage gjenopptak-brev`() {
        shouldThrow<ManglerBrevstøtte> {
            GjenopptakMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.AVSLÅTT,
                        opplysninger = emptySet(),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldThrow<ManglerBrevstøtte> {
            GjenopptakMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.INNVILGET,
                        opplysninger = emptySet(),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldNotThrowAny {
            GjenopptakMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.GJENOPPTAK,
                        opplysninger = emptySet(),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
    }
}
