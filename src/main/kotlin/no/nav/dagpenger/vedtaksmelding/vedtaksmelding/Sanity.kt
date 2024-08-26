package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.jackson.jackson
import io.ktor.util.reflect.typeInfo
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

    suspend fun hentBrevBlokk(): JsonNode {
        return httpKlient.get("$sanityUrl") {
            url {
                parameters.append("query", query)
            }
        }.body(typeInfo<JsonNode>())
    }
}

fun main() {
    val sanity = Sanity("https://rt6o382n.api.sanity.io/v2021-10-21/data/query/development")
    runBlocking {
        val brevBlokk: JsonNode = sanity.hentBrevBlokk()
        val responseDto = mapJsonToResponseDTO(brevBlokk)
        println(responseDto)
    }
}

fun mapJsonToResponseDTO(jsonNode: JsonNode): ResponseDTO {
    val result =
        jsonNode["result"].map { brevBlokkNode ->
            val textId = brevBlokkNode["textId"].asText()
            val innhold = mapInnhold(brevBlokkNode["innhold"])
            BrevBlokkDTO(textId = textId, innhold = innhold)
        }
    return ResponseDTO(result = result)
}

private fun mapInnhold(innholdNodeArray: JsonNode): List<BrevBlokkDTO.InnholdDTO> {
    return innholdNodeArray.mapNotNull { innholdNode ->
        val children = mapChildren(innholdNode["children"])
        if (children.isNotEmpty()) {
            BrevBlokkDTO.InnholdDTO(children = children)
        } else {
            null
        }
    }
}

private fun mapChildren(childrenNodeArray: JsonNode): List<BehandlingOpplysningDTO> {
    return childrenNodeArray.mapNotNull { childNode ->
        val behandlingOpplysningNode = childNode["behandlingOpplysning"]
        behandlingOpplysningNode?.let {
            BehandlingOpplysningDTO(
                textId = it["textId"].asText(),
                type = it["type"].asText(),
            )
        }
    }
}

// https://rt6o382n.api.sanity.io/v2021-10-21/data/query/development
data class ResponseDTO(val result: List<BrevBlokkDTO>)

data class BrevBlokkDTO(
    val textId: String,
    val innhold: List<InnholdDTO>,
) {
    data class InnholdDTO(
        val children: List<BehandlingOpplysningDTO>,
    )
}

data class BehandlingOpplysningDTO(
    val textId: String,
    val type: String,
)
