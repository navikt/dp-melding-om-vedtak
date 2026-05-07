package no.nav.dagpenger.vedtaksmelding

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.jackson3.jackson
import no.nav.dagpenger.vedtaksmelding.serder.applyDefault

private val sikkerlogg = KotlinLogging.logger("tjenestekall")

internal fun lagHttpKlient(
    engine: HttpClientEngine,
    expectSucces: Boolean = true,
    block: HttpClientConfig<*>.() -> Unit = {},
): HttpClient =
    HttpClient(engine) {
        expectSuccess = expectSucces
        install(ContentNegotiation) {
            jackson {
                applyDefault()
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
