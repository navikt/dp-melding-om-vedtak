package no.nav.dagpenger.vedtaksmelding.portabletext

import io.github.allangomes.kotlinwind.css.KW
import io.github.allangomes.kotlinwind.css.core.StyleValueMarker
import io.github.allangomes.kotlinwind.css.core.tokens.Token
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.html
import kotlinx.html.span
import kotlinx.html.stream.createHTML
import kotlinx.html.style
import kotlinx.html.svg
import no.nav.dagpenger.vedtaksmelding.model.Opplysning

object HtmlConverter {
    fun toHtml(
        brevBlokker: List<BrevBlokk>,
        opplysninger: List<Opplysning>,
    ): String {
        val mapping: Map<String, Opplysning> = opplysninger.associateBy { it.opplysningTekstId }
        return createHTML(prettyPrint = true, xhtmlCompatible = true).html {
            body {
                div(classes = "melding-om-vedtak") {
                    div(classes = "melding-om-vedtak-header") {
                        svg(classes = "melding-om-vedtak-logo") {
                            this.

                        }
                    }
                }
                brevBlokker.forEach { brevBlokk ->
                    brevBlokk.innhold.forEach { innhold ->
                        innhold.children.forEach {
                            when (it) {
                                is Child.Span -> {
                                    span {
                                        style = createStyling(innhold.style)
                                        +it.text
                                    }
                                }

                                is Child.OpplysningReference -> {
                                    val textId = it.behandlingOpplysning.textId
                                    val opplysning =
                                        mapping[textId] ?: throw RuntimeException("Opplysning ikke funnet $textId")
                                    span {
                                        +opplysning.verdi
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createStyling(style: String): String {
        return when (style) {
            "h1" ->
                kw.inline {
                    font.size[H1].weight_700
                    height[20]
                    margin.top[0].right[0].bottom[20].left[0]
                }

            else -> KW.inline { }
        }
    }
}

@StyleValueMarker
object H1 :
    Token<H1>({
        font_size[it] = "16pt"
        line_height[it] = "20pt"
        margin[it] = "0 0 26px 0"
        padding[it] = "1rem"
    }),
    Token.BorderRadius,
    Token.FontSize,
    Token.LineHeight,
    Token.Margin,
    Token.Padding
