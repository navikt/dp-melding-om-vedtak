package no.nav.dagpenger.vedtaksmelding.model

data class Opplysning(
    val opplysningTekstId: String,
    val navn: String,
    val verdi: String,
    val datatype: String,
    private val opplysningId: String,
) {
    companion object {
        private fun mapping(navn: String): String {
            return when (navn) {
                "Krav på dagpenger" -> "opplysning.krav-paa-dagpenger"
                "Krav til minsteinntekt" -> return "opplysning.krav-til-minsteinntekt"
                else -> return "ukjent.opplysning.$navn"
            }
        }
    }

    constructor(
        navn: String,
        verdi: String,
        datatype: String,
        opplysningId: String,
    ) : this(
        opplysningTekstId = mapping(navn),
        navn = navn,
        verdi = verdi,
        datatype = datatype,
        opplysningId = opplysningId,
    )
}
