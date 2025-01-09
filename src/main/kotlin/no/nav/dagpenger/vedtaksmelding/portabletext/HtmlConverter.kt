package no.nav.dagpenger.vedtaksmelding.portabletext

import io.github.allangomes.kotlinwind.css.KW
import io.github.allangomes.kotlinwind.css.XL
import io.github.allangomes.kotlinwind.css.core.FONT_SIZE
import io.github.allangomes.kotlinwind.css.features.font.Font
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.body
import kotlinx.html.html
import kotlinx.html.span
import kotlinx.html.stream.createHTML
import kotlinx.html.style
import no.nav.dagpenger.vedtaksmelding.model.Opplysning


object HtmlConverter {
    fun toHtml(
        brevBlokker: List<BrevBlokk>,
        opplysninger: List<Opplysning>,
    ): String {
        val mapping: Map<String, Opplysning> = opplysninger.associateBy { it.opplysningTekstId }
        return createHTML(prettyPrint = true, xhtmlCompatible = true).html {
            body {
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
            "h1" -> kw.inline {
                font.size[7].weight_700
                margin.

            }
            else -> KW.inline { }
        }
    }
}
