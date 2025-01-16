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
import no.nav.dagpenger.vedtaksmelding.model.Utfall.AVSLÅTT
import no.nav.dagpenger.vedtaksmelding.model.Utfall.INNVILGET
import java.time.LocalDate
import java.time.Month
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
            opplysninger = vedtakOpplysninger + inntjeningsperiodeOpplysninger,
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
                opplysningTekstId = "opplysning.fastsatt-arbeidstid-per-uke-for-tap",
                jsonPointer = "/fastsatt/fastsattVanligArbeidstid/vanligArbeidstidPerUke",
                datatype = FLYTTALL,
                enhet = TIMER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.inntektskrav-for-siste-12-mnd",
                navn = "Inntektskrav for siste 12 mnd",
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.inntektskrav-for-siste-36-mnd",
                navn = "Inntektskrav for siste 36 mnd",
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.arbeidsinntekt-siste-36-mnd",
                navn = "Arbeidsinntekt siste 36 mnd",
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.arbeidsinntekt-siste-12-mnd",
                navn = "Arbeidsinntekt siste 12 mnd",
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.antall-g-for-krav-til-12-mnd-arbeidsinntekt",
                navn = "Antall G for krav til 12 mnd arbeidsinntekt",
                datatype = FLYTTALL,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.antall-g-for-krav-til-36-mnd-arbeidsinntekt",
                navn = "Antall G for krav til 36 mnd arbeidsinntekt",
                datatype = FLYTTALL,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.gjennomsnittlig-arbeidsinntekt-siste-36-maaneder",
                navn = "Gjennomsnittlig arbeidsinntekt siste 36 måneder",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.antall-g-som-gis-som-grunnlag-ved-verneplikt",
                navn = "Antall G som gis som grunnlag ved verneplikt",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.brukt-beregningsregel",
                navn = "Brukt beregningsregel",
                datatype = TEKST,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.utbetalt-arbeidsinntekt-periode-1",
                navn = "Utbetalt arbeidsinntekt periode 1",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.utbetalt-arbeidsinntekt-periode-2",
                navn = "Utbetalt arbeidsinntekt periode 2",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.utbetalt-arbeidsinntekt-periode-3",
                navn = "Utbetalt arbeidsinntekt periode 3",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.antall-stonadsuker-som-gis-ved-ordinare-dagpenger",
                navn = "Antall stønadsuker som gis ved ordinære dagpenger",
                datatype = HELTALL,
                enhet = UKER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.krav-til-minsteinntekt",
                navn = "Krav til minsteinntekt",
                datatype = BOOLSK,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.har-samordnet",
                navn = "Har samordnet",
                datatype = BOOLSK,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.andel-av-dagsats-med-barnetillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget",
                navn = "Andel av dagsats med barnetillegg som overstiger maks andel av dagpengegrunnlaget",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.andel-av-dagsats-med-barnetillegg-avkortet-til-maks-andel-av-dagpengegrunnlaget",
                navn = "Andel av dagsats med barnetillegg avkortet til maks andel av dagpengegrunnlaget",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.antall-barn-som-gir-rett-til-barnetillegg",
                navn = "Antall barn som gir rett til barnetillegg",
                datatype = HELTALL,
                enhet = BARN,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.barnetillegg-i-kroner",
                navn = "Sum av barnetillegg",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.forste-maaned-av-opptjeningsperiode",
                navn = "Første måned av opptjeningsperiode",
                datatype = DATO,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.siste-avsluttende-kalendermaaned",
                navn = "Siste avsluttende kalendermåned",
                datatype = DATO,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.grunnlag-siste-12-mnd",
                navn = "Grunnlag siste 12 mnd.",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.6-ganger-grunnbelop",
                navn = "6 ganger grunnbeløp",
                datatype = HELTALL,
                enhet = KRONER,
            ),
        ) + vedtak.lagOpplysningerFraKvoter() + vedtak.lagInnvilgetMedVernepliktOpplysning()

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

    private fun JsonNode.finnOpplysningMedNavn(
        opplysningTekstId: String,
        navn: String,
        datatype: Datatype,
        enhet: Enhet = ENHETSLØS,
    ): Opplysning {
        return this.finnOpplysningAt(opplysningTekstId, "/opplysninger", datatype, enhet) { node ->
            node.find { it["navn"].asText() == navn }?.findValue("verdi")?.asText()
        }
    }

    private fun JsonNode.lagInnvilgetMedVernepliktOpplysning(): Set<Opplysning> {
        return this.at("/fastsatt/kvoter").let { kvoter ->
            when (kvoter) {
                is MissingNode -> emptySet()
                else -> {
                    kvoter.map { kvote ->
                        when (kvote["navn"].asText()) {
                            "Verneplikt" -> {
                                Opplysning(
                                    opplysningTekstId = "opplysning.er-innvilget-med-verneplikt",
                                    verdi = true.toString(),
                                    datatype = BOOLSK,
                                )
                            }

                            else -> NULL_OPPLYSNING
                        }
                    }
                }
            }
        }.toSet()
    }

//    private fun JsonNode.lagEgenandelOpplysning(): Opplysning {
//        this.at("/fastsatt/kvoter").let { kvoter ->
//            kvoter. { kvote ->
//                kvote["navn"].asText() == "Egenandel" {
//                    Opplysning(
//                        opplysningTekstId = "opplysning.egenandel",
//                        verdi = kvote["verdi"].asText(),
//                        datatype = HELTALL,
//                        enhet = UKER,
//                    )
//                }
//            }
//        }
//        return NULL_OPPLYSNING
//    }

    private fun JsonNode.lagOpplysningerFraKvoter(): Set<Opplysning> {
        val opplysninger = mutableSetOf<Opplysning>()
        this.at("/fastsatt/kvoter").let { kvoter ->
            when (kvoter) {
                is MissingNode -> emptySet()
                else -> {
                    kvoter.map { kvote ->
                        when (kvote["navn"].asText()) {
                            "Dagpengeperiode" -> {
                                if (!opplysninger.any { it.opplysningTekstId == "opplysning.antall-stonadsuker" }) {
                                    opplysninger.add(lagAntallStønadsukerOpplysning(kvote))
                                } else {
                                    null
                                }
                            }

                            "Egenandel" ->
                                lagEgenandelOpplysning(kvote)

                            "Verneplikt" -> {
                                if (opplysninger.any { it.opplysningTekstId == "opplysning.antall-stonadsuker" }) {
                                    opplysninger.remove(opplysninger.find { it.opplysningTekstId == "opplysning.antall-stonadsuker" })
                                }
                                opplysninger.add(lagAntallStønadsukerOpplysning(kvote))
                            }
                            else -> null
                        }
                    }
                }
            }
        }
        return opplysninger.toSet()
    }

    private fun lagAntallStønadsukerOpplysning(kvote: JsonNode): Opplysning {
        Opplysning(
            opplysningTekstId = "opplysning.antall-stonadsuker",
            verdi = kvote["verdi"].asText(),
            datatype = HELTALL,
            enhet = UKER,
        )
        return NULL_OPPLYSNING
    }

    private fun lagEgenandelOpplysning(kvote: JsonNode) =
        Opplysning(
            opplysningTekstId = "opplysning.egenandel",
            verdi = kvote["verdi"].asText(),
            datatype = HELTALL,
            enhet = KRONER,
        )

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
                                verdi = verdi,
                                datatype = datatype,
                                enhet = enhet,
                            )
                    }
                }
            }
        }
    }
}

class VilkårMangler(message: String) : RuntimeException(message)

class UtfallMangler(message: String) : RuntimeException(message)
