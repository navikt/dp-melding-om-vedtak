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
import org.junit.jupiter.api.Test
import java.util.UUID

internal class HtmlE2ETest {
    @Test
    fun `Lage html`() {
        val behandlingId = UUID.fromString("01943b06-1a68-7dad-88e1-19e31cde711c")

        // dp-melding-om-vedtak token, hentes fra azure-token-generator f,eks
        @Suppress("ktlint:standard:max-line-length")
        val token =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IllUY2VPNUlKeXlxUjZqekRTNWlBYnBlNDJKdyJ9.eyJhdWQiOiJhMjUzYzc4ZC1mMDYyLTRjZWUtODAyYS0xOWM3ZDJlYzFkZTYiLCJpc3MiOiJodHRwczovL2xvZ2luLm1pY3Jvc29mdG9ubGluZS5jb20vOTY2YWM1NzItZjViNy00YmJlLWFhODgtYzc2NDE5YzBmODUxL3YyLjAiLCJpYXQiOjE3MzgyMzAwMjEsIm5iZiI6MTczODIzMDAyMSwiZXhwIjoxNzM4MjM0MDkxLCJhaW8iOiJBV1FBbS84WkFBQUFocjJyRk5uRU5rTFlwZ0ZKNmRmTXUyV0FWOU1sNVNUemQvVm1BaDdSK09Fem5oM2dNcEhodWN0RkFQbCt0WGJEcVpmdStQYzI4Z3AvL1VHNjUxcGFyRUY2bVFmeHFHN29WZHFlZkJmTVNIOURIb1V6bWtkMzlHeXBXWWJLNlpxQiIsImF6cCI6IjVmMjhjMTBmLWVjZDUtNDVlZi05NmI4LWRiNDMxMTgwMmU4OSIsImF6cGFjciI6IjIiLCJncm91cHMiOlsiM2UyODQ2NmYtYzUzZC00NmRhLThiNDQtYTRhYmMyYWQ0NTkzIl0sIm5hbWUiOiJGX1o5OTQwMzAgRV9aOTk0MDMwIiwib2lkIjoiMDU1MzQ5YzYtMTY5MS00NzlhLWE5YjctOWI5YzBhOTY4ODY4IiwicHJlZmVycmVkX3VzZXJuYW1lIjoiRl9aOTk0MDMwLkVfWjk5NDAzMEB0cnlnZGVldGF0ZW4ubm8iLCJyaCI6IjEuQVVjQWNzVnFscmYxdmt1cWlNZGtHY0Q0VVkzSFU2Smk4TzVNZ0NvWng5THNIZVlOQWQ1SEFBLiIsInNjcCI6ImRlZmF1bHRhY2Nlc3MiLCJzaWQiOiIwMDEzYjZmOS03NmEyLWNhODEtNmQ4OC03ZjFiZTFmZGJhNDgiLCJzdWIiOiJ4ZGl5X1piX2VWa0U0NThzTlJMSGpQZlY2T3FwVlZ6OU51TnZOR0steHBzIiwidGlkIjoiOTY2YWM1NzItZjViNy00YmJlLWFhODgtYzc2NDE5YzBmODUxIiwidXRpIjoicWhOZ0NaSFdJVTZnTWNFbWVmRXRBQSIsInZlciI6IjIuMCIsIk5BVmlkZW50IjoiWjk5NDAzMCIsImF6cF9uYW1lIjoiZGV2LWdjcDphdXJhOmF6dXJlLXRva2VuLWdlbmVyYXRvciJ9.dxeA3FZKb5kC89pTRLclZzKd-a5SN3udXIxU6n_vUuY7TtJkwBjWZGtFqTkiMICy4XgMcjPQ5_VFkHUdG3hDsINpLR1jpcSoV4EAjwGc2u-k0DdxMKx9gyh4-8qHhK68lSjrEWJITYCZGc3WY_GsAQH2LD7EJG_lLAsbxMLyvhU01LfNVGdCCYMbwxu8nfbHuNOI0gOTFcCmDGAOsNsQaIiM40ue3eAZd3dhZwh7au6ilYSGD0Ix6avn7SltI8-gZV8HMNRs8C4fWgaYc78dWSqV3DFiyaYTw4yMl1geFx3bxxPSOdnWXtiXFddgB-XRmfokS0YitL7RtWrszrBqvA"

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
