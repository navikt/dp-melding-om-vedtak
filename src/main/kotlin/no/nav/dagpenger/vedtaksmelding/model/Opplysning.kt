package no.nav.dagpenger.vedtaksmelding.model

data class Opplysning(
    val id: String,
    val navn: String,
    val verdi: String,
    val datatype: String,
) {
    val tekstId: String
        get() {
            return when (navn) {
                "Krav på dagpenger" -> "opplysning.krav-paa-dagpenger"
                "Krav til minsteinntekt" -> return "opplysning.krav-til-minsteinntekt"
                else -> return "ukjent.opplysning.$navn"
            }
        }
}
