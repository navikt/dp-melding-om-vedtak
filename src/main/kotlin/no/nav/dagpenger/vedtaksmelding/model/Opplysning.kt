package no.nav.dagpenger.vedtaksmelding.model

interface Opplysning {
    val opplysningTekstId: String

    fun formatertVerdi(): String
}

open class OpplysningDataException(message: String) : RuntimeException(message)

class OpplysningIkkeFunnet(
    message: String,
) : RuntimeException(message)
