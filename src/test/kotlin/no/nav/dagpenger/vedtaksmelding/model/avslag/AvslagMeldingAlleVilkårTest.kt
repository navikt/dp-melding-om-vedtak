package no.nav.dagpenger.vedtaksmelding.model.avslag

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_ALDER
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_ANDRE_FULLE_YTELSER
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING_PERMITTERT
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_MEDLEMSKAP_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_MEDLEMSKAP_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_MINSTEINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_OPPHOLD_UTLAND_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_OPPHOLD_UTLAND_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_PERMITTERT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_PERMITTERT_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_REGISTRERT_ARBEIDSSOKER
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_STREIK_LOCKOUT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_STREIK_LOCKOUT_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_UTDANNING
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_UTESTENGT
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_UTESTENGT_HJEMMEL
import org.junit.jupiter.api.Test

class AvslagMeldingAlleVilkårTest {
    private val avslagAlleVilkårVedtakOrdinær = VedtakMapper(jsonOrdinær).vedtak()
    private val avslagAlleVilkårVedtakPermittering = VedtakMapper(jsonPermittering).vedtak()
    private val avslagAlleVilkårVedtakPermitteringFisk = VedtakMapper(jsonPermitteringFisk).vedtak()

    @Test
    fun `Riktige brevblokker i korrekt rekkefølge ved avslag på alle vilkår ved ordinære dagpenger`() {
        AvslagMelding(
            vedtak = avslagAlleVilkårVedtakOrdinær,
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_MINSTEINNTEKT.brevblokkId,
                AVSLAG_ALDER.brevblokkId,
                AVSLAG_TAPT_ARBEIDSINNTEKT.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID.brevblokkId,
                AVSLAG_MEDLEMSKAP_DEL_1.brevblokkId,
                AVSLAG_MEDLEMSKAP_DEL_2.brevblokkId,
                AVSLAG_UTESTENGT.brevblokkId,
                AVSLAG_UTESTENGT_HJEMMEL.brevblokkId,
                AVSLAG_UTDANNING.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_REGISTRERT_ARBEIDSSOKER.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId,
                AVSLAG_OPPHOLD_UTLAND_DEL_1.brevblokkId,
                AVSLAG_OPPHOLD_UTLAND_DEL_2.brevblokkId,
                AVSLAG_ANDRE_FULLE_YTELSER.brevblokkId,
                AVSLAG_STREIK_LOCKOUT_DEL_1.brevblokkId,
                AVSLAG_STREIK_LOCKOUT_DEL_2.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker i korrekt rekkefølge ved avslag på alle vilkår ved permittering`() {
        AvslagMelding(
            vedtak = avslagAlleVilkårVedtakPermittering,
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING_PERMITTERT.brevblokkId,
                AVSLAG_PERMITTERT_DEL_1.brevblokkId,
                AVSLAG_PERMITTERT_DEL_2.brevblokkId,
                AVSLAG_MINSTEINNTEKT.brevblokkId,
                AVSLAG_ALDER.brevblokkId,
                AVSLAG_TAPT_ARBEIDSINNTEKT.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID.brevblokkId,
                AVSLAG_MEDLEMSKAP_DEL_1.brevblokkId,
                AVSLAG_MEDLEMSKAP_DEL_2.brevblokkId,
                AVSLAG_UTESTENGT.brevblokkId,
                AVSLAG_UTESTENGT_HJEMMEL.brevblokkId,
                AVSLAG_UTDANNING.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_REGISTRERT_ARBEIDSSOKER.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId,
                AVSLAG_OPPHOLD_UTLAND_DEL_1.brevblokkId,
                AVSLAG_OPPHOLD_UTLAND_DEL_2.brevblokkId,
                AVSLAG_ANDRE_FULLE_YTELSER.brevblokkId,
                AVSLAG_STREIK_LOCKOUT_DEL_1.brevblokkId,
                AVSLAG_STREIK_LOCKOUT_DEL_2.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker i korrekt rekkefølge ved avslag på alle vilkår ved permittering fra fiskeindustri`() {
        AvslagMelding(
            vedtak = avslagAlleVilkårVedtakPermitteringFisk,
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING_PERMITTERT.brevblokkId,
                AVSLAG_PERMITTERT_DEL_1.brevblokkId,
                AVSLAG_PERMITTERT_DEL_2.brevblokkId,
                AVSLAG_MINSTEINNTEKT.brevblokkId,
                AVSLAG_ALDER.brevblokkId,
                AVSLAG_TAPT_ARBEIDSINNTEKT.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID.brevblokkId,
                AVSLAG_MEDLEMSKAP_DEL_1.brevblokkId,
                AVSLAG_MEDLEMSKAP_DEL_2.brevblokkId,
                AVSLAG_UTESTENGT.brevblokkId,
                AVSLAG_UTESTENGT_HJEMMEL.brevblokkId,
                AVSLAG_UTDANNING.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_REGISTRERT_ARBEIDSSOKER.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId,
                AVSLAG_OPPHOLD_UTLAND_DEL_1.brevblokkId,
                AVSLAG_OPPHOLD_UTLAND_DEL_2.brevblokkId,
                AVSLAG_ANDRE_FULLE_YTELSER.brevblokkId,
                AVSLAG_STREIK_LOCKOUT_DEL_1.brevblokkId,
                AVSLAG_STREIK_LOCKOUT_DEL_2.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }
}

//language=JSON
private val jsonOrdinær =
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
          "navn": "Oppfyller kravet til alder",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:37:49.167239",
          "hjemmel": "§ 4-23. Bortfall på grunn av alder"
        },
        {
          "navn": "Er omfattet av trygdelovgivningen i Norge",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-20T09:45:19.72993",
          "hjemmel": "§ 4-1. Forholdet til bestemmelser om internasjonal trygdekoordinering"
        },
        {
          "navn": "Oppfyller kravet til minsteinntekt",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:37:50.531663",
          "hjemmel": "§ 4-4. Krav til minsteinntekt"
        },
        {
          "navn": "Oppfyller kravet til verneplikt",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:45:35.975779",
          "hjemmel": "§ 4-19. Dagpenger etter avtjent verneplikt"
        },
        {
          "navn": "Mottar ikke andre fulle ytelser",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:55:56.627729",
          "hjemmel": "§ 4-24. Medlem som har fulle ytelser etter folketrygdloven eller avtalefestet pensjon"
        },
        {
          "navn": "Oppfyller kravet til opphold i Norge eller unntak",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:30:34.020535",
          "hjemmel": "§ 4-2. Opphold i Norge"
        },
        {
          "navn": "Oppfyller kravet til medlemskap",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:31:20.587784",
          "hjemmel": "§ 4-2. Opphold i Norge"
        },
        {
          "navn": "Oppfyller kravet til opphold i Norge",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:31:20.589332",
          "hjemmel": "§ 4-2. Opphold i Norge"
        },
        {
          "navn": "Oppfyller kravet til heltid- og deltidsarbeid",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:37:50.527381",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Oppfyller kravet til mobilitet",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:23:11.998006",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Oppfyller kravet til å være arbeidsfør",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-19T15:47:52.997433",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Oppfyller kravet til å ta ethvert arbeid",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:23:22.424583",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Krav til arbeidssøker",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:23:22.429505",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Registrert som arbeidssøker på søknadstidspunktet",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:37:50.527438",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere - registrert som arbeidssøker"
        },
        {
          "navn": "Er medlemmet ikke påvirket av streik eller lock-out?",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:55:56.630306",
          "hjemmel": "§ 4-22. Bortfall ved streik og lock-out"
        },
        {
          "navn": "Krav til tap av arbeidsinntekt",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:55:56.631843",
          "hjemmel": "§ 4-3. Krav til tap av arbeidsinntekt og arbeidstid"
        },
        {
          "navn": "Tap av arbeidstid er minst terskel",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-18T16:35:26.095209",
          "hjemmel": "§ 4-3. Krav til tap av arbeidsinntekt og arbeidstid"
        },
        {
          "navn": "Krav til tap av arbeidsinntekt og arbeidstid",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-18T16:35:59.06211",
          "hjemmel": "§ 4-3. Krav til tap av arbeidsinntekt og arbeidstid"
        },
        {
          "navn": "Krav til utdanning eller opplæring",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:55:57.729065",
          "hjemmel": "§ 4-6. Dagpenger under utdanning, opplæring, etablering av egen virksomhet m.v"
        },
        {
          "navn": "Oppfyller krav til ikke utestengt",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:55:56.63037",
          "hjemmel": "§ 4-28. Utestengning"
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

//language=JSON
private val jsonPermittering =
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
          "navn": "Oppfyller kravet til alder",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:37:49.167239",
          "hjemmel": "§ 4-23. Bortfall på grunn av alder"
        },
        {
          "navn": "Er omfattet av trygdelovgivningen i Norge",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-20T09:45:19.72993",
          "hjemmel": "§ 4-1. Forholdet til bestemmelser om internasjonal trygdekoordinering"
        },
        {
          "navn": "Oppfyller kravet til minsteinntekt",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:37:50.531663",
          "hjemmel": "§ 4-4. Krav til minsteinntekt"
        },
        {
          "navn": "Oppfyller kravet til verneplikt",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:45:35.975779",
          "hjemmel": "§ 4-19. Dagpenger etter avtjent verneplikt"
        },
        {
          "navn": "Mottar ikke andre fulle ytelser",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:55:56.627729",
          "hjemmel": "§ 4-24. Medlem som har fulle ytelser etter folketrygdloven eller avtalefestet pensjon"
        },
        {
          "navn": "Oppfyller kravet til opphold i Norge eller unntak",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:30:34.020535",
          "hjemmel": "§ 4-2. Opphold i Norge"
        },
        {
          "navn": "Oppfyller kravet til medlemskap",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:31:20.587784",
          "hjemmel": "§ 4-2. Opphold i Norge"
        },
        {
          "navn": "Oppfyller kravet til opphold i Norge",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:31:20.589332",
          "hjemmel": "§ 4-2. Opphold i Norge"
        },
        {
          "navn": "Oppfyller kravet til heltid- og deltidsarbeid",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:37:50.527381",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Oppfyller kravet til mobilitet",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:23:11.998006",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Oppfyller kravet til å være arbeidsfør",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-19T15:47:52.997433",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Oppfyller kravet til å ta ethvert arbeid",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:23:22.424583",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Krav til arbeidssøker",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:23:22.429505",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Registrert som arbeidssøker på søknadstidspunktet",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:37:50.527438",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere - registrert som arbeidssøker"
        },
        {
          "navn": "Er medlemmet ikke påvirket av streik eller lock-out?",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:55:56.630306",
          "hjemmel": "§ 4-22. Bortfall ved streik og lock-out"
        },
        {
          "navn": "Krav til tap av arbeidsinntekt",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:55:56.631843",
          "hjemmel": "§ 4-3. Krav til tap av arbeidsinntekt og arbeidstid"
        },
        {
          "navn": "Tap av arbeidstid er minst terskel",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-18T16:35:26.095209",
          "hjemmel": "§ 4-3. Krav til tap av arbeidsinntekt og arbeidstid"
        },
        {
          "navn": "Krav til tap av arbeidsinntekt og arbeidstid",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-18T16:35:59.06211",
          "hjemmel": "§ 4-3. Krav til tap av arbeidsinntekt og arbeidstid"
        },
        {
          "navn": "Krav til utdanning eller opplæring",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:55:57.729065",
          "hjemmel": "§ 4-6. Dagpenger under utdanning, opplæring, etablering av egen virksomhet m.v"
        },
        {
          "navn": "Oppfyller krav til ikke utestengt",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:55:56.63037",
          "hjemmel": "§ 4-28. Utestengning"
        },
        {
          "navn": "Oppfyller kravet til permittering",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-18T08:55:08.27086",
          "hjemmel": "§ 4-7. Dagpenger til permitterte"
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

//language=JSON
private val jsonPermitteringFisk =
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
          "navn": "Oppfyller kravet til alder",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:37:49.167239",
          "hjemmel": "§ 4-23. Bortfall på grunn av alder"
        },
        {
          "navn": "Er omfattet av trygdelovgivningen i Norge",
          "status": "Oppfylt",
          "vurderingstidspunkt": "2025-02-20T09:45:19.72993",
          "hjemmel": "§ 4-1. Forholdet til bestemmelser om internasjonal trygdekoordinering"
        },
        {
          "navn": "Oppfyller kravet til minsteinntekt",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:37:50.531663",
          "hjemmel": "§ 4-4. Krav til minsteinntekt"
        },
        {
          "navn": "Oppfyller kravet til verneplikt",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:45:35.975779",
          "hjemmel": "§ 4-19. Dagpenger etter avtjent verneplikt"
        },
        {
          "navn": "Mottar ikke andre fulle ytelser",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:55:56.627729",
          "hjemmel": "§ 4-24. Medlem som har fulle ytelser etter folketrygdloven eller avtalefestet pensjon"
        },
        {
          "navn": "Oppfyller kravet til opphold i Norge eller unntak",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:30:34.020535",
          "hjemmel": "§ 4-2. Opphold i Norge"
        },
        {
          "navn": "Oppfyller kravet til medlemskap",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:31:20.587784",
          "hjemmel": "§ 4-2. Opphold i Norge"
        },
        {
          "navn": "Oppfyller kravet til opphold i Norge",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:31:20.589332",
          "hjemmel": "§ 4-2. Opphold i Norge"
        },
        {
          "navn": "Oppfyller kravet til heltid- og deltidsarbeid",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:37:50.527381",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Oppfyller kravet til mobilitet",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:23:11.998006",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Oppfyller kravet til å være arbeidsfør",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-19T15:47:52.997433",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Oppfyller kravet til å ta ethvert arbeid",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:23:22.424583",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Krav til arbeidssøker",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-20T09:23:22.429505",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere"
        },
        {
          "navn": "Registrert som arbeidssøker på søknadstidspunktet",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:37:50.527438",
          "hjemmel": "§ 4-5. Reelle arbeidssøkere - registrert som arbeidssøker"
        },
        {
          "navn": "Er medlemmet ikke påvirket av streik eller lock-out?",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:55:56.630306",
          "hjemmel": "§ 4-22. Bortfall ved streik og lock-out"
        },
        {
          "navn": "Krav til tap av arbeidsinntekt",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:55:56.631843",
          "hjemmel": "§ 4-3. Krav til tap av arbeidsinntekt og arbeidstid"
        },
        {
          "navn": "Tap av arbeidstid er minst terskel",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-18T16:35:26.095209",
          "hjemmel": "§ 4-3. Krav til tap av arbeidsinntekt og arbeidstid"
        },
        {
          "navn": "Krav til tap av arbeidsinntekt og arbeidstid",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-18T16:35:59.06211",
          "hjemmel": "§ 4-3. Krav til tap av arbeidsinntekt og arbeidstid"
        },
        {
          "navn": "Krav til utdanning eller opplæring",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:55:57.729065",
          "hjemmel": "§ 4-6. Dagpenger under utdanning, opplæring, etablering av egen virksomhet m.v"
        },
        {
          "navn": "Oppfyller krav til ikke utestengt",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-17T16:55:56.63037",
          "hjemmel": "§ 4-28. Utestengning"
        },
        {
          "navn": "Oppfyller kravet til permittering i fiskeindustrien",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-02-18T08:55:08.27086",
          "hjemmel": "§ 4-7. Dagpenger til permitterte"
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
