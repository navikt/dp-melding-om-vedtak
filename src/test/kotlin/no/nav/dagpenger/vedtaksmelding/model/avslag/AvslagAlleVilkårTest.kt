package no.nav.dagpenger.vedtaksmelding.model.avslag

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.Avslag
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.Vedtaksmelding
import org.junit.jupiter.api.Test

class AvslagAlleVilkårTest {
    private val avslagAlleVilkårVedtak = VedtakMapper(json).vedtak()

    @Test
    fun `Riktige brevblokker i korrekt rekkefølge ved avslag på alle vilkår`() {
        Avslag(
            vedtak = avslagAlleVilkårVedtak,
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                "brev.blokk.vedtak-avslag",
                "brev.blokk.begrunnelse-avslag-minsteinntekt",
                "brev.blokk.avslag-tapt-arbeidsinntekt",
                "brev.blokk.avslag-tapt-arbeidstid",
                "brev.blokk.avslag-utestengt",
                "brev.blokk.avslag-utestengt-hjemmel",
                "brev.blokk.avslag-reell-arbeidssoker-overskrift",
                "brev.blokk.avslag-reell-arbeidssoker-heltid-deltid",
                "brev.blokk.avslag-reell-arbeidssoker-arbeid-i-hele-norge",
                "brev.blokk.avslag-reell-arbeidssoker-unntak-heltid-deltid-hele-norge",
                "brev.blokk.avslag-reell-arbeidssoker-arbeidsfor",
                "brev.blokk.avslag-reell-arbeidssoker-ethvert-arbeid",
                "brev.blokk.avslag-reell-arbeidssoker-registrert-arbeidssoker",
                "brev.blokk.avslag-reell-arbeidssoker-hjemmel",
                "brev.blokk.avslag-opphold-utlandet-del-1",
                "brev.blokk.avslag-opphold-utlandet-del-2",
                "brev.blokk.avslag-andre-fulle-ytelser",
            ) + Vedtaksmelding.fasteBlokker
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
          "navn": "Oppfyller kravet til å ta ethvert arbeid",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-16T09:16:38.778989",
          "hjemmel": "folketrygdloven § 4-5"
        },
        {
          "navn": "Mottar ikke andre fulle ytelser",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-27T12:08:17.919669",
          "hjemmel": "folketrygdloven § 4-24"
        },
        {
          "navn": "Oppfyller kravet til heltid- og deltidsarbeid",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-27T12:08:17.927834",
          "hjemmel": "folketrygdloven § 4-5"
        },
        {
          "navn": "Oppfyller kravet til mobilitet",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-27T12:08:17.927856",
          "hjemmel": "folketrygdloven § 4-5"
        },
        {
          "navn": "Oppfyller kravet til å være arbeidsfør",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-27T12:08:17.927877",
          "hjemmel": "folketrygdloven § 4-5"
        },
        {
          "navn": "Krav til tap av arbeidsinntekt",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-27T12:08:17.928029",
          "hjemmel": "folketrygdloven § 4-3"
        },
        {
          "navn": "Oppfyller krav til ikke utestengt",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-27T12:08:17.929454",
          "hjemmel": "folketrygdloven § 4-28"
        },
        {
          "navn": "Oppfyller kravet til opphold i Norge",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-27T12:08:17.933163",
          "hjemmel": "folketrygdloven § 4-5"
        },
        {
          "navn": "Registrert som arbeidssøker på søknadstidspunktet",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-29T10:13:36.435915",
          "hjemmel": "folketrygdloven § 4-5"
        },
        {
          "navn": "Krav til arbeidssøker",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-29T10:13:36.458124",
          "hjemmel": "folketrygdloven § 4-5"
        },
        {
          "navn": "Oppfyller kravet til minsteinntekt eller verneplikt",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-29T10:13:36.505549",
          "hjemmel": "folketrygdloven § 4-4"
        },
        {
          "navn": "Tap av arbeidstid er minst terskel",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-29T10:13:36.580173",
          "hjemmel": "folketrygdloven § 4-3"
        },
        {
          "navn": "Krav til tap av arbeidsinntekt og arbeidstid",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-29T10:13:36.592332",
          "hjemmel": "folketrygdloven § 4-3"
        }
      ],
      "fastsatt": {
        "utfall": false,
        "status": null,
        "grunnlag": null,
        "fastsattVanligArbeidstid": {
          "vanligArbeidstidPerUke": 37.5,
          "nyArbeidstidPerUke": 0,
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

// Disse vilkårene støttes ikke i brevløsningen ennå
//    {
//        "navn": "Oppfyller kravet til medlemskap",
//        "status": "Oppfylt",
//        "vurderingstidspunkt": "2025-01-27T12:08:17.927809",
//        "hjemmel": "folketrygdloven § 4-2"
//    },
//    {
//        "navn": "Krav til utdanning eller opplæring",
//        "status": "IkkeOppfylt",
//        "vurderingstidspunkt": "2025-01-27T12:08:17.941459",
//        "hjemmel": "folketrygdloven § 4-6"
//    },
//    {
//        "navn": "Oppfyller kravet til alder",
//        "status": "IkkeOppfylt",
//        "vurderingstidspunkt": "2025-01-27T12:08:17.941134",
//        "hjemmel": "folketrygdloven § 4-23"
//    },
//    {
//        "navn": "Er medlemmet ikke påvirket av streik eller lock-out?",
//        "status": "IkkeOppfylt",
//        "vurderingstidspunkt": "2025-01-27T12:08:17.927976",
//        "hjemmel": "folketrygdloven § 4-22"
//    },
