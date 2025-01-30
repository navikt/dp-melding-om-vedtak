package no.nav.dagpenger.vedtaksmelding

import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.vedtaksmelding.k8.setAzureAuthEnv
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.UUID

internal class HtmlE2ETest {
    @Disabled
    @Test
    fun `Lage html`() {
        val behandlingId = UUID.fromString("0194b656-2e37-7258-a4ad-33af850cb264")

        // dp-melding-om-vedtak token, hentes fra azure-token-generator f,eks
        @Suppress("ktlint:standard:max-line-length")
        val token =
            ""
        val client =
            HttpClient {
                install(Logging) {
                    level = LogLevel.ALL
                }
            }
        setAzureAuthEnv(
            app = "dp-melding-om-vedtak",
            type = "azurerator.nais.io",
        ) {
            runBlocking {
                client.post("https://dp-melding-om-vedtak.intern.dev.nav.no/melding-om-vedtak/$behandlingId/html") {
                    header(HttpHeaders.Authorization, "Bearer $token")
                    header(HttpHeaders.ContentType, "application/json")
                    setBody(
                        """
                                    
                        {
                            "fornavn": "Test ForNavn",
                            "etternavn": "Test EtterNavn",
                            "fodselsnummer": "12345678901",
                            "sakId": "sak123",
                            "saksbehandler": {
                                "fornavn": "Ola",
                                "etternavn": "Nordmann",
                                "enhet": {
                                    "navn": "Enhet Navn",
                                    "postadresse": "Postadresse 123"
                                }
                            },
                            "beslutter": {
                                "fornavn": "Kari",
                                "etternavn": "Nordmann",
                                "enhet": {
                                    "navn": "Enhet Navn",
                                    "postadresse": "Postadresse 123"
                                }
                            }
                        }
                        """.trimIndent(),
                    )
                }.let { response ->
                    println(response.bodyAsText())
                }
            }
        }
    }
}
