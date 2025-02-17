package no.nav.dagpenger.vedtaksmelding.util

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

infix fun String.finnUtvidetBeskrivelseTekst(utvidetBeskrivelseId: String): String? = finnElement(utvidetBeskrivelseId)?.text()

infix fun String.finnUtvidetBeskrivelseNode(utvidetBeskrivelseId: String): String? = finnElement(utvidetBeskrivelseId)?.toString()

infix fun String.finnElement(utvidetBeskrivelseId: String): Element? {
    val document = Jsoup.parse(this)
    document.outputSettings().prettyPrint(false)
    return document.select("[data-utvidet-beskrivelse-id]").singleOrNull {
        it.attr("data-utvidet-beskrivelse-id") == utvidetBeskrivelseId
    }
}
