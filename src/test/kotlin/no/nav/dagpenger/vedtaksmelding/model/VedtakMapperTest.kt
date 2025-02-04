package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.DATO
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.TEKST
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.BARN
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.ENHETSLØS
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.KRONER
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.TIMER
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.UKER
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak.Utfall.INNVILGET
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
        vedtak.vilkår.size shouldBe 18
    }

    @Test
    fun `Hent opplysning grunnlag`() {
        vedtak.finnOpplysning("opplysning.grunnlag") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.grunnlag",
                råVerdi = "206997",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning provingsdato som egentlig er virkningsdato`() {
        vedtak.finnOpplysning("opplysning.provingsdato") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.provingsdato",
                råVerdi = "2025-01-16",
                datatype = DATO,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning fastsatt ny arbeidstid per uke før tap`() {
        vedtak.finnOpplysning("opplysning.fastsatt-ny-arbeidstid-per-uke") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.fastsatt-ny-arbeidstid-per-uke",
                råVerdi = "10.123456",
                datatype = FLYTTALL,
                enhet = TIMER,
            )
    }

    @Test
    fun `Hent opplysning fastsatt vanlig arbeidstid per uke før tap`() {
        vedtak.finnOpplysning("opplysning.fastsatt-arbeidstid-per-uke-for-tap") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.fastsatt-arbeidstid-per-uke-for-tap",
                råVerdi = "33.45",
                datatype = FLYTTALL,
                enhet = TIMER,
            )
    }

    @Test
    fun `Hent opplysning krav til prosentvis tap av arbeidstid`() {
        vedtak.finnOpplysning("opplysning.krav-til-prosentvis-tap-av-arbeidstid") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.krav-til-prosentvis-tap-av-arbeidstid",
                råVerdi = "50.0",
                datatype = FLYTTALL,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning dagsats med barnetillegg etter samordning og 90 prosent regel`() {
        vedtak.finnOpplysning("opplysning.dagsats-med-barnetillegg-etter-samordning-og-90-prosent-regel") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.dagsats-med-barnetillegg-etter-samordning-og-90-prosent-regel",
                råVerdi = "321",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning inntektskrav siste 12 måneder`() {
        vedtak.finnOpplysning("opplysning.inntektskrav-for-siste-12-mnd") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.inntektskrav-for-siste-12-mnd",
                råVerdi = "186042",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning inntektskrav siste 36 måneder`() {
        vedtak.finnOpplysning("opplysning.inntektskrav-for-siste-36-mnd") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.inntektskrav-for-siste-36-mnd",
                råVerdi = "372084",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning arbeidsinntekt siste 12 måneder`() {
        vedtak.finnOpplysning("opplysning.arbeidsinntekt-siste-12-mnd") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.arbeidsinntekt-siste-12-mnd",
                råVerdi = "0",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning arbeidsinntekt siste 36 måneder`() {
        vedtak.finnOpplysning("opplysning.arbeidsinntekt-siste-36-mnd") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.arbeidsinntekt-siste-36-mnd",
                råVerdi = "555500",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning om antall G som gis som grunnlag ved verneplikt`() {
        vedtak.finnOpplysning("opplysning.antall-g-som-gis-som-grunnlag-ved-verneplikt") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.antall-g-som-gis-som-grunnlag-ved-verneplikt",
                råVerdi = "3.0",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning om brukt beregningsregel for grunnlagsberegning`() {
        vedtak.finnOpplysning("opplysning.brukt-beregningsregel-grunnlag") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.brukt-beregningsregel-grunnlag",
                råVerdi = "Gjennomsnittlig arbeidsinntekt siste 36 måneder",
                datatype = TEKST,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning om antall stønadsuker som gis ved ordinære dagpenger`() {
        vedtak.finnOpplysning("opplysning.antall-stonadsuker-som-gis-ved-ordinare-dagpenger") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.antall-stonadsuker-som-gis-ved-ordinare-dagpenger",
                råVerdi = "52",
                datatype = HELTALL,
                enhet = UKER,
            )
    }

    @Test
    fun `Hent opplysning antall stønadsuker`() {
        vedtak.finnOpplysning("opplysning.antall-stonadsuker") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.antall-stonadsuker",
                råVerdi = "52",
                datatype = HELTALL,
                enhet = UKER,
            )
    }

    @Test
    fun `Hent opplysning egenandel`() {
        vedtak.finnOpplysning("opplysning.egenandel") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.egenandel",
                råVerdi = "963",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning for samordnet ytelse sykepenger`() {
        vedtak.finnOpplysning("opplysning.sykepenger-dagsats") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.sykepenger-dagsats",
                råVerdi = "100",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning for samordnet ytelse pleiepenger`() {
        vedtak.finnOpplysning("opplysning.pleiepenger-dagsats") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.pleiepenger-dagsats",
                råVerdi = "150",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning for samordnet ytelse omsorgspenger`() {
        vedtak.finnOpplysning("opplysning.omsorgspenger-dagsats") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.omsorgspenger-dagsats",
                råVerdi = "150",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning for samordnet ytelse opplæringspenger`() {
        vedtak.finnOpplysning("opplysning.opplaeringspenger-dagsats") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.opplaeringspenger-dagsats",
                råVerdi = "180",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning for samordnet ytelse uføre`() {
        vedtak.finnOpplysning("opplysning.ufore-dagsats") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.ufore-dagsats",
                råVerdi = "200",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning for samordnet ytelse foreldrepenger`() {
        vedtak.finnOpplysning("opplysning.foreldrepenger-dagsats") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.foreldrepenger-dagsats",
                råVerdi = "210",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning for samordnet ytelse svangerskapspenger`() {
        vedtak.finnOpplysning("opplysning.svangerskapspenger-dagsats") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.svangerskapspenger-dagsats",
                råVerdi = "150",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning utbetalt arbeidsinntekt periode 1`() {
        vedtak.finnOpplysning("opplysning.utbetalt-arbeidsinntekt-periode-1") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.utbetalt-arbeidsinntekt-periode-1",
                råVerdi = "0",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning utbetalt arbeidsinntekt periode 2`() {
        vedtak.finnOpplysning("opplysning.utbetalt-arbeidsinntekt-periode-2") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.utbetalt-arbeidsinntekt-periode-2",
                råVerdi = "55550",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning utbetalt arbeidsinntekt periode 3`() {
        vedtak.finnOpplysning("opplysning.utbetalt-arbeidsinntekt-periode-3") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.utbetalt-arbeidsinntekt-periode-3",
                råVerdi = "499950",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning har samordnet`() {
        vedtak.finnOpplysning("opplysning.har-samordnet") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.har-samordnet",
                råVerdi = "true",
                datatype = BOOLSK,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning andel av dagsats med barnetillegg som overstiger maks andel av dagpengegrunnlaget`() {
        vedtak.finnOpplysning("opplysning.andel-av-dagsats-med-barnetillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.andel-av-dagsats-med-barnetillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget",
                råVerdi = "0",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning antall barn som gir rett til barnetillegg`() {
        vedtak.finnOpplysning("opplysning.antall-barn-som-gir-rett-til-barnetillegg") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.antall-barn-som-gir-rett-til-barnetillegg",
                råVerdi = "2",
                datatype = HELTALL,
                enhet = BARN,
            )
    }

    @Test
    fun `Hent opplysning barnetillegg i kroner`() {
        vedtak.finnOpplysning("opplysning.barnetillegg-i-kroner") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.barnetillegg-i-kroner",
                råVerdi = "74",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning første måned av opptjeningsperiode`() {
        vedtak.finnOpplysning("opplysning.forste-maaned-av-opptjeningsperiode") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.forste-maaned-av-opptjeningsperiode",
                råVerdi = "2022-01-01",
                datatype = DATO,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning siste avsluttende kalendermåned`() {
        vedtak.finnOpplysning("opplysning.siste-avsluttende-kalendermaaned") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.siste-avsluttende-kalendermaaned",
                råVerdi = "2024-12-31",
                datatype = DATO,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning 6 ganger grunnbelop`() {
        vedtak.finnOpplysning("opplysning.6-ganger-grunnbelop") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.6-ganger-grunnbelop",
                råVerdi = "744168",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning Krav til minsteinntekt`() {
        vedtak.finnOpplysning("opplysning.krav-til-minsteinntekt") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.krav-til-minsteinntekt",
                råVerdi = "true",
                datatype = BOOLSK,
            )
    }

    @Test
    fun `Hent inntjeningsperiode opplysninger`() {
        vedtak.opplysninger.single {
            it.opplysningTekstId == "opplysning.forste-maaned-aar-for-inntektsperiode-1"
        }.formatertVerdi shouldBe "januar 2024"
        vedtak.opplysninger.single {
            it.opplysningTekstId == "opplysning.forste-maaned-aar-for-inntektsperiode-2"
        }.formatertVerdi shouldBe "januar 2023"
        vedtak.opplysninger.single {
            it.opplysningTekstId == "opplysning.forste-maaned-aar-for-inntektsperiode-3"
        }.formatertVerdi shouldBe "januar 2022"

        vedtak.opplysninger.single {
            it.opplysningTekstId == "opplysning.siste-maaned-aar-for-inntektsperiode-1"
        }.formatertVerdi shouldBe "desember 2024"
        vedtak.opplysninger.single {
            it.opplysningTekstId == "opplysning.siste-maaned-aar-for-inntektsperiode-2"
        }.formatertVerdi shouldBe "desember 2023"
        vedtak.opplysninger.single {
            it.opplysningTekstId == "opplysning.siste-maaned-aar-for-inntektsperiode-3"
        }.formatertVerdi shouldBe "desember 2022"
    }

    @Test
    fun `Skal håndtere at opplysning, kvoter og samordning ikke finnes`() {
        shouldNotThrowAny {
            VedtakMapper(
                """
            {
                "behandlingId": "01944f92-9828-770f-874d-534fd69b17c1",
                "fagsakId": "fagsakid test",
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
    fun `Skal kaste exception dersom utfall, vilkår eller fagsakId mangler`() {
        shouldThrow<UtfallMangler> {
            VedtakMapper(
                """
            {
                "behandlingId": "01944f92-9828-770f-874d-534fd69b17c1",
                "fagsakId": "fagsakid test",
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
                "fagsakId": "fagsakid test",
                "fastsatt": {
                    "utfall": false
                }
            }
            """,
            ).vedtak()
        }

        shouldThrow<FagsakIdMangler> {
            VedtakMapper(
                """
            {
                "behandlingId": "01944f92-9828-770f-874d-534fd69b17c1",
                "fastsatt": {
                    "utfall": false
                },
                "vilkår": []
            }
            """,
            ).vedtak()
        }
    }
}
