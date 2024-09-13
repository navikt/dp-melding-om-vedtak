package no.nav.dagpenger.vedtaksmelding.apiconfig

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.callloging.processingTimeMillis
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.document
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.response.respond
import mu.KotlinLogging
import org.slf4j.event.Level

private val sikkerlogg = KotlinLogging.logger("tjenestekall")
private val log = KotlinLogging.logger {}

fun Application.apiConfig() {
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
                    call.respond(HttpStatusCode.Unauthorized, cause.message ?: "Unauthorized")
                }

                else -> {
                    log.error(cause) { "Uhåndtert feil: Se sikkerlogg for detaljer" }
                    sikkerlogg.error(cause) { "Uhåndtert feil: ${cause.message}" }
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}

internal val objectMapper: ObjectMapper =
    jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .enable(SerializationFeature.INDENT_OUTPUT)
