package no.nav.dagpenger.vedtaksmelding.model

import mu.KotlinLogging
import java.util.UUID

private val logger = KotlinLogging.logger {}

data class BehandlingNy(
    val id: UUID,
    val tilstand: String,
    val opplysninger: Set<Opplysning>,
) {
    fun hentOpplysning(opplysningTekstId: String): Opplysning {
        val opplysning = opplysninger.singleOrNull { it.opplysningTekstId == opplysningTekstId }
        if (opplysning == null) {
            logger.error { "Fant ikke opplysning for opplysningtekstId: $opplysningTekstId" }
            throw NoSuchElementException("Fant ikke opplysning for opplysningtekstId: $opplysningTekstId")
        }
        return opplysning
    }
}
