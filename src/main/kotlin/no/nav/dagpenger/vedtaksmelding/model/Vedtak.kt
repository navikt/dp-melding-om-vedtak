package no.nav.dagpenger.vedtaksmelding.model

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.MissingNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.dagpenger.vedtaksmelding.model.Vedtak.Opplysning2.Companion.NULL_OPPLYSNING
import no.nav.dagpenger.vedtaksmelding.model.Vedtak.Opplysning2.Datatype.DATO
import no.nav.dagpenger.vedtaksmelding.model.Vedtak.Opplysning2.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.Vedtak.Opplysning2.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.Vedtak.Opplysning2.Datatype.TEKST
import no.nav.dagpenger.vedtaksmelding.model.Vedtak.Opplysning2.Enhet
import no.nav.dagpenger.vedtaksmelding.model.Vedtak.Opplysning2.Enhet.BARN
import no.nav.dagpenger.vedtaksmelding.model.Vedtak.Opplysning2.Enhet.ENHETSLØS
import no.nav.dagpenger.vedtaksmelding.model.Vedtak.Opplysning2.Enhet.KRONER
import no.nav.dagpenger.vedtaksmelding.model.Vedtak.Opplysning2.Enhet.TIMER
import no.nav.dagpenger.vedtaksmelding.model.Vedtak.Opplysning2.Enhet.UKER

data class Vilkår(
    val navn: String,
    val status: Status,
) {
    enum class Status {
        OPPFYLT,
        IKKE_OPPFYLT,
    }
}

class Vedtak(vedtakJson: String) {
    private val vedtak: JsonNode
    private val objectMapper: ObjectMapper =
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    init {
        vedtak = objectMapper.readTree(vedtakJson)
    }

    enum class Utfall {
        INNVILGET,
        AVSLÅTT,
    }

    val utfall: Utfall =
        vedtak["fastsatt"]["utfall"].asBoolean().let { utfall ->
            when (utfall) {
                true -> Utfall.INNVILGET
                false -> Utfall.AVSLÅTT
            }
        }

    val vilkår: Set<Vilkår> =
        vedtak["vilkår"].map { vilkårNode ->
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
        }.toSet()

    val opplysninger: Set<Opplysning2> =
        setOf(
            vedtak.finnOpplysningAt(
                opplysningTekstId = "opplysning.grunnlag",
                jsonPointer = "/fastsatt/grunnlag/grunnlag",
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
            vedtak.finnOpplysningAt(
                opplysningTekstId = "opplysning.avrundet-dagsats-med-barnetillegg",
                jsonPointer = "/fastsatt/sats/dagsatsMedBarnetillegg",
                datatype = FLYTTALL,
                enhet = KRONER,
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
                opplysningTekstId = "opplysning.brukt-beregningsregel",
                navn = "Brukt beregningsregel",
                datatype = TEKST,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.samordnet-dagsats-uten-barnetillegg",
                navn = "Samordnet dagsats uten barnetillegg",
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.ukessats",
                navn = "Ukessats med barnetillegg etter samordning",
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.antall-stonadsuker",
                navn = "Antall stønadsuker",
                datatype = HELTALL,
                enhet = UKER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.egenandel",
                navn = "Egenandel",
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "oppysning.utbetalt-arbeidsinntekt-periode-1",
                navn = "Utbetalt arbeidsinntekt periode 1",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "oppysning.utbetalt-arbeidsinntekt-periode-2",
                navn = "Utbetalt arbeidsinntekt periode 2",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "oppysning.utbetalt-arbeidsinntekt-periode-3",
                navn = "Utbetalt arbeidsinntekt periode 3",
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningMedNavn(
                opplysningTekstId = "opplysning.har-samordnet",
                navn = "Har samordnet",
                datatype = Opplysning2.Datatype.BOOLSK,
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
        )

    private fun hentOpplysning(opplysningsnavn: String): String {
        val node =
            vedtak["opplysninger"].find {
                it["navn"].asText() == opplysningsnavn
            }
        return node?.get("verdi")?.asText() ?: throw OpplysningIkkeFunnet("$opplysningsnavn ikke funnet")
    }

    private fun JsonNode.finnOpplysningMedNavn(
        opplysningTekstId: String,
        navn: String,
        datatype: Vedtak.Opplysning2.Datatype,
        enhet: Enhet = ENHETSLØS,
    ): Opplysning2 {
        return this.finnOpplysningAt(opplysningTekstId, "/opplysninger", datatype, enhet) { node ->
            node.find { it["navn"].asText() == navn }?.findValue("verdi")?.asText()
        }
    }

    private fun JsonNode.finnOpplysningAt(
        opplysningTekstId: String,
        jsonPointer: String,
        datatype: Vedtak.Opplysning2.Datatype,
        enhet: Enhet = ENHETSLØS,
        predicate: (JsonNode) -> String? = { node -> node.asText() },
    ): Vedtak.Opplysning2 {
        return this.at(jsonPointer).let {
            when (it) {
                is MissingNode -> NULL_OPPLYSNING
                else -> {
                    when (val verdi = predicate(it)) {
                        null -> NULL_OPPLYSNING
                        else ->
                            Vedtak.Opplysning2(
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

    fun finnOpplysning(opplysningTekstId: String): Opplysning2? =
        this.opplysninger.singleOrNull { it.opplysningTekstId == opplysningTekstId }

    data class Opplysning2(
        val opplysningTekstId: String,
        val verdi: String,
        val datatype: Datatype,
        val enhet: Enhet,
    ) {
        companion object {
            val NULL_OPPLYSNING =
                Opplysning2(
                    opplysningTekstId = "ukjent.opplysning",
                    verdi = "ukjent",
                    datatype = TEKST,
                    enhet = ENHETSLØS,
                )
        }

        enum class Datatype {
            TEKST,
            HELTALL,
            FLYTTALL,
            DATO,
            BOOLSK,
        }

        enum class Enhet {
            KRONER,
            DAGER,
            ENHETSLØS,
            UKER,
            BARN,
            TIMER,
        }
    }

    class OpplysningIkkeFunnet(message: String) : RuntimeException(message)
}
