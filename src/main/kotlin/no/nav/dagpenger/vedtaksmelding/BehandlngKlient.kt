package no.nav.dagpenger.vedtaksmelding

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import mu.withLoggingContext
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper
import java.util.UUID

private val sikkerlogg = KotlinLogging.logger("tjenestekall")

interface BehandlingKlient {
    suspend fun hentVedtak(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
    ): Result<Vedtak>
}

internal class BehandlingHttpKlient(
    private val dpBehandlingApiUrl: String,
    private val tokenProvider: (String) -> String,
    private val httpClient: HttpClient = lagHttpKlient(engine = CIO.create { }),
) : BehandlingKlient {
    private suspend fun hentVedtakJson(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            withLoggingContext("behandlingId" to behandlingId.toString()) {
                return@withContext httpClient.get(urlString = "$dpBehandlingApiUrl/$behandlingId/vedtak") {
                    header(HttpHeaders.Authorization, "Bearer ${tokenProvider.invoke(saksbehandler.token)}")
                    accept(ContentType.Application.Json)
                }.bodyAsText().let { vedtak ->
                    sikkerlogg.info { "Hentet vedtak: $vedtak" }
                    Result.success(vedtak)
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
