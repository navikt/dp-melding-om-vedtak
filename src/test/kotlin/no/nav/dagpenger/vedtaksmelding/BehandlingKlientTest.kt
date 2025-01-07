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

                        opplysninger.single { it.navn == "opplysning.inntektsperiode-1-siste-maaned-aar" }.verdi shouldBe "september 2024"
                        opplysninger.single { it.navn == "opplysning.inntektsperiode-1-forste-maaned-aar" }.verdi shouldBe "oktober 2023"
                        opplysninger.single { it.navn == "opplysning.inntektsperiode-2-siste-maaned-aar" }.verdi shouldBe "september 2023"
                        opplysninger.single { it.navn == "opplysning.inntektsperiode-2-forste-maaned-aar" }.verdi shouldBe "oktober 2022"
                        opplysninger.single { it.navn == "opplysning.inntektsperiode-3-siste-maaned-aar" }.verdi shouldBe "september 2022"
                        opplysninger.single { it.navn == "opplysning.inntektsperiode-3-forste-maaned-aar" }.verdi shouldBe "oktober 2021"
                    }
                }
        }
    }

    @Test
    @Disabled
    fun `brukes for å hente ut en behandling manuelt, må ha saksbehandler token`() {
        val behandlingId = UUID.fromString("")

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
                println(
                    klient.hentBehandling(behandling = behandlingId, saksbehandler = Saksbehandler(token)).getOrThrow(),
                )
            }
        }
    }
}
