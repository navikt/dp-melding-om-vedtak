package no.nav.dagpenger.vedtaksmelding

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import no.nav.dagpenger.saksbehandling.api.models.HttpProblemDTO
import no.nav.dagpenger.vedtaksmelding.apiconfig.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageVedtak
import no.nav.dagpenger.vedtaksmelding.model.klage.KlagevedtakMapper
import java.util.UUID

private val sikkerlogg = KotlinLogging.logger("tjenestekall")
private val log = KotlinLogging.logger { }

interface KlageBehandlingKlient {
    suspend fun hentVedtak(
        behandlingId: UUID,
        klient: Saksbehandler,
    ): Result<KlageVedtak>
}

internal class KlageBehandlingHttpKlient(
    private val dpSaksbehandlingKlageApiUrl: String,
    private val tokenProvider: (String) -> String,
    private val httpClient: HttpClient = lagHttpKlient(engine = CIO.create { }, expectSucces = false),
) : KlageBehandlingKlient {
    private suspend fun hentVedtakJson(
        behandlingId: UUID,
        klient: Saksbehandler,
    ): Result<String> =
        httpClient
            .get(urlString = "$dpSaksbehandlingKlageApiUrl/klage/$behandlingId") {
                header(HttpHeaders.Authorization, "Bearer ${tokenProvider.invoke(klient.token)}")
                accept(ContentType.Application.Json)
            }.let { response ->
                val responseTekst = response.bodyAsText()
                when (response.status == HttpStatusCode.OK) {
                    true -> {
                        sikkerlogg.info { "Hentet vedtak for klagebehandling $behandlingId $responseTekst" }
                        Result.success(responseTekst)
                    }

                    false -> {
                        log.error { "Feil ved henting av vedtak for behandling $behandlingId: $responseTekst" }
                        Result.failure(HentVedtakException(response.status, responseTekst.tilHttpProblem(response.status)))
                    }
                }
            }

    override suspend fun hentVedtak(
        behandlingId: UUID,
        klient: Saksbehandler,
    ): Result<KlageVedtak> {
        // lag en tilsvarende KlageBehandlingMapper
        return hentVedtakJson(behandlingId, klient).map {
            KlagevedtakMapper(it).vedtak()
        }
    }
}

private fun String.tilHttpProblem(status: HttpStatusCode): HttpProblemDTO =
    try {
        Configuration.objectMapper.readValue(this, HttpProblemDTO::class.java)
    } catch (e: Exception) {
        HttpProblemDTO(
            type = "Ukjent",
            title = "Ukjent feil ved kall mot dp-behandling",
            status = status.value,
            detail = this,
        )
    }
