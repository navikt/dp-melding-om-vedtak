package no.nav.dagpenger.vedtaksmelding.sanity

import com.fasterxml.jackson.databind.JsonNode

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
