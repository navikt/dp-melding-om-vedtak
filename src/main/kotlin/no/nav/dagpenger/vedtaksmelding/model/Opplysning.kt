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
                "Krav til minsteinntekt" -> return "opplysning.krav-til-minsteinntekt"
                "Søknadsdato" -> return "opplysning.soknadsdato"
                "Siste avsluttende kalendermåned" -> return "opplysning.siste-avsluttende-kalendermaaned"
                "Inntektskrav for siste 12 mnd" -> return "opplysning.inntektskrav-for-siste-12-mnd"
                "Inntektskrav for siste 36 mnd" -> return "opplysning.inntektskrav-for-siste-36-mnd"
                "Arbeidsinntekt siste 12 mnd" -> return "opplysning.arbeidsinntekt-siste-12-mnd"
                "Arbeidsinntekt siste 36 mnd" -> return "opplysning.arbeidsinntekt-siste-36-mnd"
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
