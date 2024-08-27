package no.nav.dagpenger.vedtaksmelding.model

import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class VedtaksMelding(private val behandling: Behandling) {
    companion object {
        val FASTE_BLOKKER =
            listOf(
                "brev.blokk.rett-til-aa-klage",
                "brev.blokk.rett-til-innsyn",
                "brev.blokk.sporsmaal",
            )
    }

    private val kravPåDagpenger: Opplysning by lazy {
        behandling.opplysninger.first { it.id == "opplysning.krav-paa-dagpenger" }
    }

    private val oppfyllerMinsteinntekt: Opplysning by lazy {
        behandling.opplysninger.first { it.id == "opplysning.krav-til-minsteinntekt" }
    }

    fun blokker(): List<String> {
        try {
            val blokker = mutableListOf<String>()

            if (kravPåDagpenger.verdi == "false") {
                blokker.add("brev.blokk.vedtak-avslag")
            }

            if (oppfyllerMinsteinntekt.verdi == "false") {
                blokker.add("brev.blokk.begrunnelse-avslag-minsteinntekt")
            }

            val alleBlokker = (blokker + FASTE_BLOKKER)
            return alleBlokker
        } catch (e: Exception) {
            logger.error { "Ugyldig vedtak for behandling ${behandling.id}: ${e.message}" }
            throw UgyldigVedtakException(behandling.id)
        }
    }
}

internal class UgyldigVedtakException(private val behandlingId: String) : RuntimeException() {
    override val message: String
        get() = "Ugyldig vedtak for behandling $behandlingId"
}
