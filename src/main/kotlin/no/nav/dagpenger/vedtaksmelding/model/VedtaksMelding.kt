package no.nav.dagpenger.vedtaksmelding.model

import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.vedtaksmelding.Mediator

private val logger = KotlinLogging.logger {}

class VedtaksMelding(private val behandling: Behandling, private val mediator: Mediator) {
    companion object {
        val FASTE_BLOKKER =
            listOf(
                "brev.blokk.rett-til-aa-klage",
                "brev.blokk.rett-til-innsyn",
                "brev.blokk.sporsmaal",
            )
    }

    suspend fun hentOpplysninger(): List<Opplysning> {
        return mediator.hentOpplysningTekstIder(hentBrevBlokkIder()).map { behandling.hentOpplysning(it) }
    }

    fun hentBrevBlokkIder(): List<String> {
        try {
            val blokker = mutableListOf<String>()

            if (kravPåDagpenger.verdi == "false") {
                blokker.add("brev.blokk.vedtak-avslag")
            }

            if (oppfyllerMinsteinntekt.verdi == "false") {
                blokker.add("brev.blokk.begrunnelse-avslag-minsteinntekt")
            }
            val alleBlokker = (blokker + FASTE_BLOKKER).toList()
            return alleBlokker
        } catch (e: Exception) {
            logger.error { "Ugyldig vedtak for behandling ${behandling.id}: ${e.message}" }
            throw UgyldigVedtakException(behandling.id)
        }
    }

    private val kravPåDagpenger: Opplysning by lazy {
        behandling.opplysninger.first { it.opplysningTekstId == "opplysning.krav-paa-dagpenger" }
    }

    private val oppfyllerMinsteinntekt: Opplysning by lazy {
        behandling.opplysninger.first { it.opplysningTekstId == "opplysning.krav-til-minsteinntekt" }
    }
}

internal class UgyldigVedtakException(private val behandlingId: String) : RuntimeException() {
    override val message: String
        get() = "Ugyldig vedtak for behandling $behandlingId"
}
