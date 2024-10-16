package no.nav.dagpenger.vedtaksmelding

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
import io.ktor.util.pipeline.PipelineContext
import mu.KotlinLogging
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDTO
import no.nav.dagpenger.saksbehandling.api.models.OpplysningDTO
import no.nav.dagpenger.saksbehandling.api.models.UtvidetBeskrivelseDTO
import no.nav.dagpenger.vedtaksmelding.apiconfig.apiConfig
import no.nav.dagpenger.vedtaksmelding.apiconfig.jwt
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import java.util.UUID

private val sikkerlogger = KotlinLogging.logger("tjenestekall")

fun Application.meldingOmVedtakApi(mediator: Mediator) {
    apiConfig()
    routing {
        authenticate("azureAd") {
            get("/melding-om-vedtak/{behandlingId}") {
                val behandlingId = call.parseUUID()
                val saksbehandler = call.parseSaksbehandler()
                val vedtaksmelding = mediator.hentVedtaksmelding(behandlingId, saksbehandler)
                val meldingOmVedtakDTO =
                    MeldingOmVedtakDTO(
                        brevblokkIder = vedtaksmelding.hentBrevBlokkIder(),
                        opplysninger =
                            vedtaksmelding.hentOpplysninger().map {
                                OpplysningDTO(
                                    tekstId = it.opplysningTekstId,
                                    verdi = it.verdi,
                                    datatype = it.datatype,
                                )
                            },
                        utvidedeBeskrivelser =
                            vedtaksmelding.hentUtvidedeBeskrivelser().map {
                                UtvidetBeskrivelseDTO(
                                    brevblokkId = it.brevblokkId,
                                    tekst = it.tekst,
                                    sistEndretTidspunkt = it.sistEndretTidspunkt,
                                )
                            },
                    )
                sikkerlogger.info { "Melding om vedtak for behandlingId: $behandlingId: $meldingOmVedtakDTO" }
                call.respond(meldingOmVedtakDTO)
            }
            put("/melding-om-vedtak/{behandlingId}/{brevblokkId}/utvidet-beskrivelse") {
                requirePlainText()

                val behandlingId = call.parseUUID()
                val brevblokkId = call.parameters["brevblokkId"].toString()
                val utvidetBeskrivelseTekst = call.receiveText()
                val utvidetBeskrivelse =
                    UtvidetBeskrivelse(
                        behandlingId = behandlingId,
                        brevblokkId = brevblokkId,
                        tekst = utvidetBeskrivelseTekst,
                    )
                mediator.lagreUtvidetBeskrivelse(utvidetBeskrivelse)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}

private fun ApplicationCall.parseSaksbehandler(): Saksbehandler = Saksbehandler(this.request.jwt())

private fun ApplicationCall.parseUUID(): UUID {
    return this.parameters["behandlingId"]?.let {
        UUID.fromString(it)
    } ?: throw IllegalArgumentException("")
}

private fun PipelineContext<Unit, ApplicationCall>.requirePlainText() {
    require(call.request.headers["Content-Type"]!!.contains(ContentType.Text.Plain.toString())) {
        "Content-Type må være ${ContentType.Text.Plain}, men var ${call.request.headers["Content-Type"]}"
    }
}
