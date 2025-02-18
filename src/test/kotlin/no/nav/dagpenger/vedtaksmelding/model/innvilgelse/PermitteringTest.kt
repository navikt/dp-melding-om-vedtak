package no.nav.dagpenger.vedtaksmelding.model.innvilgelse

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.AntallPermitteringsuker
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.AntallStønadsuker
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.UKER
import org.junit.jupiter.api.Test

class PermitteringTest {
    private val innvilgelsePermittering = VedtakMapper(json).vedtak()

    @Test
    fun `Skal hente aktuelle opplysninger for innvilgelse som permittert`() {
        innvilgelsePermittering.finnOpplysning(AntallPermitteringsuker.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = AntallPermitteringsuker.opplysningTekstId,
                råVerdi = "26",
                datatype = HELTALL,
                enhet = UKER,
            )

        innvilgelsePermittering.finnOpplysning(AntallStønadsuker.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = AntallStønadsuker.opplysningTekstId,
                råVerdi = "52",
                datatype = HELTALL,
                enhet = UKER,
            )
    }
}

// language = JSON
private val json =
    """
    {
      "behandlingId": "019513c1-2383-7f80-9718-5857e7a3745d",
      "fagsakId": "15281322",
      "søknadId": "cc739146-d305-42a0-b5c3-1eb104c63ee4",
      "ident": "31838297784",
      "vedtakstidspunkt": "2025-02-17T13:16:27.593541004",
      "virkningsdato": "2025-02-17",
      "behandletAv": [],
      "vilkår": [
        {
          "navn": "Oppfyller kravet til alder",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:00.160039",
          "hjemmel": "§ 4-23. Bortfall på grunn av alder"
        },
        {
          "navn": "Mottar ikke andre fulle ytelser",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:05.06962",
          "hjemmel": "§ 4-24. Medlem som har fulle ytelser etter folketrygdloven eller avtalefestet pensjon"
        },
        {
          "navn": "Oppfyller kravet til minsteinntekt",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:05.068268",
          "hjemmel": "§ 4-4. Krav til minsteinntekt"
        },
        {
          "navn": "Oppfyller kravet til opphold i Norge eller unntak",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:05.07656",
          "hjemmel": "§ 4-2. Opphold i Norge"
        },
        {
          "navn": "Oppfyller kravet til medlemskap",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:05.076616",
          "hjemmel": "§ 4-2. Opphold i Norge"
        },
        {
          "navn": "Oppfyller kravet til opphold i Norge",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:05.082907",
          "hjemmel": "§ 4-2. Opphold i Norge"
        },
        {
          "navn": "Oppfyller kravet til heltid- og deltidsarbeid",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:05.060707",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Oppfyller kravet til mobilitet",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:05.06073",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Oppfyller kravet til å være arbeidsfør",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:05.060752",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Oppfyller kravet til å ta ethvert arbeid",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:05.060771",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Registrert som arbeidssøker på søknadstidspunktet",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:05.060798",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Krav til arbeidssøker",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:05.06676",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Er medlemmet ikke påvirket av streik eller lock-out?",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:05.079283",
          "hjemmel": "§ 4-22. Bortfall ved streik og lock-out"
        },
        {
          "navn": "Krav til tap av arbeidsinntekt",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:05.084341",
          "hjemmel": "§ 4-3. Krav til tap av arbeidsinntekt og arbeidstid"
        },
        {
          "navn": "Tap av arbeidstid er minst terskel",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:05.113193",
          "hjemmel": "§ 4-3. Krav til tap av arbeidsinntekt og arbeidstid"
        },
        {
          "navn": "Krav til tap av arbeidsinntekt og arbeidstid",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:05.115236",
          "hjemmel": "§ 4-3. Krav til tap av arbeidsinntekt og arbeidstid"
        },
        {
          "navn": "Krav til utdanning eller opplæring",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:06.792511",
          "hjemmel": "§ 4-6. Dagpenger under utdanning, opplæring, etablering av egen virksomhet m.v"
        },
        {
          "navn": "Oppfyller krav til ikke utestengt",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:05.079822",
          "hjemmel": "§ 4-28. Utestengning"
        },
        {
          "navn": "Oppfyller kravet til permittering",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-17T12:53:00.173115",
          "hjemmel": "§ 4-7. Dagpenger til permitterte"
        }
      ],
      "fastsatt": {
        "utfall": true,
        "status": null,
        "grunnlag": {
          "grunnlag": 207016,
          "begrunnelse": null
        },
        "fastsattVanligArbeidstid": {
          "vanligArbeidstidPerUke": 37.5,
          "nyArbeidstidPerUke": 0,
          "begrunnelse": null
        },
        "sats": {
          "dagsatsMedBarnetillegg": 497,
          "dagsats": null,
          "begrunnelse": null,
          "barn": []
        },
        "samordning": [],
        "kvoter": [
          {
            "navn": "Dagpengeperiode",
            "type": "uker",
            "verdi": 52
          },
          {
            "navn": "Egenandel",
            "type": "beløp",
            "verdi": 1491
          },
          {
            "navn": "Permitteringsperiode",
            "type": "uker",
            "verdi": 26
          }
        ]
      },
      "utbetalinger": [],
      "opplysninger": [
        
      ],
      "automatisk": true,
      "gjenstående": {
        "kvoter": null
      }
    }
    """.trimIndent()
