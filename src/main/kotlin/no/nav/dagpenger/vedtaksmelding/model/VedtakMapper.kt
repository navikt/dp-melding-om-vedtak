package no.nav.dagpenger.vedtaksmelding.model

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.DATO
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.TEKST
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Enhet.ENHETSLØS
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Enhet.KRONER
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Enhet.TIMER
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Enhet.UKER

data class brevKriterier(
    val navn: String,
    val verdi: Boolean,
)

class VedtakMapper(vedtakJson: String) {
    private val vedtak: JsonNode
    private val objectMapper: ObjectMapper =
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    init {
        vedtak = objectMapper.readTree(vedtakJson)
    }

    fun hentBrevKriterier(): Set<brevKriterier> {
        val innvilgelse =
            brevKriterier(
                navn = "Innvilgelse",
                verdi = vedtak["fastsatt"]["utfall"].asBoolean(),
            )
        val avslagMinsteinntekt =
            brevKriterier(
                navn = "Avslag minsteinntekt",
                verdi = vedtak["vilkår"].any { it["navn"].asText() == "Krav til minsteinntekt" && it["status"].asText() == "Ikke oppfylt" },
            )

        return setOf(innvilgelse, avslagMinsteinntekt)
    }

    fun hentOppfyllerKravTilMinsteinntekt(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.krav-til-minsteinntekt",
            verdi =
                vedtak["vilkår"].any { it["navn"].asText() == "Krav til minsteinntekt" && it["status"].asText() == "Oppfylt" }
                    .toString(),
            datatype = BOOLSK,
            enhet = ENHETSLØS,
        )
    }

    fun hentOpplysningGrunnlag(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.grunnlag",
            verdi = vedtak["fastsatt"]["grunnlag"]["grunnlag"].asText(),
            datatype = HELTALL,
            enhet = KRONER,
        )
    }

    fun hentOpplysningProvingDato(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.provingsdato",
            verdi = vedtak["virkningsdato"].asText(),
            datatype = DATO,
            enhet = ENHETSLØS,
        )
    }

    fun hentFastsattArbeidstidPerUkeForTap(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.fastsatt-arbeidstid-per-uke-for-tap",
            verdi = vedtak["fastsatt"]["fastsattVanligArbeidstid"]["vanligArbeidstidPerUke"].asText(),
            datatype = FLYTTALL,
            enhet = TIMER,
        )
    }

    fun hentInntektsKravSiste12Måneder(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.inntektskrav-for-siste-12-mnd",
            verdi = hentOpplysning("Inntektskrav for siste 12 mnd"),
            datatype = HELTALL,
            enhet = KRONER,
        )
    }

    fun hentInntektsKravSiste36Måneder(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.inntektskrav-for-siste-36-mnd",
            verdi = hentOpplysning("Inntektskrav for siste 36 mnd"),
            datatype = HELTALL,
            enhet = KRONER,
        )
    }

    fun hentArbeidsinntektSiste36Måneder(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.arbeidsinntekt-siste-36-mnd",
            verdi = hentOpplysning("Arbeidsinntekt siste 36 mnd"),
            datatype = HELTALL,
            enhet = KRONER,
        )
    }

    fun hentArbeidsinntektSiste12Måneder(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.arbeidsinntekt-siste-12-mnd",
            verdi = hentOpplysning("Arbeidsinntekt siste 12 mnd"),
            datatype = HELTALL,
            enhet = KRONER,
        )
    }

    fun hentAntallGForKravTil12MndArbeidsinntekt(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.antall-g-for-krav-til-12-mnd-arbeidsinntekt",
            verdi = hentOpplysning("Antall G for krav til 12 mnd arbeidsinntekt"),
            datatype = FLYTTALL,
            enhet = ENHETSLØS,
        )
    }

    fun hentAntallGForKravTil36MndArbeidsinntekt(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.antall-g-for-krav-til-36-mnd-arbeidsinntekt",
            verdi = hentOpplysning("Antall G for krav til 36 mnd arbeidsinntekt"),
            datatype = FLYTTALL,
            enhet = ENHETSLØS,
        )
    }

    fun hentGjennomsnittligArbeidsinntektSiste36Måneder(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.gjennomsnittlig-arbeidsinntekt-siste-36-maaneder",
            verdi = hentOpplysning("Gjennomsnittlig arbeidsinntekt siste 36 måneder"),
            datatype = FLYTTALL,
            enhet = KRONER,
        )
    }

    fun hentBruktBeregningsregel(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.brukt-beregningsregel",
            verdi = hentOpplysning("Brukt beregningsregel"),
            datatype = TEKST,
            enhet = ENHETSLØS,
        )
    }

    fun hentSamordnetDagsatsUtenBarnetillegg(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.samordnet-dagsats-uten-barnetillegg",
            verdi = hentOpplysning("Samordnet dagsats uten barnetillegg"),
            datatype = HELTALL,
            enhet = KRONER,
        )
    }

    fun hentUkessatsMedBarnetillegg(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.ukessats",
            verdi = hentOpplysning("Ukessats med barnetillegg etter samordning"),
            datatype = HELTALL,
            enhet = KRONER,
        )
    }

    fun hentAntallStonadsuker(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.antall-stonadsuker",
            verdi = hentOpplysning("Antall stønadsuker"),
            datatype = HELTALL,
            enhet = UKER,
        )
    }

    fun hentEgenandel(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.egenandel",
            verdi = hentOpplysning("Egenandel"),
            datatype = HELTALL,
            enhet = KRONER,
        )
    }

    fun hentUtbetaltArbeidsinntektPeriode1(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "oppysning.utbetalt-arbeidsinntekt-periode-1",
            verdi = hentOpplysning("Utbetalt arbeidsinntekt periode 1"),
            datatype = FLYTTALL,
            enhet = KRONER,
        )
    }

    fun hentUtbetaltArbeidsinntektPeriode2(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "oppysning.utbetalt-arbeidsinntekt-periode-2",
            verdi = hentOpplysning("Utbetalt arbeidsinntekt periode 2"),
            datatype = FLYTTALL,
            enhet = KRONER,
        )
    }

    fun hentUtbetaltArbeidsinntektPeriode3(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "oppysning.utbetalt-arbeidsinntekt-periode-3",
            verdi = hentOpplysning("Utbetalt arbeidsinntekt periode 3"),
            datatype = FLYTTALL,
            enhet = KRONER,
        )
    }

    fun hentHarSamordnet(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.har-samordnet",
            verdi = hentOpplysning("Har samordnet"),
            datatype = Opplysning2.Datatype.BOOLSK,
            enhet = Opplysning2.Enhet.ENHETSLØS,
        )
    }

    fun hentAndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.andel-av-dagsats-med-barnetillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget",
            verdi = hentOpplysning("Andel av dagsats med barnetillegg som overstiger maks andel av dagpengegrunnlaget"),
            datatype = Opplysning2.Datatype.FLYTTALL,
            enhet = Opplysning2.Enhet.KRONER,
        )
    }

    fun hentAndelAvDagsatsMedBarnetilleggAvkortetTilMaksAndelAvDagpengegrunnlaget(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.andel-av-dagsats-med-barnetillegg-avkortet-til-maks-andel-av-dagpengegrunnlaget",
            verdi = hentOpplysning("Andel av dagsats med barnetillegg avkortet til maks andel av dagpengegrunnlaget"),
            datatype = Opplysning2.Datatype.FLYTTALL,
            enhet = Opplysning2.Enhet.KRONER,
        )
    }

    fun hentAntallBarnSomGirRettTilBarnetillegg(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.antall-barn-som-gir-rett-til-barnetillegg",
            verdi = hentOpplysning("Antall barn som gir rett til barnetillegg"),
            datatype = Opplysning2.Datatype.HELTALL,
            enhet = Opplysning2.Enhet.BARN,
        )
    }

    fun hentBarnetilleggIKroner(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.barnetillegg-i-kroner",
            verdi = hentOpplysning("Sum av barnetillegg"),
            datatype = Opplysning2.Datatype.FLYTTALL,
            enhet = Opplysning2.Enhet.KRONER,
        )
    }

    fun hentForsteMaanedAvOpptjeningsperiode(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.forste-maaned-av-opptjeningsperiode",
            verdi = hentOpplysning("Første måned av opptjeningsperiode"),
            datatype = Opplysning2.Datatype.DATO,
            enhet = Opplysning2.Enhet.ENHETSLØS,
        )
    }

    fun hentSisteAvsluttendeKalendermaaned(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.siste-avsluttende-kalendermaaned",
            verdi = hentOpplysning("Siste avsluttende kalendermåned"),
            datatype = Opplysning2.Datatype.DATO,
            enhet = Opplysning2.Enhet.ENHETSLØS,
        )
    }

    fun hentDagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.avrundet-dagsats-med-barnetillegg",
            verdi = hentOpplysning("Dagsats med barnetillegg etter samordning og 90% regel"),
            datatype = Opplysning2.Datatype.FLYTTALL,
            enhet = Opplysning2.Enhet.KRONER,
        )
    }

    private fun hentOpplysning(opplysningsnavn: String): String {
        val node =
            vedtak["opplysninger"].find {
                it["navn"].asText() == opplysningsnavn
            }
        return node?.get("verdi")?.asText() ?: throw OpplysningIkkeFunnet("$opplysningsnavn ikke funnet")
    }

    fun hentGrunnlagSiste12Måneder(): Any {
        return Opplysning2(
            opplysningTekstId = "opplysning.grunnlag-siste-12-mnd.",
            verdi = hentOpplysning("Grunnlag siste 12 mnd."),
            datatype = FLYTTALL,
            enhet = KRONER,
        )
    }

    data class Opplysning2(
        val opplysningTekstId: String,
        val verdi: String,
        val datatype: Datatype,
        val enhet: Enhet,
    ) {
        enum class Datatype {
            TEKST,
            HELTALL,
            FLYTTALL,
            DATO,
            BOOLSK,
        }

        enum class Enhet {
            KRONER,
            DAGER,
            ENHETSLØS,
            UKER,
            BARN,
            TIMER,
        }
    }

    class OpplysningIkkeFunnet(message: String) : RuntimeException(message)
}

// Må hente fra opplysninger:
// "Søknadsdato" -> "opplysning.soknadsdato"
// "Søknadstidspunkt" -> "opplysning.soknadstidspunkt"
