package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import io.kotest.assertions.throwables.shouldThrow
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.vedtaksmelding.model.Behandling
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class MediatorTest {
    private val behandlingId = UUIDv7.ny()
    private val saksbehandler = Saksbehandler("tulleToken")
    private val opplysninger =
        setOf(
            Opplysning(
                id = "opplysning.krav-til-minsteinntekt",
                navn = "curae",
                verdi = "true",
                datatype = "bolsk",
                opplysningId = "aliquet",
            ),
            Opplysning(
                id = "opplysning.krav-paa-dagpenger",
                navn = "curae",
                verdi = "true",
                datatype = "bolsk",
                opplysningId = "aliquet",
            ),
        )
    private val behandling =
        Behandling(
            id = behandlingId.toString(),
            tilstand = "tilstand",
            opplysninger = opplysninger,
        )

    @Test
    fun `skal sende ett eller annet vedta`() {
        val behandlingKlient =
            mockk<BehandlingKlient>().also {
                coEvery { it.hentBehandling(behandlingId, saksbehandler) } returns Result.success(behandling)
            }

        val mediator = Mediator(behandlingKlient)
        runBlocking {
            mediator.sendVedtak(
                behandlingId = behandlingId,
                saksbehandler = saksbehandler,
            )
        }

        coVerify(exactly = 1) {
            behandlingKlient.hentBehandling(behandlingId, saksbehandler)
        }
    }

    @Test
    fun `Kaster feil dersom vi ikke får hentet opplysninger fra dp-behandling`() {
        val mediator =
            Mediator(
                mockk<BehandlingKlient>().also {
                    coEvery { it.hentBehandling(any(), any()) } throws RuntimeException("Noe gikk galt")
                },
            )
        runBlocking {
            shouldThrow<RuntimeException> {
                mediator.sendVedtak(
                    behandlingId = behandlingId,
                    saksbehandler = saksbehandler,
                )
            }
        }
    }
}
