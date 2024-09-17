package no.nav.dagpenger.vedtaksmelding.model

import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.Mediator

private val logger = KotlinLogging.logger {}
private val sikkerlogg = KotlinLogging.logger("tjenestekall")

class VedtaksMelding(private val behandling: Behandling, private val mediator: Mediator) {
    companion object {
        val FASTE_BLOKKER =
            listOf(
                "brev.blokk.sporsmaal",
                "brev.blokk.rett-til-aa-klage",
                "brev.blokk.rett-til-innsyn",
            )
    }

    suspend fun hentOpplysninger(): List<Opplysning> {
        val opplysningstekstIder = mediator.hentOpplysningTekstIder(hentBrevBlokkIder())
        logger.info { "Skal hente opplysninger basert på følgende tekstider: $opplysningstekstIder" }
        return opplysningstekstIder.map { behandling.hentOpplysning(it) }
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
            sikkerlogg.error { "Ugyldig vedtak for behandling ${behandling}: ${e.message}" }
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
