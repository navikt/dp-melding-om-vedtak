package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.DATO
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.TEKST
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Enhet.ENHETSLØS
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Enhet.KRONER
import org.junit.jupiter.api.Test

class VedtakMapperTest {
    // "Krav til minsteinntekt" -> "opplysning.krav-til-minsteinntekt"
    // "Krav på dagpenger" -> "opplysning.krav-paa-dagpenger"
    private val resourseRetriever = object {}.javaClass
    private val vedtakMapper = VedtakMapper(resourseRetriever.getResource("/json/vedtak.json").readText())

    @Test
    fun `hent opplysning krav-til-minsteinntekt`() {
        vedtakMapper.hentOppfyllerKravTilMinsteinntekt() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.krav-til-minsteinntekt",
                verdi = "true",
                datatype = BOOLSK,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning grunnlag`() {
        vedtakMapper.hentOpplysningGrunnlag() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.grunnlag",
                verdi = "614871",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysnings provingsdato som egentlig er virkningsdato`() {
        vedtakMapper.hentOpplysningProvingDato() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.provingsdato",
                verdi = "2024-11-29",
                datatype = DATO,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysnings-inntekts-krav-siste-12-måneder `() {
        vedtakMapper.hentInntektsKravSiste12Måneder() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.inntektskrav-for-siste-12-mnd",
                verdi = "186042",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysnings-inntekts-krav-siste-36-måneder `() {
        vedtakMapper.hentInntektsKravSiste36Måneder() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.inntektskrav-for-siste-36-mnd",
                verdi = "372084",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning-arbeidsinntekt-siste-12-måneder `() {
        vedtakMapper.hentArbeidsinntektSiste12Måneder() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.arbeidsinntekt-siste-12-mnd",
                verdi = "500000",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning-arbeidsinntekt-siste-36-måneder `() {
        vedtakMapper.hentArbeidsinntektSiste36Måneder() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.arbeidsinntekt-siste-36-mnd",
                verdi = "1700000",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `skal hente antall G for krav til 12 mnd arbeidsinntekt`() {
        vedtakMapper.hentAntallGForKravTil12MndArbeidsinntekt() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.antall-g-for-krav-til-12-mnd-arbeidsinntekt",
                verdi = "1.5",
                datatype = FLYTTALL,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `skal hente antall G for krav til 36 mnd arbeidsinntekt`() {
        vedtakMapper.hentAntallGForKravTil36MndArbeidsinntekt() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.antall-g-for-krav-til-36-mnd-arbeidsinntekt",
                verdi = "3.0",
                datatype = FLYTTALL,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `skal hente gjennomsnittlig arbeidsinntekt siste 36 måneder`() {
        vedtakMapper.hentGjennomsnittligArbeidsinntektSiste36Måneder() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.gjennomsnittlig-arbeidsinntekt-siste-36-maaneder",
                verdi = "614871.2733389170",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `skal hente brukt beregningsregel`() {
        vedtakMapper.hentBruktBeregningsregel() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.brukt-beregningsregel",
                verdi = "Gjennomsnittlig arbeidsinntekt siste 36 måneder",
                datatype = TEKST,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning samordnet dagsats uten barnetillegg`() {
        vedtakMapper.hentSamordnetDagsatsUtenBarnetillegg() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.samordnet-dagsats-uten-barnetillegg",
                verdi = "1276",
                datatype = Opplysning2.Datatype.HELTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning ukessats med barnetillegg etter samordning`() {
        vedtakMapper.hentUkessatsMedBarnetillegg() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.ukessats",
                verdi = "6560",
                datatype = Opplysning2.Datatype.HELTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning antall stønadsuker`() {
        vedtakMapper.hentAntallStonadsuker() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.antall-stonadsuker",
                verdi = "104",
                datatype = Opplysning2.Datatype.HELTALL,
                enhet = Opplysning2.Enhet.UKER,
            )
    }

    @Test
    fun `Hent opplysning egenandel`() {
        vedtakMapper.hentEgenandel() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.egenandel",
                verdi = "3936",
                datatype = Opplysning2.Datatype.HELTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning utbetalt arbeidsinntekt periode 1`() {
        vedtakMapper.hentUtbetaltArbeidsinntektPeriode1() shouldBe
            Opplysning2(
                opplysningTekstId = "oppysning.utbetalt-arbeidsinntekt-periode-1",
                verdi = "500000",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning utbetalt arbeidsinntekt periode 2`() {
        vedtakMapper.hentUtbetaltArbeidsinntektPeriode2() shouldBe
            Opplysning2(
                opplysningTekstId = "oppysning.utbetalt-arbeidsinntekt-periode-2",
                verdi = "600000",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning utbetalt arbeidsinntekt periode 3`() {
        vedtakMapper.hentUtbetaltArbeidsinntektPeriode3() shouldBe
            Opplysning2(
                opplysningTekstId = "oppysning.utbetalt-arbeidsinntekt-periode-3",
                verdi = "600000",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning har samordnet`() {
        vedtakMapper.hentHarSamordnet() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.har-samordnet",
                verdi = "true",
                datatype = Opplysning2.Datatype.BOOLSK,
                enhet = Opplysning2.Enhet.ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning andel av dagsats med barnetillegg som overstiger maks andel av dagpengegrunnlaget`() {
        vedtakMapper.hentAndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.andel-av-dagsats-med-barnetillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget",
                verdi = "0",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning andel av dagsats med barnetillegg avkortet til maks andel av dagpengegrunnlaget`() {
        vedtakMapper.hentAndelAvDagsatsMedBarnetilleggAvkortetTilMaksAndelAvDagpengegrunnlaget() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.andel-av-dagsats-med-barnetillegg-avkortet-til-maks-andel-av-dagpengegrunnlaget",
                verdi = "1476",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning antall barn som gir rett til barnetillegg`() {
        vedtakMapper.hentAntallBarnSomGirRettTilBarnetillegg() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.antall-barn-som-gir-rett-til-barnetillegg",
                verdi = "1",
                datatype = Opplysning2.Datatype.HELTALL,
                enhet = Opplysning2.Enhet.BARN,
            )
    }

    @Test
    fun `Hent opplysning barnetillegg i kroner`() {
        vedtakMapper.hentBarnetilleggIKroner() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.barnetillegg-i-kroner",
                verdi = "36",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning fastsatt arbeidstid per uke før tap`() {
        vedtakMapper.hentFastsattArbeidstidPerUkeForTap() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.fastsatt-arbeidstid-per-uke-for-tap",
                verdi = "37.5",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.TIMER,
            )
    }

    @Test
    fun `Hent opplysning første måned av opptjeningsperiode`() {
        vedtakMapper.hentForsteMaanedAvOpptjeningsperiode() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.forste-maaned-av-opptjeningsperiode",
                verdi = "2021-11-01",
                datatype = Opplysning2.Datatype.DATO,
                enhet = Opplysning2.Enhet.ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning siste avsluttende kalendermåned`() {
        vedtakMapper.hentSisteAvsluttendeKalendermaaned() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.siste-avsluttende-kalendermaaned",
                verdi = "2024-10-31",
                datatype = Opplysning2.Datatype.DATO,
                enhet = Opplysning2.Enhet.ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning dagsats med barnetillegg etter samordning og 90% regel`() {
        vedtakMapper.hentDagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.avrundet-dagsats-med-barnetillegg",
                verdi = "1312",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent grunn lag siste 12 månede `() {
        vedtakMapper.hentGrunnlagSiste12Måneder() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.grunnlag-siste-12-mnd.",
                verdi = "513677.2888214466",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }
}
