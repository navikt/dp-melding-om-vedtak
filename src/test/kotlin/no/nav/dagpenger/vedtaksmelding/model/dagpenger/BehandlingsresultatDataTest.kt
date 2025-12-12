package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.OpplysningDataException
import no.nav.dagpenger.vedtaksmelding.util.readFile
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

@Suppress("ktlint:standard:max-line-length")
class BehandlingsresultatDataTest {
    companion object {
        private val testOpplysningId = UUID.fromString("12345678-1234-1234-1234-123456789012")

        private fun lagBehandlingsresultatData(
            virkningsdato: String,
            periodeFraOgMed: String,
            periodeTilOgMed: String? = null,
            verdi: Int,
        ): BehandlingsresultatData {
            val tilOgMedFelt = if (periodeTilOgMed != null) """"gyldigTilOgMed": "$periodeTilOgMed",""" else ""

            @Language("JSON")
            val json =
                """
                {
                  "behandlingId": "0194b207-d65a-7aa4-9fb1-b22189a404d8",
                  "førteTil": "Innvilgelse",
                  "rettighetsperioder": [
                    {
                      "fraOgMed": "$virkningsdato",
                      "harRett": true,
                      "opprinnelse": "Ny"
                    }
                  ],
                  "opplysninger": [
                    {
                      "opplysningTypeId": "$testOpplysningId",
                      "perioder": [
                        {
                          "gyldigFraOgMed": "$periodeFraOgMed",
                          $tilOgMedFelt
                          "verdi": {
                            "datatype": "heltall",
                            "verdi": $verdi
                          }
                        }
                      ]
                    }
                  ]
                }
                """.trimIndent()
            return BehandlingsresultatData(json)
        }
    }

    @Test
    fun `skal kunne parse vedtak resultat data`() {
        val behandlingsresultatData = BehandlingsresultatData("/json/avslag_resultat.json".readFile())

        behandlingsresultatData.flyttall(DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid.opplysningTypeId) shouldBe 50
        behandlingsresultatData.penger(DagpengerOpplysning.InntektskravSiste12Måneder.opplysningTypeId) shouldBe 186042
        behandlingsresultatData.tekst(DagpengerOpplysning.BruktBeregningsregelGrunnlag.opplysningTypeId) shouldBe
            "Gjennomsnittlig arbeidsinntekt siste 36 måneder"
        behandlingsresultatData.boolsk(DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder.opplysningTypeId) shouldBe true
        behandlingsresultatData.heltall(DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger.opplysningTypeId) shouldBe 0
        behandlingsresultatData.dato(DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode.opplysningTypeId) shouldBe
            LocalDate.of(
                2022,
                1,
                1,
            )
        behandlingsresultatData.virkningsdato() shouldBe LocalDate.of(2025, 1, 29)
    }

    @Test
    fun `Skal hente ut ufall basert på førteTil`() {
        BehandlingsresultatData(tomBehandlingResulstat(førteTil = "Innvilgelse")).utfall() shouldBe Vedtak.Utfall.INNVILGET
        BehandlingsresultatData(tomBehandlingResulstat(førteTil = "Avslag")).utfall() shouldBe Vedtak.Utfall.AVSLÅTT

        shouldThrow<BehandlingsresultatData.UtfallIkkeStøttet> {
            BehandlingsresultatData(tomBehandlingResulstat(førteTil = "Gjenopptak")).utfall()
        }
        shouldThrow<BehandlingsresultatData.UtfallIkkeStøttet> {
            BehandlingsresultatData(tomBehandlingResulstat(førteTil = "Endring")).utfall()
        }
        shouldThrow<BehandlingsresultatData.UtfallIkkeStøttet> {
            BehandlingsresultatData(tomBehandlingResulstat(førteTil = "Stans")).utfall()
        }
    }

    @Test
    fun `Ved innvilgelse er virkningsdato gitt av eldste rettighetsperiode med harRett = true og opprinnelse ny`() {
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
        BehandlingsresultatData(behandlingResultatJson).virkningsdato() shouldBe LocalDate.of(2024, 10, 30)
    }

    @Test
    fun `Ved Avslag er virkningsdato gitt av eldste rettighetsperiode med harRett = false og opprinnelse ny`() {
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
        BehandlingsresultatData(behandlingResultatJson).virkningsdato() shouldBe LocalDate.of(2024, 10, 30)
    }

    @Test
    fun `skal finne opplysning når virkningsdato er lik fraOgMed`() {
        val data =
            lagBehandlingsresultatData(
                virkningsdato = "2025-01-29",
                periodeFraOgMed = "2025-01-29",
                verdi = 42,
            )
        data.heltall(testOpplysningId) shouldBe 42
    }

    @Test
    fun `skal finne opplysning når virkningsdato er innenfor periode med tilOgMed`() {
        val data =
            lagBehandlingsresultatData(
                virkningsdato = "2025-01-29",
                periodeFraOgMed = "2025-01-01",
                periodeTilOgMed = "2025-12-31",
                verdi = 99,
            )
        data.heltall(testOpplysningId) shouldBe 99
    }

    @Test
    fun `skal finne opplysning når virkningsdato er lik tilOgMed`() {
        val data =
            lagBehandlingsresultatData(
                virkningsdato = "2025-12-31",
                periodeFraOgMed = "2025-01-01",
                periodeTilOgMed = "2025-12-31",
                verdi = 77,
            )
        data.heltall(testOpplysningId) shouldBe 77
    }

    @Test
    fun `skal ikke finne opplysning når virkningsdato er før fraOgMed`() {
        val data =
            lagBehandlingsresultatData(
                virkningsdato = "2025-01-29",
                periodeFraOgMed = "2025-02-01",
                periodeTilOgMed = "2025-12-31",
                verdi = 55,
            )
        shouldThrow<BehandlingsresultatData.NyPeriodeIkkeFunnet> {
            data.heltall(testOpplysningId)
        }
    }

    @Test
    fun `skal ikke finne opplysning når virkningsdato er etter tilOgMed`() {
        val data =
            lagBehandlingsresultatData(
                virkningsdato = "2025-12-31",
                periodeFraOgMed = "2025-01-01",
                periodeTilOgMed = "2025-12-30",
                verdi = 33,
            )
        shouldThrow<BehandlingsresultatData.NyPeriodeIkkeFunnet> {
            data.heltall(testOpplysningId)
        }
    }

    @Test
    fun `skal kaste exception når flere perioder inkluderer samme virkningsdato`() {
        @Language("JSON")
        val json =
            """
            {
              "behandlingId": "0194b207-d65a-7aa4-9fb1-b22189a404d8",
              "førteTil": "Innvilgelse",
              "rettighetsperioder": [
                {
                  "fraOgMed": "2025-06-15",
                  "harRett": true,
                  "opprinnelse": "Ny"
                }
              ],
              "opplysninger": [
                {
                  "opplysningTypeId": "$testOpplysningId",
                  "perioder": [
                    {
                      "gyldigFraOgMed": "2025-01-01",
                      "gyldigTilOgMed": "2025-12-31",
                      "verdi": {
                        "datatype": "heltall",
                        "verdi": 100
                      }
                    },
                    {
                      "gyldigFraOgMed": "2025-06-01",
                      "gyldigTilOgMed": "2025-06-30",
                      "verdi": {
                        "datatype": "heltall",
                        "verdi": 200
                      }
                    }
                  ]
                }
              ]
            }
            """.trimIndent()

        val data = BehandlingsresultatData(json)
        shouldThrow<OpplysningDataException> {
            data.heltall(testOpplysningId)
        }
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
