package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import io.kotest.matchers.shouldNotBe
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.server.application.call
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Test

class AuthTest {
    private val mockAzure = mockAzure()

    @Test
    fun `henter ut token`() {
        var jwt: String? = null
        testApplication {
            application {
                routing {
                    get("/token") {
                        jwt = call.request.jwt()
                    }
                }
            }

            client.get("/token") {
                header(
                    HttpHeaders.Authorization,
                    "Bearer ${
                        mockAzure.lagTokenMedClaims(
                            claims =
                                mapOf(
                                    "groups" to listOf("SaksbehandlerADGruppe"),
                                    "NAVident" to "Z999999",
                                ),
                        )
                    }",
                )
            }
        }
        jwt shouldNotBe null
    }
}
