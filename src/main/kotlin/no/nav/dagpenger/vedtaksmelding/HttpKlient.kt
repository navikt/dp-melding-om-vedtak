package no.nav.dagpenger.vedtaksmelding

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.jackson.jackson
import mu.KotlinLogging

private val sikkerlogg = KotlinLogging.logger("tjenestekall")

internal fun lagHttpKlient(
    engine: HttpClientEngine,
    block: HttpClientConfig<*>.() -> Unit = {},
): HttpClient {
    return HttpClient(engine) {
        expectSuccess = true
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
        }
        install(Logging) {
            logger =
                object : Logger {
                    override fun log(message: String) {
                        sikkerlogg.info { message }
                    }
                }
            level = LogLevel.INFO
        }
        block()
    }
}
