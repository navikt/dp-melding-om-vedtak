package no.nav.dagpenger.vedtaksmelding

import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.vedtaksmelding.apiconfig.Saksbehandler
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.UUID

class KlageBehandlingHttpKlientTest {
    @Test
    @Disabled
    fun `hent en klage for realz`() {
        @Suppress("ktlint:standard:max-line-length")
        KlageBehandlingHttpKlient(
            dpSaksbehandlingKlageApiUrl = "https://dp-saksbehandling.intern.dev.nav.no/klage",
            tokenProvider = { "" },
        ).let {
            runBlocking {
                it.hentVedtak(
                    behandlingId = UUID.fromString("0196a5b8-3dab-779f-ba9c-a116e298b2b1"),
                    klient = Saksbehandler(token = ""),
                ).getOrThrow()
            }
        }
    }
}

// 0196a5b8-3b8c-7ebe-983b-d78078e51cbd
// https://azure-token-generator.intern.dev.nav.no/api/obo?aud=dev-gcp:teamdagpenger:dp-saksbehandling
