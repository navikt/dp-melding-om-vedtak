package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

class Sanity(
    private val sanityUrl: String,
    private val httpKlient: HttpClient = lagHttpKlient(engine = CIO.create { }),
) {
    companion object {
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
        logger.info { "Henter opplysning fra Sanity med url: $sanityUrl" }
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

private fun JsonNode.mapJsonToResponseDTO(): ResponseDTO {
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
private data class ResponseDTO(val result: List<BrevBlokkDTO>)

private data class BrevBlokkDTO(
    val textId: String,
    val innhold: InnholdDTO,
) {
    data class InnholdDTO(
        val behandlingOpplysninger: List<BehandlingOpplysningDTO>,
    )
}

private data class BehandlingOpplysningDTO(
    val textId: String,
    val type: String,
)
