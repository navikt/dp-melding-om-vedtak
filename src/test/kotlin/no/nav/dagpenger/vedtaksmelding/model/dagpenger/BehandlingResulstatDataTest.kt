package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.util.readFile
import org.junit.jupiter.api.Test
import java.time.LocalDate

@Suppress("ktlint:standard:max-line-length")
class BehandlingResulstatDataTest {
    @Test
    fun `skal kunne parse vedtak resultat data`() {
        val behandlingResultatData = BehandlingResultatData("/json/avslag_resultat.json".readFile())

        behandlingResultatData.flyttall(DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid.opplysningTypeId) shouldBe 50
        behandlingResultatData.penger(DagpengerOpplysning.InntektskravSiste12Måneder.opplysningTypeId) shouldBe 186042
        behandlingResultatData.tekst(DagpengerOpplysning.BruktBeregningsregelGrunnlag.opplysningTypeId) shouldBe
            "Gjennomsnittlig arbeidsinntekt siste 36 måneder"
        behandlingResultatData.boolsk(DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder.opplysningTypeId) shouldBe true
        behandlingResultatData.heltall(DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger.opplysningTypeId) shouldBe 0
        behandlingResultatData.dato(DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode.opplysningTypeId) shouldBe
            LocalDate.of(
                2022,
                1,
                1,
            )
        behandlingResultatData.provingsDato() shouldBe LocalDate.of(2025, 1, 29)
    }
}
