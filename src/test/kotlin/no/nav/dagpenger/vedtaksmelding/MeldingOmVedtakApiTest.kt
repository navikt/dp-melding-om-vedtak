package no.nav.dagpenger.vedtaksmelding

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.assertions.json.shouldEqualSpecifiedJsonIgnoringOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
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
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import no.nav.dagpenger.saksbehandling.api.models.BehandlerDTO
import no.nav.dagpenger.saksbehandling.api.models.BehandlerEnhetDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding.FasteBrevblokker.RETT_TIL_Å_KLAGE
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.ENHETSLØS
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
    private val saksbehandler = Saksbehandler(saksbehandlerToken)
    private val behandlingId = UUIDv7.ny()

    @Test
    fun `skal hente brevblokker til melding om vedtak`() {
        val brevBlokker = listOf("A", "B", "C")
        val opplysning1 =
            Opplysning(
                opplysningTekstId = "opplysning.opplysning-1",
                verdi = "true",
                datatype = BOOLSK,
                enhet = ENHETSLØS,
            )
        val opplysning2 =
            Opplysning(
                opplysningTekstId = "opplysning.opplysning-2",
                verdi = "true",
                datatype = BOOLSK,
                enhet = ENHETSLØS,
            )
        val opplysninger = listOf(opplysning1, opplysning2)
        val utvidedeBeskrivelser =
            listOf(
                UtvidetBeskrivelse(
                    behandlingId = behandlingId,
                    brevblokkId = "A",
                    tekst = "Utvidet beskrivelse for brevblokk A",
                    sistEndretTidspunkt = LocalDateTime.MAX,
                ),
                UtvidetBeskrivelse(
                    behandlingId = behandlingId,
                    brevblokkId = "B",
                    tekst = "Utvidet beskrivelse for brevblokk B",
                    sistEndretTidspunkt = LocalDateTime.MIN,
                ),
            )
        val vedtaksmelding =
            mockk<VedtakMelding>().also {
                coEvery { it.brevBlokkIder() } returns brevBlokker
                coEvery { it.hentOpplysninger() } returns opplysninger
            }
        val mediator =
            mockk<Mediator>().also {
                coEvery {
                    it.hentVedtaksmelding(behandlingId, saksbehandler)
                } returns Result.success(vedtaksmelding)
                coEvery { it.hentUtvidedeBeskrivelser(behandlingId) } returns utvidedeBeskrivelser
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
                          "tekstId": "opplysning.opplysning-1",
                          "verdi": "true",
                          "datatype": "boolean"
                        },
                        {
                          "tekstId": "opplysning.opplysning-2",
                          "verdi": "true",
                          "datatype": "boolean"
                        }
                      ],
                      "utvidedeBeskrivelser": [
                        {
                          "brevblokkId": "A",
                          "tekst": "Utvidet beskrivelse for brevblokk A",
                          "sistEndretTidspunkt": "+999999999-12-31T23:59:59.999999999",
                          "tittel": "Ukjent tittel"
                        },
                        {
                          "brevblokkId": "B",
                          "tekst": "Utvidet beskrivelse for brevblokk B",
                          "sistEndretTidspunkt": "-999999999-01-01T00:00:00",
                          "tittel": "Ukjent tittel"
                        }
                      ]
                    }

                    """.trimIndent()
            }
        }
        coVerify(exactly = 1) {
            mediator.hentVedtaksmelding(behandlingId, saksbehandler)
        }
    }

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
    fun `Returnerer en default MeldingOmVedtakDTO dersom underliggende systemer feiler`() {
        val utvidedeBeskrivelser =
            listOf(
                UtvidetBeskrivelse(
                    behandlingId = behandlingId,
                    brevblokkId = "A",
                    tekst = "Utvidet beskrivelse for brevblokk A",
                    sistEndretTidspunkt = LocalDateTime.MAX,
                ),
                UtvidetBeskrivelse(
                    behandlingId = behandlingId,
                    brevblokkId = "B",
                    tekst = "Utvidet beskrivelse for brevblokk B",
                    sistEndretTidspunkt = LocalDateTime.MIN,
                ),
            )
        testApplication {
            application {
                meldingOmVedtakApi(
                    mockk<Mediator>().also {
                        coEvery { it.hentVedtaksmelding(behandlingId, saksbehandler) } returns
                            Result.failure(
                                RuntimeException("Noe gikk galt"),
                            )
                        coEvery { it.hentUtvidedeBeskrivelser(behandlingId) } returns utvidedeBeskrivelser
                    },
                )
            }
            client.get("/melding-om-vedtak/$behandlingId") {
                autentisert(token = saksbehandlerToken)
            }.let { response ->
                response.status shouldBe HttpStatusCode.OK
                response.bodyAsText() shouldEqualJson
                    """{"brevblokkIder" : ["${RETT_TIL_Å_KLAGE.brevBlokkId}"], "opplysninger" : [], "utvidedeBeskrivelser" : []}"""
            }
        }
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
                    it.hentVedtakHtml(
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
                coEvery { it.hentUtvidedeBeskrivelser(behandlingId, saksbehandler) } returns
                    listOf(
                        UtvidetBeskrivelse(
                            behandlingId = behandlingId,
                            brevblokkId = RETT_TIL_Å_KLAGE.brevBlokkId,
                            tekst = "hallo",
                            sistEndretTidspunkt = LocalDateTime.MAX,
                        ),
                        UtvidetBeskrivelse(
                            behandlingId = behandlingId,
                            brevblokkId = "brev.blokk.rett-til-aa-random",
                            tekst = "random test",
                            sistEndretTidspunkt = LocalDateTime.MAX,
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
        val requestBody =
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
            }
        }
    }

    private fun HttpRequestBuilder.autentisert(token: String) {
        header(HttpHeaders.Authorization, "Bearer $token")
    }
}
