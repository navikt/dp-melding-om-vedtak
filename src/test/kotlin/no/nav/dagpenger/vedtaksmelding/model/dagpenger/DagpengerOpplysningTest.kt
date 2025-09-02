package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import no.nav.dagpenger.vedtaksmelding.util.readFile
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.YearMonth

class DagpengerOpplysningTest {
    @Test
    fun `Skal kunne lage alle type opplysninger ordninær`() {
        val opplysningData = OpplysningData("/json/innvigelse_ord_resultat.json".readFile())
        setOf(
            DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid(opplysningData),
            DagpengerOpplysning.InntektskravSiste12Måneder(opplysningData),
            DagpengerOpplysning.InntektskravSiste36Måneder(opplysningData),
            DagpengerOpplysning.ArbeidsinntektSiste12Måneder(opplysningData),
            DagpengerOpplysning.ArbeidsinntektSiste36Måneder(opplysningData),
            DagpengerOpplysning.AntallGSomGisSomGrunnlagVedVerneplikt(opplysningData),
            DagpengerOpplysning.BruktBeregningsregelGrunnlag(opplysningData),
            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder(opplysningData),
            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste12Måneder(opplysningData),
            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste36Måneder(opplysningData),
            DagpengerOpplysning.UtbetaltArbeidsinntektPeriode1(opplysningData),
            DagpengerOpplysning.UtbetaltArbeidsinntektPeriode2(opplysningData),
            DagpengerOpplysning.UtbetaltArbeidsinntektPeriode3(opplysningData),
            DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger(opplysningData),
            DagpengerOpplysning.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget(opplysningData),
            DagpengerOpplysning.BarnetilleggIKroner(opplysningData),
            DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode(opplysningData),
            DagpengerOpplysning.SisteMånedAvOpptjeningsperiode(opplysningData),
            DagpengerOpplysning.SeksGangerGrunnbeløp(opplysningData),
            DagpengerOpplysning.Aldersgrense(opplysningData),
            DagpengerOpplysning.Grunnlag(opplysningData),
            DagpengerOpplysning.DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel(opplysningData),
            DagpengerOpplysning.Prøvingsdato(opplysningData),
            DagpengerOpplysning.FastsattVanligArbeidstidPerUke(opplysningData),
            DagpengerOpplysning.FastsattNyArbeidstidPerUke(opplysningData),
            DagpengerOpplysning.HarSamordnet(opplysningData),
            DagpengerOpplysning.SykepengerDagsats(opplysningData),
            DagpengerOpplysning.PleiepengerDagsats(opplysningData),
            DagpengerOpplysning.OmsorgspengerDagsats(opplysningData),
            DagpengerOpplysning.OpplæringspengerDagsats(opplysningData),
            DagpengerOpplysning.UføreDagsats(opplysningData),
            DagpengerOpplysning.ForeldrepengerDagsats(opplysningData),
            DagpengerOpplysning.SvangerskapspengerDagsats(opplysningData),
            DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(opplysningData),
            DagpengerOpplysning.PeriodeSomGisVedVerneplikt(opplysningData),
            DagpengerOpplysning.Egenandel(opplysningData),
            DagpengerOpplysning.KravTilArbeidssøker(opplysningData),
            DagpengerOpplysning.OppfyllerKravTilMobilitet(opplysningData),
            DagpengerOpplysning.OppfyllerKravTilArbeidsfør(opplysningData),
            DagpengerOpplysning.OppfyllerKravTilArbeidssøker(opplysningData),
            DagpengerOpplysning.OppfyllerKravetTilEthvertArbeid(opplysningData),
            DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(opplysningData),
            DagpengerOpplysning.OppfyllerKravetTilIkkeUtestengt(opplysningData),
            DagpengerOpplysning.OppfyllerKravetTilOpphold(opplysningData),
            DagpengerOpplysning.IkkeFulleYtelser(opplysningData),
            DagpengerOpplysning.KravTilTapAvArbeidsinntektOgArbeidstid(opplysningData),
            DagpengerOpplysning.KravTilTaptArbeidstid(opplysningData),
            DagpengerOpplysning.KravTilTapAvArbeidsinntekt(opplysningData),
            DagpengerOpplysning.IkkeStreikEllerLockout(opplysningData),
            DagpengerOpplysning.KravTilAlder(opplysningData),
            DagpengerOpplysning.KravTilUtdanning(opplysningData),
            DagpengerOpplysning.OppfyllerMedlemskap(opplysningData),
        ).also {
            DagpengerOpplysning.AntallStønadsuker.fra(it)
        }
        DagpengerOpplysning.GrunnlagetForVernepliktErHoyereEnnDagpengeGrunnlaget.fra(opplysningData) shouldNotBe null
        DagpengerOpplysning.ErInnvilgetMedVerneplikt.fra(opplysningData) shouldNotBe null
        DagpengerOpplysning.AntallPermitteringsuker.fra(opplysningData) shouldBe null
        DagpengerOpplysning.AntallPermitteringsukerFisk.fra(opplysningData) shouldBe null
        DagpengerOpplysning.OppfyllerKravetTilPermittering.fra(opplysningData) shouldBe null
        DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri.fra(opplysningData) shouldBe null
    }

    @Test
    fun `Opplysninger ved permittering`() {
        val opplysningData = OpplysningData("/json/innvigelse_permittering_resultat.json".readFile())
        DagpengerOpplysning.OppfyllerKravetTilPermittering.fra(opplysningData).let {
            require(it != null)
            it.verdi shouldBe true
        }

        DagpengerOpplysning.AntallPermitteringsuker.fra(opplysningData).let {
            require(it != null)
            it.verdi shouldBe 26
        }
    }

    @Test
    @Disabled("Trenger ny testdata med fisk")
    fun `Opplysninger ved permittering fisk`() {
        val opplysningData = OpplysningData("/json/innvigelse_permittering_resultat.json".readFile())
        DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri.fra(opplysningData).let {
            require(it != null)
            it.verdi shouldBe true
        }

        DagpengerOpplysning.AntallPermitteringsukerFisk.fra(opplysningData).let {
            require(it != null)
            it.verdi shouldBe 26
        }
    }

    @Test
    fun `AntallStønadsuker blir satt basert på andre opplysninger`() {
        DagpengerOpplysning.AntallStønadsuker.fra(
            setOf<DagpengerOpplysning<*, *>>(
                DagpengerOpplysning.GrunnlagetForVernepliktErHoyereEnnDagpengeGrunnlaget(true),
                DagpengerOpplysning.PeriodeSomGisVedVerneplikt(26),
                DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger(104),
            ),
        ) shouldBe DagpengerOpplysning.AntallStønadsuker(26)

        DagpengerOpplysning.AntallStønadsuker.fra(
            setOf<DagpengerOpplysning<*, *>>(
                DagpengerOpplysning.PeriodeSomGisVedVerneplikt(26),
                DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger(104),
            ),
        ) shouldBe DagpengerOpplysning.AntallStønadsuker(104)
    }

    @Test
    fun `Skal kunne lage alle type opplysninger med verdi`() {
        val opplysningData = OpplysningData("/json/avslag_resultat.json".readFile())

        DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid(opplysningData).verdi shouldBe 50.0
        DagpengerOpplysning.InntektskravSiste12Måneder(opplysningData).verdi shouldBe 186042
        DagpengerOpplysning.InntektskravSiste36Måneder(opplysningData).verdi shouldBe 372084
        DagpengerOpplysning.ArbeidsinntektSiste12Måneder(opplysningData).verdi shouldBe 0
        DagpengerOpplysning.ArbeidsinntektSiste36Måneder(opplysningData).verdi shouldBe 55550
        DagpengerOpplysning.AntallGSomGisSomGrunnlagVedVerneplikt(opplysningData).verdi shouldBe 3
        DagpengerOpplysning.BruktBeregningsregelGrunnlag(opplysningData).verdi shouldBe "Gjennomsnittlig arbeidsinntekt siste 36 måneder"
        DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder(opplysningData).verdi shouldBe true
        DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste12Måneder(opplysningData).verdi shouldBe false
        DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste36Måneder(opplysningData).verdi shouldBe false
        DagpengerOpplysning.UtbetaltArbeidsinntektPeriode1(opplysningData).verdi shouldBe 0
        DagpengerOpplysning.UtbetaltArbeidsinntektPeriode2(opplysningData).verdi shouldBe 5555
        DagpengerOpplysning.UtbetaltArbeidsinntektPeriode3(opplysningData).verdi shouldBe 49995
        DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger(opplysningData).verdi shouldBe 0
        DagpengerOpplysning.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget(opplysningData)
            .verdi shouldBe 0
        DagpengerOpplysning.AntallBarnSomGirRettTilBarnetillegg(opplysningData).verdi shouldBe 1
        DagpengerOpplysning.BarnetilleggIKroner(opplysningData).verdi shouldBe 37
        DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode(opplysningData).also {
            it.verdi shouldBe LocalDate.of(2022, 1, 1)
            it.deriverteOpplysninger shouldBe
                setOf(
                    DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode.FørsteMånedOgÅrForInntektsperiode1(
                        YearMonth.of(
                            2024,
                            1,
                        ),
                    ),
                    DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode.FørsteMånedOgÅrForInntektsperiode2(
                        YearMonth.of(
                            2023,
                            1,
                        ),
                    ),
                    DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode.FørsteMånedOgÅrForInntektsperiode3(
                        YearMonth.of(
                            2022,
                            1,
                        ),
                    ),
                )
        }
        DagpengerOpplysning.SisteMånedAvOpptjeningsperiode(opplysningData).also {
            it.verdi shouldBe LocalDate.of(2024, 12, 31)
            it.deriverteOpplysninger shouldBe
                setOf(
                    DagpengerOpplysning.SisteMånedAvOpptjeningsperiode.SisteMånedOgÅrForInntektsperiode1(
                        YearMonth.of(
                            2024,
                            12,
                        ),
                    ),
                    DagpengerOpplysning.SisteMånedAvOpptjeningsperiode.SisteMånedOgÅrForInntektsperiode2(
                        YearMonth.of(
                            2023,
                            12,
                        ),
                    ),
                    DagpengerOpplysning.SisteMånedAvOpptjeningsperiode.SisteMånedOgÅrForInntektsperiode3(
                        YearMonth.of(
                            2022,
                            12,
                        ),
                    ),
                )
        }
        DagpengerOpplysning.GrunnlagetForVernepliktErHoyereEnnDagpengeGrunnlaget.fra(opplysningData)?.verdi shouldBe true
        DagpengerOpplysning.SeksGangerGrunnbeløp(opplysningData).verdi shouldBe 744168
        DagpengerOpplysning.Aldersgrense(opplysningData).verdi shouldBe 67
        DagpengerOpplysning.Grunnlag(opplysningData).verdi shouldBe 372084
        DagpengerOpplysning.DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel(opplysningData).verdi shouldBe 930
        DagpengerOpplysning.Prøvingsdato(opplysningData).verdi shouldBe LocalDate.of(2025, 1, 29)
        DagpengerOpplysning.FastsattVanligArbeidstidPerUke(opplysningData).verdi shouldBe 37.5
        DagpengerOpplysning.FastsattNyArbeidstidPerUke(opplysningData).verdi shouldBe 0
        DagpengerOpplysning.HarSamordnet(opplysningData).verdi shouldBe false
        DagpengerOpplysning.SykepengerDagsats(opplysningData).verdi shouldBe 0
        DagpengerOpplysning.PleiepengerDagsats(opplysningData).verdi shouldBe 0
        DagpengerOpplysning.OmsorgspengerDagsats(opplysningData).verdi shouldBe 0
        DagpengerOpplysning.OpplæringspengerDagsats(opplysningData).verdi shouldBe 0
        DagpengerOpplysning.UføreDagsats(opplysningData).verdi shouldBe 0
        DagpengerOpplysning.ForeldrepengerDagsats(opplysningData).verdi shouldBe 0
        DagpengerOpplysning.SvangerskapspengerDagsats(opplysningData).verdi shouldBe 0
        DagpengerOpplysning.ErInnvilgetMedVerneplikt.fra(opplysningData).let {
            require(it != null)
            it.verdi shouldBe true
        }
        DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(opplysningData).verdi shouldBe false
        DagpengerOpplysning.PeriodeSomGisVedVerneplikt(opplysningData).verdi shouldBe 26
        DagpengerOpplysning.Egenandel(opplysningData).verdi shouldBe 2790
        DagpengerOpplysning.KravTilArbeidssøker(opplysningData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravTilMobilitet(opplysningData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravTilArbeidsfør(opplysningData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravTilArbeidssøker(opplysningData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravetTilEthvertArbeid(opplysningData).verdi shouldBe true
        DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(opplysningData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravetTilIkkeUtestengt(opplysningData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravetTilOpphold(opplysningData).verdi shouldBe true
        DagpengerOpplysning.IkkeFulleYtelser(opplysningData).verdi shouldBe true
        DagpengerOpplysning.KravTilTapAvArbeidsinntektOgArbeidstid(opplysningData).verdi shouldBe true
        DagpengerOpplysning.KravTilTaptArbeidstid(opplysningData).verdi shouldBe true
        DagpengerOpplysning.KravTilTapAvArbeidsinntekt(opplysningData).verdi shouldBe true
        DagpengerOpplysning.IkkeStreikEllerLockout(opplysningData).verdi shouldBe true
        DagpengerOpplysning.KravTilAlder(opplysningData).verdi shouldBe true
        DagpengerOpplysning.KravTilUtdanning(opplysningData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerMedlemskap(opplysningData).verdi shouldBe true
    }
}
