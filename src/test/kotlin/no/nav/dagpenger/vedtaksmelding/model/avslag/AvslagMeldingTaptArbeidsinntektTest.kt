package no.nav.dagpenger.vedtaksmelding.model.avslag

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.TAPT_ARBEIDSINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak.Utfall.AVSLÅTT
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vilkår
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vilkår.Status.IKKE_OPPFYLT
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class AvslagMeldingTaptArbeidsinntektTest {
    private val avslagTaptArbeidsinntektVedtak = VedtakMapper(json).vedtak()

    @Test
    fun `Brevstøtte for avslag grunnet for lite tapt arbeidstid`() {
        shouldNotThrow<VedtakMelding.ManglerBrevstøtte> {
            AvslagMelding(
                vedtak = avslagTaptArbeidsinntektVedtak,
                alleBrevblokker = emptyList(),
            )
        }
    }

    @Test
    fun `Riktige brevblokker for avslag tapt arbeidsinntekt`() {
        val behandlingId = UUIDv7.ny()
        val taptArbeidsinntektIkkeOppfylt =
            Vilkår(
                navn = TAPT_ARBEIDSINNTEKT.navn,
                status = IKKE_OPPFYLT,
            )

        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(taptArbeidsinntektIkkeOppfylt),
                    utfall = AVSLÅTT,
                    opplysninger = emptySet(),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                "brev.blokk.vedtak-avslag",
                "brev.blokk.avslag-tapt-arbeidsinntekt",
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
          "navn": "Krav til tap av arbeidsinntekt",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.071",
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
