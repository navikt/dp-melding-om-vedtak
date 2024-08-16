package no.nav.dagpenger.vedtaksmelding.model

class VedtaksMelding(private val behandling: Behandling) {
    companion object {
        val FASTE_BLOKKER =
            setOf(
                "brev.blokk.rett-til-aa-klage",
                "brev.blokk.rett-til-innsyn",
                "brev.blokk.sporsmaal",
            )
    }

    private val kravPåDagpenger: String by lazy {
        behandling.opplysninger.first { it.id == "opplysning.krav-paa-dagpenger" }.verdi
    }
    private val oppfyllerMinsteinntekt by lazy {
        behandling.opplysninger.first { it.id == "opplysning.krav-til-minsteinntekt" }.verdi
    }

    fun blokker(): List<String> {
        val blokker = mutableListOf<String>()

        if (kravPåDagpenger == "false") {
            blokker.add("brev.blokk.vedtak-avslag")
        }

        if (oppfyllerMinsteinntekt == "false") {
            blokker.add("brev.blokk.begrunnelse-avslag-minsteinntekt")
        }

        return (blokker + FASTE_BLOKKER).toList()
    }
}
