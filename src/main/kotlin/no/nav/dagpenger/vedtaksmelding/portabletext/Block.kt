@file:Suppress("PropertyName")

package no.nav.dagpenger.vedtaksmelding.portabletext

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class BrevBlokk(
    val _type: String,
    val utvidetBeskrivelse: Boolean,
    val title: String,
    val textId: String,
    val innhold: List<Block>,
)

data class Block(
    val _type: String,
    @JsonDeserialize(using = ListChildDeserializer::class)
    val children: List<Child>,
    @JsonDeserialize(using = ListMarkDefDeserializer::class)
    val markDefs: List<MarkDef>,
    val style: String,
    val _key: String,
)

sealed interface Child {
    val _type: String

    data class Span(
        val text: String,
        val marks: List<String>,
        val _key: String,
    ) : Child {
        override val _type: String = "span"
    }

    data class OpplysningReference(
        val _key: String,
        val _ref: String,
        val behandlingOpplysning: BehandlingOpplysning,
    ) : Child {
        override val _type: String = "opplysningReference"

        data class BehandlingOpplysning(
            val textId: String,
            val type: String?,
        ) {
            val _type: String = "behandlingOpplysning"
        }
    }
}

sealed interface MarkDef {
    val _type: String
    val _key: String

    data class Link(
        override val _key: String,
        val href: String,
    ) : MarkDef {
        override val _type: String = "link"
    }
}

object ListChildDeserializer : JsonDeserializer<List<Child>>() {
    override fun deserialize(
        p: JsonParser,
        ctxt: DeserializationContext,
    ): List<Child> {
        return p.readValueAsTree<JsonNode>().map {
            when (val type = it.get("_type").asText()) {
                "span" -> {
                    p.codec.treeToValue(it, Child.Span::class.java)
                }

                "opplysningReference" -> {
                    p.codec.treeToValue(it, Child.OpplysningReference::class.java)
                }

                else -> {
                    throw IllegalArgumentException("Unknown type $type")
                }
            }
        }
    }
}

object ListMarkDefDeserializer : JsonDeserializer<List<MarkDef>>() {
    override fun deserialize(
        p: JsonParser,
        ctxt: DeserializationContext,
    ): List<MarkDef> {
        return p.readValueAsTree<JsonNode>().map {
            val get =
                try {
                    it.get("_type").asText()
                } catch (e: Exception) {
                    println(e)
                    throw e
                }
            when (get) {
                "link" -> p.codec.treeToValue(it, MarkDef.Link::class.java)
                else -> throw IllegalArgumentException("Unknown type")
            }
        }
    }
}

internal val hubba: ObjectMapper
    get() {
        return jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .enable(SerializationFeature.INDENT_OUTPUT)
    }
