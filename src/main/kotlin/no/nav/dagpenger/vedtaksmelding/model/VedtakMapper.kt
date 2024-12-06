package no.nav.dagpenger.vedtaksmelding.model

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.FLYTTALL

class VedtakMapper(vedtakJson: String) {
    private val vedtak: JsonNode
    private val objectMapper: ObjectMapper =
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    init {
        vedtak = objectMapper.readTree(vedtakJson)
    }

    fun hentOppfyllerKravTilMinsteinntekt(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.krav-til-minsteinntekt",
            verdi =
                vedtak["vilkår"].any { it["navn"].asText() == "Krav til minsteinntekt" && it["status"].asText() == "Oppfylt" }
                    .toString(),
            datatype = Opplysning2.Datatype.BOOLSK,
            enhet = Opplysning2.Enhet.ENHETSLØS,
        )
    }

    fun hentOpplysningGrunnlag(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.grunnlag",
            verdi = vedtak["fastsatt"]["grunnlag"]["grunnlag"].asText(),
            datatype = Opplysning2.Datatype.HELTALL,
            enhet = Opplysning2.Enhet.KRONER,
        )
    }

    fun hentOpplysningProvingDato(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.provingsdato",
            verdi = vedtak["virkningsdato"].asText(),
            datatype = Opplysning2.Datatype.DATO,
            enhet = Opplysning2.Enhet.ENHETSLØS,
        )
    }

    fun hentInntektsKravSiste12Måneder(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.inntektskrav-for-siste-12-mnd",
            verdi = hentOpplysning("Inntektskrav for siste 12 mnd"),
            datatype = Opplysning2.Datatype.HELTALL,
            enhet = Opplysning2.Enhet.KRONER,
        )
    }

    fun hentInntektsKravSiste36Måneder(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.inntektskrav-for-siste-36-mnd",
            verdi = hentOpplysning("Inntektskrav for siste 36 mnd"),
            datatype = Opplysning2.Datatype.HELTALL,
            enhet = Opplysning2.Enhet.KRONER,
        )
    }

    fun hentArbeidsinntektSiste36Måneder(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.arbeidsinntekt-siste-36-mnd",
            verdi = hentOpplysning("Arbeidsinntekt siste 36 mnd"),
            datatype = Opplysning2.Datatype.HELTALL,
            enhet = Opplysning2.Enhet.KRONER,
        )
    }

    fun hentArbeidsinntektSiste12Måneder(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.arbeidsinntekt-siste-12-mnd",
            verdi = hentOpplysning("Arbeidsinntekt siste 12 mnd"),
            datatype = Opplysning2.Datatype.HELTALL,
            enhet = Opplysning2.Enhet.KRONER,
        )
    }

    fun hentAntallGForKravTil12MndArbeidsinntekt(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.antall-g-for-krav-til-12-mnd-arbeidsinntekt",
            verdi = hentOpplysning("Antall G for krav til 12 mnd arbeidsinntekt"),
            datatype = FLYTTALL,
            enhet = Opplysning2.Enhet.ENHETSLØS,
        )
    }

    fun hentAntallGForKravTil36MndArbeidsinntekt(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.antall-g-for-krav-til-36-mnd-arbeidsinntekt",
            verdi = hentOpplysning("Antall G for krav til 36 mnd arbeidsinntekt"),
            datatype = Opplysning2.Datatype.FLYTTALL,
            enhet = Opplysning2.Enhet.ENHETSLØS,
        )
    }

    fun hentGjennomsnittligArbeidsinntektSiste36Måneder(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.gjennomsnittlig-arbeidsinntekt-siste-36-maaneder",
            verdi = hentOpplysning("Gjennomsnittlig arbeidsinntekt siste 36 måneder"),
            datatype = Opplysning2.Datatype.FLYTTALL,
            enhet = Opplysning2.Enhet.KRONER,
        )
    }

    private fun hentOpplysning(opplysningsnavn: String): String {
        return vedtak["opplysninger"].find {
            it["navn"].asText() == opplysningsnavn
        }?.get("verdi")?.asText() ?: throw OpplysningIkkeFunnet("$opplysningsnavn ikke funnet")
    }

    data class Opplysning2(
        val opplysningTekstId: String,
        val verdi: String,
        val datatype: Datatype,
        val enhet: Enhet,
    ) {
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
        }
    }

    class OpplysningIkkeFunnet(message: String) : RuntimeException(message)
}

// Må hente fra opplysninger:
// "Søknadsdato" -> "opplysning.soknadsdato"
// "Søknadstidspunkt" -> "opplysning.soknadstidspunkt"
