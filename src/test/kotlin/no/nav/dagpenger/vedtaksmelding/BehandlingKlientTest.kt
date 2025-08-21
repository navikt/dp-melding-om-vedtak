package no.nav.dagpenger.vedtaksmelding

import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.vedtaksmelding.apiconfig.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.util.readFile
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.UUID

internal class BehandlingKlientTest {
    @Test
    fun `Skal kalle behandling endepunkt med riktig headers og parse response`() {
        val behandlingId = UUID.fromString("01937743-812d-7a69-b492-d25eb9768c68")
        val vedtakJson = "/json/vedtak.json".readFile()

        val behandlingKlient =
            BehandlingHttpKlient(
                dpBehandlingApiUrl = "http://localhost",
                tokenProvider = { "token" },
                httpClient =
                    lagHttpKlient(
                        MockEngine { request ->
                            request.url.encodedPath shouldBe "/$behandlingId/vedtak"
                            request.headers["Authorization"] shouldBe "Bearer token"
                            respond(
                                content = vedtakJson,
                                headers = headersOf("Content-Type" to listOf("application/json")),
                            )
                        },
                    ),
            )
        runBlocking {
            behandlingKlient.hentVedtak(behandlingId, Saksbehandler("token")).isSuccess shouldBe true
        }
    }

    @Test
    fun `Skal videre sende HttpProblemDTO ved feil`() {
        val behandlingId = UUID.fromString("01937743-812d-7a69-b492-d25eb9768c68")

        val behandlingKlient =
            BehandlingHttpKlient(
                dpBehandlingApiUrl = "http://localhost",
                tokenProvider = { "token" },
                httpClient =
                    lagHttpKlient(
                        expectSucces = false,
                        engine =
                            MockEngine { request ->
                                respond(
                                    content =
                                        """
                                        {
                                            "type" : "type",
                                            "title" : "title",
                                            "status" : 500,
                                            "detail" : "detail",
                                            "instance" : "instance"
                                        }
                                        """.trimIndent(),
                                    headers = headersOf("Content-Type" to listOf("application/json")),
                                    status = HttpStatusCode.InternalServerError,
                                )
                            },
                    ),
            )
        runBlocking {
            behandlingKlient.hentVedtak(behandlingId, Saksbehandler("token")).let {
                it.isFailure shouldBe true
                it.exceptionOrNull().let { throwable ->
                    require(throwable is HentVedtakException)
                    throwable.httpProblem.let { httpProblem ->
                        httpProblem.type shouldBe "type"
                        httpProblem.title shouldBe "title"
                        httpProblem.status shouldBe 500
                        httpProblem.detail shouldBe "detail"
                    }
                }
            }
        }
    }

    @Disabled
    @Test
    fun `brukes for å hente ut en behandling manuelt, må ha saksbehandler token`() {
        val behandlingId = UUID.fromString("01943b06-1a68-7dad-88e1-19e31cde711c")

        // saksbhehandler token, hentes fra azure-token-generator f,eks
        @Suppress("ktlint:standard:max-line-length")
        val token = ""

        val tokenProvider = Configuration.dpBehandlingTokenProvider
        val klient =
            BehandlingHttpKlient(
                dpBehandlingApiUrl = "https://dp-behandling.intern.dev.nav.no/behandling",
                tokenProvider = tokenProvider,
            )
        runBlocking {
//                val behandling =
//                    klient.hentBehandling(behandling = behandlingId, saksbehandler = Saksbehandler(token))
//                println(behandling)

            klient
                .hentVedtak(behandlingId = behandlingId, klient = Saksbehandler(token))
                .onFailure { println(it) }
                .getOrThrow()
                .let { vedtak ->
                    println(vedtak)
                }
        }
    }
}
