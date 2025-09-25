package no.nav.dagpenger.vedtaksmelding.portabletext

import io.ktor.util.escapeHTML
import kotlinx.html.FlowContent
import kotlinx.html.FlowOrPhrasingContent
import kotlinx.html.HTML
import kotlinx.html.HTMLTag
import kotlinx.html.HtmlBlockInlineTag
import kotlinx.html.OL
import kotlinx.html.TagConsumer
import kotlinx.html.UL
import kotlinx.html.a
import kotlinx.html.attributesMapOf
import kotlinx.html.b
import kotlinx.html.body
import kotlinx.html.br
import kotlinx.html.code
import kotlinx.html.div
import kotlinx.html.em
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.h3
import kotlinx.html.h4
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.lang
import kotlinx.html.li
import kotlinx.html.meta
import kotlinx.html.ol
import kotlinx.html.p
import kotlinx.html.s
import kotlinx.html.span
import kotlinx.html.stream.createHTML
import kotlinx.html.style
import kotlinx.html.svg
import kotlinx.html.title
import kotlinx.html.u
import kotlinx.html.ul
import kotlinx.html.unsafe
import kotlinx.html.visit
import no.nav.dagpenger.saksbehandling.api.models.BehandlerDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Suppress("ktlint:standard:max-line-length")
object HtmlConverter {
    fun MeldingOmVedtakDataDTO.hentNavn(): String =
        listOfNotNull(fornavn, mellomnavn, etternavn)
            .filter {
                it.isNotBlank()
            }.joinToString(" ")

    fun BehandlerDTO.navn(): String = listOf(fornavn, etternavn).filter { it.isNotBlank() }.joinToString(" ")

    fun FlowContent.header(
        meldingOmVedtakData: MeldingOmVedtakDataDTO,
        sakId: String,
    ) {
        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.of("no", "NO")))
        div(classes = "melding-om-vedtak-header") {
            svg(classes = "melding-om-vedtak-logo") {
                attributes["height"] = "16"
                attributes["viewBox"] = "0 0 193 58"
                attributes["fill"] = "none"
                path {
                    attributes["fill-rule"] = "evenodd"
                    attributes["clip-rule"] = "evenodd"
                    attributes["d"] =
                        "M190.8 0.799988H170.9C170.9 0.799988 169.5 0.8 169 2L158 35.8L147 2C146.5 0.8 145.1 0.799988 145.1 0.799988H106.8C106 0.799988 105.3 1.49999 105.3 2.29999V13.8C105.3 4.69999 95.6 0.799988 90 0.799988C77.3 0.799988 68.8 9.19999 66.2 21.9C66.1 13.5 65.4 10.4 63.1 7.39999C62.1 5.89999 60.6 4.60001 58.9 3.60001C55.5 1.60001 52.5 0.899994 46 0.899994H38.3C38.3 0.899994 36.9 0.900006 36.4 2.10001L29.4 19.4V2.39999C29.4 1.59999 28.7 0.899994 27.9 0.899994H10.2C10.2 0.899994 8.8 0.900006 8.3 2.10001L1.10002 20.1C1.10002 20.1 0.400012 21.9 2.00001 21.9H8.8V56.1C8.8 57 9.5 57.6 10.3 57.6H27.9C28.7 57.6 29.4 56.9 29.4 56.1V21.9H36.3C40.2 21.9 41.1 22 42.6 22.7C43.5 23.1 44.4 23.7 44.8 24.6C45.7 26.3 46 28.4 46 34.6V56.1C46 57 46.7 57.6 47.5 57.6H64.3C64.3 57.6 66.2 57.6 66.9 55.7L70.6 46.5C75.6 53.5 83.7 57.6 93.9 57.6H96.1C96.1 57.6 98 57.6 98.8 55.7L105.3 39.6V56.1C105.3 57 106 57.6 106.8 57.6H124C124 57.6 125.9 57.6 126.7 55.7C126.7 55.7 133.6 38.6 133.6 38.5C133.9 37.1 132.1 37.1 132.1 37.1H126V7.79999L145.3 55.7C146.1 57.6 147.9 57.6 147.9 57.6H168.2C168.2 57.6 170.1 57.6 170.9 55.7L192.3 2.70001C193 0.900012 190.9 0.899994 190.9 0.899994L190.8 0.799988ZM105.2 37H93.7C89.1 37 85.4 33.3 85.4 28.7C85.4 24.1 89.1 20.4 93.7 20.4H96.9C101.5 20.4 105.2 24.1 105.2 28.7V37Z"
                    attributes["fill"] = "#C30000"
                }
            }
            p { +"Navn: ${meldingOmVedtakData.hentNavn()}" }
            p { +"Fødselsnummer: ${meldingOmVedtakData.fodselsnummer}" }
            div(classes = "melding-om-vedtak-saksnummer-dato") {
                p(classes = "melding-om-vedtak-saksnummer-dato-left") { +"Saksid: $sakId" } //
                p(classes = "melding-om-vedtak-saksnummer-dato-right") { +currentDate } //
            }
        }
    }

    fun HTML.meta(sakId: String) {
        lang = "no"
        head {
            meta { charset = "UTF-8" }
            title { +"Vedtak fra NAV" } // todo hente tittel
            style {
                unsafe {
                    raw(css(sakId))
                }
            }
        }
    }

    fun FlowContent.footer(meldingOmVedtakData: MeldingOmVedtakDataDTO) {
        div {
            p { +"Med vennlig hilsen" }
            meldingOmVedtakData.beslutter?.let { beslutter ->
                p(classes = "melding-om-vedtak-signatur") {
                    +beslutter.navn()
                    br { }
                    +"Beslutter"
                    br { }
                    +beslutter.enhet.navn
                    br { }
                }
            }
            p(classes = "melding-om-vedtak-signatur") {
                +meldingOmVedtakData.saksbehandler.navn()
                br { }
                +"Saksbehandler"
                br { }
                +meldingOmVedtakData.saksbehandler.enhet.navn
                br { }
            }
        }
    }

    fun toHtml(
        meldingOmVedtakData: MeldingOmVedtakDataDTO,
        utvidetBeskrivelse: Set<UtvidetBeskrivelse> = emptySet(),
    ): String {
        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.of("no", "NO")))
        val sakId = meldingOmVedtakData.sakId ?: "TBD"

        return createHTML(prettyPrint = true, xhtmlCompatible = true).html {
            meta(sakId)
            body {
                div(classes = "melding-om-vedtak") {
                    header(meldingOmVedtakData, sakId)
                    utvidetBeskrivelse.forEach {
                        div(classes = "melding-om-vedtak-tekst-blokk") {
                            it.tekst?.let { tekst ->
                                unsafe {
                                    raw(tekst)
                                }
                            }
                        }
                    }
                    footer(meldingOmVedtakData)
                }
            }
        }
    }

    fun toHtml(
        brevBlokker: List<BrevBlokk>,
        opplysninger: List<Opplysning>,
        meldingOmVedtakData: MeldingOmVedtakDataDTO,
        utvidetBeskrivelse: Set<UtvidetBeskrivelse> = emptySet(),
    ): String {
        val mapping: Map<String, Opplysning> = opplysninger.associateBy { it.opplysningTekstId }
        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.of("no", "NO")))
        val sakId = meldingOmVedtakData.sakId ?: "TBD"

        fun groupBlocks(blocks: List<Block>): List<List<Block>> {
            val groupedBlocks = mutableListOf<MutableList<Block>>()
            var currentGroup = mutableListOf<Block>()

            var currentListItemGroup: String? = null
            for (block in blocks) {
                if (block.listItem == null) {
                    groupedBlocks.add(currentGroup)
                    groupedBlocks.add(mutableListOf(block))
                    currentGroup = mutableListOf()
                    currentListItemGroup = null
                } else {
                    if (currentListItemGroup == null || currentListItemGroup == block.listItem) {
                        currentListItemGroup = block.listItem
                        currentGroup.add(block)
                    } else {
                        groupedBlocks.add(currentGroup)
                        currentGroup = mutableListOf(block)
                        currentListItemGroup = block.listItem
                    }
                }
            }
            groupedBlocks.add(currentGroup)
            return groupedBlocks.filter { it.isNotEmpty() }
        }

        return createHTML(prettyPrint = true, xhtmlCompatible = true).html {
            meta(sakId)
            body {
                div(classes = "melding-om-vedtak") {
                    header(meldingOmVedtakData, sakId)
                    brevBlokker.forEachIndexed { index, brevBlokk ->
                        div(classes = "melding-om-vedtak-tekst-blokk") {
                            val groupedBlocks = groupBlocks(brevBlokk.innhold)

                            groupedBlocks.forEachIndexed { _, blocks ->

                                attributes["data-brevblokk-id"] = brevBlokk.textId
                                maybeWrapList(blocks) { block: Block ->
                                    wrapHeadings(block) { children ->
                                        val marks = block.markDefs.associateBy { it._key }
                                        children.forEach {
                                            handleChild(it, mapping, marks)
                                        }
                                    }
                                }
                            }
                            maybeAddUtvidetBesskrivelse(utvidetBeskrivelse, brevBlokk)
                        }
                    }
                    footer(meldingOmVedtakData)
                }
            }
        }
    }

    private fun FlowContent.maybeAddUtvidetBesskrivelse(
        utvidetBeskrivelse: Set<UtvidetBeskrivelse>,
        brevBlokk: BrevBlokk,
    ) {
        if (brevBlokk.utvidetBeskrivelse) {
            p {
                attributes["data-utvidet-beskrivelse-id"] = brevBlokk.textId
                utvidetBeskrivelse
                    .find { it.brevblokkId == brevBlokk.textId }
                    ?.tekst
                    ?.let { html ->
                        unsafe {
                            val s = html.sanitize()
                            raw(s)
                        }
                    }
            }
        }
    }

    private fun String.sanitize(): String =
        split("\n", "\r", "\r\n", "\u2028", "\u2029")
            .joinToString("<br/>") { it.escapeHTML() }

    private fun FlowContent.maybeWrapList(
        blocks: List<Block>,
        b: FlowContent.(block: Block) -> Unit,
    ) {
        when (blocks.size) {
            0 -> {
                // do nothing
            }

            1 -> {
                b(blocks.single())
            }

            else -> {
                // is a list
                when (blocks.first().listItem) {
                    "bullet" ->
                        ul {
                            blocks.forEach { block ->
                                wrapListElement(block, b)
                            }
                        }

                    "number" ->
                        ol {
                            blocks.forEach { block ->
                                wrapListElement(block, b)
                            }
                        }

                    else -> {
                        blocks.forEach { block ->
                            b(block)
                        }
                    }
                }
            }
        }
    }

    private fun FlowContent.processMarks(
        text: String,
        marks: List<Mark>,
        markDefs: Map<String, MarkDef>,
    ) {
        when (marks.isEmpty()) {
            true -> +text
            false -> {
                val mark: Mark = marks.first()
                when (mark) {
                    is Mark.Annotation -> {
                        val markDef: MarkDef =
                            markDefs[mark._key]
                                ?: throw RuntimeException("Mark not found for ${mark._key}")
                        when (markDef) {
                            is MarkDef.Link -> {
                                a {
                                    attributes["href"] = markDef.href
                                    +text
                                }
                            }
                        }
                    }

                    Mark.Code -> code { processMarks(text, marks.drop(1), markDefs) }
                    Mark.Em -> em { processMarks(text, marks.drop(1), markDefs) }
                    Mark.StrikeThrough -> s { processMarks(text, marks.drop(1), markDefs) }
                    Mark.Strong -> b { processMarks(text, marks.drop(1), markDefs) }
                    Mark.Underline -> u { processMarks(text, marks.drop(1), markDefs) }
                }
            }
        }
    }

    private fun FlowContent.handleChild(
        child: Child,
        mapping: Map<String, Opplysning>,
        markDefs: Map<String, MarkDef>,
    ) {
        when (child) {
            is Child.Span -> {
                processMarks(child.text, child.marks, markDefs)
            }

            is Child.OpplysningReference -> {
                val textId = child.behandlingOpplysning.textId
                val opplysning =
                    mapping[textId]
                        ?: throw RuntimeException("Opplysning ikke funnet $textId")
                span("melding-om-vedtak-opplysning-verdi") {
                    +opplysning.formatertVerdi()
                }
            }
        }
    }

    private fun FlowContent.wrapHeadings(
        block: Block,
        b: FlowContent.(children: List<Child>) -> Unit = {},
    ) {
        when {
            block.isListItem() -> {
                b(block.children)
            }

            else -> {
                when (block.style) {
                    "h1" -> {
                        h1 { b(block.children) }
                    }

                    "h2" -> {
                        h2 { b(block.children) }
                    }

                    "h3" -> {
                        h3 { b(block.children) }
                    }

                    "h4" -> {
                        h4 { b(block.children) }
                    }

                    else -> {
                        p { b(block.children) }
                    }
                }
            }
        }
    }

    private fun OL.wrapListElement(
        block: Block,
        b: FlowContent.(block: Block) -> Unit = {},
    ) {
        li {
            b(block)
        }
    }

    private fun UL.wrapListElement(
        block: Block,
        b: FlowContent.(block: Block) -> Unit = {},
    ) {
        li {
            b(block)
        }
    }
}

inline fun FlowOrPhrasingContent.path(
    classes: String? = null,
    crossinline block: PATH.() -> Unit = {},
): Unit =
    PATH(
        attributesMapOf("class", classes),
        consumer,
    ).visit(block)

open class PATH(
    initialAttributes: Map<String, String>,
    override val consumer: TagConsumer<*>,
) : HTMLTag("path", consumer, initialAttributes, null, true, false),
    HtmlBlockInlineTag
