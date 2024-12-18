package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.DATO
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.TEKST
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Enhet.ENHETSLØS
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Enhet.KRONER
import org.junit.jupiter.api.Test

class VedtakTest {
    private val resourseRetriever = object {}.javaClass
    private val vedtakMapper =
        resourseRetriever.getResource("/json/vedtak.json")?.let { VedtakMapper(it.readText()) }
            ?: throw RuntimeException("Fant ikke ressurs")

    @Test
    fun `Hent utfall`() {
        vedtakMapper.utfall shouldBe VedtakMapper.Utfall.INNVILGET
    }

    @Test
    fun `hent brevKriterier`() {
        vedtakMapper.vilkår.size shouldBe 10
    }

    @Test
    fun `Hent opplysning grunnlag`() {
        vedtakMapper.finnOpplysning("opplysning.grunnlag") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.grunnlag",
                verdi = "614871",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysnings provingsdato som egentlig er virkningsdato`() {
        vedtakMapper.finnOpplysning("opplysning.provingsdato") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.provingsdato",
                verdi = "2024-11-29",
                datatype = DATO,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning fastsatt arbeidstid per uke før tap`() {
        vedtakMapper.finnOpplysning("opplysning.fastsatt-arbeidstid-per-uke-for-tap") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.fastsatt-arbeidstid-per-uke-for-tap",
                verdi = "37.5",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.TIMER,
            )
    }

    @Test
    fun `Hent opplysning dagsats med barnetillegg etter samordning og 90% regel`() {
        vedtakMapper.finnOpplysning("opplysning.avrundet-dagsats-med-barnetillegg") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.avrundet-dagsats-med-barnetillegg",
                verdi = "1312",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysnings-inntekts-krav-siste-12-måneder `() {
        vedtakMapper.finnOpplysning("opplysning.inntektskrav-for-siste-12-mnd") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.inntektskrav-for-siste-12-mnd",
                verdi = "186042",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysnings-inntekts-krav-siste-36-måneder `() {
        vedtakMapper.finnOpplysning("opplysning.inntektskrav-for-siste-36-mnd") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.inntektskrav-for-siste-36-mnd",
                verdi = "372084",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning-arbeidsinntekt-siste-36-måneder `() {
        vedtakMapper.finnOpplysning("opplysning.arbeidsinntekt-siste-36-mnd") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.arbeidsinntekt-siste-36-mnd",
                verdi = "1700000",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning-arbeidsinntekt-siste-12-måneder `() {
        vedtakMapper.finnOpplysning("opplysning.arbeidsinntekt-siste-12-mnd") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.arbeidsinntekt-siste-12-mnd",
                verdi = "500000",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `skal hente antall G for krav til 12 mnd arbeidsinntekt`() {
        vedtakMapper.finnOpplysning("opplysning.antall-g-for-krav-til-12-mnd-arbeidsinntekt") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.antall-g-for-krav-til-12-mnd-arbeidsinntekt",
                verdi = "1.5",
                datatype = FLYTTALL,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `skal hente antall G for krav til 36 mnd arbeidsinntekt`() {
        vedtakMapper.finnOpplysning("opplysning.antall-g-for-krav-til-36-mnd-arbeidsinntekt") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.antall-g-for-krav-til-36-mnd-arbeidsinntekt",
                verdi = "3.0",
                datatype = FLYTTALL,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `skal hente gjennomsnittlig arbeidsinntekt siste 36 måneder`() {
        vedtakMapper.finnOpplysning("opplysning.gjennomsnittlig-arbeidsinntekt-siste-36-maaneder") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.gjennomsnittlig-arbeidsinntekt-siste-36-maaneder",
                verdi = "614871.2733389170",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `skal hente brukt beregningsregel`() {
        vedtakMapper.finnOpplysning("opplysning.brukt-beregningsregel") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.brukt-beregningsregel",
                verdi = "Gjennomsnittlig arbeidsinntekt siste 36 måneder",
                datatype = TEKST,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning samordnet dagsats uten barnetillegg`() {
        vedtakMapper.finnOpplysning("opplysning.samordnet-dagsats-uten-barnetillegg") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.samordnet-dagsats-uten-barnetillegg",
                verdi = "1276",
                datatype = Opplysning2.Datatype.HELTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning ukessats med barnetillegg etter samordning`() {
        vedtakMapper.finnOpplysning("opplysning.ukessats") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.ukessats",
                verdi = "6560",
                datatype = Opplysning2.Datatype.HELTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning antall stønadsuker`() {
        vedtakMapper.finnOpplysning("opplysning.antall-stonadsuker") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.antall-stonadsuker",
                verdi = "104",
                datatype = Opplysning2.Datatype.HELTALL,
                enhet = Opplysning2.Enhet.UKER,
            )
    }

    @Test
    fun `Hent opplysning egenandel`() {
        vedtakMapper.finnOpplysning("opplysning.egenandel") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.egenandel",
                verdi = "3936",
                datatype = Opplysning2.Datatype.HELTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning utbetalt arbeidsinntekt periode 1`() {
        vedtakMapper.finnOpplysning("oppysning.utbetalt-arbeidsinntekt-periode-1") shouldBe
            Opplysning2(
                opplysningTekstId = "oppysning.utbetalt-arbeidsinntekt-periode-1",
                verdi = "500000",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning utbetalt arbeidsinntekt periode 2`() {
        vedtakMapper.finnOpplysning("oppysning.utbetalt-arbeidsinntekt-periode-2") shouldBe
            Opplysning2(
                opplysningTekstId = "oppysning.utbetalt-arbeidsinntekt-periode-2",
                verdi = "600000",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning utbetalt arbeidsinntekt periode 3`() {
        vedtakMapper.finnOpplysning("oppysning.utbetalt-arbeidsinntekt-periode-3") shouldBe
            Opplysning2(
                opplysningTekstId = "oppysning.utbetalt-arbeidsinntekt-periode-3",
                verdi = "600000",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning har samordnet`() {
        vedtakMapper.finnOpplysning("opplysning.har-samordnet") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.har-samordnet",
                verdi = "true",
                datatype = Opplysning2.Datatype.BOOLSK,
                enhet = Opplysning2.Enhet.ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning andel av dagsats med barnetillegg som overstiger maks andel av dagpengegrunnlaget`() {
        vedtakMapper.finnOpplysning("opplysning.andel-av-dagsats-med-barnetillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.andel-av-dagsats-med-barnetillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget",
                verdi = "0",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning andel av dagsats med barnetillegg avkortet til maks andel av dagpengegrunnlaget`() {
        vedtakMapper.finnOpplysning("opplysning.andel-av-dagsats-med-barnetillegg-avkortet-til-maks-andel-av-dagpengegrunnlaget") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.andel-av-dagsats-med-barnetillegg-avkortet-til-maks-andel-av-dagpengegrunnlaget",
                verdi = "1476",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning antall barn som gir rett til barnetillegg`() {
        vedtakMapper.finnOpplysning("opplysning.antall-barn-som-gir-rett-til-barnetillegg") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.antall-barn-som-gir-rett-til-barnetillegg",
                verdi = "1",
                datatype = Opplysning2.Datatype.HELTALL,
                enhet = Opplysning2.Enhet.BARN,
            )
    }

    @Test
    fun `Hent opplysning barnetillegg i kroner`() {
        vedtakMapper.finnOpplysning("opplysning.barnetillegg-i-kroner") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.barnetillegg-i-kroner",
                verdi = "36",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }

    @Test
    fun `Hent opplysning første måned av opptjeningsperiode`() {
        vedtakMapper.finnOpplysning("opplysning.forste-maaned-av-opptjeningsperiode") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.forste-maaned-av-opptjeningsperiode",
                verdi = "2021-11-01",
                datatype = Opplysning2.Datatype.DATO,
                enhet = Opplysning2.Enhet.ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning siste avsluttende kalendermåned`() {
        vedtakMapper.finnOpplysning("opplysning.siste-avsluttende-kalendermaaned") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.siste-avsluttende-kalendermaaned",
                verdi = "2024-10-31",
                datatype = Opplysning2.Datatype.DATO,
                enhet = Opplysning2.Enhet.ENHETSLØS,
            )
    }

    @Test
    fun `Hent grunn lag siste 12 månede `() {
        vedtakMapper.finnOpplysning("opplysning.grunnlag-siste-12-mnd") shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.grunnlag-siste-12-mnd",
                verdi = "513677.2888214466",
                datatype = Opplysning2.Datatype.FLYTTALL,
                enhet = Opplysning2.Enhet.KRONER,
            )
    }
}
