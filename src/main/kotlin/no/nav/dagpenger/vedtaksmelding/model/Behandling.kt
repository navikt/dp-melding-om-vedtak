package no.nav.dagpenger.vedtaksmelding.model

import java.util.UUID

data class Behandling(
    val id: UUID,
    val tilstand: String,
    val opplysninger: Set<Opplysning>,
) {
    fun hentOpplysning(opplysningTekstId: String): Opplysning {
        val opplysning = opplysninger.singleOrNull { it.opplysningTekstId == opplysningTekstId }
        if (opplysning == null) {
            throw FantIkkeOpplysning("Fant ikke opplysning for opplysningtekstId: $opplysningTekstId")
        }
        return opplysning
    }
}

class FantIkkeOpplysning(message: String) : RuntimeException(message)
