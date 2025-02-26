package no.nav.dagpenger.vedtaksmelding.apiconfig

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.calllogging.processingTimeMillis
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.document
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.response.respond
import mu.KotlinLogging
import no.nav.dagpenger.saksbehandling.api.models.HttpProblemDTO
import no.nav.dagpenger.vedtaksmelding.Configuration.objectMapper
import no.nav.dagpenger.vedtaksmelding.HentVedtakException
import no.nav.dagpenger.vedtaksmelding.metrics.metrics
import org.slf4j.event.Level
import java.net.URI

private val sikkerlogg = KotlinLogging.logger("tjenestekall")
private val log = KotlinLogging.logger {}

fun Application.apiConfig() {
    metrics()

    install(Authentication) {
        jwt("azureAd")
    }

    install(ContentNegotiation) {
        jackson {
            register(ContentType.Application.Json, JacksonConverter(objectMapper))
        }
    }

    install(CallLogging) {
        disableDefaultColors()
        filter { call ->
            !setOf(
                "isalive",
                "isready",
                "metrics",
            ).contains(call.request.document())
        }
        level = Level.INFO
        format { call ->
            val status = call.response.status()?.value ?: "Unhandled"
            val method = call.request.httpMethod.value
            val path = call.request.path()
            val duration = call.processingTimeMillis()
            val queryParams = call.request.queryParameters.entries()
            "$status $method $path $queryParams $duration ms"
        }
        this.logger = log
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is IllegalAccessException -> {
                    log.warn { "Unauthorized: ${cause.message}" }
                    sikkerlogg.warn { "Unauthorized, se sikkerlogg for detaljer: ${cause.stackTrace}" }

                    val problem =
                        HttpProblemDTO(
                            title = "Unauthorized",
                            detail = cause.message,
                            status = HttpStatusCode.Unauthorized.value,
                            instance = call.request.path(),
                            type = URI.create("dagpenger.nav.no/saksbehandling:problem:unauthorized").toString(),
                        )
                    call.respond(HttpStatusCode.Unauthorized, problem)
                }

                is IllegalArgumentException -> {
                    // TODO: Detaljer ut exception, kanskje kast egendefinert? Rydding må til
                    log.error { "Bad request: ${cause.message}" }
                    val problem =
                        HttpProblemDTO(
                            title = "Bad request",
                            detail = cause.message,
                            status = HttpStatusCode.BadRequest.value,
                            instance = call.request.path(),
                            type = URI.create("dagpenger.nav.no/saksbehandling:problem:bad-request").toString(),
                        )
                    call.respond(HttpStatusCode.BadRequest, problem)
                }
                is HentVedtakException -> {
                    call.respond(cause.statusCode, cause.httpProblem)
                }

                else -> {
                    log.error(cause) { "Uhåndtert feil: Se sikkerlogg for detaljer" }
                    sikkerlogg.error(cause) { "Uhåndtert feil: ${cause.message}" }
                    val problem =
                        HttpProblemDTO(
                            title = "Uhåndtert feil",
                            detail = cause.message,
                            status = HttpStatusCode.InternalServerError.value,
                            instance = call.request.path(),
                            type = URI.create("dagpenger.nav.no/saksbehandling:problem:uhåndtert-feil").toString(),
                        )
                    call.respond(HttpStatusCode.InternalServerError, problem)
                }
            }
        }
    }
}
