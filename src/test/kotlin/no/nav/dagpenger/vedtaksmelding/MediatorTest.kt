package no.nav.dagpenger.vedtaksmelding

import io.kotest.assertions.throwables.shouldThrow
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.vedtaksmelding.db.VedtaksmeldingRepository
import no.nav.dagpenger.vedtaksmelding.model.Behandling
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.sanity.SanityKlient
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class MediatorTest {
    private val behandlingId = UUIDv7.ny()
    private val saksbehandler = Saksbehandler("tulleToken")
    private val opplysninger =
        setOf(
            Opplysning(
                opplysningTekstId = "opplysning.krav-til-minsteinntekt",
                navn = "curae",
                verdi = "true",
                datatype = "bolsk",
                opplysningId = "aliquet",
            ),
            Opplysning(
                opplysningTekstId = "opplysning.krav-paa-dagpenger",
                navn = "curae",
                verdi = "true",
                datatype = "bolsk",
                opplysningId = "aliquet",
            ),
        )
    private val behandling =
        Behandling(
            id = behandlingId,
            tilstand = "tilstand",
            opplysninger = opplysninger,
        )

    @Test
    fun `skal sende ett eller annet vedtak`() {
        val behandlingKlient =
            mockk<BehandlingKlient>().also {
                coEvery { it.hentBehandling(behandlingId, saksbehandler) } returns Result.success(behandling)
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
                mockk(),
                vedtaksmeldingRepository = mockk<VedtaksmeldingRepository>(relaxed = true),
            )
        runBlocking {
            shouldThrow<RuntimeException> {
                mediator.hentVedtaksmelding(
                    behandlingId = behandlingId,
                    saksbehandler = saksbehandler,
                )
            }
        }
    }

//    @Test
//    fun `Skal lagre utvidet beskrivelse og hente vedtaksmelding med riktige utvidede beskrivelser`() {
//        val utvidetBeskrivelse =
//            UtvidetBeskrivelse(
//                behandlingId = UUIDv7.ny(),
//                brevblokkId = "brevblokk",
//                tekst = "Mikke Mus er kul!",
//            )
//        withMigratedDb { dataSource ->
//            val mediator =
//                Mediator(
//                    behandlingKlient = mockk<BehandlingKlient>(),
//                    sanityKlient = mockk<SanityKlient>(),
//                    vedtaksmeldingRepository = PostgresVedtaksmeldingRepository(dataSource),
//                )
//
//            mediator.lagreUtvidetBeskrivelse(utvidetBeskrivelse)
//        }
//    }
}
