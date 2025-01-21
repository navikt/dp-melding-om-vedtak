package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.UKER
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class AvslagArbeidstidTest {
    private val avslagArbeidstidVedtak = VedtakMapper(json).vedtak()

    @Test
    @Disabled
    fun `Hent relevate opplysninger ved avslag tapt arbeidstid`() {
        avslagArbeidstidVedtak.finnOpplysning("opplysning.er-innvilget-med-verneplikt") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.er-innvilget-med-verneplikt",
                verdi = true.toString(),
                datatype = BOOLSK,
            )
        avslagArbeidstidVedtak.finnOpplysning("opplysning.antall-stonadsuker") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.antall-stonadsuker",
                verdi = "26",
                datatype = HELTALL,
                enhet = UKER,
            )
        avslagArbeidstidVedtak.finnOpplysning("opplysning.antall-stonadsuker-som-gis-ved-ordinare-dagpenger") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.antall-stonadsuker-som-gis-ved-ordinare-dagpenger",
                verdi = "104",
                datatype = HELTALL,
                enhet = UKER,
            )
    }

    @Test
    fun `Brevstøtte for avslag grunnet for lite tapt arbeidstid`() {
        shouldNotThrow<Vedtaksmelding.ManglerBrevstøtte> {
            Avslag(avslagArbeidstidVedtak, mockk())
        }
    }
}

//language=JSON
val json =
    """
    {
      "behandlingId": "01948850-dda4-7221-a1f6-4ecab5f70013",
      "fagsakId": "15257652",
      "søknadId": "485173e8-2bd4-4d90-af9c-5d01aee1fa4a",
      "ident": "17826297034",
      "vedtakstidspunkt": "2025-01-21T11:08:59.296917635",
      "virkningsdato": "2025-01-21",
      "behandletAv": [],
      "vilkår": [
        {
          "navn": "Rettighetstype",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:09.002217",
          "hjemmel": "folketrygdloven kapittel 4"
        },
        {
          "navn": "Oppfyller kravet til alder",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:09.002468",
          "hjemmel": "folketrygdloven § 4-23"
        },
        {
          "navn": "Registrert som arbeidssøker på søknadstidspunktet",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.06828",
          "hjemmel": "folketrygdloven § 4-5"
        },
        {
          "navn": "Oppfyller kravet til minsteinntekt eller verneplikt",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.070006",
          "hjemmel": "folketrygdloven § 4-4"
        },
        {
          "navn": "Mottar ikke andre fulle ytelser",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.071",
          "hjemmel": "folketrygdloven § 4-24"
        },
        {
          "navn": "Oppfyller kravet til medlemskap",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.073808",
          "hjemmel": "folketrygdloven § 4-2"
        },
        {
          "navn": "Er medlemmet ikke påvirket av streik eller lock-out?",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.073824",
          "hjemmel": "folketrygdloven § 4-22"
        },
        {
          "navn": "Oppfyller krav til ikke utestengt",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.076481",
          "hjemmel": "folketrygdloven § 4-28"
        },
        {
          "navn": "Oppfyller kravet til opphold i Norge",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.079514",
          "hjemmel": "folketrygdloven § 4-5"
        },
        {
          "navn": "Krav til tap av arbeidsinntekt",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.079531",
          "hjemmel": "folketrygdloven § 4-3"
        },
        {
          "navn": "Oppfyller kravet til heltid- og deltidsarbeid",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.645389",
          "hjemmel": "folketrygdloven § 4-5"
        },
        {
          "navn": "Oppfyller kravet til mobilitet",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.645404",
          "hjemmel": "folketrygdloven § 4-5"
        },
        {
          "navn": "Oppfyller kravet til å være arbeidsfør",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.645413",
          "hjemmel": "folketrygdloven § 4-5"
        },
        {
          "navn": "Oppfyller kravet til å ta ethvert arbeid",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.645422",
          "hjemmel": "folketrygdloven § 4-5"
        },
        {
          "navn": "Krav til arbeidssøker",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.648061",
          "hjemmel": "folketrygdloven § 4-5"
        },
        {
          "navn": "Krav til utdanning eller opplæring",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.650392",
          "hjemmel": "folketrygdloven § 4-6"
        },
        {
          "navn": "Tap av arbeidstid er minst terskel",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-21T11:06:58.171429",
          "hjemmel": "folketrygdloven § 4-3"
        },
        {
          "navn": "Krav til tap av arbeidsinntekt og arbeidstid",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-21T11:06:58.212861",
          "hjemmel": "folketrygdloven § 4-3"
        }
      ],
      "fastsatt": {
        "utfall": false,
        "status": null,
        "grunnlag": null,
        "fastsattVanligArbeidstid": {
          "vanligArbeidstidPerUke": 37.5,
          "nyArbeidstidPerUke": 20,
          "begrunnelse": null
        },
        "sats": null,
        "samordning": [],
        "kvoter": null
      },
      "utbetalinger": [],
      "opplysninger": [
        {
          "id": "01948854-5e53-7bc0-81eb-5e6019c903e1",
          "navn": "Ny arbeidstid per uke",
          "verdi": "20.0",
          "status": "Faktum",
          "datatype": "desimaltall",
          "redigerbar": true,
          "synlig": true,
          "formål": "Regel",
          "gyldigFraOgMed": null,
          "gyldigTilOgMed": null,
          "kilde": {
            "type": "Saksbehandler",
            "registrert": "2025-01-21T11:06:57.90184",
            "ident": "Z994251",
            "meldingId": null
          },
          "utledetAv": null
        },
        {
          "id": "01948850-e99d-7037-9851-8b15c4b2ed28",
          "navn": "Fastsatt arbeidstid per uke før tap",
          "verdi": "37.5",
          "status": "Faktum",
          "datatype": "desimaltall",
          "redigerbar": false,
          "synlig": true,
          "formål": "Regel",
          "gyldigFraOgMed": null,
          "gyldigTilOgMed": null,
          "kilde": null,
          "utledetAv": {
            "regel": {
              "navn": "MinstAv"
            },
            "opplysninger": [
              "01948850-e75f-710b-8b4e-527662639a13",
              "01948850-e764-7663-8f83-0bf956b83249",
              "01948850-e77c-7209-b7e3-724d67f67801",
              "01948850-e99a-7a48-b1b5-f67e5c03159b"
            ]
          }
        },
        {
          "id": "01948850-e75f-710b-8b4e-527662639a27",
          "navn": "Krav til prosentvis tap av arbeidstid",
          "verdi": "50.0",
          "status": "Faktum",
          "datatype": "desimaltall",
          "redigerbar": false,
          "synlig": true,
          "formål": "Regel",
          "gyldigFraOgMed": null,
          "gyldigTilOgMed": null,
          "kilde": null,
          "utledetAv": {
            "regel": {
              "navn": "Oppslag"
            },
            "opplysninger": [
              "01948850-df4a-79b7-9bd4-f81f6e629ec1"
            ]
          }
        }
      ],
      "automatisk": false,
      "gjenstående": {
        "kvoter": null
      }
    }
    """.trimIndent()
