package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private val objectMapper = ObjectMapper()

class SanityMappingTest {
    private fun toJsonNode(jsonString: String): JsonNode {
        return objectMapper.readTree(jsonString)
    }

    @Test
    fun `test av enkel brevblokk mapping med kun en opplysning`() {
        val jsonNode = toJsonNode(insanity)
        val responseDTO = jsonNode.mapJsonToResponseDTO()
        responseDTO.result[0].textId shouldBe "brev.blokk.vedtak-avslag-forskuttert"
        responseDTO.result[0].innhold.behandlingOpplysninger[0].textId shouldBe "Søknadsdato"
        responseDTO.result[0].innhold.behandlingOpplysninger[0].type shouldBe "dato"
    }

    @Test
    fun `test av brevblokk med flere opplysnigner`() {
        val jsonNode = toJsonNode(sanityFlereOpplysninger)
        val responseDTO = jsonNode.mapJsonToResponseDTO()
        responseDTO.result[0].innhold.behandlingOpplysninger.size shouldBe 4
        responseDTO.result[0].textId shouldBe "brev.blokk.begrunnelse-avslag-minsteinntekt"
        responseDTO.result[0].innhold.behandlingOpplysninger[0].textId shouldBe "Arbeidsinntekt siste 12 mnd"
        responseDTO.result[0].innhold.behandlingOpplysninger[1].textId shouldBe "Inntektskrav for siste 12 mnd"
    }

    // language=JSON
    val sanityFlereOpplysninger =
        """
        {
          "query": "*[_type == \"brevBlokk\"]{\n                              textId,\n                              innhold[]{\n                                _type == \"block\" =\u003e {\n                                  children[]{\n                                    _type == \"opplysningReference\" =\u003e {\n                                      \"behandlingOpplysning\": @-\u003e{\n                                        textId, type\n                                      }\n                                    }\n                                  }\n                                }\n                                }[innhold[].children[].behandlingOpplysning != {}]\n                              }",
          "result": [
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
                    }
                  ]
                }
              ]
            }
          ]
        }
        """.trimIndent()

    //language=JSON
    val insanity =
        """
        {
          "query": "*[_type == \"brevBlokk\"]{\n                              textId,\n                              innhold[]{\n                                _type == \"block\" =\u003e {\n                                  children[]{\n                                    _type == \"opplysningReference\" =\u003e {\n                                      \"behandlingOpplysning\": @-\u003e{\n                                        textId, type\n                                      }\n                                    }\n                                  }\n                                }\n                                }[innhold[].children[].behandlingOpplysning != {}]\n                              }",
          "result": [
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
          }
          ]
          }
        """.trimIndent()

    // language=JSON
    val sanityPayload =
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
