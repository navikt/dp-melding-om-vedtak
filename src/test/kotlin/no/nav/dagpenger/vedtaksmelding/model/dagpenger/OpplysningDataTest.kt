package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.util.readFile
import org.junit.jupiter.api.Test
import java.time.LocalDate

@Suppress("ktlint:standard:max-line-length")
class OpplysningDataTest {
    @Test
    fun `skal kunne parse vedtak resultat data`() {
        val opplysningData = OpplysningData("/json/avslag_resultat.json".readFile())

        opplysningData.flyttall(DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid.opplysningTypeId) shouldBe 50
        opplysningData.penger(DagpengerOpplysning.InntektskravSiste12Måneder.opplysningTypeId) shouldBe 186042
        opplysningData.tekst(DagpengerOpplysning.BruktBeregningsregelGrunnlag.opplysningTypeId) shouldBe "Gjennomsnittlig arbeidsinntekt siste 36 måneder"
        opplysningData.boolsk(DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder.opplysningTypeId) shouldBe true
        opplysningData.heltall(DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger.opplysningTypeId) shouldBe 0
        opplysningData.dato(DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode.opplysningTypeId) shouldBe
            LocalDate.of(
                2022,
                1,
                1,
            )
        opplysningData.provingsDato() shouldBe LocalDate.of(2025, 1, 29)
    }
}
