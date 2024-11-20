package no.nav.dagpenger.vedtaksmelding.model

data class Opplysning(
    val opplysningTekstId: String,
    val navn: String,
    val verdi: String,
    val datatype: String,
    private val opplysningId: String,
) {
    companion object {
        private fun mapping(navn: String): String =
            when (navn) {
                "Krav til minsteinntekt" -> "opplysning.krav-til-minsteinntekt"
                "Krav på dagpenger" -> "opplysning.krav-paa-dagpenger"
                "Søknadsdato" -> "opplysning.soknadsdato"
                "Søknadstidspunkt" -> "opplysning.soknadstidspunkt"
                "Siste avsluttende kalendermåned" -> "opplysning.siste-avsluttende-kalendermaaned"
                "Inntektskrav for siste 12 mnd" -> "opplysning.inntektskrav-for-siste-12-mnd"
                "Inntektskrav for siste 36 mnd" -> "opplysning.inntektskrav-for-siste-36-mnd"
                "Arbeidsinntekt siste 12 mnd" -> "opplysning.arbeidsinntekt-siste-12-mnd"
                "Arbeidsinntekt siste 36 mnd" -> "opplysning.arbeidsinntekt-siste-36-mnd"
                "Antall G for krav til 12 mnd arbeidsinntekt" -> "opplysning.antall-g-for-krav-til-12-mnd-arbeidsinntekt"
                "Antall G for krav til 36 mnd arbeidsinntekt" -> "opplysning.antall-g-for-krav-til-36-mnd-arbeidsinntekt"
                "Gjennomsnittlig arbeidsinntekt siste 36 måneder" -> "opplysning.gjennomsnittlig-arbeidsinntekt-siste-36-maaneder"
                "Brukt beregningsregel" -> "opplysning.brukt-beregningsregel"
                "Dagsats med barnetillegg etter samordning og 90% regel" -> "opplysning.avrundet-dagsats-med-barnetillegg"
                "Samordnet dagsats uten barnetillegg" -> "opplysning.avrundet-dagsats-uten-barnetillegg"
                "Ukessats med barnetillegg etter samordning" -> "opplysning.ukessats"
                "Antall stønadsuker" -> "opplysning.antall-stonadsuker"
                "Egenandel" -> "opplysning.egenandel"
                "Grunnlag siste 12 mnd." -> "opplysning.grunnlag-siste-12-mnd."
                "Fastsatt arbeidstid per uke før tap" -> "opplysning.fastsatt-arbeidstid-per-uke-for-tap"
                "Prøvingsdato" -> "opplysning.provingsdato"
                "Første måned av opptjeningsperiode" -> "opplysning.forste-maaned-av-opptjeningsperiode"
                "Antall barn som gir rett til barnetillegg" -> "opplysning.antall-barn-som-gir-rett-til-barnetillegg"
                "Sum av barnetillegg" -> "opplysning.barnetillegg-i-kroner"
                "Grunnlag" -> "opplysning.grunnlag"
                "Andel av dagsats med barnetillegg som overstiger maks andel av dagpengegrunnlaget" -> "opplysning.90-regel-brukt"
                else -> "ukjent.opplysning.$navn"
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
