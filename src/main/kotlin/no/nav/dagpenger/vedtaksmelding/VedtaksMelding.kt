package no.nav.dagpenger.vedtaksmelding

class VedtaksMelding(private val behandling: Behandling) {

    private val kravPåDagpenger: String =
        behandling.opplysninger.first { it.id == "opplysning.krav-paa-dagpenger" }.verdi
    private val oppfyllerMinsteinntekt =
        behandling.opplysninger.first() { it.id == "opplysning.krav-til-minsteinntekt" }.verdi

    fun blokker(): Set<String> {
        val blokker = mutableSetOf<String>()


        if (kravPåDagpenger == "false") {
            blokker.add("brev.blokk.vedtak-avslag")
        }

        if (oppfyllerMinsteinntekt == "false") {
            blokker.add("brev.blokk.begrunnelse-avslag-minsteinntekt")
        }

        return blokker + setOf(
            "brev.blokk.rett-til-aa-klage",
            "brev.blokk.rett-til-innsyn",
            "brev.blokk.sporsmaal"
        )
    }

}
