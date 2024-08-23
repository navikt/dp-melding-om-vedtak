package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.jackson.jackson
import kotlinx.coroutines.runBlocking

internal fun lagHttpKlient(engine: HttpClientEngine): HttpClient {
    return HttpClient(engine) {
        expectSuccess = true
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
        }
    }
}

class Sanity(
    private val sanityUrl: String,
    private val httpKlient: HttpClient = lagHttpKlient(engine = CIO.create { }),
) {
    companion object {
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

    suspend fun hentBrevBlokk(): String {
        return httpKlient.get("$sanityUrl") {
            url {
                parameters.append("query", query)
            }
        }.bodyAsText()
    }
}

data class ResponseDTO(val query: Any, val result: List<BrevBlokkDTO>)

data class BrevBlokkDTO(
    val textId: String,
    val innhold: List<InnholdDTO>,
) {
    data class InnholdDTO(
        val children: List<BehandlingOpplysningDTO>,
    ) {
        data class BehandlingOpplysningDTO(
            val textId: String,
            val type: String,
        )
    }
}

fun main() {
    val sanity = Sanity("https://rt6o382n.api.sanity.io/v2021-10-21/data/query/development")
    runBlocking {
        println(sanity.hentBrevBlokk())
    }
}
// https://rt6o382n.api.sanity.io/v2021-10-21/data/query/development
