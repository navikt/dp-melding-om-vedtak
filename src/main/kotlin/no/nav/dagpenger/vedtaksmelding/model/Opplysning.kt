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
                "Krav på dagpenger" -> return "opplysning.krav-paa-dagpenger"
                "Søknadsdato" -> return "opplysning.soknadsdato"
                "Søknadstidspunkt" -> return "opplysning.soknadstidspunkt"
                "Siste avsluttende kalendermåned" -> return "opplysning.siste-avsluttende-kalendermaaned"
                "Inntektskrav for siste 12 mnd" -> return "opplysning.inntektskrav-for-siste-12-mnd"
                "Inntektskrav for siste 36 mnd" -> return "opplysning.inntektskrav-for-siste-36-mnd"
                "Arbeidsinntekt siste 12 mnd" -> return "opplysning.arbeidsinntekt-siste-12-mnd"
                "Arbeidsinntekt siste 36 mnd" -> return "opplysning.arbeidsinntekt-siste-36-mnd"
                "Antall G for krav til 12 mnd arbeidsinntekt" -> return "opplysning.antall-g-for-krav-til-12-mnd-arbeidsinntekt"
                "Antall G for krav til 36 mnd arbeidsinntekt" -> return "opplysning.antall-g-for-krav-til-36-mnd-arbeidsinntekt"
                "Fastsatt arbeidstid per uke før tap" -> return "opplysning.fastsatt-arbeidstid-per-uke-foer-tap"
                "Gjennomsnittlig arbeidsinntekt siste 36 måneder" -> return "opplysning.gjennomsnittlig-arbeidsinntekt-siste-36-maaneder"
                "Brukt beregningsregel" -> return "opplysning.brukt-beregningsregel"
                "Avrundet dagsats med barnetillegg" -> return "opplysning.avrundet-dagsats-med-barnetillegg"
                "Avrundet dagsats uten barnetillegg" -> return "opplysning.avrundet-dagsats-uten-barnetillegg"
                "Ukessats" -> return "opplysning.ukessats"
                "Antall stønadsuker" -> return "opplysning.antall-stonadsuker"
                "Egenandel" -> return "opplysning.egenandel"
                "Grunnlag siste 12 mnd." -> return "opplysning.grunnlag-siste-12-mnd."
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
