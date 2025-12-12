package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.Enhet
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import java.time.LocalDate
import java.time.YearMonth
import java.util.UUID

interface DeriverbarOpplysning {
    val deriverteOpplysninger: Set<DagpengerOpplysning<*, *>>
}

sealed class DagpengerOpplysning<E : Enhet, V : Any>(
    open val verdi: V,
) : Opplysning {
    companion object {
        val logger = KotlinLogging.logger {}
    }

    protected abstract val enhet: E

    override fun formatertVerdi(): String = enhet.formatertVerdi(verdi)

    class KravTilProsentvisTapAvArbeidstid(
        override val verdi: Double,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Double>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9435-72a8-b1ce-9575cbc2a762")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.krav-til-prosentvis-tap-av-arbeidstid"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.flyttall(
                opplysningTypeId,
            ),
        )
    }

    class InntektskravSiste12Måneder(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9413-77ce-92ec-d29700f04248")
        }

        override val opplysningTekstId: String = "opplysning.inntektskrav-for-siste-12-mnd"
        override val enhet: Enhet.KRONER = Enhet.KRONER

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class InntektskravSiste36Måneder(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9413-77ce-92ec-d29700f04249")
        }

        override val opplysningTekstId: String = "opplysning.inntektskrav-for-siste-36-mnd"
        override val enhet: Enhet.KRONER = Enhet.KRONER

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class ArbeidsinntektSiste12Måneder(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9413-77ce-92ec-d29700f04241")
        }

        override val enhet: Enhet.KRONER = Enhet.KRONER
        override val opplysningTekstId: String = "opplysning.arbeidsinntekt-siste-12-mnd"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class ArbeidsinntektSiste36Måneder private constructor(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9413-77ce-92ec-d29700f04242")
        }

        override val enhet: Enhet.KRONER = Enhet.KRONER
        override val opplysningTekstId: String = "opplysning.arbeidsinntekt-siste-36-mnd"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class AntallGSomGisSomGrunnlagVedVerneplikt(
        override val verdi: Double,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Double>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9421-766c-9dc6-41fe6c9a1dff")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.antall-g-som-gis-som-grunnlag-ved-verneplikt"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.flyttall(
                opplysningTypeId,
            ),
        )
    }

    class BruktBeregningsregelGrunnlag(
        override val verdi: String,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, String>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9410-7481-b263-4606fdd10cba")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.brukt-beregningsregel-grunnlag"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(behandlingsresultatData.tekst(opplysningTypeId))

        override fun formatertVerdi(): String = this.verdi.lowercase()
    }

    class HarBruktBeregningsregelArbeidstidSiste6Måneder(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9435-72a8-b1ce-9575cbc2a764")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.har-brukt-beregningsregel-arbeidstid-siste-6-mnd"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class HarBruktBeregningsregelArbeidstidSiste12Måneder(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9435-72a8-b1ce-9575cbc2a765")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.har-brukt-beregningsregel-arbeidstid-siste-12-mnd"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class HarBruktBeregningsregelArbeidstidSiste36Måneder(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9435-72a8-b1ce-9575cbc2a766")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.har-brukt-beregningsregel-arbeidstid-siste-36-mnd"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class UtbetaltArbeidsinntektPeriode1(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9410-7481-b263-4606fdd10cad")
        }

        override val enhet: Enhet.KRONER = Enhet.KRONER
        override val opplysningTekstId: String = "opplysning.utbetalt-arbeidsinntekt-periode-1"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class UtbetaltArbeidsinntektPeriode2(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9410-7481-b263-4606fdd10cae")
        }

        override val enhet: Enhet.KRONER = Enhet.KRONER
        override val opplysningTekstId: String = "opplysning.utbetalt-arbeidsinntekt-periode-2"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class UtbetaltArbeidsinntektPeriode3(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9410-7481-b263-4606fdd10caf")
        }

        override val enhet: Enhet.KRONER = Enhet.KRONER
        override val opplysningTekstId: String = "opplysning.utbetalt-arbeidsinntekt-periode-3"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class AntallStønadsukerSomGisVedOrdinæreDagpenger(
        override val verdi: Int,
    ) : DagpengerOpplysning<Enhet.UKER, Int>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-943d-77a7-969c-147999f15459")
        }

        override val enhet: Enhet.UKER = Enhet.UKER
        override val opplysningTekstId: String = "opplysning.antall-stonadsuker-som-gis-ved-ordinare-dagpenger"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.heltall(
                opplysningTypeId,
            ),
        )
    }

    class AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9428-74d5-b160-f63a4c61a242")
        }

        override val enhet: Enhet.KRONER = Enhet.KRONER
        override val opplysningTekstId: String =
            "opplysning.andel-av-dagsats-med-barnetillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class AntallBarnSomGirRettTilBarnetillegg(
        override val verdi: Int,
    ) : DagpengerOpplysning<Enhet.BARN, Int>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9428-74d5-b160-f63a4c61a23c")
        }

        override val enhet: Enhet.BARN = Enhet.BARN
        override val opplysningTekstId = "opplysning.antall-barn-som-gir-rett-til-barnetillegg"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.heltall(
                opplysningTypeId,
            ),
        )
    }

    class BarnetilleggIKroner(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9428-74d5-b160-f63a4c61a244")
        }

        override val opplysningTekstId = "opplysning.barnetillegg-i-kroner"
        override val enhet: Enhet.KRONER = Enhet.KRONER

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class FørsteMånedAvOpptjeningsperiode(
        override val verdi: LocalDate,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, LocalDate>(verdi),
        DeriverbarOpplysning {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9413-77ce-92ec-d29700f04247")
        }

        override val opplysningTekstId: String = "opplysning.første-måned-av-opptjeningsperiode"
        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(behandlingsresultatData.dato(opplysningTypeId))

        override val deriverteOpplysninger: Set<DagpengerOpplysning<Enhet.ENHETSLØS, YearMonth>> =
            setOf(
                FørsteMånedOgÅrForInntektsperiode1(YearMonth.from(verdi.plusYears(2))),
                FørsteMånedOgÅrForInntektsperiode2(YearMonth.from(verdi.plusYears(1))),
                FørsteMånedOgÅrForInntektsperiode3(YearMonth.from(verdi)),
            )

        class FørsteMånedOgÅrForInntektsperiode1(
            override val verdi: YearMonth,
        ) : DagpengerOpplysning<Enhet.ENHETSLØS, YearMonth>(verdi) {
            override val opplysningTekstId = "opplysning.forste-maaned-aar-for-inntektsperiode-1"
            override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        }

        class FørsteMånedOgÅrForInntektsperiode2(
            override val verdi: YearMonth,
        ) : DagpengerOpplysning<Enhet.ENHETSLØS, YearMonth>(verdi) {
            override val opplysningTekstId = "opplysning.forste-maaned-aar-for-inntektsperiode-2"
            override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        }

        class FørsteMånedOgÅrForInntektsperiode3(
            override val verdi: YearMonth,
        ) : DagpengerOpplysning<Enhet.ENHETSLØS, YearMonth>(verdi) {
            override val opplysningTekstId = "opplysning.forste-maaned-aar-for-inntektsperiode-3"
            override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        }
    }

    class SisteMånedAvOpptjeningsperiode(
        override val verdi: LocalDate,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, LocalDate>(verdi),
        DeriverbarOpplysning {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9414-7823-8d29-0e25b7feb7d0")
        }

        override val opplysningTekstId: String = "opplysning.siste-avsluttende-kalendermaaned"
        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(behandlingsresultatData.dato(opplysningTypeId))

        override val deriverteOpplysninger: Set<DagpengerOpplysning<Enhet.ENHETSLØS, YearMonth>> =
            setOf(
                SisteMånedOgÅrForInntektsperiode1(YearMonth.from(verdi)),
                SisteMånedOgÅrForInntektsperiode2(YearMonth.from(verdi.minusYears(1))),
                SisteMånedOgÅrForInntektsperiode3(YearMonth.from(verdi.minusYears(2))),
            )

        class SisteMånedOgÅrForInntektsperiode1(
            override val verdi: YearMonth,
        ) : DagpengerOpplysning<Enhet.ENHETSLØS, YearMonth>(verdi) {
            override val opplysningTekstId = "opplysning.siste-maaned-aar-for-inntektsperiode-1"
            override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        }

        class SisteMånedOgÅrForInntektsperiode2(
            override val verdi: YearMonth,
        ) : DagpengerOpplysning<Enhet.ENHETSLØS, YearMonth>(verdi) {
            override val opplysningTekstId = "opplysning.siste-maaned-aar-for-inntektsperiode-2"
            override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        }

        class SisteMånedOgÅrForInntektsperiode3(
            override val verdi: YearMonth,
        ) : DagpengerOpplysning<Enhet.ENHETSLØS, YearMonth>(verdi) {
            override val opplysningTekstId = "opplysning.siste-maaned-aar-for-inntektsperiode-3"
            override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        }
    }

    class SeksGangerGrunnbeløp(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9410-7481-b263-4606fdd10ca8")
        }

        override val opplysningTekstId = "opplysning.6-ganger-grunnbelop"
        override val enhet = Enhet.KRONER

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class Aldersgrense(
        override val verdi: Int,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Int>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-940b-76ff-acf5-ba7bcb367234")
        }

        override val opplysningTekstId: String = "opplysning.aldersgrense"
        override val enhet = Enhet.ENHETSLØS

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.heltall(
                opplysningTypeId,
            ),
        )
    }

    class Grunnlag(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9410-7481-b263-4606fdd10cbd")
        }

        override val opplysningTekstId = "opplysning.grunnlag"
        override val enhet = Enhet.KRONER

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9428-74d5-b160-f63a4c61a24f")
        }

        override val opplysningTekstId = "opplysning.dagsats-med-barnetillegg-etter-samordning-og-90-prosent-regel"
        override val enhet = Enhet.KRONER

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    // todo Her er opplysninger som mangler opplysningTypeId
    class Virkningsdato(
        override val verdi: LocalDate,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, LocalDate>(verdi) {
        override val opplysningTekstId = "opplysning.virkningsdato"
        override val enhet = Enhet.ENHETSLØS

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(behandlingsresultatData.virkningsdato())
    }

    class FastsattVanligArbeidstidPerUke(
        override val verdi: Double,
    ) : DagpengerOpplysning<Enhet.TIMER, Double>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9435-72a8-b1ce-9575cbc2a76a")
        }

        override val opplysningTekstId = "opplysning.fastsatt-arbeidstid-per-uke-for-tap"
        override val enhet = Enhet.TIMER

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.flyttall(
                opplysningTypeId,
            ),
        )
    }

    class FastsattNyArbeidstidPerUke(
        override val verdi: Double,
    ) : DagpengerOpplysning<Enhet.TIMER, Double>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9435-72a8-b1ce-9575cbc2a76b")
        }

        override val opplysningTekstId = "opplysning.fastsatt-ny-arbeidstid-per-uke"
        override val enhet = Enhet.TIMER

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.flyttall(
                opplysningTypeId,
            ),
        )
    }

    class HarSamordnet(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9428-74d5-b160-f63a4c61a250")
        }

        override val opplysningTekstId = "opplysning.har-samordnet"
        override val enhet = Enhet.ENHETSLØS

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class SykepengerDagsats(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9433-70e9-a85b-c246150c45d4")
        }

        override val opplysningTekstId = "opplysning.sykepenger-dagsats"
        override val enhet = Enhet.KRONER

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class PleiepengerDagsats(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9433-70e9-a85b-c246150c45d5")
        }

        override val opplysningTekstId = "opplysning.pleiepenger-dagsats"
        override val enhet = Enhet.KRONER

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class OmsorgspengerDagsats(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9433-70e9-a85b-c246150c45d6")
        }

        override val opplysningTekstId = "opplysning.omsorgspenger-dagsats"
        override val enhet = Enhet.KRONER

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class OpplæringspengerDagsats(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9433-70e9-a85b-c246150c45d7")
        }

        override val opplysningTekstId = "opplysning.opplaeringspenger-dagsats"
        override val enhet = Enhet.KRONER

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class UføreDagsats(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9433-70e9-a85b-c246150c45d8")
        }

        override val opplysningTekstId = "opplysning.ufore-dagsats"
        override val enhet = Enhet.KRONER

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class ForeldrepengerDagsats(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9433-70e9-a85b-c246150c45d9")
        }

        override val opplysningTekstId = "opplysning.foreldrepenger-dagsats"
        override val enhet = Enhet.KRONER

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class SvangerskapspengerDagsats(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9433-70e9-a85b-c246150c45da")
        }

        override val opplysningTekstId = "opplysning.svangerskapspenger-dagsats"
        override val enhet = Enhet.KRONER

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(opplysningTypeId),
        )
    }

    // todo sjekke denne
    // hos pjs er dette  "Grunnlaget for verneplikt er høyere enn dagpengegrunnlaget"
    // Kan være null
    class ErInnvilgetMedVerneplikt(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9421-766c-9dc6-41fe6c9a1e05")
        }

        override val opplysningTekstId = "opplysning.er-innvilget-med-verneplikt"
        override val enhet = Enhet.ENHETSLØS

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    // todo denne må deriveres fra andre opplysninger i vedtaket
    // Denne er komplisert, Settes basert på om kvote. Dersom både verneplikt kvote  og  dagpenger kvote finnes
    // tar verneplikt kvoten presedens.
    // i dp-behandling: Dersom KravTilMinsteinntektId("0194881f-9413-77ce-92ec-d29700f0424c") er true
    // settes dagpenger kvoten til AntallStønadsukerSomGisVedOrdinæreDagpenger(vårt navn, id: "0194881f-943d-77a7-969c-147999f15459")
    //
    // Hos oss : Dersom grunnlagForVernepliktErGunstigst er true settes verneplikt kvoten  til vernepliktPeriode(0194881f-9421-766c-9dc6-41fe6c9a1e01)
    // Dersom  GrunnlagetForVernepliktErHoyereEnnDagpengeGrunnlaget er true, så settes AntallStønadsuker til
    // PeriodeSomGisVedVerneplikt ellers til AntallStønadsukerSomGisVedOrdinæreDagpenger
    class AntallStønadsuker(
        override val verdi: Int,
    ) : DagpengerOpplysning<Enhet.UKER, Int>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-943d-77a7-969c-147999f15459")

            fun fra(opplysninger: Set<DagpengerOpplysning<*, *>>): AntallStønadsuker? {
                val antallStønadsuker =
                    when (opplysninger.any { it is GrunnlagetForVernepliktErHoyereEnnDagpengeGrunnlaget && it.verdi }) {
                        true -> {
                            opplysninger.filterIsInstance<PeriodeSomGisVedVerneplikt>().singleOrNull()?.verdi
                        }

                        false -> {
                            opplysninger
                                .filterIsInstance<AntallStønadsukerSomGisVedOrdinæreDagpenger>()
                                .singleOrNull()
                                ?.verdi
                        }
                    }
                return antallStønadsuker?.let { AntallStønadsuker(it) }
            }
        }

        override val enhet: Enhet.UKER = Enhet.UKER
        override val opplysningTekstId: String = "opplysning.antall-stonadsuker"
    }

    class OppfyllerKravTilMinsteinntekt(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9413-77ce-92ec-d29700f0424c")
        }

        override val opplysningTekstId = "opplysning.oppfyller-krav-til-minsteinntekt"
        override val enhet = Enhet.ENHETSLØS

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    // todo kan være nullable
    class GrunnlagetForVernepliktErHoyereEnnDagpengeGrunnlaget(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9421-766c-9dc6-41fe6c9a1e05")
        }

        override val opplysningTekstId = "opplysning.grunnlaget-for-verneplikt-er-hoyere-enn-dagpengegrunnlaget"
        override val enhet = Enhet.ENHETSLØS

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class PeriodeSomGisVedVerneplikt(
        override val verdi: Int,
    ) : DagpengerOpplysning<Enhet.UKER, Int>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9421-766c-9dc6-41fe6c9a1e01")
        }

        override val enhet: Enhet.UKER = Enhet.UKER
        override val opplysningTekstId: String = "opplysning.periode-som-gis-ved-verneplikt"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.heltall(
                opplysningTypeId,
            ),
        )
    }

    // Denne baserer seg på om oppfyllerKravetTilPermittering(0194d111-db2f-7395-bcfb-959f245fd2a6) er true i PJ.
    // nullable
    class AntallPermitteringsuker(
        override val verdi: Int,
    ) : DagpengerOpplysning<Enhet.UKER, Int>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0195042d-918e-7fae-8fb7-7f38eed42710")
        }

        override val enhet: Enhet.UKER = Enhet.UKER
        override val opplysningTekstId: String = "opplysning.antall-permitteringsuker"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.heltall(
                opplysningTypeId,
            ),
        )
    }

    // Denne baserer seg på om oppfyllerKravetTilPermitteringFiskeindustriId ("019522b0-c722-76d4-8d7f-78f556c51f72") er true i PJ.
    // nullable
    class AntallPermitteringsukerFisk(
        override val verdi: Int,
    ) : DagpengerOpplysning<Enhet.UKER, Int>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0195235a-599b-7b27-97a8-bc6142066a87")
        }

        override val enhet: Enhet.UKER = Enhet.UKER
        override val opplysningTekstId: String = "opplysning.antall-permitteringsuker-fisk"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.heltall(
                opplysningTypeId,
            ),
        )
    }

    class Egenandel(
        override val verdi: Number,
    ) : DagpengerOpplysning<Enhet.KRONER, Number>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-943f-78d9-b874-00a4944c54ef")
        }

        override val enhet: Enhet.KRONER = Enhet.KRONER
        override val opplysningTekstId: String = "opplysning.egenandel"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.penger(
                opplysningTypeId,
            ),
        )
    }

    class KravTilArbeidssøker(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9442-707b-a6ee-e96c06877be2")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.krav-til-arbeidssoker"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class OppfyllerKravTilMobilitet(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9442-707b-a6ee-e96c06877bdb")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.oppfyller-krav-til-mobilitet"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class OppfyllerKravTilArbeidsfør(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9442-707b-a6ee-e96c06877bdd")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.oppfyller-krav-til-arbeidsfor"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class OppfyllerKravTilArbeidssøker(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9442-707b-a6ee-e96c06877bd8")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.oppfyller-krav-til-arbeidssoker"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class OppfyllerKravetTilEthvertArbeid(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9442-707b-a6ee-e96c06877bdf")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.oppfyller-kravet-til-ethvert-arbeid"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class OppyllerKravTilRegistrertArbeidssøker(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9442-707b-a6ee-e96c06877be1")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.oppfyller-krav-til-registrert-arbeidssoker"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class OppfyllerKravetTilIkkeUtestengt(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9447-7e36-a569-3e9f42bff9f7")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.oppfyller-kravet-til-ikke-utestengt"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class OppfyllerKravetTilOpphold(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9443-72b4-8b30-5f6cdb24d54b")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.oppfyller-kravet-til-opphold"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class IkkeFulleYtelser(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-943f-78d9-b874-00a4944c54f1")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.ikke-fulle-ytelser"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class KravTilTapAvArbeidsinntektOgArbeidstid(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9435-72a8-b1ce-9575cbc2a76f")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.krav-til-tap-av-arbeidsinntekt-og-arbeidstid"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class KravTilTaptArbeidstid(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9435-72a8-b1ce-9575cbc2a76e")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.krav-til-tapt-arbeidstid"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class KravTilTapAvArbeidsinntekt(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9435-72a8-b1ce-9575cbc2a761")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.krav-til-tap-av-arbeidsinntekt"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class IkkeStreikEllerLockout(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-91df-746a-a8ac-4a6b2b30685f")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.ikke-streik-eller-lockout"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class KravTilAlder(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-940b-76ff-acf5-ba7bcb367237")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.krav-til-alder"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class KravTilUtdanning(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9445-734c-a7ee-045edf29b52d")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.krav-til-utdanning"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class OppfyllerMedlemskap(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9443-72b4-8b30-5f6cdb24d54d")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.oppfyller-medlemskap"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class OppfyllerKravetTilPermittering(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194d111-db2f-7395-bcfb-959f245fd2a6")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.oppfyller-kravet-til-permittering"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    class OppfyllerKravetTilPermitteringFiskeindustri(
        override val verdi: Boolean,
    ) : DagpengerOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("019522b0-c722-76d4-8d7f-78f556c51f72")
        }

        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
        override val opplysningTekstId: String = "opplysning.oppfyller-kravet-til-permittering-fiskeindustri"

        constructor(behandlingsresultatData: BehandlingsresultatData) : this(
            behandlingsresultatData.boolsk(
                opplysningTypeId,
            ),
        )
    }

    override fun equals(other: Any?): Boolean =
        this.opplysningTekstId == (other as? DagpengerOpplysning<*, *>)?.opplysningTekstId &&
            this.verdi == other.verdi &&
            this.enhet == other.enhet

    override fun hashCode(): Int {
        var result = opplysningTekstId.hashCode()
        result = 31 * result + verdi.hashCode()
        result = 31 * result + enhet.hashCode()
        return result
    }

    override fun toString(): String = "Opplysning(opplysningTekstId='$opplysningTekstId', verdi=$verdi, enhet=$enhet)"
}
