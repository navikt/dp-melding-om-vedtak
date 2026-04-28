package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import no.nav.dagpenger.vedtaksmelding.model.OpplysningDataException
import java.time.LocalDate

enum class Opprinnelse {
    ARVET,
    NY,
    ;

    companion object {
        fun fra(verdi: String): Opprinnelse =
            when (verdi) {
                "Arvet" -> ARVET
                "Ny" -> NY
                else -> throw OpplysningDataException("Ukjent opprinnelse: $verdi")
            }
    }
}

data class Periode<V : Any>(
    val verdi: V,
    val opprinnelse: Opprinnelse,
    val gyldigFraOgMed: LocalDate? = null,
    val gyldigTilOgMed: LocalDate? = null,
) {
    fun inkludererDato(dato: LocalDate): Boolean {
        val etterStart = gyldigFraOgMed == null || dato >= gyldigFraOgMed
        val førSlutt = gyldigTilOgMed == null || dato <= gyldigTilOgMed
        return etterStart && førSlutt
    }
}
