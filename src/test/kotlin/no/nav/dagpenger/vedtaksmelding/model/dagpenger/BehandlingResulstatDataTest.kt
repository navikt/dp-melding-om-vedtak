package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.util.readFile
import org.junit.jupiter.api.Test
import java.time.LocalDate

@Suppress("ktlint:standard:max-line-length")
class BehandlingResulstatDataTest {
    @Test
    fun `skal kunne parse vedtak resultat data`() {
        val behandlingResultatData = BehandlingResultatData("/json/perioder.json".readFile())

        behandlingResultatData.flyttall(DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid.opplysningTypeId) shouldBe 50
        behandlingResultatData.penger(DagpengerOpplysning.InntektskravSiste12Måneder.opplysningTypeId) shouldBe 195240
        behandlingResultatData.tekst(DagpengerOpplysning.BruktBeregningsregelGrunnlag.opplysningTypeId) shouldBe "Inntekt etter avkortning og oppjustering siste 12 måneder"
        behandlingResultatData.boolsk(DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder.opplysningTypeId) shouldBe true
        behandlingResultatData.heltall(DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger.opplysningTypeId) shouldBe 52
        behandlingResultatData.dato(DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode.opplysningTypeId) shouldBe
            LocalDate.of(
                2022,
                8,
                1,
            )

        behandlingResultatData.perioder<Double>(DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid.opplysningTypeId)
            .single().verdi shouldBe 50
        behandlingResultatData.perioder<Number>(DagpengerOpplysning.InntektskravSiste12Måneder.opplysningTypeId)
            .single().verdi shouldBe 195240
        behandlingResultatData.perioder<String>(DagpengerOpplysning.BruktBeregningsregelGrunnlag.opplysningTypeId)
            .single().verdi shouldBe "Inntekt etter avkortning og oppjustering siste 12 måneder"

        behandlingResultatData.perioder<Boolean>(DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder.opplysningTypeId)
            .single().verdi shouldBe true

        behandlingResultatData.perioder<Int>(DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger.opplysningTypeId)
            .single().verdi shouldBe 52

        behandlingResultatData.perioder<LocalDate>(DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode.opplysningTypeId)
            .single().verdi shouldBe LocalDate.of(2022, 8, 1)
    }
}
