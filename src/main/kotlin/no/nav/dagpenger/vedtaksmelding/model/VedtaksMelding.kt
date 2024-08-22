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

    private val kravPåDagpenger: Opplysning by lazy {
        behandling.opplysninger.first { it.id == "opplysning.krav-paa-dagpenger" }
    }
    private val oppfyllerMinsteinntekt: Opplysning by lazy {
        behandling.opplysninger.first { it.id == "opplysning.krav-til-minsteinntekt" }
    }

    fun hubba(): Pair<List<String>, Set<Opplysning>> {
        return Pair(blokker(), setOf(kravPåDagpenger, oppfyllerMinsteinntekt))
    }

    fun blokker(): List<String> {
        val blokker = mutableListOf<String>()

        if (kravPåDagpenger.verdi == "false") {
            blokker.add("brev.blokk.vedtak-avslag")
        }

        if (oppfyllerMinsteinntekt.verdi == "false") {
            blokker.add("brev.blokk.begrunnelse-avslag-minsteinntekt")
        }

        return (blokker + FASTE_BLOKKER).toList()
    }
}
