package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

private val objectMapper = ObjectMapper()

class SanityMappingTest {
    private fun toJsonNode(jsonString: String): JsonNode {
        return objectMapper.readTree(jsonString)
    }

    @Test
    @Disabled
    fun `test av enkel brevblokk mapping`() {
        val jsonNode = toJsonNode(insanity)
        val responseDTO = mapJsonToResponseDTO(jsonNode)
        responseDTO.result[0].textId shouldBe "brev.blokk.vedtak-avslag-forskuttert"
        responseDTO.result[0].innhold.size shouldBe 1
        responseDTO.result[0].innhold[0].children[0].textId shouldBe "Søknadsdato"
        responseDTO.result[0].innhold[0].children[0].type shouldBe "dato"
    }

    fun mapJsonToResponseDTO(jsonNode: JsonNode): ResponseDTO {
        val result =
            jsonNode["result"].map { brevBlokkNode ->
                val textId = brevBlokkNode["textId"].asText()
                val innhold =
                    brevBlokkNode["innhold"].map { innholdNode ->
                        val children =
                            innholdNode["children"].mapNotNull { childNode ->
                                val node = childNode["behandlingOpplysning"]
                                if (node != null) {
                                    node.let { behandlingOpplysningNode ->
                                        BehandlingOpplysningDTO(
                                            textId = behandlingOpplysningNode["textId"].asText(),
                                            type = behandlingOpplysningNode["type"].asText(),
                                        )
                                    }
                                } else {
                                    null
                                }
                            }
                        BrevBlokkDTO.InnholdDTO(children = children)
                    }
                BrevBlokkDTO(
                    textId = textId,
                    innhold = innhold,
                )
            }
        return ResponseDTO(result = result)
    }

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
