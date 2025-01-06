package no.nav.dagpenger.vedtaksmelding

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.vedtaksmelding.db.VedtaksmeldingRepository
import no.nav.dagpenger.vedtaksmelding.model.AvslagMinsteInntekt
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.Utfall
import no.nav.dagpenger.vedtaksmelding.model.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.Vilkår
import no.nav.dagpenger.vedtaksmelding.sanity.SanityKlient
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class MediatorTest {
    private val behandlingId = UUIDv7.ny()
    private val saksbehandler = Saksbehandler("tulleToken")

    @Test
    fun `skal sende ett eller annet vedtak`() {
        val vedtak =
            Vedtak(
                vilkår = setOf(Vilkår("Oppfyller kravet til minsteinntekt eller verneplikt", Vilkår.Status.IKKE_OPPFYLT)),
                utfall = Utfall.AVSLÅTT,
                opplysninger = emptySet(),
            )
        val behandlingKlient =
            mockk<BehandlingKlient>().also {
                coEvery { it.hentVedtak(behandlingId, saksbehandler) } returns Result.success(vedtak)
            }

        val mediator =
            Mediator(
                behandlingKlient = behandlingKlient,
                sanityKlient = mockk<SanityKlient>(),
                vedtaksmeldingRepository = mockk<VedtaksmeldingRepository>(relaxed = true),
            )

        runBlocking {
            mediator.hentVedtaksmelding(
                behandlingId = behandlingId,
                saksbehandler = saksbehandler,
            ).getOrThrow().shouldBeInstanceOf<AvslagMinsteInntekt>()
        }

        coVerify(exactly = 1) {
            behandlingKlient.hentVedtak(behandlingId, saksbehandler)
        }
    }

    @Test
    fun `Kaster feil dersom vi ikke får hentet vedtak fra dp-behandling`() {
        val mediator =
            Mediator(
                mockk<BehandlingKlient>().also {
                    coEvery { it.hentVedtak(any(), any()) } throws RuntimeException("Noe gikk galt")
                },
                mockk(),
                vedtaksmeldingRepository = mockk<VedtaksmeldingRepository>(relaxed = true),
            )
        runBlocking {
            shouldThrow<RuntimeException> {
                mediator.hentVedtaksmelding(
                    behandlingId = behandlingId,
                    saksbehandler = saksbehandler,
                ).getOrThrow()
            }
        }
    }
}
