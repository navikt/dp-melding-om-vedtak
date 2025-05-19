package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AntallBarnSomGirRettTilBarnetillegg
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AntallGSomGisSomGrunnlagVedVerneplikt
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AntallStønadsuker
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AntallStønadsukerSomGisVedOrdinæreDagpenger
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.ArbeidsinntektSiste12Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.ArbeidsinntektSiste36Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.BarnetilleggIKroner
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.BruktBeregningsregelGrunnlag
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.Egenandel
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.FastsattNyArbeidstidPerUke
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.FastsattVanligArbeidstidPerUke
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.ForeldrepengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.FørsteMånedAvOpptjeningsperiode
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.FørsteMånedOgÅrForInntektsperiode1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.FørsteMånedOgÅrForInntektsperiode2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.FørsteMånedOgÅrForInntektsperiode3
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.Grunnlag
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.HarBruktBeregningsregelArbeidstidSiste12Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.HarBruktBeregningsregelArbeidstidSiste36Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.HarBruktBeregningsregelArbeidstidSiste6Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.HarSamordnet
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.InntektskravSiste12Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.InntektskravSiste36Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.KravTilProsentvisTapAvArbeidstid
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.OmsorgspengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.OpplæringspengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.PleiepengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.Prøvingsdato
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.SeksGangerGrunnbeløp
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.SisteMånedAvOpptjeningsperiode
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.SisteMånedOgÅrForInntektsperiode1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.SisteMånedOgÅrForInntektsperiode2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.SisteMånedOgÅrForInntektsperiode3
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.SvangerskapspengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.SykepengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.UføreDagsats
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.UtbetaltArbeidsinntektPeriode1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.UtbetaltArbeidsinntektPeriode2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.UtbetaltArbeidsinntektPeriode3
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
        vedtak.finnOpplysning(Grunnlag.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = Grunnlag.opplysningTekstId,
                råVerdi = "206997",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning provingsdato som egentlig er virkningsdato`() {
        vedtak.finnOpplysning(Prøvingsdato.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = Prøvingsdato.opplysningTekstId,
                råVerdi = "2025-01-16",
                datatype = DATO,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning fastsatt ny arbeidstid per uke før tap`() {
        vedtak.finnOpplysning(FastsattNyArbeidstidPerUke.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = FastsattNyArbeidstidPerUke.opplysningTekstId,
                råVerdi = "10.123456",
                datatype = FLYTTALL,
                enhet = TIMER,
            )
    }

    @Test
    fun `Hent opplysning fastsatt vanlig arbeidstid per uke før tap`() {
        vedtak.finnOpplysning(FastsattVanligArbeidstidPerUke.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = FastsattVanligArbeidstidPerUke.opplysningTekstId,
                råVerdi = "33.45",
                datatype = FLYTTALL,
                enhet = TIMER,
            )
    }

    @Test
    fun `Hent opplysning krav til prosentvis tap av arbeidstid`() {
        vedtak.finnOpplysning(KravTilProsentvisTapAvArbeidstid.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = KravTilProsentvisTapAvArbeidstid.opplysningTekstId,
                råVerdi = "50.0",
                datatype = FLYTTALL,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning dagsats med barnetillegg etter samordning og 90 prosent regel`() {
        vedtak.finnOpplysning(DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel.opplysningTekstId,
                råVerdi = "321",
                datatype = HELTALL,
                enhet = KRONER,
            ).also {
                it.formatertVerdi shouldBe "321 kroner"
            }
    }

    @Test
    fun `Hent opplysning inntektskrav siste 12 måneder`() {
        vedtak.finnOpplysning(InntektskravSiste12Måneder.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = InntektskravSiste12Måneder.opplysningTekstId,
                råVerdi = "186042",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning inntektskrav siste 36 måneder`() {
        vedtak.finnOpplysning(InntektskravSiste36Måneder.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = InntektskravSiste36Måneder.opplysningTekstId,
                råVerdi = "372084",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning arbeidsinntekt siste 12 måneder`() {
        vedtak.finnOpplysning(ArbeidsinntektSiste12Måneder.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = ArbeidsinntektSiste12Måneder.opplysningTekstId,
                råVerdi = "0",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning arbeidsinntekt siste 36 måneder`() {
        vedtak.finnOpplysning(ArbeidsinntektSiste36Måneder.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = ArbeidsinntektSiste36Måneder.opplysningTekstId,
                råVerdi = "555500",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning om antall G som gis som grunnlag ved verneplikt`() {
        vedtak.finnOpplysning(AntallGSomGisSomGrunnlagVedVerneplikt.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = AntallGSomGisSomGrunnlagVedVerneplikt.opplysningTekstId,
                råVerdi = "3.0",
                datatype = FLYTTALL,
            )
    }

    @Test
    fun `Hent opplysning om brukt beregningsregel for grunnlagsberegning i lowercase`() {
        vedtak.finnOpplysning(BruktBeregningsregelGrunnlag.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = BruktBeregningsregelGrunnlag.opplysningTekstId,
                råVerdi = "Gjennomsnittlig arbeidsinntekt siste 36 måneder",
                datatype = TEKST,
                enhet = ENHETSLØS,
            ).also { it.formatertVerdi shouldBe "gjennomsnittlig arbeidsinntekt siste 36 måneder" }
    }

    @Test
    fun `Hent opplysning om brukt beregningsregel for arbeidstid siste 6 måneder`() {
        vedtak.finnOpplysning(HarBruktBeregningsregelArbeidstidSiste6Måneder.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = HarBruktBeregningsregelArbeidstidSiste6Måneder.opplysningTekstId,
                råVerdi = "true",
                datatype = BOOLSK,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning om brukt beregningsregel for arbeidstid siste 12 måneder`() {
        vedtak.finnOpplysning(HarBruktBeregningsregelArbeidstidSiste12Måneder.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = HarBruktBeregningsregelArbeidstidSiste12Måneder.opplysningTekstId,
                råVerdi = "false",
                datatype = BOOLSK,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning om brukt beregningsregel for arbeidstid siste 36 måneder`() {
        vedtak.finnOpplysning(HarBruktBeregningsregelArbeidstidSiste36Måneder.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = HarBruktBeregningsregelArbeidstidSiste36Måneder.opplysningTekstId,
                råVerdi = "false",
                datatype = BOOLSK,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning om antall stønadsuker som gis ved ordinære dagpenger`() {
        vedtak.finnOpplysning(AntallStønadsukerSomGisVedOrdinæreDagpenger.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = AntallStønadsukerSomGisVedOrdinæreDagpenger.opplysningTekstId,
                råVerdi = "52",
                datatype = HELTALL,
                enhet = UKER,
            )
    }

    @Test
    fun `Hent opplysning antall stønadsuker`() {
        vedtak.finnOpplysning(AntallStønadsuker.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = AntallStønadsuker.opplysningTekstId,
                råVerdi = "52",
                datatype = HELTALL,
                enhet = UKER,
            )
    }

    @Test
    fun `Hent opplysning egenandel`() {
        vedtak.finnOpplysning(Egenandel.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = Egenandel.opplysningTekstId,
                råVerdi = "963",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning for samordnet ytelse sykepenger`() {
        vedtak.finnOpplysning(SykepengerDagsats.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = SykepengerDagsats.opplysningTekstId,
                råVerdi = "100",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning for samordnet ytelse pleiepenger`() {
        vedtak.finnOpplysning(PleiepengerDagsats.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = PleiepengerDagsats.opplysningTekstId,
                råVerdi = "150",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning for samordnet ytelse omsorgspenger`() {
        vedtak.finnOpplysning(OmsorgspengerDagsats.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = OmsorgspengerDagsats.opplysningTekstId,
                råVerdi = "150",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning for samordnet ytelse opplæringspenger`() {
        vedtak.finnOpplysning(OpplæringspengerDagsats.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = OpplæringspengerDagsats.opplysningTekstId,
                råVerdi = "180",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning for samordnet ytelse uføre`() {
        vedtak.finnOpplysning(UføreDagsats.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = UføreDagsats.opplysningTekstId,
                råVerdi = "200",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning for samordnet ytelse foreldrepenger`() {
        vedtak.finnOpplysning(ForeldrepengerDagsats.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = ForeldrepengerDagsats.opplysningTekstId,
                råVerdi = "210",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning for samordnet ytelse svangerskapspenger`() {
        vedtak.finnOpplysning(SvangerskapspengerDagsats.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = SvangerskapspengerDagsats.opplysningTekstId,
                råVerdi = "150",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning utbetalt arbeidsinntekt periode 1`() {
        vedtak.finnOpplysning(UtbetaltArbeidsinntektPeriode1.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = UtbetaltArbeidsinntektPeriode1.opplysningTekstId,
                råVerdi = "0",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning utbetalt arbeidsinntekt periode 2`() {
        vedtak.finnOpplysning(UtbetaltArbeidsinntektPeriode2.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = UtbetaltArbeidsinntektPeriode2.opplysningTekstId,
                råVerdi = "55550",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning utbetalt arbeidsinntekt periode 3`() {
        vedtak.finnOpplysning(UtbetaltArbeidsinntektPeriode3.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = UtbetaltArbeidsinntektPeriode3.opplysningTekstId,
                råVerdi = "499950",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning har samordnet`() {
        vedtak.finnOpplysning(HarSamordnet.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = HarSamordnet.opplysningTekstId,
                råVerdi = "true",
                datatype = BOOLSK,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning andel av dagsats med barnetillegg som overstiger maks andel av dagpengegrunnlaget`() {
        vedtak.finnOpplysning(AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget.opplysningTekstId,
                råVerdi = "0",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning antall barn som gir rett til barnetillegg`() {
        vedtak.finnOpplysning(AntallBarnSomGirRettTilBarnetillegg.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = AntallBarnSomGirRettTilBarnetillegg.opplysningTekstId,
                råVerdi = "2",
                datatype = HELTALL,
                enhet = BARN,
            )
    }

    @Test
    fun `Hent opplysning barnetillegg i kroner`() {
        vedtak.finnOpplysning(BarnetilleggIKroner.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = BarnetilleggIKroner.opplysningTekstId,
                råVerdi = "74",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning første måned av opptjeningsperiode`() {
        vedtak.finnOpplysning(FørsteMånedAvOpptjeningsperiode.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = FørsteMånedAvOpptjeningsperiode.opplysningTekstId,
                råVerdi = "2022-01-01",
                datatype = DATO,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning siste avsluttende kalendermåned`() {
        vedtak.finnOpplysning(SisteMånedAvOpptjeningsperiode.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = SisteMånedAvOpptjeningsperiode.opplysningTekstId,
                råVerdi = "2024-12-31",
                datatype = DATO,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning 6 ganger grunnbelop`() {
        vedtak.finnOpplysning(SeksGangerGrunnbeløp.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = SeksGangerGrunnbeløp.opplysningTekstId,
                råVerdi = "744168",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent inntjeningsperiode opplysninger`() {
        vedtak.opplysninger.single {
            it.opplysningTekstId == FørsteMånedOgÅrForInntektsperiode1.opplysningTekstId
        }.formatertVerdi shouldBe "januar 2024"
        vedtak.opplysninger.single {
            it.opplysningTekstId == FørsteMånedOgÅrForInntektsperiode2.opplysningTekstId
        }.formatertVerdi shouldBe "januar 2023"
        vedtak.opplysninger.single {
            it.opplysningTekstId == FørsteMånedOgÅrForInntektsperiode3.opplysningTekstId
        }.formatertVerdi shouldBe "januar 2022"

        vedtak.opplysninger.single {
            it.opplysningTekstId == SisteMånedOgÅrForInntektsperiode1.opplysningTekstId
        }.formatertVerdi shouldBe "desember 2024"
        vedtak.opplysninger.single {
            it.opplysningTekstId == SisteMånedOgÅrForInntektsperiode2.opplysningTekstId
        }.formatertVerdi shouldBe "desember 2023"
        vedtak.opplysninger.single {
            it.opplysningTekstId == SisteMånedOgÅrForInntektsperiode3.opplysningTekstId
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
