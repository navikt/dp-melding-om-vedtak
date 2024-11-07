package no.nav.dagpenger.vedtaksmelding

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.assertions.json.shouldEqualSpecifiedJsonIgnoringOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
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
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import no.nav.dagpenger.vedtaksmelding.model.VedtaksMelding
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
        val vedtak =
            mockk<VedtaksMelding>().also {
                coEvery { it.hentBrevBlokkIder() } returns brevBlokker
                coEvery { it.hentOpplysninger() } returns opplysninger
                every { it.hentUtvidedeBeskrivelser() } returns utvidedeBeskrivelser
            }
        val mediator =
            mockk<Mediator>().also {
                coEvery {
                    it.hentVedtaksmelding(behandlingId, saksbehandler)
                } returns vedtak
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
                      ],
                      "utvidedeBeskrivelser": [
                        {
                          "brevblokkId": "A",
                          "tekst": "Utvidet beskrivelse for brevblokk A",
                          "sistEndretTidspunkt": "+999999999-12-31T23:59:59.999999999"
                        },
                        {
                          "brevblokkId": "B",
                          "tekst": "Utvidet beskrivelse for brevblokk B",
                          "sistEndretTidspunkt": "-999999999-01-01T00:00:00"
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
    fun `returner 401 hvis token ikke inneholder sakbehandler AD gruppe`() =
        testApplication {
            application {
                meldingOmVedtakApi(mockk())
            }

            client.get("/melding-om-vedtak/${UUID.randomUUID()}").status shouldBe HttpStatusCode.Unauthorized
        }

    private fun HttpRequestBuilder.autentisert(token: String) {
        header(HttpHeaders.Authorization, "Bearer $token")
    }
}
