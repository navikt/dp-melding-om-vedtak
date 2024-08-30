package no.nav.dagpenger.vedtaksmelding.model

class Behandling(
    val id: String,
    val tilstand: String,
    val opplysninger: Set<Opplysning>,
) {
    fun hentOpplysning(opplysningTekstId: String): Opplysning {
        return opplysninger.single { it.opplysningTekstId == opplysningTekstId }
    }
}
