package no.nav.dagpenger.vedtaksmelding.model.avslag

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.Avslag
import no.nav.dagpenger.vedtaksmelding.model.Utfall
import no.nav.dagpenger.vedtaksmelding.model.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.model.Vilkår
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class AvslagUtestengtTest {
    private val avslagUtestengtVedtak = VedtakMapper(json).vedtak()

    @Test
    fun `Riktige brevblokker for avslag utestengt`() {
        val behandlingId = UUIDv7.ny()
        val utestengtVilkår =
            Vilkår(
                navn = "Oppfyller krav til ikke utestengt",
                status = Vilkår.Status.IKKE_OPPFYLT,
            )

        Avslag(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(utestengtVilkår),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                "brev.blokk.vedtak-avslag",
                "brev.blokk.avslag-utestengt",
                "brev.blokk.avslag-utestengt-hjemmel",
            ) + Vedtaksmelding.fasteBlokker
    }

    @Test
    fun `Brevstøtte for avslag utestengt`() {
        shouldNotThrow<Vedtaksmelding.ManglerBrevstøtte> {
            Avslag(avslagUtestengtVedtak, emptyList())
        }
    }
}

//language=JSON
private val json =
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
          "status": "IkkeOppfylt",
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
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-01-21T11:06:58.171429",
          "hjemmel": "folketrygdloven § 4-3"
        },
        {
          "navn": "Krav til tap av arbeidsinntekt og arbeidstid",
          "status": "Oppfylt",
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
      "opplysninger": [],
      "automatisk": false,
      "gjenstående": {
        "kvoter": null
      }
    }
    """.trimIndent()
