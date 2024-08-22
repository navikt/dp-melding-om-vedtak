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
import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.Behandling
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import java.util.UUID

private val logger = KotlinLogging.logger {}

interface BehandlingKlient {
    suspend fun hentBehandling(
        behandling: UUID,
        saksbehandler: Saksbehandler,
    ): Result<Behandling>
}

internal class BehandlngHttpKlient(
    private val dpBehandlingApiUrl: String,
    private val tokenProvider: (String) -> String,
    private val httpClient: HttpClient = createHttpClient(engine = CIO.create { }),
) : BehandlingKlient {
    override suspend fun hentBehandling(
        behandling: UUID,
        saksbehandler: Saksbehandler,
    ): Result<Behandling> {
        return kotlin.runCatching {
            httpClient.get(urlString = "$dpBehandlingApiUrl/$behandling") {
                header(HttpHeaders.Authorization, "Bearer ${tokenProvider.invoke(saksbehandler.token)}")
                accept(ContentType.Application.Json)
            }.body<BehandlingDTO>().let { behandlingDTO ->
                Behandling(
                    id = behandlingDTO.behandlingId,
                    tilstand = behandlingDTO.tilstand,
                    opplysninger =
                        behandlingDTO.opplysning.map { opplysningDTO ->
                            Opplysning(
                                id = opplysningDTO.id,
                                navn = opplysningDTO.navn,
                                verdi = opplysningDTO.verdi,
                                datatype = opplysningDTO.datatype,
                            )
                        }.toSet(),
                )
            }
        }.onFailure { logger.error(it) { "Kall til dp-behandling feilet ${it.message}" } }
    }
}

private data class BehandlingDTO(
    val behandlingId: String,
    val tilstand: String,
    val opplysning: List<OpplysningDTO>,
) {
    data class OpplysningDTO(
        val id: String,
        val navn: String,
        val verdi: String,
        val datatype: String,
    )
}

fun createHttpClient(engine: HttpClientEngine) =
    HttpClient(engine) {
        expectSuccess = true
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
        }
    }
