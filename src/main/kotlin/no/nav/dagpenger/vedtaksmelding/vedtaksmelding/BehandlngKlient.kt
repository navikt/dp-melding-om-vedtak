package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.jackson.jackson
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import java.util.*

internal class BehandlngHttpKlient(
    private val dpBehandlingApiUrl: String,
    private val tokenProvider: () -> String,
    private val httpClient: HttpClient = createHttpClient(engine = CIO.create { }),
) : BehandlingKlient {
    override suspend fun hentOpplysninger(behandling: UUID): Result<Set<Opplysning>> {
        return kotlin.runCatching {
            httpClient.get(urlString = dpBehandlingApiUrl) {
                header(HttpHeaders.Authorization, "Bearer ${tokenProvider.invoke()}")
                accept(ContentType.Application.Json)
            }.body<List<Opplysning>>().toSet()
        }.onFailure { throwable -> logger.error(throwable) { "Kall til dp-behandling feilet" } }
    }
}

interface BehandlingKlient {
    suspend fun hentOpplysninger(behandlingId: UUID): Result<Set<Opplysning>>
}

fun createHttpClient(
    engine: HttpClientEngine,
) = HttpClient(engine) {
    expectSuccess = true

    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }
}

