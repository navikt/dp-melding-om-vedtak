package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import io.kotest.assertions.json.shouldEqualSpecifiedJsonIgnoringOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Test
import java.util.UUID

class MeldingOmVedtakApiTest {
    private val testNavIdent = "Z999999"
    private val mockAzure = mockAzure()

    @Test
    fun `skal hente brevblokker til melding om vedtak`() =
        testApplication {
            application {
                meldingOmVedtakApi()
            }

            client.get("/melding-om-vedtak/${UUID.randomUUID()}") {
                autentisert(token = mockAzure.lagTokenMedClaims(claims = mapOf("groups" to listOf("SaksbehandlerADGruppe"))))
            }.let { response ->
                response.status shouldBe HttpStatusCode.OK
                response.contentType().toString() shouldContain "application/json"
                val expectedResponse =
                    """
                    [ {
                        "tekstId" : "someTekstId",
                        "opplysninger" : [ {
                            "tekstId" : "someOpplysningTekstId",
                            "verdi" : "someVerdi"
                        } ]
                    } ]
                    """.trimIndent()
                response.bodyAsText() shouldEqualSpecifiedJsonIgnoringOrder expectedResponse
            }
        }

    @Test
    fun `returner 401 hvis token ikke inneholder sakbehandler AD gruppe`() =
        testApplication {
            application {
                meldingOmVedtakApi()
            }

            client.get("/melding-om-vedtak/${UUID.randomUUID()}").let { response ->
                response.status shouldBe HttpStatusCode.Unauthorized
            }
        }

    fun HttpRequestBuilder.autentisert(token: String = gyldigSaksbehandlerToken()) {
        header(HttpHeaders.Authorization, "Bearer $token")
    }

    fun gyldigSaksbehandlerToken(adGrupper: List<String> = emptyList()): String =
        mockAzure.lagTokenMedClaims(
            mapOf(
                "groups" to listOf("SaksbehandlerADGruppe") + adGrupper,
                "NAVident" to testNavIdent,
            ),
        )
}
