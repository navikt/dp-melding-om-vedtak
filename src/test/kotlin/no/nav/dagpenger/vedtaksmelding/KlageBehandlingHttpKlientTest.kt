package no.nav.dagpenger.vedtaksmelding

import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import org.junit.jupiter.api.Test
import java.util.UUID

class KlageBehandlingHttpKlientTest() {
    @Test
    fun `hent en klage`() {
        KlageBehandlingHttpKlient(
            dpSaksbehandlingKlageApiUrl = "https://dp-saksbehandling.intern.dev.nav.no/klage",
            tokenProvider = {
                ""
            },
        ).let {
            runBlocking {
                it.hentVedtak(
                    behandlingId = UUID.fromString("0196a5b8-3b8c-7ebe-983b-d78078e51cbd"),
                    saksbehandler = Saksbehandler(token = ""),
                ).getOrThrow()
            }
        }
    }
}

// 0196a5b8-3b8c-7ebe-983b-d78078e51cbd
//https://azure-token-generator.intern.dev.nav.no/api/obo?aud=dev-gcp:teamdagpenger:dp-saksbehandling