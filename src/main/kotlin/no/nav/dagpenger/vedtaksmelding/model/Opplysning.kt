package no.nav.dagpenger.vedtaksmelding.model

interface Opplysning {
    val opplysningTekstId: String

    fun formatertVerdi(): String
}

class OpplysningIkkeFunnet(
    message: String,
) : RuntimeException(message)
