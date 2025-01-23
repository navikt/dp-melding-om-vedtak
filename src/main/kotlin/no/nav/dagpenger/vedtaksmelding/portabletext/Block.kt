@file:Suppress("PropertyName")

package no.nav.dagpenger.vedtaksmelding.portabletext

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
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
import no.nav.dagpenger.vedtaksmelding.portabletext.Child.Type.OPPLYSNING_REFERENCE
import no.nav.dagpenger.vedtaksmelding.portabletext.Child.Type.SPAN
import no.nav.dagpenger.vedtaksmelding.portabletext.MarkDef.Type.LINK

data class BrevBlokk(
    val _type: String,
    val utvidetBeskrivelse: Boolean,
    val title: String,
    val textId: String,
    val innhold: List<Block>,
)

data class Block(
    val _type: String,
    @JsonDeserialize(using = Child.ListChildDeserializer::class)
    val children: List<Child>,
    @JsonDeserialize(using = MarkDef.ListMarkDefDeserializer::class)
    val markDefs: List<MarkDef>,
    val style: String,
    val _key: String,
    val listItem: String?,
) {
    fun isListItem() = listItem != null
}

sealed interface Child {
    enum class Type {
        @JsonProperty("span")
        SPAN,

        @JsonProperty("opplysningReference")
        OPPLYSNING_REFERENCE,
    }

    val _type: Type

    data class Span(
        val text: String,
        val marks: List<String>,
        val _key: String,
    ) : Child {
        override val _type = SPAN
    }

    data class OpplysningReference(
        val _key: String,
        val _ref: String,
        val behandlingOpplysning: BehandlingOpplysning,
    ) : Child {
        override val _type = OPPLYSNING_REFERENCE

        data class BehandlingOpplysning(
            val textId: String,
            val type: String?,
        ) {
            val _type: String = "behandlingOpplysning"
        }
    }

    object ListChildDeserializer : JsonDeserializer<List<Child>>() {
        override fun deserialize(
            p: JsonParser,
            ctxt: DeserializationContext,
        ): List<Child> {
            return p.readValueAsTree<JsonNode>().map {
                when (val type = p.codec.treeToValue(it.get("_type"), Child.Type::class.java)) {
                    SPAN -> {
                        p.codec.treeToValue(it, Child.Span::class.java)
                    }

                    OPPLYSNING_REFERENCE -> {
                        p.codec.treeToValue(it, Child.OpplysningReference::class.java)
                    }
                }
            }
        }
    }
}

sealed interface MarkDef {
    val _type: Type
    val _key: String

    enum class Type {
        @JsonProperty("link")
        LINK,
    }

    data class Link(
        override val _key: String,
        val href: String,
    ) : MarkDef {
        override val _type: Type = LINK
    }

    object ListMarkDefDeserializer : JsonDeserializer<List<MarkDef>>() {
        override fun deserialize(
            p: JsonParser,
            ctxt: DeserializationContext,
        ): List<MarkDef> {
            return p.readValueAsTree<JsonNode>().map {
                when (p.codec.treeToValue(it.get("_type"), Type::class.java)) {
                    LINK -> p.codec.treeToValue(it, Link::class.java)
                    else -> throw IllegalArgumentException("Unknown type")
                }
            }
        }
    }
}
