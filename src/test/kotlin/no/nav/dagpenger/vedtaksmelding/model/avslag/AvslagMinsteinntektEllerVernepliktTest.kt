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

class AvslagMinsteinntektEllerVernepliktTest {
    @Test
    fun `Brevstøtte for avslag minsteinntekt eller verneplikt`() {
        val avslagMinsteinntektVedtak = VedtakMapper(json).vedtak()
        shouldNotThrow<Vedtaksmelding.ManglerBrevstøtte> {
            Avslag(avslagMinsteinntektVedtak, emptyList())
        }
    }

    @Test
    fun `Rikige brevblokker for avslag på minsteinntekt`() {
        val minsteInntektIkkeOppfylt =
            Vilkår(
                navn = "Oppfyller kravet til minsteinntekt eller verneplikt",
                status = Vilkår.Status.IKKE_OPPFYLT,
            )
        Avslag(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    vilkår = setOf(minsteInntektIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                "brev.blokk.vedtak-avslag",
                "brev.blokk.begrunnelse-avslag-minsteinntekt",
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
          "navn": "Oppfyller kravet til minsteinntekt eller verneplikt",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.070006",
          "hjemmel": "folketrygdloven § 4-4"
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
