package no.nav.dagpenger.vedtaksmelding.model.avslag

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_ALDER
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_ANDRE_FULLE_YTELSER
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_MINSTEINNTEKT_BEGRUNNELSE
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_OPPHOLD_UTLAND_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_OPPHOLD_UTLAND_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ARBEID_NORGE
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_REGISTRERT_ARBEIDSSOKER
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_STREIK_LOCKOUT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_STREIK_LOCKOUT_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_UTESTENGT
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_UTESTENGT_HJEMMEL
import org.junit.jupiter.api.Test

class AvslagMeldingAlleVilkårTest {
    private val avslagAlleVilkårVedtak = VedtakMapper(json).vedtak()

    @Test
    fun `Riktige brevblokker i korrekt rekkefølge ved avslag på alle vilkår`() {
        AvslagMelding(
            vedtak = avslagAlleVilkårVedtak,
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_MINSTEINNTEKT_BEGRUNNELSE.brevblokkId,
                AVSLAG_ALDER.brevblokkId,
                AVSLAG_TAPT_ARBEIDSINNTEKT.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID.brevblokkId,
                AVSLAG_UTESTENGT.brevblokkId,
                AVSLAG_UTESTENGT_HJEMMEL.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ARBEID_NORGE.brevblokkId,
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
            ) + VedtakMelding.fasteBlokker
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
            "navn": "Oppfyller kravet til alder",
            "status": "IkkeOppfylt",
            "vurderingstidspunkt": "2025-01-27T12:08:17.941134",
            "hjemmel": "folketrygdloven § 4-23"
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
        },
        {
          "navn": "Er medlemmet ikke påvirket av streik eller lock-out?",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-27T12:08:17.927976",
          "hjemmel": "folketrygdloven § 4-22"
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
//        "navn": "Er medlemmet ikke påvirket av streik eller lock-out?",
//        "status": "IkkeOppfylt",
//        "vurderingstidspunkt": "2025-01-27T12:08:17.927976",
//        "hjemmel": "folketrygdloven § 4-22"
//    },
