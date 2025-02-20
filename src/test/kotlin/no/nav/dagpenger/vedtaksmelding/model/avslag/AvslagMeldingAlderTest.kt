package no.nav.dagpenger.vedtaksmelding.model.avslag

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.Aldersgrense
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_ALDER
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.ENHETSLØS
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak.Utfall
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vilkår
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class AvslagMeldingAlderTest {
    private val avslagAlderVedtak = VedtakMapper(json).vedtak()

    @Test
    fun `Hent relevate opplysninger ved avslag alder`() {
        avslagAlderVedtak.finnOpplysning(Aldersgrense.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = Aldersgrense.opplysningTekstId,
                råVerdi = "67",
                datatype = HELTALL,
                enhet = ENHETSLØS,
            )
    }

    @Test
    fun `Riktige brevblokker for avslag alder`() {
        val behandlingId = UUIDv7.ny()
        val alderIkkeOppfylt =
            Vilkår(
                navn = VilkårTyper.IKKE_PASSERT_ALDERSGRENSE.vilkårNavn,
                status = Vilkår.Status.IKKE_OPPFYLT,
            )

        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(alderIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_ALDER.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
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
          "navn": "Oppfyller kravet til alder",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-27T12:08:17.941134",
          "hjemmel": "folketrygdloven § 4-23"
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
          "id": "0194a772-ac7d-7c06-b093-9910c74fb344",
          "opplysningTypeId": "0194881f-940b-76ff-acf5-ba7bcb367234",
          "navn": "Aldersgrense",
          "verdi": "67",
          "status": "Faktum",
          "datatype": "heltall",
          "redigerbar": false,
          "synlig": false,
          "formål": "Regel",
          "gyldigFraOgMed": null,
          "gyldigTilOgMed": null,
          "kilde": null,
          "utledetAv": {
            "regel": {
              "navn": "Oppslag"
            },
            "opplysninger": [
              "0194a772-ac74-78d2-93ee-83ddde7af10f"
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
