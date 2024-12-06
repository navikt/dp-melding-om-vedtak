package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.DATO
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Enhet.ENHETSLØS
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper.Opplysning2.Enhet.KRONER
import org.junit.jupiter.api.Test

class VedtakMapperTest {
    // "Krav til minsteinntekt" -> "opplysning.krav-til-minsteinntekt"

    private val resourseRetriever = object {}.javaClass
    private val vedtakMapper = VedtakMapper(resourseRetriever.getResource("/json/vedtak.json").readText())

    @Test
    fun `hent opplysning krav-til-minsteinntekt`() {
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
        vedtakMapper.hentOpplysningGrunnlag() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.grunnlag",
                verdi = "614871",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysnings provingsdato som egentlig er virkningsdato`() {
        vedtakMapper.hentOpplysningProvingDato() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.provingsdato",
                verdi = "2024-11-29",
                datatype = DATO,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Hent opplysnings-inntekts-krav-siste-12-måneder `() {
        vedtakMapper.hentInntektsKravSiste12Måneder() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.inntektskrav-for-siste-12-mnd",
                verdi = "186042",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysnings-inntekts-krav-siste-36-måneder `() {
        vedtakMapper.hentInntektsKravSiste36Måneder() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.inntektskrav-for-siste-36-mnd",
                verdi = "372084",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning-arbeidsinntekt-siste-12-måneder `() {
        vedtakMapper.hentArbeidsinntektSiste12Måneder() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.arbeidsinntekt-siste-12-mnd",
                verdi = "500000",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `Hent opplysning-arbeidsinntekt-siste-36-måneder `() {
        vedtakMapper.hentArbeidsinntektSiste36Måneder() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.arbeidsinntekt-siste-36-mnd",
                verdi = "1700000",
                datatype = HELTALL,
                enhet = KRONER,
            )
    }

    @Test
    fun `skal hente antall G for krav til 12 mnd arbeidsinntekt`() {
        vedtakMapper.hentAntallGForKravTil12MndArbeidsinntekt() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.antall-g-for-krav-til-12-mnd-arbeidsinntekt",
                verdi = "1.5",
                datatype = FLYTTALL,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `skal hente antall G for krav til 36 mnd arbeidsinntekt`() {
        vedtakMapper.hentAntallGForKravTil36MndArbeidsinntekt() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.antall-g-for-krav-til-36-mnd-arbeidsinntekt",
                verdi = "3.0",
                datatype = FLYTTALL,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `skal hente gjennomsnittlig arbeidsinntekt siste 36 måneder`() {
        vedtakMapper.hentGjennomsnittligArbeidsinntektSiste36Måneder() shouldBe
            Opplysning2(
                opplysningTekstId = "opplysning.gjennomsnittlig-arbeidsinntekt-siste-36-maaneder",
                verdi = "614871.2733389170",
                datatype = FLYTTALL,
                enhet = KRONER,
            )
    }
}
