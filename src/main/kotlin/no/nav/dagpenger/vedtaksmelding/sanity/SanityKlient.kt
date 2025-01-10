package no.nav.dagpenger.vedtaksmelding.sanity

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.Configuration
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.lagHttpKlient
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk

private val log = KotlinLogging.logger { }

class SanityKlient(
    private val sanityUrl: String,
    private val httpKlient: HttpClient = lagHttpKlient(engine = CIO.create { }, httpClientConfig),
) {
    companion object {
        val httpClientConfig: HttpClientConfig<*>.() -> Unit = {
            install(Logging) {
                level = LogLevel.BODY
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            log.info { message }
                        }
                    }
            }
        }

        private val objectMapper =
            jacksonObjectMapper()
                .registerModule(JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val query = """*[_type == "brevBlokk"]{
                              ...,  
                              innhold[]{
                                ... ,
                                _type == "block" => {
                                  ... ,
                                  children[]{
                                    ... ,
                                    _type == "opplysningReference" => {
                                      ... ,
                                      "behandlingOpplysning": @->{
                                        ...,
                                      }
                                    }
                                  }
                                }
                                }[innhold[].children[].behandlingOpplysning != {}]
                              }"""
    }

    suspend fun hentOpplysningTekstIder(brevBlokkIder: List<String>): List<String> {
        log.info { "Henter opplysning fra Sanity med url: $sanityUrl" }
        val brevblokkDTOer =
            httpKlient.get("$sanityUrl") {
                url {
                    parameters.append("query", query)
                }
            }.bodyAsText().let { responseBody ->
                log.info { "Sanity response: $responseBody" }
                objectMapper.readTree(responseBody)
            }.mapJsonToResponseDTO().result

        val behandlingOpplysningDTOer = mutableSetOf<BehandlingOpplysningDTO>()
        brevblokkDTOer.filter { it.textId in brevBlokkIder }
            .forEach {
                behandlingOpplysningDTOer.addAll(it.innhold.behandlingOpplysninger)
            }

        return behandlingOpplysningDTOer.map { it.textId }
    }

    suspend fun hentBrevBlokkerJson(): String {
        log.info { "Henter brevblokker fra Sanity med url: $sanityUrl" }
        return httpKlient.get("$sanityUrl") {
            url {
                parameters.append("query", query)
            }
        }.bodyAsText()
    }

    suspend fun hentBrevBlokker(): List<BrevBlokk> {
        log.info { "Henter brevblokker fra Sanity med url: $sanityUrl" }
        return httpKlient.get("$sanityUrl") {
            url {
                parameters.append("query", query)
            }
        }.body<ResultDTO>().result
    }
}

data class ResultDTO(
    val result: List<BrevBlokk>,
)

fun main() {
    val sanityKlient = SanityKlient(no.nav.dagpenger.vedtaksmelding.Configuration.sanityApiUrl)
    runBlocking {
        println(sanityKlient.hentBrevBlokkerJson())
    }
}
