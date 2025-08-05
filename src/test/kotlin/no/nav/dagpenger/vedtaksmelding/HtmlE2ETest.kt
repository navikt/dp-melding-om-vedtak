package no.nav.dagpenger.vedtaksmelding

import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.UUID
import org.junit.jupiter.api.Disabled

internal class HtmlE2ETest {
    @Disabled
    @Test
    fun `Lage html`() {
        val behandlingId = UUID.fromString("0194a6e3-1919-766c-a02b-839319759913")

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
        val jsonString = """
                        {
                            "behandlingstype": "RETT_TIL_DAGPENGER",
                            "fornavn": "Test ForNavn",
                            "etternavn": "Test EtterNavn",
                            "fodselsnummer": "12345678901",
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
                        """.trimIndent()
        runBlocking {
            client
                .post("https://dp-melding-om-vedtak.intern.dev.nav.no/melding-om-vedtak/$behandlingId/html") {
                    header(HttpHeaders.Authorization, "Bearer $token")
                    header(HttpHeaders.ContentType, "application/json")
                    setBody(jsonString)
                }.let { response ->
                    response.status shouldBe HttpStatusCode.OK
                    println(response.bodyAsText())
                }

            client
                .post("https://dp-melding-om-vedtak.intern.dev.nav.no/melding-om-vedtak/$behandlingId/vedtaksmelding") {
                    header(HttpHeaders.Authorization, "Bearer $token")
                    header(HttpHeaders.ContentType, "application/json")
                    setBody(jsonString)
                }.let { response ->
                    response.status shouldBe HttpStatusCode.OK
                    println(response.bodyAsText())
                }
        }
    }
}
