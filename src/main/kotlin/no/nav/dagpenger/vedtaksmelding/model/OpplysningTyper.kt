package no.nav.dagpenger.vedtaksmelding.model

import java.util.UUID

enum class OpplysningTyper(val opplysningTypeId: UUID?, val opplysningTekstId: String) {
    KravTilProsentvisTapAvArbeidstid(
        opplysningTypeId = UUID.fromString("0194881f-9435-72a8-b1ce-9575cbc2a762"),
        opplysningTekstId = "opplysning.krav-til-prosentvis-tap-av-arbeidstid",
    ),
    InntektskravSiste12Måneder(
        opplysningTypeId = UUID.fromString("0194881f-9413-77ce-92ec-d29700f04248"),
        opplysningTekstId = "opplysning.inntektskrav-for-siste-12-mnd",
    ),
    InntektskravSiste36Måneder(
        opplysningTypeId = UUID.fromString("0194881f-9413-77ce-92ec-d29700f04249"),
        opplysningTekstId = "opplysning.inntektskrav-for-siste-36-mnd",
    ),
    ArbeidsinntektSiste12Måneder(
        opplysningTypeId = UUID.fromString("0194881f-9413-77ce-92ec-d29700f04241"),
        opplysningTekstId = "opplysning.arbeidsinntekt-siste-12-mnd",
    ),
    ArbeidsinntektSiste36Måneder(
        opplysningTypeId = UUID.fromString("0194881f-9413-77ce-92ec-d29700f04242"),
        opplysningTekstId = "opplysning.arbeidsinntekt-siste-36-mnd",
    ),
    AntallGSomGisSomGrunnlagVedVerneplikt(
        opplysningTypeId = UUID.fromString("0194881f-9421-766c-9dc6-41fe6c9a1dff"),
        opplysningTekstId = "opplysning.antall-g-som-gis-som-grunnlag-ved-verneplikt",
    ),
    BruktBeregningsregelGrunnlag(
        opplysningTypeId = UUID.fromString("0194881f-9410-7481-b263-4606fdd10cba"),
        opplysningTekstId = "opplysning.brukt-beregningsregel-grunnlag",
    ),
    UtbetaltArbeidsinntektPeriode1(
        opplysningTypeId = UUID.fromString("0194881f-9410-7481-b263-4606fdd10cad"),
        opplysningTekstId = "opplysning.utbetalt-arbeidsinntekt-periode-1",
    ),
    UtbetaltArbeidsinntektPeriode2(
        opplysningTypeId = UUID.fromString("0194881f-9410-7481-b263-4606fdd10cae"),
        opplysningTekstId = "opplysning.utbetalt-arbeidsinntekt-periode-2",
    ),
    UtbetaltArbeidsinntektPeriode3(
        opplysningTypeId = UUID.fromString("0194881f-9410-7481-b263-4606fdd10caf"),
        opplysningTekstId = "opplysning.utbetalt-arbeidsinntekt-periode-3",
    ),
    AntallStønadsukerSomGisVedOrdinæreDagpenger(
        opplysningTypeId = UUID.fromString("0194881f-943d-77a7-969c-147999f15459"),
        opplysningTekstId = "opplysning.antall-stonadsuker-som-gis-ved-ordinare-dagpenger",
    ),
    AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget(
        opplysningTypeId = UUID.fromString("0194881f-9428-74d5-b160-f63a4c61a242"),
        opplysningTekstId = "opplysning.andel-av-dagsats-med-barnetillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget",
    ),
    AntallBarnSomGirRettTilBarnetillegg(
        opplysningTypeId = UUID.fromString("0194881f-9428-74d5-b160-f63a4c61a23c"),
        opplysningTekstId = "opplysning.antall-barn-som-gir-rett-til-barnetillegg",
    ),
    BarnetilleggIKroner(
        opplysningTypeId = UUID.fromString("0194881f-9428-74d5-b160-f63a4c61a244"),
        opplysningTekstId = "opplysning.barnetillegg-i-kroner",
    ),
    FørsteMånedAvOpptjeningsperiode(
        opplysningTypeId = UUID.fromString("0194881f-9413-77ce-92ec-d29700f04247"),
        opplysningTekstId = "opplysning.forste-maaned-av-opptjeningsperiode",
    ),
    SisteMånedAvOpptjeningsperiode(
        opplysningTypeId = UUID.fromString("0194881f-9414-7823-8d29-0e25b7feb7d0"),
        opplysningTekstId = "opplysning.siste-avsluttende-kalendermaaned",
    ),
    SeksGangerGrunnbeløp(
        opplysningTypeId = UUID.fromString("0194881f-9410-7481-b263-4606fdd10ca8"),
        opplysningTekstId = "opplysning.6-ganger-grunnbelop",
    ),
    Aldersgrense(
        opplysningTypeId = UUID.fromString("0194881f-940b-76ff-acf5-ba7bcb367234"),
        opplysningTekstId = "opplysning.aldersgrense",
    ),
    Grunnlag(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.grunnlag",
    ),
    DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.dagsats-med-barnetillegg-etter-samordning-og-90-prosent-regel",
    ),
    Prøvingsdato(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.provingsdato",
    ),
    FastsattVanligArbeidstidPerUke(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.fastsatt-arbeidstid-per-uke-for-tap",
    ),
    FastsattNyArbeidstidPerUke(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.fastsatt-ny-arbeidstid-per-uke",
    ),
    ProsentvisTaptArbeidstid(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.prosentvis-tapt-arbeidstid",
    ),
    HarSamordnet(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.har-samordnet",
    ),
    SykepengerDagsats(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.sykepenger-dagsats",
    ),
    PleiepengerDagsats(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.pleiepenger-dagsats",
    ),
    OmsorgspengerDagsats(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.omsorgspenger-dagsats",
    ),
    OpplæringspengerDagsats(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.opplaringspenger-dagsats",
    ),
    UføreDagsats(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.ufore-dagsats",
    ),
    ForeldrepengerDagsats(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.foreldrepenger-dagsats",
    ),
    SvangerskapspengerDagsats(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.svangerskapspenger-dagsats",
    ),
    ErInnvilgetMedVerneplikt(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.er-innvilget-med-verneplikt",
    ),
    AntallStønadsuker(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.antall-stonadsuker",
    ),
    AntallPermitteringsuker(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.antall-permitteringsuker",
    ),
    Egenandel(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.egenandel",
    ),
    FørsteMånedOgÅrForInntektsperiode1(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.forste-maaned-aar-for-inntektsperiode-1",
    ),
    FørsteMånedOgÅrForInntektsperiode2(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.forste-maaned-aar-for-inntektsperiode-2",
    ),
    FørsteMånedOgÅrForInntektsperiode3(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.forste-maaned-aar-for-inntektsperiode-3",
    ),
    SisteMånedOgÅrForInntektsperiode1(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.siste-maaned-aar-for-inntektsperiode-1",
    ),
    SisteMånedOgÅrForInntektsperiode2(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.siste-maaned-aar-for-inntektsperiode-2",
    ),
    SisteMånedOgÅrForInntektsperiode3(
        opplysningTypeId = null,
        opplysningTekstId = "opplysning.siste-maaned-aar-for-inntektsperiode-3",
    ),
}
