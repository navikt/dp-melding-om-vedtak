package no.nav.dagpenger.vedtaksmelding

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
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.VedtaksMelding
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test
import java.util.UUID

class MeldingOmVedtakApiTest {
    private val testNavIdent = "Z999999"
    private val saksbehandlerToken =
        mockAzure().lagTokenMedClaims(
            claims =
                mapOf(
                    "groups" to listOf("SaksbehandlerADGruppe"),
                    "NAVident" to testNavIdent,
                ),
        )
    private val saksbehandler = Saksbehandler(saksbehandlerToken)
    private val behandlingId = UUIDv7.ny()

    @Test
    fun `skal hente brevblokker til melding om vedtak`() {
        val brevBlokker = listOf("A", "B", "C")
        val opplysning1 =
            Opplysning(
                opplysningTekstId = "opplysning.krav-paa-dagpenger",
                navn = "Krav på dagpenger",
                verdi = "true",
                datatype = "boolean",
                opplysningId = "test",
            )
        val opplysning2 =
            Opplysning(
                opplysningTekstId = "opplysning.krav-til-minsteinntekt",
                navn = "Krav til minsteinntekt",
                verdi = "true",
                datatype = "boolean",
                opplysningId = "test",
            )
        val opplysninger = listOf(opplysning1, opplysning2)
        val vedtak =
            mockk<VedtaksMelding>().also {
                coEvery { it.hentBrevBlokkIder() } returns brevBlokker
                coEvery { it.hentOpplysninger() } returns opplysninger
            }
        val mediator =
            mockk<Mediator>().also {
                coEvery { it.sendVedtak(behandlingId, saksbehandler) } returns vedtak
            }
        testApplication {
            application {
                meldingOmVedtakApi(mediator)
            }

            client.get("/melding-om-vedtak/$behandlingId") {
                autentisert(token = saksbehandlerToken)
            }.let { response ->
                response.status shouldBe HttpStatusCode.OK
                response.contentType().toString() shouldContain "application/json"
                response.bodyAsText() shouldEqualSpecifiedJsonIgnoringOrder
                    //language=json
                    """
                                          {
                                          "brevblokkIder": [
                                            "A",
                                            "B",
                                            "C"
                                          ],
                                          "opplysninger": [
                                            {
                                              "tekstId": "opplysning.krav-paa-dagpenger",
                                              "verdi": "true",
                                              "datatype": "boolean"
                                            },
                                            {
                                              "tekstId": "opplysning.krav-til-minsteinntekt",
                                              "verdi": "true",
                                              "datatype": "boolean"
                                            }
                                          ]
                    }
                    """.trimIndent()
            }
        }
        coVerify(exactly = 1) {
            mediator.sendVedtak(behandlingId, saksbehandler)
        }
    }

    @Test
    fun `returner 401 hvis token ikke inneholder sakbehandler AD gruppe`() =
        testApplication {
            application {
                meldingOmVedtakApi(mockk())
            }

            client.get("/melding-om-vedtak/${UUID.randomUUID()}").let { response ->
                response.status shouldBe HttpStatusCode.Unauthorized
            }
        }

    fun HttpRequestBuilder.autentisert(token: String) {
        header(HttpHeaders.Authorization, "Bearer $token")
    }
}
