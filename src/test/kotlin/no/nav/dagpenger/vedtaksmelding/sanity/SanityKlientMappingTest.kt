package no.nav.dagpenger.vedtaksmelding.sanity

import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.vedtaksmelding.lagHttpKlient
import org.junit.jupiter.api.Test

class SanityKlientMappingTest {
    @Test
    fun `test av enkel brevblokk mapping med kun en opplysning`() {
        runBlocking {
            SanityKlient(
                sanityUrl = "http://locahost/sanity", httpKlient = lagHttpKlient(engine = lageMockEngine()),
            ).hentOpplysningTekstIder(listOf("brev.blokk.vedtak-avslag-forskuttert")) shouldBe listOf("Søknadsdato")
        }
    }

    @Test
    fun `test av brevblokk med flere opplysnigner`() {
        runBlocking {
            SanityKlient(
                sanityUrl = "http://locahost/sanity", httpKlient = lagHttpKlient(engine = lageMockEngine()),
            ).hentOpplysningTekstIder(listOf("brev.blokk.begrunnelse-avslag-minsteinntekt")) shouldBe
                listOf(
                    "Arbeidsinntekt siste 12 mnd",
                    "Inntektskrav for siste 12 mnd",
                    "Arbeidsinntekt siste 36 mnd",
                    "Inntektskrav for siste 36 mnd",
                )
        }
    }

    @Test
    fun `test av brevblokk uten opplysninger`() {
        runBlocking {
            SanityKlient(
                sanityUrl = "http://locahost/sanity", httpKlient = lagHttpKlient(engine = lageMockEngine()),
            ).hentOpplysningTekstIder(listOf("brev.blokk.vedtak-innvilget")) shouldBe emptyList()
        }
    }

    private fun lageMockEngine(payload: String = sanityPayload): MockEngine {
        return MockEngine { request ->
            respond(sanityPayload)
        }
    }

    // language=JSON
    private val sanityPayload =
        """
        {
          "query": "*[_type == \"brevBlokk\"]{\n                              textId,\n                              innhold[]{\n                                _type == \"block\" =\u003e {\n                                  children[]{\n                                    _type == \"opplysningReference\" =\u003e {\n                                      \"behandlingOpplysning\": @-\u003e{\n                                        textId, type\n                                      }\n                                    }\n                                  }\n                                }\n                                }[innhold[].children[].behandlingOpplysning != {}]\n                              }",
          "result": [
            {
              "textId": "brev.blokk.rett-til-innsyn",
              "innhold": [
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                }
              ]
            },
            {
              "textId": "brev.blokk.vedtak-innvilget",
              "innhold": [
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                }
              ]
            },
            {
              "textId": "brev.blokk.vedtak-avslag-forskuttert",
              "innhold": [
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {},
                    {
                      "behandlingOpplysning": {
                        "textId": "Søknadsdato",
                        "type": "dato"
                      }
                    },
                    {}
                  ]
                }
              ]
            },
            {
              "textId": "brev.blokk.vedtak-avslag",
              "innhold": [
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {},
                    {
                      "behandlingOpplysning": {
                        "textId": "Søknadsdato",
                        "type": "dato"
                      }
                    },
                    {}
                  ]
                }
              ]
            },
            {
              "textId": "brev.blokk.vedlegg-innvilget-orientering",
              "innhold": [
                {
                  "children": [
                    {}
                  ]
                }
              ]
            },
            {
              "textId": "brev.blokk.vedlegg-innvilget-plikter-og-rettigheter",
              "innhold": [
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                }
              ]
            },
            {
              "textId": "brev.blokk.dine-rettigheter",
              "innhold": [
                {
                  "children": [
                    {}
                  ]
                }
              ]
            },
            {
              "textId": "brev.blokk.begrunnelse-avslag-minsteinntekt",
              "innhold": [
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {},
                    {
                      "behandlingOpplysning": {
                        "textId": "Arbeidsinntekt siste 12 mnd",
                        "type": "penger"
                      }
                    },
                    {},
                    {
                      "behandlingOpplysning": {
                        "textId": "Inntektskrav for siste 12 mnd",
                        "type": "penger"
                      }
                    },
                    {}
                  ]
                },
                {
                  "children": [
                    {},
                    {
                      "behandlingOpplysning": {
                        "textId": "Arbeidsinntekt siste 36 mnd",
                        "type": "penger"
                      }
                    },
                    {},
                    {
                      "behandlingOpplysning": {
                        "textId": "Inntektskrav for siste 36 mnd",
                        "type": "penger"
                      }
                    },
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                }
              ]
            },
            {
              "textId": "brev.blokk.sporsmaal",
              "innhold": [
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                }
              ]
            },
            {
              "textId": "brev.blokk.vedlegg-avslag-orientering",
              "innhold": [
                {
                  "children": [
                    {}
                  ]
                }
              ]
            },
            {
              "textId": "brev.blokk.rett-til-aa-klage",
              "innhold": [
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                }
              ]
            },
            {
              "textId": "brev.blokk.vedlegg-veiledning-fra-nav",
              "innhold": [
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                }
              ]
            },
            {
              "textId": "brev.blokk.vedlegg-avslag-klage",
              "innhold": [
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                },
                {
                  "children": [
                    {}
                  ]
                }
              ]
            }
          ],
          "syncTags": [
            "s1:yDKmsw",
            "s1:0/KP/w",
            "s1:QrHnGA",
            "s1:4NhEjA",
            "s1:3kgPGA",
            "s1:RzYwmw"
          ],
          "ms": 20
        }

        """.trimIndent()
}
