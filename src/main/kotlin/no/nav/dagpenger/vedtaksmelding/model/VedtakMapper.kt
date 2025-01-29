package no.nav.dagpenger.vedtaksmelding.model

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.MissingNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Companion.NULL_OPPLYSNING
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.DATO
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.TEKST
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.BARN
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.ENHETSLØS
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.KRONER
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.TIMER
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.UKER
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.KravTilProsentvisTapAvArbeidstid
import no.nav.dagpenger.vedtaksmelding.model.Utfall.AVSLÅTT
import no.nav.dagpenger.vedtaksmelding.model.Utfall.INNVILGET
import java.time.LocalDate
import java.time.Month
import java.util.Locale
import java.util.UUID

class VedtakMapper(vedtakJson: String) {
    private val vedtak: JsonNode
    private val objectMapper: ObjectMapper =
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    init {
        vedtak = objectMapper.readTree(vedtakJson)
    }

    fun vedtak(): Vedtak {
        return Vedtak(
            behandlingId = behandlingId,
            utfall = utfall,
            vilkår = vilkår,
            opplysninger = vedtakOpplysninger + inntjeningsperiodeOpplysninger + prosentvisTaptArbeidstidOpplysninger,
        )
    }

    private val behandlingId =
        UUID.fromString(vedtak.get("behandlingId").asText())
            ?: throw IllegalArgumentException("behandlingId mangler")

    private val utfall: Utfall =
        vedtak.get("fastsatt")?.get("utfall")?.asBoolean()?.let { utfall ->
            when (utfall) {
                true -> INNVILGET
                false -> AVSLÅTT
            }
        } ?: throw UtfallMangler("Utfall mangler i path: /fastsatt/utfall")

    private val vilkår: Set<Vilkår> =
        vedtak.get("vilkår")?.map { vilkårNode ->
            Vilkår(
                navn = vilkårNode["navn"].asText(),
                status =
                    vilkårNode["status"].asText().let {
                        when (it) {
                            "Oppfylt" -> Vilkår.Status.OPPFYLT
                            else -> Vilkår.Status.IKKE_OPPFYLT
                        }
                    },
            )
        }?.toSet() ?: throw VilkårMangler("Vilkår mangler i path: /vilkår")

    private val vedtakOpplysninger: Set<Opplysning> =
        setOf(
            vedtak.finnOpplysningAt(
                opplysningTekstId = "opplysning.grunnlag",
                jsonPointer = "/fastsatt/grunnlag/grunnlag",
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningAt(
                opplysningTekstId = "opplysning.dagsats-med-barnetillegg-etter-samordning-og-90-prosent-regel",
                jsonPointer = "/fastsatt/sats/dagsatsMedBarnetillegg",
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningAt(
                opplysningTekstId = "opplysning.provingsdato",
                jsonPointer = "/virkningsdato",
                datatype = DATO,
            ),
            vedtak.finnOpplysningAt(
                opplysningTekstId = "opplysning.fastsatt-ny-arbeidstid-per-uke",
                jsonPointer = "/fastsatt/fastsattVanligArbeidstid/nyArbeidstidPerUke",
                datatype = FLYTTALL,
                enhet = TIMER,
            ),
            vedtak.finnOpplysningAt(
                opplysningTekstId = "opplysning.fastsatt-arbeidstid-per-uke-for-tap",
                jsonPointer = "/fastsatt/fastsattVanligArbeidstid/vanligArbeidstidPerUke",
                datatype = FLYTTALL,
                enhet = TIMER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = KravTilProsentvisTapAvArbeidstid,
                datatype = FLYTTALL,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.inntektskrav-for-siste-12-mnd",
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.inntektskrav-for-siste-36-mnd",
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.arbeidsinntekt-siste-36-mnd",
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.arbeidsinntekt-siste-12-mnd",
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.antall-g-for-krav-til-12-mnd-arbeidsinntekt",
                datatype = FLYTTALL,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.antall-g-for-krav-til-36-mnd-arbeidsinntekt",
                datatype = FLYTTALL,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.gjennomsnittlig-arbeidsinntekt-siste-36-maaneder",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.antall-g-som-gis-som-grunnlag-ved-verneplikt",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.brukt-beregningsregel",
                datatype = TEKST,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.utbetalt-arbeidsinntekt-periode-1",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.utbetalt-arbeidsinntekt-periode-2",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.utbetalt-arbeidsinntekt-periode-3",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.antall-stonadsuker-som-gis-ved-ordinare-dagpenger",
                datatype = HELTALL,
                enhet = UKER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.krav-til-minsteinntekt",
                datatype = BOOLSK,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.andel-av-dagsats-med-barnetillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.andel-av-dagsats-med-barnetillegg-avkortet-til-maks-andel-av-dagpengegrunnlaget",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.antall-barn-som-gir-rett-til-barnetillegg",
                datatype = HELTALL,
                enhet = BARN,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.barnetillegg-i-kroner",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.forste-maaned-av-opptjeningsperiode",
                datatype = DATO,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.siste-avsluttende-kalendermaaned",
                datatype = DATO,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.grunnlag-siste-12-mnd",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = "opplysning.6-ganger-grunnbelop",
                datatype = HELTALL,
                enhet = KRONER,
            ),
        ) + vedtak.lagOpplysningerFraKvoter() + vedtak.lagOpplysningerForSamordning()

    private fun formaterDesimaltall(
        desimaltall: Double,
        antallDesimaler: Int = 1,
    ) = when {
        desimaltall % 1 == 0.0 -> String.format("%.0f", desimaltall)
        else -> String.format(Locale.US, "%.${antallDesimaler}f", desimaltall)
    }

    private val prosentvisTaptArbeidstidOpplysninger = vedtakOpplysninger.finnProsentvisTaptArbeidstid()

    private fun Set<Opplysning>.finnProsentvisTaptArbeidstid(): Set<Opplysning> {
        val opplysninger = mutableSetOf<Opplysning>()
        val fastsattVanligArbeidsid: Double? =
            this.singleOrNull {
                it.opplysningTekstId == "opplysning.fastsatt-arbeidstid-per-uke-for-tap"
            }?.let { opplysning ->
                opplysning.verdi.toDouble()
            }
        val fastsattNyArbeidstid: Double? =
            this.singleOrNull {
                it.opplysningTekstId == "opplysning.fastsatt-ny-arbeidstid-per-uke"
            }?.let { opplysning ->
                opplysning.verdi.toDouble()
            }
        if (fastsattVanligArbeidsid != null && fastsattNyArbeidstid != null) {
            val prosentvisTaptArbeidstid = ((fastsattVanligArbeidsid - fastsattNyArbeidstid) / fastsattVanligArbeidsid) * 100
            opplysninger.add(
                Opplysning(
                    opplysningTekstId = "opplysning.prosentvis-tapt-arbeidstid",
                    verdi = formaterDesimaltall(prosentvisTaptArbeidstid),
                    datatype = FLYTTALL,
                ),
            )
        }
        return opplysninger
    }

    private val prosentvisTaptArbeidstidOpplysninger = vedtakOpplysninger.finnProsentvisTaptArbeidstid()

    private fun Set<Opplysning>.finnProsentvisTaptArbeidstid(): Set<Opplysning> {
        val opplysninger = mutableSetOf<Opplysning>()
        val fastsattVanligArbeidsid: Double? =
            this.singleOrNull {
                it.opplysningTekstId == "opplysning.fastsatt-arbeidstid-per-uke-for-tap"
            }?.let { opplysning ->
                opplysning.verdi.toDouble()
            }
        val fastsattNyArbeidstid: Double? =
            this.singleOrNull {
                it.opplysningTekstId == "opplysning.fastsatt-ny-arbeidstid-per-uke"
            }?.let { opplysning ->
                opplysning.verdi.toDouble()
            }
        if (fastsattVanligArbeidsid != null && fastsattNyArbeidstid != null) {
            val prosentvisTaptArbeidstid = ((fastsattVanligArbeidsid - fastsattNyArbeidstid) / fastsattVanligArbeidsid) * 100
            opplysninger.add(
                Opplysning(
                    opplysningTekstId = "opplysning.prosentvis-tapt-arbeidstid",
                    verdi = String.format(Locale.US, "%.2f", prosentvisTaptArbeidstid),
                    datatype = FLYTTALL,
                ),
            )
        }
        return opplysninger
    }


    private val inntjeningsperiodeOpplysninger = vedtakOpplysninger.finnInntjeningsPeriode()

    private fun Set<Opplysning>.finnInntjeningsPeriode(): Set<Opplysning> {
        val opptjeningsperiodeOpplysninger = mutableSetOf<Opplysning>()

        val opptjeningsperiodeStart: LocalDate? =
            this.singleOrNull {
                it.opplysningTekstId == "opplysning.forste-maaned-av-opptjeningsperiode"
            }?.let { opplysning ->
                LocalDate.parse(opplysning.verdi)
            }
        val opptjeningsperiodeSlutt: LocalDate? =
            this.singleOrNull {
                it.opplysningTekstId == "opplysning.siste-avsluttende-kalendermaaned"
            }?.let { opplysning ->
                LocalDate.parse(opplysning.verdi)
            }

        if (opptjeningsperiodeStart != null && opptjeningsperiodeSlutt != null) {
            opptjeningsperiodeOpplysninger.add(
                Opplysning(
                    opplysningTekstId = "opplysning.forste-maaned-aar-for-inntektsperiode-1",
                    verdi = opptjeningsperiodeStart.plusYears(2).norskMånedOgÅr(),
                    datatype = TEKST,
                    enhet = ENHETSLØS,
                ),
            )
            opptjeningsperiodeOpplysninger.add(
                Opplysning(
                    opplysningTekstId = "opplysning.forste-maaned-aar-for-inntektsperiode-2",
                    verdi = opptjeningsperiodeStart.plusYears(1).norskMånedOgÅr(),
                    datatype = TEKST,
                    enhet = ENHETSLØS,
                ),
            )
            opptjeningsperiodeOpplysninger.add(
                Opplysning(
                    opplysningTekstId = "opplysning.forste-maaned-aar-for-inntektsperiode-3",
                    verdi = opptjeningsperiodeStart.norskMånedOgÅr(),
                    datatype = TEKST,
                    enhet = ENHETSLØS,
                ),
            )

            opptjeningsperiodeOpplysninger.add(
                Opplysning(
                    opplysningTekstId = "opplysning.siste-maaned-aar-for-inntektsperiode-1",
                    verdi = opptjeningsperiodeSlutt.norskMånedOgÅr(),
                    datatype = TEKST,
                    enhet = ENHETSLØS,
                ),
            )
            opptjeningsperiodeOpplysninger.add(
                Opplysning(
                    opplysningTekstId = "opplysning.siste-maaned-aar-for-inntektsperiode-2",
                    verdi = opptjeningsperiodeSlutt.minusYears(1).norskMånedOgÅr(),
                    datatype = TEKST,
                    enhet = ENHETSLØS,
                ),
            )
            opptjeningsperiodeOpplysninger.add(
                Opplysning(
                    opplysningTekstId = "opplysning.siste-maaned-aar-for-inntektsperiode-3",
                    verdi = opptjeningsperiodeSlutt.minusYears(2).norskMånedOgÅr(),
                    datatype = TEKST,
                    enhet = ENHETSLØS,
                ),
            )
        }
        return opptjeningsperiodeOpplysninger
    }

    private fun LocalDate.norskMånedOgÅr() =
        when (this.month) {
            Month.JANUARY -> "januar ${this.year}"
            Month.FEBRUARY -> "februar ${this.year}"
            Month.MARCH -> "mars ${this.year}"
            Month.APRIL -> "april ${this.year}"
            Month.MAY -> "mai ${this.year}"
            Month.JUNE -> "juni ${this.year}"
            Month.JULY -> "juli ${this.year}"
            Month.AUGUST -> "august ${this.year}"
            Month.SEPTEMBER -> "september ${this.year}"
            Month.OCTOBER -> "oktober ${this.year}"
            Month.NOVEMBER -> "november ${this.year}"
            Month.DECEMBER -> "desember ${this.year}"
        }

    private fun JsonNode.finnOpplysningFraType(
        opplysningType: OpplysningTyper,
        datatype: Datatype,
        enhet: Enhet = ENHETSLØS,
    ): Opplysning {
        return finnOpplysningAt(
            opplysningTekstId = opplysningType.opplysningTekstId,
            jsonPointer = "/opplysninger",
            datatype = datatype,
            enhet = enhet
        ) { opplysning ->
            opplysning.find { it["opplysningTypeId"].asText() == opplysningType.opplysningTypeId.toString() }?.findValue("verdi")?.asText()
        }
    }

    private fun JsonNode.lagOpplysningerForSamordning(): Set<Opplysning> {
        val opplysninger = mutableSetOf<Opplysning>()
        val samordnedeYtelser = this.at("/fastsatt/samordning")

        if (samordnedeYtelser is MissingNode) return emptySet()
        val samordnetOpplysning = harSamordnet(samordnedeYtelser)
        opplysninger.add(samordnetOpplysning)
        samordnedeYtelser.forEach { samordnetYtelse ->
            when (samordnetYtelse["type"].asText()) {
                "Sykepenger dagsats" ->
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = "opplysning.sykepenger-dagsats",
                            verdi = samordnetYtelse["beløp"].asText(),
                            datatype = FLYTTALL,
                            enhet = KRONER,
                        ),
                    )

                "Pleiepenger dagsats" ->
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = "opplysning.pleiepenger-dagsats",
                            verdi = samordnetYtelse["beløp"].asText(),
                            datatype = FLYTTALL,
                            enhet = KRONER,
                        ),
                    )

                "Omsorgspenger dagsats" ->
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = "opplysning.omsorgspenger-dagsats",
                            verdi = samordnetYtelse["beløp"].asText(),
                            datatype = FLYTTALL,
                            enhet = KRONER,
                        ),
                    )

                "Opplæringspenger dagsats" ->
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = "opplysning.opplaeringspenger-dagsats",
                            verdi = samordnetYtelse["beløp"].asText(),
                            datatype = FLYTTALL,
                            enhet = KRONER,
                        ),
                    )

                "Uføre dagsats" ->
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = "opplysning.ufore-dagsats",
                            verdi = samordnetYtelse["beløp"].asText(),
                            datatype = FLYTTALL,
                            enhet = KRONER,
                        ),
                    )

                "Foreldrepenger dagsats" ->
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = "opplysning.foreldrepenger-dagsats",
                            verdi = samordnetYtelse["beløp"].asText(),
                            datatype = FLYTTALL,
                            enhet = KRONER,
                        ),
                    )

                "Svangerskapspenger dagsats" ->
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = "opplysning.svangerskapspenger-dagsats",
                            verdi = samordnetYtelse["beløp"].asText(),
                            datatype = FLYTTALL,
                            enhet = KRONER,
                        ),
                    )
            }
        }
        return opplysninger
    }

    private fun harSamordnet(samordnedeYtelser: JsonNode): Opplysning {
        return when (samordnedeYtelser.size()) {
            0 ->
                Opplysning(
                    opplysningTekstId = "opplysning.har-samordnet",
                    verdi = false.toString(),
                    datatype = BOOLSK,
                )

            else ->
                Opplysning(
                    opplysningTekstId = "opplysning.har-samordnet",
                    verdi = true.toString(),
                    datatype = BOOLSK,
                )
        }
    }

    private fun JsonNode.lagOpplysningerFraKvoter(): Set<Opplysning> {
        val opplysninger = mutableSetOf<Opplysning>()
        val kvoter = this.at("/fastsatt/kvoter")

        if (kvoter is MissingNode) return emptySet()

        kvoter.forEach { kvote ->
            when (kvote["navn"].asText()) {
                "Dagpengeperiode" -> bestemStønadsukerOpplysning(opplysninger, kvote)
                "Verneplikt" -> {
                    bestemStønadsukerOpplysning(opplysninger, kvote)
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = "opplysning.er-innvilget-med-verneplikt",
                            verdi = true.toString(),
                            datatype = BOOLSK,
                        ),
                    )
                }

                "Egenandel" ->
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = "opplysning.egenandel",
                            verdi = kvote["verdi"].asText(),
                            datatype = HELTALL,
                            enhet = KRONER,
                        ),
                    )
            }
        }

        return opplysninger
    }

    private fun bestemStønadsukerOpplysning(
        opplysninger: MutableSet<Opplysning>,
        kvote: JsonNode,
    ) {
        val antallStønadsukerTekstId = "opplysning.antall-stonadsuker"
        val stønadsukerOpplysning =
            Opplysning(
                opplysningTekstId = antallStønadsukerTekstId,
                verdi = kvote["verdi"].asText(),
                datatype = HELTALL,
                enhet = UKER,
            )

        when (kvote["navn"].asText()) {
            // Ikke optimalt, men dersom Verneplikt og Dagpengeperiode finnes i kvoter, så tar Verneplikt presedens.
            // Dette burde endres i vedtaksAPIet siden vi tolker regelverk her, og det er ikke vår oppgave.
            "Dagpengeperiode" -> {
                if (opplysninger.none { it.opplysningTekstId == antallStønadsukerTekstId }) {
                    opplysninger.add(stønadsukerOpplysning)
                }
            }

            "Verneplikt" -> {
                opplysninger.removeIf { it.opplysningTekstId == antallStønadsukerTekstId }
                opplysninger.add(stønadsukerOpplysning)
            }
        }
    }

    private fun JsonNode.finnOpplysningAt(
        opplysningTekstId: String,
        jsonPointer: String,
        datatype: Datatype,
        enhet: Enhet = ENHETSLØS,
        predicate: (JsonNode) -> String? = { node -> node.asText() },
    ): Opplysning {
        return this.at(jsonPointer).let {
            when (it) {
                is MissingNode -> NULL_OPPLYSNING
                else -> {
                    when (val verdi = predicate(it)) {
                        null -> NULL_OPPLYSNING
                        else ->
                            Opplysning(
                                opplysningTekstId = opplysningTekstId,
                                datatype = datatype,
                                enhet = enhet,
                                verdi =
                                    when (datatype) {
                                        FLYTTALL -> {
                                            when (enhet) {
                                                KRONER -> formaterDesimaltall(verdi.toDouble(), antallDesimaler = 2)
                                                else -> formaterDesimaltall(verdi.toDouble(), antallDesimaler = 1)
                                            }
                                        }

                                        else -> verdi
                                    },
                            )
                    }
                }
            }
        }
    }
}

class VilkårMangler(message: String) : RuntimeException(message)

class UtfallMangler(message: String) : RuntimeException(message)
