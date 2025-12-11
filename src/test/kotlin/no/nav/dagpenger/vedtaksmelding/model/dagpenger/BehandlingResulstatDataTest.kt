package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.util.readFile
import org.intellij.lang.annotations.Language
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

    @Test
    fun `Skal hente ut ufall basert på førteTil`() {
        BehandlingResultatData(tomBehandlingResulstat(førteTil = "Innvilgelse")).utfall() shouldBe Vedtak.Utfall.INNVILGET
        BehandlingResultatData(tomBehandlingResulstat(førteTil = "Avslag")).utfall() shouldBe Vedtak.Utfall.AVSLÅTT

        shouldThrow<BehandlingResultatData.UtfallIkkeStøttet> {
            BehandlingResultatData(tomBehandlingResulstat(førteTil = "Gjenopptak")).utfall()
        }
        shouldThrow<BehandlingResultatData.UtfallIkkeStøttet> {
            BehandlingResultatData(tomBehandlingResulstat(førteTil = "Endring")).utfall()
        }
        shouldThrow<BehandlingResultatData.UtfallIkkeStøttet> {
            BehandlingResultatData(tomBehandlingResulstat(førteTil = "Stans")).utfall()
        }
    }

    @Test
    fun `Ved innvilgelse er prøvingsdato gitt av eldste rettighetsperiode med harRett = true og opprinnelse ny`() {
        val behandlingResultatJson =
            """
            {
              "opplysninger": [],
              "rettighetsperioder": [
                {
                  "fraOgMed": "2023-10-30",
                  "tilOgMed": "2023-11-03",
                  "harRett": false,
                  "opprinnelse": "Ny"
                },
                {
                  "fraOgMed": "2025-10-30",
                  "tilOgMed": "2025-11-03",
                  "harRett": true,
                  "opprinnelse": "Ny"
                },
                {
                  "fraOgMed": "2024-10-30",
                  "tilOgMed": "2024-11-03",
                  "harRett": true,
                  "opprinnelse": "Ny"
                },
                {
                  "fraOgMed": "2023-10-30",
                  "tilOgMed": "2023-11-03",
                  "harRett": true,
                  "opprinnelse": "Arvet"
                }
              ],
              "førteTil": "Innvilgelse"
            }
            """.trimIndent()
        BehandlingResultatData(behandlingResultatJson).provingsDato() shouldBe LocalDate.of(2024, 10, 30)
    }

    @Test
    fun `Ved Avslag er prøvingsdato gitt av eldste rettighetsperiode med harRett = false og opprinnelse ny`() {
        val behandlingResultatJson =
            """
            {
              "opplysninger": [],
              "rettighetsperioder": [
                {
                  "fraOgMed": "2023-10-30",
                  "tilOgMed": "2023-11-03",
                  "harRett": true,
                  "opprinnelse": "Ny"
                },
                {
                  "fraOgMed": "2025-10-30",
                  "tilOgMed": "2025-11-03",
                  "harRett": false,
                  "opprinnelse": "Ny"
                },
                {
                  "fraOgMed": "2024-10-30",
                  "tilOgMed": "2024-11-03",
                  "harRett": false,
                  "opprinnelse": "Ny"
                },
                {
                  "fraOgMed": "2023-10-30",
                  "tilOgMed": "2023-11-03",
                  "harRett": false,
                  "opprinnelse": "Arvet"
                }
              ],
              "førteTil": "Avslag"
            }
            """.trimIndent()
        BehandlingResultatData(behandlingResultatJson).provingsDato() shouldBe LocalDate.of(2024, 10, 30)
    }

    private fun tomBehandlingResulstat(førteTil: String): String {
        @Language("JSON")
        return """
            {
            
              "opplysninger": [],
              "rettighetsperioder": [
                {
                  "fraOgMed": "2025-10-30",
                  "tilOgMed": "2025-11-03",
                  "harRett": true,
                  "opprinnelse": "Ny"
                }
              ],
              "førteTil": "$førteTil"
            }
            """.trimIndent()
    }
}
