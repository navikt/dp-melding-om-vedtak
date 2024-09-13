package no.nav.dagpenger.vedtaksmelding.sanity

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.lagHttpKlient

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

        private val query = """*[_type == "brevBlokk"]{
                              textId,
                              innhold[]{
                                _type == "block" => {
                                  children[]{
                                    _type == "opplysningReference" => {
                                      "behandlingOpplysning": @->{
                                        textId, type
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
                objectMapper.readTree(responseBody)
            }.mapJsonToResponseDTO().result

        val behandlingOpplysningDTOer = mutableSetOf<BehandlingOpplysningDTO>()
        brevblokkDTOer.filter { it.textId in brevBlokkIder }
            .forEach {
                behandlingOpplysningDTOer.addAll(it.innhold.behandlingOpplysninger)
            }

        return behandlingOpplysningDTOer.map { it.textId }
    }
}
