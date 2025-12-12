package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.util.readFile
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.YearMonth

@Suppress("ktlint:standard:max-line-length")
class DagpengerOpplysningTest {
    @Test
    fun `Skal kunne lage alle type opplysninger ordninær`() {
        val behandlingsresultatData = BehandlingsresultatData("/json/innvigelse_ord_resultat.json".readFile())
        setOf(
            DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid(behandlingsresultatData),
            DagpengerOpplysning.InntektskravSiste12Måneder(behandlingsresultatData),
            DagpengerOpplysning.InntektskravSiste36Måneder(behandlingsresultatData),
            DagpengerOpplysning.ArbeidsinntektSiste12Måneder(behandlingsresultatData),
            DagpengerOpplysning.ArbeidsinntektSiste36Måneder(behandlingsresultatData),
            DagpengerOpplysning.AntallGSomGisSomGrunnlagVedVerneplikt(behandlingsresultatData),
            DagpengerOpplysning.BruktBeregningsregelGrunnlag(behandlingsresultatData),
            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder(behandlingsresultatData),
            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste12Måneder(behandlingsresultatData),
            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste36Måneder(behandlingsresultatData),
            DagpengerOpplysning.UtbetaltArbeidsinntektPeriode1(behandlingsresultatData),
            DagpengerOpplysning.UtbetaltArbeidsinntektPeriode2(behandlingsresultatData),
            DagpengerOpplysning.UtbetaltArbeidsinntektPeriode3(behandlingsresultatData),
            DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger(behandlingsresultatData),
            DagpengerOpplysning.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget(
                behandlingsresultatData,
            ),
            DagpengerOpplysning.BarnetilleggIKroner(behandlingsresultatData),
            DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode(behandlingsresultatData),
            DagpengerOpplysning.SisteMånedAvOpptjeningsperiode(behandlingsresultatData),
            DagpengerOpplysning.SeksGangerGrunnbeløp(behandlingsresultatData),
            DagpengerOpplysning.Aldersgrense(behandlingsresultatData),
            DagpengerOpplysning.Grunnlag(behandlingsresultatData),
            DagpengerOpplysning.DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel(behandlingsresultatData),
            DagpengerOpplysning.Virkningsdato(behandlingsresultatData),
            DagpengerOpplysning.FastsattVanligArbeidstidPerUke(behandlingsresultatData),
            DagpengerOpplysning.FastsattNyArbeidstidPerUke(behandlingsresultatData),
            DagpengerOpplysning.HarSamordnet(behandlingsresultatData),
            DagpengerOpplysning.SykepengerDagsats(behandlingsresultatData),
            DagpengerOpplysning.PleiepengerDagsats(behandlingsresultatData),
            DagpengerOpplysning.OmsorgspengerDagsats(behandlingsresultatData),
            DagpengerOpplysning.OpplæringspengerDagsats(behandlingsresultatData),
            DagpengerOpplysning.UføreDagsats(behandlingsresultatData),
            DagpengerOpplysning.ForeldrepengerDagsats(behandlingsresultatData),
            DagpengerOpplysning.SvangerskapspengerDagsats(behandlingsresultatData),
            DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(behandlingsresultatData),
            DagpengerOpplysning.PeriodeSomGisVedVerneplikt(behandlingsresultatData),
            DagpengerOpplysning.Egenandel(behandlingsresultatData),
            DagpengerOpplysning.KravTilArbeidssøker(behandlingsresultatData),
            DagpengerOpplysning.OppfyllerKravTilMobilitet(behandlingsresultatData),
            DagpengerOpplysning.OppfyllerKravTilArbeidsfør(behandlingsresultatData),
            DagpengerOpplysning.OppfyllerKravTilArbeidssøker(behandlingsresultatData),
            DagpengerOpplysning.OppfyllerKravetTilEthvertArbeid(behandlingsresultatData),
            DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(behandlingsresultatData),
            DagpengerOpplysning.OppfyllerKravetTilIkkeUtestengt(behandlingsresultatData),
            DagpengerOpplysning.OppfyllerKravetTilOpphold(behandlingsresultatData),
            DagpengerOpplysning.IkkeFulleYtelser(behandlingsresultatData),
            DagpengerOpplysning.KravTilTapAvArbeidsinntektOgArbeidstid(behandlingsresultatData),
            DagpengerOpplysning.KravTilTaptArbeidstid(behandlingsresultatData),
            DagpengerOpplysning.KravTilTapAvArbeidsinntekt(behandlingsresultatData),
            DagpengerOpplysning.IkkeStreikEllerLockout(behandlingsresultatData),
            DagpengerOpplysning.KravTilAlder(behandlingsresultatData),
            DagpengerOpplysning.KravTilUtdanning(behandlingsresultatData),
            DagpengerOpplysning.OppfyllerMedlemskap(behandlingsresultatData),
            DagpengerOpplysning.GrunnlagetForVernepliktErHoyereEnnDagpengeGrunnlaget(behandlingsresultatData),
            DagpengerOpplysning.ErInnvilgetMedVerneplikt(behandlingsresultatData),
        ).also {
            DagpengerOpplysning.AntallStønadsuker.fra(it)
        }

        shouldThrow<BehandlingsresultatData.BehandlingResultatOpplysningIkkeFunnet> {
            DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri(behandlingsresultatData)
        }

        shouldThrow<BehandlingsresultatData.BehandlingResultatOpplysningIkkeFunnet> {
            DagpengerOpplysning.AntallPermitteringsuker(behandlingsresultatData)
        }

        shouldThrow<BehandlingsresultatData.BehandlingResultatOpplysningIkkeFunnet> {
            DagpengerOpplysning.AntallPermitteringsukerFisk(behandlingsresultatData)
        }

        shouldThrow<BehandlingsresultatData.BehandlingResultatOpplysningIkkeFunnet> {
            DagpengerOpplysning.OppfyllerKravetTilPermittering(behandlingsresultatData)
        }
    }

    @Test
    fun `Opplysninger ved permittering`() {
        val behandlingsresultatData = BehandlingsresultatData("/json/innvigelse_permittering_resultat.json".readFile())
        DagpengerOpplysning.OppfyllerKravetTilPermittering(behandlingsresultatData).verdi shouldBe true

        DagpengerOpplysning.AntallPermitteringsuker(behandlingsresultatData).verdi shouldBe 26
    }

    @Test
    @Disabled("Trenger ny testdata med fisk")
    fun `Opplysninger ved permittering fisk`() {
        val behandlingsresultatData = BehandlingsresultatData("/json/innvigelse_permittering_resultat.json".readFile())
        DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri(behandlingsresultatData) shouldBe true
        DagpengerOpplysning.AntallPermitteringsukerFisk(behandlingsresultatData).verdi shouldBe 26
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
    fun `AntallStønadsuker  kan være null`() {
        DagpengerOpplysning.AntallStønadsuker.fra(
            setOf<DagpengerOpplysning<*, *>>(
                DagpengerOpplysning.GrunnlagetForVernepliktErHoyereEnnDagpengeGrunnlaget(true),
                DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger(104),
            ),
        ) shouldBe null

        DagpengerOpplysning.AntallStønadsuker.fra(
            setOf<DagpengerOpplysning<*, *>>(
                DagpengerOpplysning.PeriodeSomGisVedVerneplikt(26),
            ),
        ) shouldBe null
    }

    @Test
    fun `Skal kunne lage alle type opplysninger med verdi`() {
        val behandlingsresultatData = BehandlingsresultatData("/json/avslag_resultat.json".readFile())

        DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid(behandlingsresultatData).verdi shouldBe 50.0
        DagpengerOpplysning.InntektskravSiste12Måneder(behandlingsresultatData).verdi shouldBe 186042
        DagpengerOpplysning.InntektskravSiste36Måneder(behandlingsresultatData).verdi shouldBe 372084
        DagpengerOpplysning.ArbeidsinntektSiste12Måneder(behandlingsresultatData).verdi shouldBe 0
        DagpengerOpplysning.ArbeidsinntektSiste36Måneder(behandlingsresultatData).verdi shouldBe 55550
        DagpengerOpplysning.AntallGSomGisSomGrunnlagVedVerneplikt(behandlingsresultatData).verdi shouldBe 3
        DagpengerOpplysning.BruktBeregningsregelGrunnlag(behandlingsresultatData).verdi shouldBe
            "Gjennomsnittlig arbeidsinntekt siste 36 måneder"
        DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste12Måneder(behandlingsresultatData).verdi shouldBe false
        DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste36Måneder(behandlingsresultatData).verdi shouldBe false
        DagpengerOpplysning.UtbetaltArbeidsinntektPeriode1(behandlingsresultatData).verdi shouldBe 0
        DagpengerOpplysning.UtbetaltArbeidsinntektPeriode2(behandlingsresultatData).verdi shouldBe 5555
        DagpengerOpplysning.UtbetaltArbeidsinntektPeriode3(behandlingsresultatData).verdi shouldBe 49995
        DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger(behandlingsresultatData).verdi shouldBe 0
        DagpengerOpplysning
            .AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget(
                behandlingsresultatData,
            ).verdi shouldBe 0
        DagpengerOpplysning.AntallBarnSomGirRettTilBarnetillegg(behandlingsresultatData).verdi shouldBe 1
        DagpengerOpplysning.BarnetilleggIKroner(behandlingsresultatData).verdi shouldBe 37
        DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode(behandlingsresultatData).also {
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
        DagpengerOpplysning.SisteMånedAvOpptjeningsperiode(behandlingsresultatData).also {
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
        DagpengerOpplysning.GrunnlagetForVernepliktErHoyereEnnDagpengeGrunnlaget(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.SeksGangerGrunnbeløp(behandlingsresultatData).verdi shouldBe 744168
        DagpengerOpplysning.Aldersgrense(behandlingsresultatData).verdi shouldBe 67
        DagpengerOpplysning.Grunnlag(behandlingsresultatData).verdi shouldBe 372084
        DagpengerOpplysning.DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel(behandlingsresultatData).verdi shouldBe 930
        DagpengerOpplysning.Virkningsdato(behandlingsresultatData).verdi shouldBe LocalDate.of(2025, 1, 29)
        DagpengerOpplysning.FastsattVanligArbeidstidPerUke(behandlingsresultatData).verdi shouldBe 37.5
        DagpengerOpplysning.FastsattNyArbeidstidPerUke(behandlingsresultatData).verdi shouldBe 0
        DagpengerOpplysning.HarSamordnet(behandlingsresultatData).verdi shouldBe false
        DagpengerOpplysning.SykepengerDagsats(behandlingsresultatData).verdi shouldBe 0
        DagpengerOpplysning.PleiepengerDagsats(behandlingsresultatData).verdi shouldBe 0
        DagpengerOpplysning.OmsorgspengerDagsats(behandlingsresultatData).verdi shouldBe 0
        DagpengerOpplysning.OpplæringspengerDagsats(behandlingsresultatData).verdi shouldBe 0
        DagpengerOpplysning.UføreDagsats(behandlingsresultatData).verdi shouldBe 0
        DagpengerOpplysning.ForeldrepengerDagsats(behandlingsresultatData).verdi shouldBe 0
        DagpengerOpplysning.SvangerskapspengerDagsats(behandlingsresultatData).verdi shouldBe 0
        DagpengerOpplysning.ErInnvilgetMedVerneplikt(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(behandlingsresultatData).verdi shouldBe false
        DagpengerOpplysning.PeriodeSomGisVedVerneplikt(behandlingsresultatData).verdi shouldBe 26
        DagpengerOpplysning.Egenandel(behandlingsresultatData).verdi shouldBe 2790
        DagpengerOpplysning.KravTilArbeidssøker(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravTilMobilitet(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravTilArbeidsfør(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravTilArbeidssøker(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravetTilEthvertArbeid(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravetTilIkkeUtestengt(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravetTilOpphold(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.IkkeFulleYtelser(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.KravTilTapAvArbeidsinntektOgArbeidstid(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.KravTilTaptArbeidstid(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.KravTilTapAvArbeidsinntekt(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.IkkeStreikEllerLockout(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.KravTilAlder(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.KravTilUtdanning(behandlingsresultatData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerMedlemskap(behandlingsresultatData).verdi shouldBe true
    }
}
