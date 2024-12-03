package no.nav.dagpenger.vedtaksmelding.portabletext

import kotlinx.html.body
import kotlinx.html.html
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.stream.createHTML

object HtmlConverter {
    fun toHtml(brevBlokker: List<BrevBlokk>): String {
        return createHTML(prettyPrint = true, xhtmlCompatible = true).html {
            body {
                brevBlokker.forEach { brevBlokk ->
                    brevBlokk.innhold.forEach { innhold ->
                        innhold.children.filter { child -> child._type == Child.Type.SPAN }.forEach { child ->
                            child as Child.Span
                            span {
                                +child.text
                            }
                            p {}
                        }
                    }
                }
            }
        }
    }
}
