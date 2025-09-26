package no.nav.dagpenger.vedtaksmelding

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.withLoggingContext
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authenticate
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.request.receive
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
import no.nav.dagpenger.saksbehandling.api.models.BrevVariantRequestDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.saksbehandling.api.models.UtvidetBeskrivelseSistEndretTidspunktDTO
import no.nav.dagpenger.saksbehandling.api.models.UtvidetBeskrivelseTekstDTO
import no.nav.dagpenger.vedtaksmelding.apiconfig.apiConfig
import no.nav.dagpenger.vedtaksmelding.apiconfig.klient
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import java.util.UUID

private val logger = KotlinLogging.logger {}

fun Application.meldingOmVedtakApi(mediator: Mediator) {
    apiConfig()
    routing {
        swaggerUI(path = "openapi", swaggerFile = "melding-om-vedtak-api.yaml")
        if (Configuration.isNotProd) {
            staticResources(
                "/static",
                "static",
            )
        }
        authenticate("azureAd", "azureAd-M2M") {
            put("/melding-om-vedtak/{behandlingId}/brev-variant") {
                val behandlingId = call.parseUUID()
                val brevVariantRequest = call.receive<BrevVariantRequestDTO>()
                withLoggingContext("behandlingId" to behandlingId.toString()) {
                    runCatching {
                        mediator.lagreBrevVariant(
                            behandlingId = behandlingId,
                            brevVariant = brevVariantRequest.brevVariant,
                        )
                        call.respond(HttpStatusCode.NoContent)
                    }.onFailure { t ->
                        logger.error(t) { "Feil ved lagring av brev-variant for behandlingId: $behandlingId" }
                        throw t
                    }
                }
            }
            post("/melding-om-vedtak/{behandlingId}/html") {
                val behandlingId = call.parseUUID()
                val klient = call.request.klient()
                val meldingOmVedtakData = call.receive<MeldingOmVedtakDataDTO>()
                withLoggingContext("behandlingId" to behandlingId.toString()) {
                    runCatching {
                        val meldingOmVedtakResponseDTO =
                            mediator.hentForhåndsvisning(
                                behandlingId = behandlingId,
                                klient = klient,
                                meldingOmVedtakData = meldingOmVedtakData,
                            )
                        call.respond(meldingOmVedtakResponseDTO)
                    }.onFailure { t ->
                        logger.warn(t) { "Feil ved henting av html ved forhåndsvisning av brev for behandlingId: $behandlingId" }
                        throw t
                    }
                }
            }
            post("/melding-om-vedtak/{behandlingId}/vedtaksmelding") {
                val behandlingId = call.parseUUID()
                val klient = call.request.klient()

                val meldingOmVedtakData = call.receive<MeldingOmVedtakDataDTO>()
                withLoggingContext("behandlingId" to behandlingId.toString()) {
                    runCatching {
                        val vedtaksHtml =
                            mediator.hentEndeligBrev(
                                behandlingId = behandlingId,
                                klient = klient,
                                meldingOmVedtakData = meldingOmVedtakData,
                            )
                        call.respond(vedtaksHtml)
                    }.onFailure { t ->
                        logger.warn(
                            t,
                        ) { "Feil ved henting av endelig brev for behandlingId: $behandlingId" }
                        throw t
                    }
                }
            }
            put("/melding-om-vedtak/{behandlingId}/{brevblokkId}/utvidet-beskrivelse") {
                requirePlainText()

                val behandlingId = call.parseUUID()
                val brevblokkId = call.parameters["brevblokkId"].toString()
                withLoggingContext("behandlingId" to behandlingId.toString(), "brevblokkId" to brevblokkId) {
                    val utvidetBeskrivelseTekst = call.receiveText()
                    val utvidetBeskrivelse =
                        UtvidetBeskrivelse(
                            behandlingId = behandlingId,
                            brevblokkId = brevblokkId,
                            tekst = utvidetBeskrivelseTekst,
                        )
                    val sistEndretTidspunkt = mediator.lagreUtvidetBeskrivelse(utvidetBeskrivelse)
                    call.respond(HttpStatusCode.OK, UtvidetBeskrivelseSistEndretTidspunktDTO(sistEndretTidspunkt))
                }
            }

            put("/melding-om-vedtak/{behandlingId}/{brevblokkId}/utvidet-beskrivelse-json") {
                val behandlingId = call.parseUUID()
                val brevblokkId = call.parameters["brevblokkId"].toString()
                withLoggingContext("behandlingId" to behandlingId.toString(), "brevblokkId" to brevblokkId) {
                    val utvidetBeskrivelseTekstDTO = call.receive<UtvidetBeskrivelseTekstDTO>()
                    val utvidetBeskrivelse =
                        UtvidetBeskrivelse(
                            behandlingId = behandlingId,
                            brevblokkId = brevblokkId,
                            tekst = utvidetBeskrivelseTekstDTO.tekst,
                        )
                    val sistEndretTidspunkt = mediator.lagreUtvidetBeskrivelse(utvidetBeskrivelse)
                    call.respond(HttpStatusCode.OK, UtvidetBeskrivelseSistEndretTidspunktDTO(sistEndretTidspunkt))
                }
            }
        }
    }
}

private fun ApplicationCall.parseUUID(): UUID =
    this.parameters["behandlingId"]?.let {
        UUID.fromString(it)
    } ?: throw IllegalArgumentException("")

private fun RoutingContext.requirePlainText() {
    require(call.request.headers["Content-Type"]!!.contains(ContentType.Text.Plain.toString())) {
        "Content-Type må være ${ContentType.Text.Plain}, men var ${call.request.headers["Content-Type"]}"
    }
}
