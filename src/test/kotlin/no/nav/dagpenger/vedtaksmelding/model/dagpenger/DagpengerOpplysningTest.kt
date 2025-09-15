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
        val behandlingResultatData = BehandlingResultatData("/json/innvigelse_ord_resultat.json".readFile())
        setOf(
            DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid(behandlingResultatData),
            DagpengerOpplysning.InntektskravSiste12Måneder(behandlingResultatData),
            DagpengerOpplysning.InntektskravSiste36Måneder(behandlingResultatData),
            DagpengerOpplysning.ArbeidsinntektSiste12Måneder(behandlingResultatData),
            DagpengerOpplysning.ArbeidsinntektSiste36Måneder(behandlingResultatData),
            DagpengerOpplysning.AntallGSomGisSomGrunnlagVedVerneplikt(behandlingResultatData),
            DagpengerOpplysning.BruktBeregningsregelGrunnlag(behandlingResultatData),
            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder(behandlingResultatData),
            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste12Måneder(behandlingResultatData),
            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste36Måneder(behandlingResultatData),
            DagpengerOpplysning.UtbetaltArbeidsinntektPeriode1(behandlingResultatData),
            DagpengerOpplysning.UtbetaltArbeidsinntektPeriode2(behandlingResultatData),
            DagpengerOpplysning.UtbetaltArbeidsinntektPeriode3(behandlingResultatData),
            DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger(behandlingResultatData),
            DagpengerOpplysning.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget(
                behandlingResultatData,
            ),
            DagpengerOpplysning.BarnetilleggIKroner(behandlingResultatData),
            DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode(behandlingResultatData),
            DagpengerOpplysning.SisteMånedAvOpptjeningsperiode(behandlingResultatData),
            DagpengerOpplysning.SeksGangerGrunnbeløp(behandlingResultatData),
            DagpengerOpplysning.Aldersgrense(behandlingResultatData),
            DagpengerOpplysning.Grunnlag(behandlingResultatData),
            DagpengerOpplysning.DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel(behandlingResultatData),
            DagpengerOpplysning.Prøvingsdato(behandlingResultatData),
            DagpengerOpplysning.FastsattVanligArbeidstidPerUke(behandlingResultatData),
            DagpengerOpplysning.FastsattNyArbeidstidPerUke(behandlingResultatData),
            DagpengerOpplysning.HarSamordnet(behandlingResultatData),
            DagpengerOpplysning.SykepengerDagsats(behandlingResultatData),
            DagpengerOpplysning.PleiepengerDagsats(behandlingResultatData),
            DagpengerOpplysning.OmsorgspengerDagsats(behandlingResultatData),
            DagpengerOpplysning.OpplæringspengerDagsats(behandlingResultatData),
            DagpengerOpplysning.UføreDagsats(behandlingResultatData),
            DagpengerOpplysning.ForeldrepengerDagsats(behandlingResultatData),
            DagpengerOpplysning.SvangerskapspengerDagsats(behandlingResultatData),
            DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(behandlingResultatData),
            DagpengerOpplysning.PeriodeSomGisVedVerneplikt(behandlingResultatData),
            DagpengerOpplysning.Egenandel(behandlingResultatData),
            DagpengerOpplysning.KravTilArbeidssøker(behandlingResultatData),
            DagpengerOpplysning.OppfyllerKravTilMobilitet(behandlingResultatData),
            DagpengerOpplysning.OppfyllerKravTilArbeidsfør(behandlingResultatData),
            DagpengerOpplysning.OppfyllerKravTilArbeidssøker(behandlingResultatData),
            DagpengerOpplysning.OppfyllerKravetTilEthvertArbeid(behandlingResultatData),
            DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(behandlingResultatData),
            DagpengerOpplysning.OppfyllerKravetTilIkkeUtestengt(behandlingResultatData),
            DagpengerOpplysning.OppfyllerKravetTilOpphold(behandlingResultatData),
            DagpengerOpplysning.IkkeFulleYtelser(behandlingResultatData),
            DagpengerOpplysning.KravTilTapAvArbeidsinntektOgArbeidstid(behandlingResultatData),
            DagpengerOpplysning.KravTilTaptArbeidstid(behandlingResultatData),
            DagpengerOpplysning.KravTilTapAvArbeidsinntekt(behandlingResultatData),
            DagpengerOpplysning.IkkeStreikEllerLockout(behandlingResultatData),
            DagpengerOpplysning.KravTilAlder(behandlingResultatData),
            DagpengerOpplysning.KravTilUtdanning(behandlingResultatData),
            DagpengerOpplysning.OppfyllerMedlemskap(behandlingResultatData),
            DagpengerOpplysning.GrunnlagetForVernepliktErHoyereEnnDagpengeGrunnlaget(behandlingResultatData),
            DagpengerOpplysning.ErInnvilgetMedVerneplikt(behandlingResultatData),
        ).also {
            DagpengerOpplysning.AntallStønadsuker.fra(it)
        }

        shouldThrow<BehandlingResultatData.BehandlingResultatOpplysningIkkeFunnet> {
            DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri(behandlingResultatData)
        }

        shouldThrow<BehandlingResultatData.BehandlingResultatOpplysningIkkeFunnet> {
            DagpengerOpplysning.AntallPermitteringsuker(behandlingResultatData)
        }

        shouldThrow<BehandlingResultatData.BehandlingResultatOpplysningIkkeFunnet> {
            DagpengerOpplysning.AntallPermitteringsukerFisk(behandlingResultatData)
        }

        shouldThrow<BehandlingResultatData.BehandlingResultatOpplysningIkkeFunnet> {
            DagpengerOpplysning.OppfyllerKravetTilPermittering(behandlingResultatData)
        }
    }

    @Test
    fun `Opplysninger ved permittering`() {
        val behandlingResultatData = BehandlingResultatData("/json/innvigelse_permittering_resultat.json".readFile())
        DagpengerOpplysning.OppfyllerKravetTilPermittering(behandlingResultatData).verdi shouldBe true

        DagpengerOpplysning.AntallPermitteringsuker(behandlingResultatData).verdi shouldBe 26
    }

    @Test
    @Disabled("Trenger ny testdata med fisk")
    fun `Opplysninger ved permittering fisk`() {
        val behandlingResultatData = BehandlingResultatData("/json/innvigelse_permittering_resultat.json".readFile())
        DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri(behandlingResultatData) shouldBe true
        DagpengerOpplysning.AntallPermitteringsukerFisk(behandlingResultatData).verdi shouldBe 26
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
        val behandlingResultatData = BehandlingResultatData("/json/avslag_resultat.json".readFile())

        DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid(behandlingResultatData).verdi shouldBe 50.0
        DagpengerOpplysning.InntektskravSiste12Måneder(behandlingResultatData).verdi shouldBe 186042
        DagpengerOpplysning.InntektskravSiste36Måneder(behandlingResultatData).verdi shouldBe 372084
        DagpengerOpplysning.ArbeidsinntektSiste12Måneder(behandlingResultatData).verdi shouldBe 0
        DagpengerOpplysning.ArbeidsinntektSiste36Måneder(behandlingResultatData).verdi shouldBe 55550
        DagpengerOpplysning.AntallGSomGisSomGrunnlagVedVerneplikt(behandlingResultatData).verdi shouldBe 3
        DagpengerOpplysning.BruktBeregningsregelGrunnlag(behandlingResultatData).verdi shouldBe "Gjennomsnittlig arbeidsinntekt siste 36 måneder"
        DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste12Måneder(behandlingResultatData).verdi shouldBe false
        DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste36Måneder(behandlingResultatData).verdi shouldBe false
        DagpengerOpplysning.UtbetaltArbeidsinntektPeriode1(behandlingResultatData).verdi shouldBe 0
        DagpengerOpplysning.UtbetaltArbeidsinntektPeriode2(behandlingResultatData).verdi shouldBe 5555
        DagpengerOpplysning.UtbetaltArbeidsinntektPeriode3(behandlingResultatData).verdi shouldBe 49995
        DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger(behandlingResultatData).verdi shouldBe 0
        DagpengerOpplysning.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget(
            behandlingResultatData,
        )
            .verdi shouldBe 0
        DagpengerOpplysning.AntallBarnSomGirRettTilBarnetillegg(behandlingResultatData).verdi shouldBe 1
        DagpengerOpplysning.BarnetilleggIKroner(behandlingResultatData).verdi shouldBe 37
        DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode(behandlingResultatData).also {
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
        DagpengerOpplysning.SisteMånedAvOpptjeningsperiode(behandlingResultatData).also {
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
        DagpengerOpplysning.GrunnlagetForVernepliktErHoyereEnnDagpengeGrunnlaget(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.SeksGangerGrunnbeløp(behandlingResultatData).verdi shouldBe 744168
        DagpengerOpplysning.Aldersgrense(behandlingResultatData).verdi shouldBe 67
        DagpengerOpplysning.Grunnlag(behandlingResultatData).verdi shouldBe 372084
        DagpengerOpplysning.DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel(behandlingResultatData).verdi shouldBe 930
        DagpengerOpplysning.Prøvingsdato(behandlingResultatData).verdi shouldBe LocalDate.of(2025, 1, 29)
        DagpengerOpplysning.FastsattVanligArbeidstidPerUke(behandlingResultatData).verdi shouldBe 37.5
        DagpengerOpplysning.FastsattNyArbeidstidPerUke(behandlingResultatData).verdi shouldBe 0
        DagpengerOpplysning.HarSamordnet(behandlingResultatData).verdi shouldBe false
        DagpengerOpplysning.SykepengerDagsats(behandlingResultatData).verdi shouldBe 0
        DagpengerOpplysning.PleiepengerDagsats(behandlingResultatData).verdi shouldBe 0
        DagpengerOpplysning.OmsorgspengerDagsats(behandlingResultatData).verdi shouldBe 0
        DagpengerOpplysning.OpplæringspengerDagsats(behandlingResultatData).verdi shouldBe 0
        DagpengerOpplysning.UføreDagsats(behandlingResultatData).verdi shouldBe 0
        DagpengerOpplysning.ForeldrepengerDagsats(behandlingResultatData).verdi shouldBe 0
        DagpengerOpplysning.SvangerskapspengerDagsats(behandlingResultatData).verdi shouldBe 0
        DagpengerOpplysning.ErInnvilgetMedVerneplikt(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(behandlingResultatData).verdi shouldBe false
        DagpengerOpplysning.PeriodeSomGisVedVerneplikt(behandlingResultatData).verdi shouldBe 26
        DagpengerOpplysning.Egenandel(behandlingResultatData).verdi shouldBe 2790
        DagpengerOpplysning.KravTilArbeidssøker(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravTilMobilitet(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravTilArbeidsfør(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravTilArbeidssøker(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravetTilEthvertArbeid(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravetTilIkkeUtestengt(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerKravetTilOpphold(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.IkkeFulleYtelser(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.KravTilTapAvArbeidsinntektOgArbeidstid(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.KravTilTaptArbeidstid(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.KravTilTapAvArbeidsinntekt(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.IkkeStreikEllerLockout(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.KravTilAlder(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.KravTilUtdanning(behandlingResultatData).verdi shouldBe true
        DagpengerOpplysning.OppfyllerMedlemskap(behandlingResultatData).verdi shouldBe true
    }
}
