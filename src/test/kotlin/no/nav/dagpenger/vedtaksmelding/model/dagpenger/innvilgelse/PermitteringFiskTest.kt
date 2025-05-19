package no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AntallPermitteringsukerFisk
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AntallStønadsuker
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.PERMITTERING_FISK
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ARBEIDSFORHOLD_AVSLUTT_PERMITTERT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ARBEIDSTIDEN_DIN
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE_PERMITTERT_FISK_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE_PERMITTERT_FISK_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_HVA_SKJER_ETTER_PERMITTERINGEN
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_PERMITTERT_FISK
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_UTBETALING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_UTEN_EGENANDEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT_FISK
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.UKER
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak.Utfall
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vilkår
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vilkår.Status.OPPFYLT
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class PermitteringFiskTest {
    private val innvilgelsePermitteringFisk = VedtakMapper(json).vedtak()

    @Test
    fun `Skal hente aktuelle opplysninger for innvilgelse som permittert fra fiskeindustri`() {
        innvilgelsePermitteringFisk.finnOpplysning(AntallPermitteringsukerFisk.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = AntallPermitteringsukerFisk.opplysningTekstId,
                råVerdi = "52",
                datatype = HELTALL,
                enhet = UKER,
            )

        innvilgelsePermitteringFisk.finnOpplysning(AntallStønadsuker.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = AntallStønadsuker.opplysningTekstId,
                råVerdi = "104",
                datatype = HELTALL,
                enhet = UKER,
            )
    }

    @Test
    fun `Rikig brevblokker rekkefølge for innvilgelse med permittering fra fiseindustri`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_PERMITTERT_FISK.brevblokkId,
                INNVILGELSE_UTEN_EGENANDEL.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT_FISK.brevblokkId,
                INNVILGELSE_DAGPENGEPERIODE_PERMITTERT_FISK_DEL_1.brevblokkId,
                INNVILGELSE_DAGPENGEPERIODE_PERMITTERT_FISK_DEL_2.brevblokkId,
                INNVILGELSE_ARBEIDSFORHOLD_AVSLUTT_PERMITTERT.brevblokkId,
                INNVILGELSE_HVA_SKJER_ETTER_PERMITTERINGEN.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_GRUNNLAG.brevblokkId,
                INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId,
                INNVILGELSE_MELDEKORT.brevblokkId,
                INNVILGELSE_UTBETALING.brevblokkId,
                INNVILGELSE_SKATTEKORT.brevblokkId,
                INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker

        InnvilgelseMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    vilkår =
                        setOf(
                            Vilkår(
                                navn = PERMITTERING_FISK.vilkårNavn,
                                status = OPPFYLT,
                            ),
                        ),
                    utfall = Utfall.INNVILGET,
                    opplysninger = setOf(),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
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
          "navn": "${PERMITTERING_FISK.vilkårNavn}",
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
            "verdi": 104
          },
          {
            "navn": "Egenandel",
            "type": "beløp",
            "verdi": 0
          },
          {
            "navn": "FiskePermitteringsperiode",
            "type": "uker",
            "verdi": 52
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
