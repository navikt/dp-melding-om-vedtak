package no.nav.dagpenger.vedtaksmelding.sanity

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.lagHttpKlient
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk
import no.nav.dagpenger.vedtaksmelding.portabletext.Child

private val log = KotlinLogging.logger { }

class SanityKlient(
    private val sanityUrl: String,
    private val httpKlient: HttpClient = lagHttpKlient(engine = CIO.create { }, block = httpClientConfig),
) {
    companion object {
        val httpClientConfig: HttpClientConfig<*>.() -> Unit = {
            install(Logging) {
                level = LogLevel.INFO
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            log.info { message }
                        }
                    }
            }
        }

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
        return hentBrevBlokker().asSequence()
            .filter { it.textId in brevBlokkIder }
            .flatMap { it.innhold }
            .flatMap { it.children }
            .filterIsInstance<Child.OpplysningReference>()
            .map { it.behandlingOpplysning.textId }
            .toList()
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
