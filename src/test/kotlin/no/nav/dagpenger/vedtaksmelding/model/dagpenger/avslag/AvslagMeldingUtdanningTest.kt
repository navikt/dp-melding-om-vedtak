package no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vilkår
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.IKKE_UTDANNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_UTDANNING
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class AvslagMeldingUtdanningTest {
    private val avslagUtdanningVedtak = VedtakMapper(json).vedtak()

    @Test
    fun `Riktige brevblokker for avslag på grunn av utdanning`() {
        val behandlingId = UUIDv7.ny()
        val utdanningVilkår =
            Vilkår(
                navn = IKKE_UTDANNING.vilkårNavn,
                status = Vilkår.Status.IKKE_OPPFYLT,
            )

        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(utdanningVilkår),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_UTDANNING.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Brevstøtte for avslag utdanning`() {
        shouldNotThrow<VedtakMelding.ManglerBrevstøtte> {
            AvslagMelding(avslagUtdanningVedtak, emptyList())
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
          "navn": "Krav til utdanning eller opplæring",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.076481",
          "hjemmel": "folketrygdloven § 4-28"
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
