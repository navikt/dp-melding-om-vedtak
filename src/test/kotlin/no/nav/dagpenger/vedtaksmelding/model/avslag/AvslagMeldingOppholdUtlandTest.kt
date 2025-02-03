package no.nav.dagpenger.vedtaksmelding.model.avslag

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_OPPHOLD_UTLAND_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_OPPHOLD_UTLAND_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.OPPHOLD_I_NORGE
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak.Utfall
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vilkår
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vilkår.Status.IKKE_OPPFYLT
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class AvslagMeldingOppholdUtlandTest {
    private val avslagOppholdNorgeVedtak = VedtakMapper(avslagOppholdNorgeJson).vedtak()

    @Test
    fun `Riktige brevblokker for avslag opphold utland`() {
        val behandlingId = UUIDv7.ny()
        val oppholdNorgeIkkeOppfylt =
            Vilkår(
                navn = OPPHOLD_I_NORGE.navn,
                status = IKKE_OPPFYLT,
            )

        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(oppholdNorgeIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_OPPHOLD_UTLAND_DEL_1.brevblokkId,
                AVSLAG_OPPHOLD_UTLAND_DEL_2.brevblokkId,
            ) + VedtakMelding.fasteBlokker
    }

    @Test
    fun `Brevstøtte for avslag grunnet opphold i utlandet`() {
        shouldNotThrow<VedtakMelding.ManglerBrevstøtte> {
            AvslagMelding(avslagOppholdNorgeVedtak, emptyList())
        }
    }
}

//language=JSON
private val avslagOppholdNorgeJson =
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
          "navn": "Oppfyller kravet til opphold i Norge",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-21T11:03:11.079514",
          "hjemmel": "folketrygdloven § 4-5"
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
