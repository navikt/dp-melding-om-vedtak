package no.nav.dagpenger.vedtaksmelding

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.assertions.json.shouldEqualSpecifiedJsonIgnoringOrder
import io.kotest.matchers.shouldBe
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import no.nav.dagpenger.saksbehandling.api.models.BehandlerDTO
import no.nav.dagpenger.saksbehandling.api.models.BehandlerEnhetDTO
import no.nav.dagpenger.saksbehandling.api.models.HttpProblemDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakResponseDTO
import no.nav.dagpenger.saksbehandling.api.models.UtvidetBeskrivelseDTO
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding.FasteBrevblokker.RETT_TIL_Å_KLAGE
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
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
    private val behandlingId = UUIDv7.ny()

    @Test
    fun `Skal lagre utvidet beskrivelse for en gitt brevblokk for en behandling`() {
        val brevblokkId = "brevblokkId"
        val utvidetBeskrivelseTekst = "En fritekst av noe slag"
        val utvidetBeskrivelseCapturingSlot = slot<UtvidetBeskrivelse>()
        val mediator =
            mockk<Mediator>().also {
                coEvery {
                    it.lagreUtvidetBeskrivelse(capture(utvidetBeskrivelseCapturingSlot))
                } returns LocalDateTime.MAX
            }
        testApplication {
            application {
                meldingOmVedtakApi(mediator)
            }

            client.put("/melding-om-vedtak/$behandlingId/$brevblokkId/utvidet-beskrivelse") {
                autentisert(token = saksbehandlerToken)
                header(HttpHeaders.ContentType, ContentType.Text.Plain)
                setBody(utvidetBeskrivelseTekst)
            }.let { response ->
                response.status shouldBe HttpStatusCode.OK
                response.bodyAsText() shouldEqualJson """{"sistEndretTidspunkt" : "+999999999-12-31T23:59:59.999999999"}"""
            }
        }
        utvidetBeskrivelseCapturingSlot.captured shouldBe
            UtvidetBeskrivelse(
                behandlingId = behandlingId,
                brevblokkId = brevblokkId,
                tekst = utvidetBeskrivelseTekst,
            )
    }

    @Test
    fun `Skal returnere en html ved bruk av melding-om-vedtak {behandlingId} html`() {
        val behandlingId = UUID.randomUUID()
        val requestBody =
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
            """.trimIndent()

        val mediator =
            mockk<Mediator>().also {
                coEvery {
                    it.hentVedtak(
                        behandlingId,
                        Saksbehandler(saksbehandlerToken),
                        meldingOmVedtakData =
                            MeldingOmVedtakDataDTO(
                                fornavn = "Test ForNavn",
                                etternavn = "Test EtterNavn",
                                fodselsnummer = "12345678901",
                                saksbehandler =
                                    BehandlerDTO(
                                        fornavn = "Ola",
                                        etternavn = "Nordmann",
                                        enhet =
                                            BehandlerEnhetDTO(
                                                navn = "Enhet Navn",
                                                postadresse = "Postadresse 123",
                                            ),
                                    ),
                                beslutter =
                                    BehandlerDTO(
                                        fornavn = "Kari",
                                        etternavn = "Nordmann",
                                        enhet =
                                            BehandlerEnhetDTO(
                                                navn = "Enhet Navn",
                                                postadresse = "Postadresse 123",
                                            ),
                                    ),
                            ),
                    )
                } returns
                    MeldingOmVedtakResponseDTO(
                        html = "<html><body>Test HTML Test ForNavn</body></html>",
                        utvidedeBeskrivelser =
                            listOf(
                                UtvidetBeskrivelseDTO(
                                    brevblokkId = RETT_TIL_Å_KLAGE.brevBlokkId,
                                    tekst = "hallo",
                                    sistEndretTidspunkt = LocalDateTime.MAX,
                                    tittel = "Tittel",
                                ),
                                UtvidetBeskrivelseDTO(
                                    brevblokkId = "brev.blokk.rett-til-aa-random",
                                    tekst = "random test",
                                    sistEndretTidspunkt = LocalDateTime.MAX,
                                    tittel = "Tittel 2",
                                ),
                            ),
                    )
            }
        testApplication {
            application {
                meldingOmVedtakApi(mediator)
            }

            client.post("/melding-om-vedtak/$behandlingId/html") {
                autentisert(token = saksbehandlerToken)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                setBody(requestBody)
            }.let { response ->
                response.status shouldBe HttpStatusCode.OK
                response.bodyAsText() shouldEqualSpecifiedJsonIgnoringOrder
                    //language=JSON
                    """
                    {
                      "utvidedeBeskrivelser": [
                         {
                           "brevblokkId": "${RETT_TIL_Å_KLAGE.brevBlokkId}",
                           "tekst": "hallo",
                           "sistEndretTidspunkt": "+999999999-12-31T23:59:59.999999999"
                         },
                         {
                           "brevblokkId": "brev.blokk.rett-til-aa-random",
                           "tekst": "random test",
                           "sistEndretTidspunkt": "+999999999-12-31T23:59:59.999999999"
                         }
                       ],
                     "html" : "<html><body>Test HTML Test ForNavn</body></html>"
                    } 
                    """.trimIndent()
            }
        }
    }

    @Test
    fun `Skal returnere en html ved bruk av melding-om-vedtak {behandlingId} vedtakshtml`() {
        val behandlingId = UUID.randomUUID()
        val mediator =
            mockk<Mediator>().also {
                coEvery {
                    it.hentEndeligVedtak(
                        behandlingId,
                        Saksbehandler(saksbehandlerToken),
                        meldingOmVedtakData =
                            MeldingOmVedtakDataDTO(
                                fornavn = "Test ForNavn",
                                etternavn = "Test EtterNavn",
                                fodselsnummer = "12345678901",
                                saksbehandler =
                                    BehandlerDTO(
                                        fornavn = "Ola",
                                        etternavn = "Nordmann",
                                        enhet =
                                            BehandlerEnhetDTO(
                                                navn = "Enhet Navn",
                                                postadresse = "Postadresse 123",
                                            ),
                                    ),
                                beslutter =
                                    BehandlerDTO(
                                        fornavn = "Kari",
                                        etternavn = "Nordmann",
                                        enhet =
                                            BehandlerEnhetDTO(
                                                navn = "Enhet Navn",
                                                postadresse = "Postadresse 123",
                                            ),
                                    ),
                            ),
                    )
                } returns "<html><body>Test HTML Test ForNavn</body></html>"
            }
        testApplication {
            application {
                meldingOmVedtakApi(mediator)
            }

            client.post("/melding-om-vedtak/$behandlingId/vedtaksmelding") {
                autentisert(token = saksbehandlerToken)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                setBody(requestBody)
            }.let { response ->
                response.status shouldBe HttpStatusCode.OK
                response.bodyAsText() shouldBe "<html><body>Test HTML Test ForNavn</body></html>"
            }
        }
    }

    @Test
    fun `Skal feile hvis requestbody ikke er riktig ved bruk av melding-om-vedtak {behandlingId} html`() {
        val behandlingId = UUID.randomUUID()
        val requestBody =
            """
            {
                "navn": "Test Navn",
                "fodselsnummer": "12345678901",
               
            }
            """.trimIndent()

        val mediator = Mediator(mockk(), mockk(), mockk())

        testApplication {
            application {
                meldingOmVedtakApi(mediator)
            }

            client.post("/melding-om-vedtak/$behandlingId/html") {
                autentisert(token = saksbehandlerToken)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                setBody(requestBody)
            }.let { response ->
                response.status shouldBe HttpStatusCode.InternalServerError
                response.bodyAsText() shouldEqualSpecifiedJsonIgnoringOrder
                    //language=JSON
                    """
                     {
                    "type" : "dagpenger.nav.no/saksbehandling:problem:uhåndtert-feil",
                    "title" : "Uhåndtert feil",
                    "status" : 500,
                    "detail" : "Failed to convert request body to class no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO",
                    "instance" : "dp-melding-om-vedtak/melding-om-vedtak/$behandlingId/html"
                    }
                    """.trimIndent()
            }
        }
    }

    @Test
    fun `Skal sende videre httpProblem fra dp-behandling ved feil`() {
        val behandlingId = UUID.randomUUID()
        val mediator =
            mockk<Mediator>().also {
                coEvery {
                    it.hentVedtak(
                        behandlingId,
                        any(),
                        any(),
                    )
                } throws
                    HentVedtakException(
                        HttpStatusCode.InternalServerError,
                        HttpProblemDTO(
                            "dagpenger.nav.no/saksbehandling:problem:uhåndtert-feil",
                            "Uhåndtert feil",
                            500,
                            "detail",
                            "/melding-om-vedtak/$behandlingId/html",
                        ),
                    )
            }
        testApplication {
            application {
                meldingOmVedtakApi(mediator)
            }

            client.post("/melding-om-vedtak/$behandlingId/html") {
                autentisert(token = saksbehandlerToken)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                setBody(requestBody)
            }.let { response ->
                response.status shouldBe HttpStatusCode.InternalServerError
                response.bodyAsText() shouldEqualSpecifiedJsonIgnoringOrder
                    //language=JSON
                    """
                     {
                    "type" : "dagpenger.nav.no/saksbehandling:problem:uhåndtert-feil",
                    "title" : "Uhåndtert feil",
                    "status" : 500,
                    "detail" : "detail",
                    "instance" : "/melding-om-vedtak/$behandlingId/html"
                    }
                    """.trimIndent()
            }
        }
    }

    @Test
    fun `Skal ha swagger APi`() {
        testApplication {
            application {
                meldingOmVedtakApi(mockk())
            }

            client.get("/openapi") {
            }.let { response ->
                response.status shouldBe HttpStatusCode.OK
            }
        }
    }

    private val requestBody =
        """
        {
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

    private fun HttpRequestBuilder.autentisert(token: String) {
        header(HttpHeaders.Authorization, "Bearer $token")
    }
}
