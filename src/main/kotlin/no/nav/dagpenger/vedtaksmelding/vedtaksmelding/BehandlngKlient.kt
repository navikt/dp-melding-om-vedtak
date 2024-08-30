package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
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
    private val httpClient: HttpClient = lagHttpKlient(engine = CIO.create { }),
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
                                navn = opplysningDTO.navn,
                                verdi = opplysningDTO.verdi,
                                datatype = opplysningDTO.datatype,
                                opplysningId = opplysningDTO.id,
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
