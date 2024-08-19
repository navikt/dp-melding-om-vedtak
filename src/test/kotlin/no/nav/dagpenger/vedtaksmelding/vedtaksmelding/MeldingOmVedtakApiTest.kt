package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import io.kotest.assertions.json.shouldEqualSpecifiedJsonIgnoringOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Test
import java.util.UUID

class MeldingOmVedtakApiTest {
    @Test
    fun `skal hente brevblokker til melding om vedtak`() =

        testApplication {
            application {
                meldingOmVedtakApi()
            }

            client.get("/melding-om-vedtak/${UUID.randomUUID()}").let { response ->
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
}
