package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.vedtaksmelding.Configuration
import no.nav.dagpenger.vedtaksmelding.k8.setAzureAuthEnv
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.UUID

internal class BehandlingKlientTest {
    fun `test`() {
    }

    @Test
    @Disabled
    fun `manuell test`() {
        val behandlingId = UUID.fromString("019145eb-6fbb-769f-b1b1-d2450b383a98")

        @Suppress("ktlint:standard:max-line-length")
        val token = ""

        // krever at man er logget inn paa naisdevice og dev-gcp
        setAzureAuthEnv(
            app = "dp-melding-om-vedtak",
            type = "azurerator.nais.io",
        ) {
            val tokenProvider = Configuration.dpBehandlingOboExchanger
            val klient =
                BehandlngHttpKlient(
                    dpBehandlingApiUrl = "https://dp-behandling.intern.dev.nav.no/behandling",
                    tokenProvider = tokenProvider,
                )
            runBlocking {
                val opplysninger =
                    klient.hentOpplysninger(behandling = behandlingId, saksbehandler = Saksbehandler(token))
                println(opplysninger)
            }
        }
    }
}
