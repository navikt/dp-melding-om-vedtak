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

fun JsonNode.mapJsonToResponseDTO(): ResponseDTO {
    val result =
        this["result"].map { brevBlokkNode ->
            val textId = brevBlokkNode["textId"].asText()
            val innhold = brevBlokkNode.mapInnhold()
            BrevBlokkDTO(textId = textId, innhold = innhold)
        }
    return ResponseDTO(result = result)
}

private fun JsonNode.mapInnhold(): BrevBlokkDTO.InnholdDTO {
    val alleBehandlingOpplysninger =
        this["innhold"].mapNotNull { innholdNode ->
            innholdNode.mapBehandlingOpplysning()
        }.flatten()

    return BrevBlokkDTO.InnholdDTO(behandlingOpplysninger = alleBehandlingOpplysninger)
}

private fun JsonNode.mapBehandlingOpplysning(): List<BehandlingOpplysningDTO> {
    return this["children"].mapNotNull { childNode ->
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
    val innhold: InnholdDTO,
) {
    data class InnholdDTO(
        val behandlingOpplysninger: List<BehandlingOpplysningDTO>,
    )
}

data class BehandlingOpplysningDTO(
    val textId: String,
    val type: String,
)

/*fun main() {
    val sanity = Sanity("https://rt6o382n.api.sanity.io/v2021-10-21/data/query/development")
    runBlocking {
        val brevBlokk: JsonNode = sanity.hentBrevBlokk()
        val responseDto = mapJsonToResponseDTO(brevBlokk)
        println(responseDto)
    }
}*/
