package no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.ManglerBrevstøtte
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
                        opplysninger = setOf(DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(false)),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
    }
}
