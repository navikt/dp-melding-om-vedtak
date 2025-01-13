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
import no.nav.dagpenger.vedtaksmelding.Configuration.azureAdClient
import no.nav.dagpenger.vedtaksmelding.k8.setAzureAuthEnv
import org.junit.jupiter.api.Test
import java.util.UUID

internal class HtmlE2ETest {
    @Test
    fun `Lage html`() {
        val behandlingId = UUID.fromString("01943b06-1a68-7dad-88e1-19e31cde711c")

        // saksbhehandler token, hentes fra azure-token-generator f,eks
        @Suppress("ktlint:standard:max-line-length")
        val token =
            ""

        val oboExchanger: (String) -> String by lazy {
            val scope = "api://dev-gcp.teamdagpenger.dp-melding-om-vedtak/.default"
            { token: String ->
                val accessToken = azureAdClient.onBehalfOf(token, scope).accessToken
                requireNotNull(accessToken) { "Failed to get access token" }
                accessToken
            }
        }

        val client =
            HttpClient {
                install(Logging) {
                    level = LogLevel.ALL
                }
            }
        setAzureAuthEnv(
            app = "dp-saksbehandling-frontend",
            type = "azurerator.nais.io",
        ) {
            runBlocking {
                val oboToken =
                    oboExchanger(token).also {
                        println(it)
                    }
                client.post("https://dp-melding-om-vedtak.intern.dev.nav.no/melding-om-vedtak/$behandlingId/html") {
                    header(HttpHeaders.Authorization, "Bearer $oboToken")
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
