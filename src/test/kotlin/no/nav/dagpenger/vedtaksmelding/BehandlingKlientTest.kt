package no.nav.dagpenger.vedtaksmelding

import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.vedtaksmelding.k8.setAzureAuthEnv
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.UUID

internal class BehandlingKlientTest {
    private val resourseRetriever = object {}.javaClass

    @Test
    fun `Test av request og parsing av respons`() {
        val behandlingId = UUID.fromString("0192c344-8223-7884-b951-d8f64a0744ae")
        val dpBehandlingApiUrl = "https://dp-behandling.intern.dev.nav.no/behandling"
        val responseJson = resourseRetriever.getResource("/json/behandling.json").readText()
        val inntektsperiode1FørsteMånedÅr =
            Opplysning(
                opplysningTekstId = "opplysning.inntektsperiode-1-forste-maaned-aar",
                navn = "opplysning.inntektsperiode-1-forste-maaned-aar",
                verdi = "oktober 2021",
                datatype = "tekst",
                opplysningId = "utledet",
            )
        val inntektsperiode2FørsteMånedÅr =
            Opplysning(
                opplysningTekstId = "opplysning.inntektsperiode-2-forste-maaned-aar",
                navn = "opplysning.inntektsperiode-2-forste-maaned-aar",
                verdi = "oktober 2022",
                datatype = "tekst",
                opplysningId = "utledet",
            )
        val inntektsperiode3FørsteMånedÅr =
            Opplysning(
                opplysningTekstId = "opplysning.inntektsperiode-3-forste-maaned-aar",
                navn = "opplysning.inntektsperiode-3-forste-maaned-aar",
                verdi = "oktober 2023",
                datatype = "tekst",
                opplysningId = "utledet",
            )
        val inntektsperiode1SisteMånedÅr =
            Opplysning(
                opplysningTekstId = "opplysning.inntektsperiode-1-siste-maaned-aar",
                navn = "opplysning.inntektsperiode-1-siste-maaned-aar",
                verdi = "september 2022",
                datatype = "tekst",
                opplysningId = "utledet",
            )
        val inntektsperiode2SisteMånedÅr =
            Opplysning(
                opplysningTekstId = "opplysning.inntektsperiode-2-siste-maaned-aar",
                navn = "opplysning.inntektsperiode-2-siste-maaned-aar",
                verdi = "september 2023",
                datatype = "tekst",
                opplysningId = "utledet",
            )
        val inntektsperiode3SisteMånedÅr =
            Opplysning(
                opplysningTekstId = "opplysning.inntektsperiode-3-siste-maaned-aar",
                navn = "opplysning.inntektsperiode-3-siste-maaned-aar",
                verdi = "september 2024",
                datatype = "tekst",
                opplysningId = "utledet",
            )
        val mockEngine =
            MockEngine { request ->
                request.headers["Authorization"] shouldBe "Bearer tulleToken"
                request.url.toString() shouldBe "$dpBehandlingApiUrl/$behandlingId"
                respond(responseJson, headers = headersOf("Content-Type", "application/json"))
            }
        val klient =
            BehandlngHttpKlient(
                dpBehandlingApiUrl = dpBehandlingApiUrl,
                tokenProvider = { "tulleToken" },
                httpClient = lagHttpKlient(engine = mockEngine),
            )
        runBlocking {
            klient.hentBehandling(behandling = behandlingId, saksbehandler = Saksbehandler("tulleToken")).getOrThrow()
                .let { behandling ->
                    behandling.id shouldBe behandlingId
                    behandling.tilstand shouldBe "ForslagTilVedtak"
                    behandling.opplysninger.let { opplysninger ->
                        opplysninger.size shouldBeGreaterThan 0
                        opplysninger.first { it.navn == "fagsakId" } shouldBe
                            Opplysning(
                                opplysningTekstId = "ukjent.opplysning.fagsakId",
                                navn = "fagsakId",
                                verdi = "15117125",
                                datatype = "heltall",
                                opplysningId = "0192c344-8223-7884-b951-d8f64a0744a9",
                            )
                        opplysninger.contains(inntektsperiode1FørsteMånedÅr)
                        opplysninger.contains(inntektsperiode2FørsteMånedÅr)
                        opplysninger.contains(inntektsperiode3FørsteMånedÅr)
                        opplysninger.contains(inntektsperiode1SisteMånedÅr)
                        opplysninger.contains(inntektsperiode2SisteMånedÅr)
                        opplysninger.contains(inntektsperiode3SisteMånedÅr)
                    }
                }
        }
    }

    @Test
    @Disabled
    fun `brukes for å hente ut en behandling manuelt, må ha saksbehandler token`() {
        val behandlingId = UUID.fromString("019145eb-6fbb-769f-b1b1-d2450b383a98")

        // saksbhehandler token, hentes fra azure-token-generator f,eks
        @Suppress("ktlint:standard:max-line-length")
        val token = ""

        // krever at man er logget inn paa naisdevice, dev-gcp
        // k8s kontekst er satt til dev-gcp og i riktig namespace
        setAzureAuthEnv(
            app = "dp-melding-om-vedtak",
            type = "azurerator.nais.io",
        ) {
            val tokenProvider = Configuration.dpBehandlingOboExchanger
            val klient =
                BehandlngHttpKlient(
                    dpBehandlingApiUrl = "https://dp-behandling.intern.dev.nav.no/behandling",
                    tokenProvider = tokenProvider,
                )
            runBlocking {
                val behandling =
                    klient.hentBehandling(behandling = behandlingId, saksbehandler = Saksbehandler(token))
                println(behandling)
            }
        }
    }
}
