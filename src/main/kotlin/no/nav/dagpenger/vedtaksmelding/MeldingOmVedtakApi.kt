package no.nav.dagpenger.vedtaksmelding

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
import mu.KotlinLogging
import mu.withLoggingContext
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakResponseDTO
import no.nav.dagpenger.saksbehandling.api.models.OpplysningDTO
import no.nav.dagpenger.saksbehandling.api.models.UtvidetBeskrivelseDTO
import no.nav.dagpenger.saksbehandling.api.models.UtvidetBeskrivelseSistEndretTidspunktDTO
import no.nav.dagpenger.vedtaksmelding.apiconfig.apiConfig
import no.nav.dagpenger.vedtaksmelding.apiconfig.jwt
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import java.util.UUID

private val sikkerlogger = KotlinLogging.logger("tjenestekall")
private val logger = KotlinLogging.logger {}

fun Application.meldingOmVedtakApi(mediator: Mediator) {
    apiConfig()
    routing {
        authenticate("azureAd") {
            get("/melding-om-vedtak/{behandlingId}") {
                val behandlingId = call.parseUUID()
                withLoggingContext("behandlingId" to behandlingId.toString()) {
                    val saksbehandler = call.parseSaksbehandler()
                    val utvidetBeskrivelse = mediator.hentUtvidedeBeskrivelser(behandlingId)
                    val meldingOmVedtakDTO: MeldingOmVedtakDTO =
                        runCatching {
                            mediator.hentVedtaksmelding(behandlingId, saksbehandler).map { vedtaksmelding ->
                                MeldingOmVedtakDTO(
                                    brevblokkIder = vedtaksmelding.brevBlokkIder(),
                                    opplysninger =
                                        vedtaksmelding.hentOpplysninger().map {
                                            OpplysningDTO(
                                                tekstId = it.opplysningTekstId,
                                                verdi = it.verdi,
                                                datatype = it.mapDatatype(),
                                            )
                                        },
                                    utvidedeBeskrivelser =
                                        utvidetBeskrivelse.map {
                                            UtvidetBeskrivelseDTO(
                                                brevblokkId = it.brevblokkId,
                                                tekst = it.tekst ?: "",
                                                sistEndretTidspunkt = it.sistEndretTidspunkt,
                                                tittel = it.tittel,
                                            )
                                        },
                                )
                            }.getOrThrow()
                        }
                            .onSuccess {
                                sikkerlogger.info { "Melding om vedtak: $it" }
                            }.onFailure { t ->
                                logger.error(t) { "Feil ved henting av melding om vedtak for behandling $behandlingId" }
                            }.getOrElse {
                                MeldingOmVedtakDTO(listOf("brev.blokk.rett-til-aa-klage"), emptyList(), emptyList())
                            }
                    call.respond(meldingOmVedtakDTO)
                }
            }
            post("/melding-om-vedtak/{behandlingId}/html") {
                val behandlingId = call.parseUUID()
                val behandler = call.parseSaksbehandler()
                val meldingOmVedtakData = call.receive<MeldingOmVedtakDataDTO>()
                withLoggingContext("behandlingId" to behandlingId.toString()) {
                    kotlin.runCatching {
                        val vedtaksHtml =
                            mediator.hentVedtaksHtml(
                                behandlingId = behandlingId,
                                behandler = behandler,
                                meldingOmVedtakData = meldingOmVedtakData,
                            )
                        val utvidetBeskrivelser = mediator.hentUtvidedeBeskrivelser(behandlingId, saksbehandler = behandler)
                        val meldingOmVedtakResponseDTO =
                            MeldingOmVedtakResponseDTO(
                                utvidedeBeskrivelser =
                                    utvidetBeskrivelser.map {
                                        UtvidetBeskrivelseDTO(
                                            brevblokkId = it.brevblokkId,
                                            tekst = it.tekst ?: "",
                                            sistEndretTidspunkt = it.sistEndretTidspunkt,
                                            tittel = it.tittel,
                                        )
                                    },
                                html = vedtaksHtml,
                            )

                        call.respond(meldingOmVedtakResponseDTO)
                    }.onFailure { t ->
                        logger.error(t) { "Feil ved henting av vedtaks html" }
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
            }
            post("/melding-om-vedtak/{behandlingId}/vedtaksmelding") {
                val behandlingId = call.parseUUID()
                val behandler = call.parseSaksbehandler()
                val meldingOmVedtakData = call.receive<MeldingOmVedtakDataDTO>()
                withLoggingContext("behandlingId" to behandlingId.toString()) {
                    kotlin.runCatching {
                        val vedtaksHtml =
                            mediator.hentVedtaksHtml(
                                behandlingId = behandlingId,
                                behandler = behandler,
                                meldingOmVedtakData = meldingOmVedtakData,
                            )
                        call.respond(vedtaksHtml)
                    }.onFailure { t ->
                        logger.error(t) { "Feil ved henting av vedtaks html" }
                        call.respond(HttpStatusCode.InternalServerError)
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
        }
    }
}

private fun ApplicationCall.parseSaksbehandler(): Saksbehandler = Saksbehandler(this.request.jwt())

private fun ApplicationCall.parseUUID(): UUID {
    return this.parameters["behandlingId"]?.let {
        UUID.fromString(it)
    } ?: throw IllegalArgumentException("")
}

private fun RoutingContext.requirePlainText() {
    require(call.request.headers["Content-Type"]!!.contains(ContentType.Text.Plain.toString())) {
        "Content-Type må være ${ContentType.Text.Plain}, men var ${call.request.headers["Content-Type"]}"
    }
}

private fun Opplysning.mapDatatype(): String {
    return when (this.datatype) {
        Opplysning.Datatype.TEKST -> "tekst"
        Opplysning.Datatype.HELTALL -> {
            when (this.enhet) {
                Opplysning.Enhet.KRONER -> "penger"
                Opplysning.Enhet.BARN -> "barn"
                else -> "heltall"
            }
        }

        Opplysning.Datatype.FLYTTALL -> {
            when (this.enhet) {
                Opplysning.Enhet.KRONER -> "penger"
                else -> "desimaltall"
            }
        }

        Opplysning.Datatype.DATO -> "dato"
        Opplysning.Datatype.BOOLSK -> "boolean"
    }
}
