package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode.FørsteMånedOgÅrForInntektsperiode1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode.FørsteMånedOgÅrForInntektsperiode2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode.FørsteMånedOgÅrForInntektsperiode3
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning.SisteMånedAvOpptjeningsperiode.SisteMånedOgÅrForInntektsperiode1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning.SisteMånedAvOpptjeningsperiode.SisteMånedOgÅrForInntektsperiode2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning.SisteMånedAvOpptjeningsperiode.SisteMånedOgÅrForInntektsperiode3
import no.nav.dagpenger.vedtaksmelding.util.readFile
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.UUID

class VedtakMapperTest {
    @Test
    fun `Skal kunne hente opplysninger for permittering`() {
        val vedtak = VedtakMapper("/json/innvigelse_permittering_resultat.json".readFile()).vedtak()
        vedtak.behandlingId shouldBe UUID.fromString("0198c683-0770-734f-8ae2-34dfa8a714e5")
        vedtak.utfall shouldBe Vedtak.Utfall.INNVILGET
        vedtak.also {
            it.hentOpplysning<DagpengerOpplysning.OppfyllerKravetTilPermittering>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.AntallPermitteringsuker>() shouldNotBe null
        }
    }

    @Test
    @Disabled("Må ha testdata")
    fun `Skal kunne hente opplysninger for permittering i fiskeindustri`() {
        val vedtak = VedtakMapper("/json/innvigelse_permittering_resultat.json".readFile()).vedtak()
        vedtak.behandlingId shouldBe UUID.fromString("0198c683-0770-734f-8ae2-34dfa8a714e5")
        vedtak.utfall shouldBe Vedtak.Utfall.INNVILGET
        vedtak.also {
            it.hentOpplysning<DagpengerOpplysning.AntallPermitteringsukerFisk>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri>() shouldNotBe null
        }
    }

    @Test
    fun `Skal kunne lage alle type ordinære opplysninger, behandlingId og utfall for innvigelse ordinær`() {
        val vedtak = VedtakMapper("/json/innvigelse_ord_resultat_til_og_med_dato.json".readFile()).vedtak()
        vedtak.behandlingId shouldBe UUID.fromString("0198eba3-b1c3-7d50-a7ee-f0f8cd1cbf6b")
        vedtak.utfall shouldBe Vedtak.Utfall.INNVILGET
        vedtak.also {
            it.hentOpplysning<DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.InntektskravSiste12Måneder>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.InntektskravSiste36Måneder>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.ArbeidsinntektSiste12Måneder>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.ArbeidsinntektSiste36Måneder>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.AntallGSomGisSomGrunnlagVedVerneplikt>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.BruktBeregningsregelGrunnlag>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste12Måneder>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste36Måneder>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.UtbetaltArbeidsinntektPeriode1>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.UtbetaltArbeidsinntektPeriode2>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.UtbetaltArbeidsinntektPeriode3>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger>() shouldNotBe null
            it.hentOpplysning<AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.AntallBarnSomGirRettTilBarnetillegg>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.BarnetilleggIKroner>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.SisteMånedAvOpptjeningsperiode>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.SeksGangerGrunnbeløp>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.Aldersgrense>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.Grunnlag>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.Virkningsdato>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.FastsattVanligArbeidstidPerUke>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.FastsattNyArbeidstidPerUke>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.HarSamordnet>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.SykepengerDagsats>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.PleiepengerDagsats>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.OmsorgspengerDagsats>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.OpplæringspengerDagsats>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.UføreDagsats>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.ForeldrepengerDagsats>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.SvangerskapspengerDagsats>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.OppfyllerKravTilMinsteinntekt>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.PeriodeSomGisVedVerneplikt>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.Egenandel>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.KravTilArbeidssøker>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.OppfyllerKravTilMobilitet>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.OppfyllerKravTilArbeidsfør>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.OppfyllerKravTilArbeidssøker>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.OppfyllerKravetTilEthvertArbeid>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.OppfyllerKravetTilIkkeUtestengt>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.OppfyllerKravetTilOpphold>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.IkkeFulleYtelser>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.KravTilTapAvArbeidsinntektOgArbeidstid>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.KravTilTaptArbeidstid>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.KravTilTapAvArbeidsinntekt>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.IkkeStreikEllerLockout>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.KravTilAlder>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.KravTilUtdanning>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.OppfyllerMedlemskap>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.AntallStønadsuker>() shouldNotBe null

            // Deriverte opplysninger
            it.hentOpplysning<FørsteMånedOgÅrForInntektsperiode1>() shouldNotBe null
            it.hentOpplysning<FørsteMånedOgÅrForInntektsperiode2>() shouldNotBe null
            it.hentOpplysning<FørsteMånedOgÅrForInntektsperiode3>() shouldNotBe null

            it.hentOpplysning<SisteMånedOgÅrForInntektsperiode1>() shouldNotBe null
            it.hentOpplysning<SisteMånedOgÅrForInntektsperiode2>() shouldNotBe null
            it.hentOpplysning<SisteMånedOgÅrForInntektsperiode3>() shouldNotBe null

            // nullable opplysninger
            it.hentOpplysning<DagpengerOpplysning.GrunnlagetForVernepliktErHoyereEnnDagpengeGrunnlaget>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.ErInnvilgetMedVerneplikt>() shouldNotBe null
            it.hentOpplysning<DagpengerOpplysning.SisteDagMedRett>() shouldNotBe null
        }
    }
}
