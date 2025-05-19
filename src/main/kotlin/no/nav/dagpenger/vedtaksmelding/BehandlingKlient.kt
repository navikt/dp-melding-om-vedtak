package no.nav.dagpenger.vedtaksmelding

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import mu.KotlinLogging
import no.nav.dagpenger.saksbehandling.api.models.HttpProblemDTO
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import java.util.UUID

private val sikkerlogg = KotlinLogging.logger("tjenestekall")
private val log = KotlinLogging.logger { }

interface BehandlingKlient {
    suspend fun hentVedtak(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
    ): Result<Vedtak>
}

internal class BehandlingHttpKlient(
    private val dpBehandlingApiUrl: String,
    private val tokenProvider: (String) -> String,
    private val httpClient: HttpClient = lagHttpKlient(engine = CIO.create { }, expectSucces = false),
) : BehandlingKlient {
    private suspend fun hentVedtakJson(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
    ): Result<String> {
        return httpClient.get(urlString = "$dpBehandlingApiUrl/$behandlingId/vedtak") {
            header(HttpHeaders.Authorization, "Bearer ${tokenProvider.invoke(saksbehandler.token)}")
            accept(ContentType.Application.Json)
        }.let { response ->
            val responseTekst = response.bodyAsText()
            when (response.status == HttpStatusCode.OK) {
                true -> {
                    sikkerlogg.info { "Hentet vedtak for behandling $behandlingId $responseTekst" }
                    Result.success(responseTekst)
                }

                false -> {
                    log.error { "Feil ved henting av vedtak for behandling $behandlingId: $responseTekst" }
                    Result.failure(HentVedtakException(response.status, responseTekst.tilHttpProblem(response.status)))
                }
            }
        }
    }

    override suspend fun hentVedtak(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
    ): Result<Vedtak> {
        return hentVedtakJson(behandlingId, saksbehandler).map { VedtakMapper(it).vedtak() }
    }
}

private fun String.tilHttpProblem(status: HttpStatusCode): HttpProblemDTO {
    return try {
        Configuration.objectMapper.readValue(this, HttpProblemDTO::class.java)
    } catch (e: Exception) {
        HttpProblemDTO(
            type = "Ukjent",
            title = "Ukjent feil ved kall mot dp-behandling",
            status = status.value,
            detail = this,
        )
    }
}

internal class HentVedtakException(val statusCode: HttpStatusCode, val httpProblem: HttpProblemDTO) : RuntimeException()
