package no.nav.dagpenger.vedtaksmelding.model

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.Opplysning2.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.Opplysning2.Datatype.DATO
import no.nav.dagpenger.vedtaksmelding.model.Opplysning2.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.Opplysning2.Enhet.ENHETSLØS
import no.nav.dagpenger.vedtaksmelding.model.Opplysning2.Enhet.KRONER
import org.junit.jupiter.api.Test

class VedtakMapperTest {
    // "Krav til minsteinntekt" -> "opplysning.krav-til-minsteinntekt"

    private val resourseRetriever = object {}.javaClass

    @Test
    fun `Test å lag opplysning krav-til-minsteinntekt`() {
        val vedtakJson = resourseRetriever.getResource("/json/vedtak.json").readText()
        val vedtakMapper = VedtakMapper(vedtakJson)

        vedtakMapper.hentOppfyllerKravTilMinsteinntekt() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.krav-til-minsteinntekt",
                verdi = "true",
                datatype = BOOLSK,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysning grunnlag`() {
        val vedtakJson = resourseRetriever.getResource("/json/vedtak.json").readText()
        val vedtakMapper = VedtakMapper(vedtakJson)

        vedtakMapper.hentOpplysningGrunnlag() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.grunnlag",
                verdi = "614871",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysnings provingsdato som egentlig er virkningsdato`()  {
        val vedtakJson = resourseRetriever.getResource("/json/vedtak.json").readText()
        val vedtakMapper = VedtakMapper(vedtakJson)

        vedtakMapper.hentOpplysningProvingDato() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.provingsdato",
                verdi = "2024-11-29",
                datatype = DATO,
                enhet = ENHETSLØS,
            )
    }





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

class VedtakMapper(vedtakJson: String) {
    fun hentOppfyllerKravTilMinsteinntekt(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.krav-til-minsteinntekt",
            verdi =
                vedtak["vilkår"].any { it["navn"].asText() == "Krav til minsteinntekt" && it["status"].asText() == "Oppfylt" }
                    .toString(),
            datatype = BOOLSK,
            enhet = ENHETSLØS,
        )
    }

    fun hentOpplysningGrunnlag(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.grunnlag",
            verdi = vedtak["fastsatt"]["grunnlag"]["grunnlag"].asText(),
            datatype = HELTALL,
            enhet = KRONER,
        )
    }

    fun hentOpplysningProvingDato(): Opplysning2 {
        return Opplysning2(
            opplysningTekstId = "opplysning.provingsdato",
            verdi = vedtak["virkningsdato"].asText(),
            datatype = DATO,
            enhet = ENHETSLØS,
        )
    }

    private val vedtak: JsonNode
    private val objectMapper: ObjectMapper =
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    init {
        vedtak = objectMapper.readTree(vedtakJson)
    }
}

// Må hente fra opplysninger:



/*

"Søknadsdato" -> "opplysning.soknadsdato"
"Søknadstidspunkt" -> "opplysning.soknadstidspunkt"*/
