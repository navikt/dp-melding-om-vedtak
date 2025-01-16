package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.DATO
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.TEKST
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.BARN
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.ENHETSLØS
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.KRONER
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.TIMER
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.UKER
import no.nav.dagpenger.vedtaksmelding.model.Utfall.INNVILGET
import org.junit.jupiter.api.Test

class VedtakMapperTest {
    private val resourceRetriever = object {}.javaClass
    private val vedtak =
        resourceRetriever.getResource("/json/vedtak.json")?.let { VedtakMapper(it.readText()).vedtak() }
            ?: throw RuntimeException("Fant ikke ressurs")

    @Test
    fun `Hent utfall`() {
        vedtak.utfall shouldBe INNVILGET
    }

    @Test
    fun `hent brevKriterier`() {
        vedtak.vilkår.size shouldBe 10
    }

    @Test
    fun `Hent opplysning grunnlag`() {
        vedtak.finnOpplysning("opplysning.grunnlag") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.grunnlag",
                verdi = "614871",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning provingsdato som egentlig er virkningsdato`() {
        vedtak.finnOpplysning("opplysning.provingsdato") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.provingsdato",
                verdi = "2024-11-29",
                datatype = DATO,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning fastsatt arbeidstid per uke før tap`() {
        vedtak.finnOpplysning("opplysning.fastsatt-arbeidstid-per-uke-for-tap") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.fastsatt-arbeidstid-per-uke-for-tap",
                verdi = "37.5",
                datatype = FLYTTALL,
                enhet = TIMER,
            )
    }

    @Test
    fun `Hent opplysning dagsats med barnetillegg etter samordning og 90% regel`() {
        vedtak.finnOpplysning("opplysning.dagsats-med-barnetillegg-etter-samordning-og-90-prosent-regel") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.dagsats-med-barnetillegg-etter-samordning-og-90-prosent-regel",
                verdi = "1312",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysnings-inntekts-krav-siste-12-måneder `() {
        vedtak.finnOpplysning("opplysning.inntektskrav-for-siste-12-mnd") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.inntektskrav-for-siste-12-mnd",
                verdi = "186042",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysnings-inntekts-krav-siste-36-måneder `() {
        vedtak.finnOpplysning("opplysning.inntektskrav-for-siste-36-mnd") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.inntektskrav-for-siste-36-mnd",
                verdi = "372084",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning-arbeidsinntekt-siste-36-måneder `() {
        vedtak.finnOpplysning("opplysning.arbeidsinntekt-siste-36-mnd") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.arbeidsinntekt-siste-36-mnd",
                verdi = "1700000",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning-arbeidsinntekt-siste-12-måneder `() {
        vedtak.finnOpplysning("opplysning.arbeidsinntekt-siste-12-mnd") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.arbeidsinntekt-siste-12-mnd",
                verdi = "500000",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `skal hente antall G for krav til 12 mnd arbeidsinntekt`() {
        vedtak.finnOpplysning("opplysning.antall-g-for-krav-til-12-mnd-arbeidsinntekt") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.antall-g-for-krav-til-12-mnd-arbeidsinntekt",
                verdi = "1.5",
                datatype = FLYTTALL,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `skal hente antall G for krav til 36 mnd arbeidsinntekt`() {
        vedtak.finnOpplysning("opplysning.antall-g-for-krav-til-36-mnd-arbeidsinntekt") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.antall-g-for-krav-til-36-mnd-arbeidsinntekt",
                verdi = "3.0",
                datatype = FLYTTALL,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `skal hente gjennomsnittlig arbeidsinntekt siste 36 måneder`() {
        vedtak.finnOpplysning("opplysning.gjennomsnittlig-arbeidsinntekt-siste-36-maaneder") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.gjennomsnittlig-arbeidsinntekt-siste-36-maaneder",
                verdi = "614871.2733389170",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `skal hente brukt beregningsregel`() {
        vedtak.finnOpplysning("opplysning.brukt-beregningsregel") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.brukt-beregningsregel",
                verdi = "Gjennomsnittlig arbeidsinntekt siste 36 måneder",
                datatype = TEKST,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning antall stønadsuker`() {
        vedtak.finnOpplysning("opplysning.antall-stonadsuker") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.antall-stonadsuker",
                verdi = "104",
                datatype = HELTALL,
                enhet = UKER,
            )
    }

    @Test
    fun `Hent opplysning egenandel`() {
        vedtak.finnOpplysning("opplysning.egenandel") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.egenandel",
                verdi = "3936",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning utbetalt arbeidsinntekt periode 1`() {
        vedtak.finnOpplysning("opplysning.utbetalt-arbeidsinntekt-periode-1") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.utbetalt-arbeidsinntekt-periode-1",
                verdi = "500000",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning utbetalt arbeidsinntekt periode 2`() {
        vedtak.finnOpplysning("opplysning.utbetalt-arbeidsinntekt-periode-2") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.utbetalt-arbeidsinntekt-periode-2",
                verdi = "600000",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning utbetalt arbeidsinntekt periode 3`() {
        vedtak.finnOpplysning("opplysning.utbetalt-arbeidsinntekt-periode-3") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.utbetalt-arbeidsinntekt-periode-3",
                verdi = "600000",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning har samordnet`() {
        vedtak.finnOpplysning("opplysning.har-samordnet") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.har-samordnet",
                verdi = "true",
                datatype = BOOLSK,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning andel av dagsats med barnetillegg som overstiger maks andel av dagpengegrunnlaget`() {
        vedtak.finnOpplysning("opplysning.andel-av-dagsats-med-barnetillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.andel-av-dagsats-med-barnetillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget",
                verdi = "0",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning andel av dagsats med barnetillegg avkortet til maks andel av dagpengegrunnlaget`() {
        vedtak.finnOpplysning("opplysning.andel-av-dagsats-med-barnetillegg-avkortet-til-maks-andel-av-dagpengegrunnlaget") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.andel-av-dagsats-med-barnetillegg-avkortet-til-maks-andel-av-dagpengegrunnlaget",
                verdi = "1476",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning antall barn som gir rett til barnetillegg`() {
        vedtak.finnOpplysning("opplysning.antall-barn-som-gir-rett-til-barnetillegg") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.antall-barn-som-gir-rett-til-barnetillegg",
                verdi = "1",
                datatype = HELTALL,
                enhet = BARN,
            )
    }

    @Test
    fun `Hent opplysning barnetillegg i kroner`() {
        vedtak.finnOpplysning("opplysning.barnetillegg-i-kroner") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.barnetillegg-i-kroner",
                verdi = "36",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning første måned av opptjeningsperiode`() {
        vedtak.finnOpplysning("opplysning.forste-maaned-av-opptjeningsperiode") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.forste-maaned-av-opptjeningsperiode",
                verdi = "2021-11-01",
                datatype = DATO,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning siste avsluttende kalendermåned`() {
        vedtak.finnOpplysning("opplysning.siste-avsluttende-kalendermaaned") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.siste-avsluttende-kalendermaaned",
                verdi = "2024-10-31",
                datatype = DATO,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning grunnlag siste 12 måneder`() {
        vedtak.finnOpplysning("opplysning.grunnlag-siste-12-mnd") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.grunnlag-siste-12-mnd",
                verdi = "513677.2888214466",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning 6 ganger grunnbelop`() {
        vedtak.finnOpplysning("opplysning.6-ganger-grunnbelop") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.6-ganger-grunnbelop",
                verdi = "744168",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning Krav til minsteinntekt`() {
        vedtak.finnOpplysning("opplysning.krav-til-minsteinntekt") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.krav-til-minsteinntekt",
                verdi = "true",
                datatype = BOOLSK,
            )
    }

    @Test
    fun `Hent inntjeningsperiode opplysninger`() {
        vedtak.opplysninger.single {
            it.opplysningTekstId == "opplysning.forste-maaned-aar-for-inntektsperiode-1"
        }.verdi shouldBe "november 2023"
        vedtak.opplysninger.single {
            it.opplysningTekstId == "opplysning.forste-maaned-aar-for-inntektsperiode-2"
        }.verdi shouldBe "november 2022"
        vedtak.opplysninger.single {
            it.opplysningTekstId == "opplysning.forste-maaned-aar-for-inntektsperiode-3"
        }.verdi shouldBe "november 2021"

        vedtak.opplysninger.single {
            it.opplysningTekstId == "opplysning.siste-maaned-aar-for-inntektsperiode-1"
        }.verdi shouldBe "oktober 2024"
        vedtak.opplysninger.single {
            it.opplysningTekstId == "opplysning.siste-maaned-aar-for-inntektsperiode-2"
        }.verdi shouldBe "oktober 2023"
        vedtak.opplysninger.single {
            it.opplysningTekstId == "opplysning.siste-maaned-aar-for-inntektsperiode-3"
        }.verdi shouldBe "oktober 2022"
    }

    @Test
    fun `Skal håndtere at opplysning og kvoter ikke finnes`() {
        shouldNotThrowAny {
            VedtakMapper(
                """
            {
                "behandlingId": "01944f92-9828-770f-874d-534fd69b17c1",
                "fastsatt": {
                    "utfall": true
                },
                "vilkår": []
            }
            """,
            ).vedtak()
        }
    }

    @Test
    fun `Skal kaste exception dersom utfall eller vilkår mangler`() {
        shouldThrow<UtfallMangler> {
            VedtakMapper(
                """
            {
                "behandlingId": "01944f92-9828-770f-874d-534fd69b17c1",
                "fastsatt": {
                    },
                "vilkår": []
            }
            """,
            ).vedtak()
        }
        shouldThrow<VilkårMangler> {
            VedtakMapper(
                """
            {
                "behandlingId": "01944f92-9828-770f-874d-534fd69b17c1",
                "fastsatt": {
                    "utfall": false
                }
            }
            """,
            ).vedtak()
        }
    }
}
