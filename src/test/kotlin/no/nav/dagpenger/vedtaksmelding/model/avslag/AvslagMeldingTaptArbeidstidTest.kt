package no.nav.dagpenger.vedtaksmelding.model.avslag

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.FastsattNyArbeidstidPerUke
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.FastsattVanligArbeidstidPerUke
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.KravTilProsentvisTapAvArbeidstid
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.ProsentvisTaptArbeidstid
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.PERMITTERING_FISK
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.TAPT_ARBEIDSTID
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.TAPT_ARBEIDSTID_ELLER_ARBEIDSINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_FASTSATT_VANLIG_ARBEDSTID_0
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.TIMER
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak.Utfall
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vilkår
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class AvslagMeldingTaptArbeidstidTest {
    private val avslagArbeidstidVedtak = VedtakMapper(json).vedtak()

    @Test
    fun `Hent relevate opplysninger ved avslag tapt arbeidstid`() {
        avslagArbeidstidVedtak.finnOpplysning(FastsattVanligArbeidstidPerUke.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = FastsattVanligArbeidstidPerUke.opplysningTekstId,
                råVerdi = "37.5",
                datatype = FLYTTALL,
                enhet = TIMER,
            )
        avslagArbeidstidVedtak.finnOpplysning(FastsattNyArbeidstidPerUke.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = FastsattNyArbeidstidPerUke.opplysningTekstId,
                råVerdi = "20",
                datatype = FLYTTALL,
                enhet = TIMER,
            )
        avslagArbeidstidVedtak.finnOpplysning(KravTilProsentvisTapAvArbeidstid.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = KravTilProsentvisTapAvArbeidstid.opplysningTekstId,
                råVerdi = "50.0",
                datatype = FLYTTALL,
            )
        avslagArbeidstidVedtak.finnOpplysning(ProsentvisTaptArbeidstid.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = ProsentvisTaptArbeidstid.opplysningTekstId,
                råVerdi = "46.666666666666664",
                datatype = FLYTTALL,
            )
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid når fastsatt vanlig arbeidstid er større enn 0 og rettighet er ordinær`() {
        val behandlingId = UUIDv7.ny()
        val arbeidstidIkkeOppfylt =
            Vilkår(
                navn = TAPT_ARBEIDSTID.vilkårNavn,
                status = Vilkår.Status.IKKE_OPPFYLT,
            )

        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(arbeidstidIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger =
                        setOf(
                            Opplysning(
                                opplysningTekstId = FastsattVanligArbeidstidPerUke.opplysningTekstId,
                                råVerdi = "37.5",
                                datatype = FLYTTALL,
                                enhet = TIMER,
                            ),
                        ),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_2.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid når fastsatt vanlig arbeidstid er større enn 0 og rettighet er permittering fisk`() {
        val behandlingId = UUIDv7.ny()
        val arbeidstidIkkeOppfylt =
            Vilkår(
                navn = TAPT_ARBEIDSTID.vilkårNavn,
                status = Vilkår.Status.IKKE_OPPFYLT,
            )
        val permitteringFiskVilkår =
            Vilkår(
                navn = PERMITTERING_FISK.vilkårNavn,
                status = Vilkår.Status.OPPFYLT,
            )

        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(arbeidstidIkkeOppfylt, permitteringFiskVilkår),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger =
                        setOf(
                            Opplysning(
                                opplysningTekstId = FastsattVanligArbeidstidPerUke.opplysningTekstId,
                                råVerdi = "37.5",
                                datatype = FLYTTALL,
                                enhet = TIMER,
                            ),
                        ),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid når fastsatt vanlig arbeidstid er 0`() {
        val behandlingId = UUIDv7.ny()
        val arbeidstidIkkeOppfylt =
            Vilkår(
                navn = TAPT_ARBEIDSTID.vilkårNavn,
                status = Vilkår.Status.IKKE_OPPFYLT,
            )

        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(arbeidstidIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger =
                        setOf(
                            Opplysning(
                                opplysningTekstId = FastsattVanligArbeidstidPerUke.opplysningTekstId,
                                råVerdi = "0.0",
                                datatype = FLYTTALL,
                                enhet = TIMER,
                            ),
                        ),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_FASTSATT_VANLIG_ARBEDSTID_0.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid når fastsatt vanlig arbeidstid er mindre enn 0`() {
        val behandlingId = UUIDv7.ny()
        val arbeidstidIkkeOppfylt =
            Vilkår(
                navn = TAPT_ARBEIDSTID.vilkårNavn,
                status = Vilkår.Status.IKKE_OPPFYLT,
            )

        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(arbeidstidIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger =
                        setOf(
                            Opplysning(
                                opplysningTekstId = FastsattVanligArbeidstidPerUke.opplysningTekstId,
                                råVerdi = "-0.5",
                                datatype = FLYTTALL,
                                enhet = TIMER,
                            ),
                        ),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_FASTSATT_VANLIG_ARBEDSTID_0.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Brevstøtte for avslag grunnet for lite tapt arbeidstid`() {
        shouldNotThrow<VedtakMelding.ManglerBrevstøtte> {
            AvslagMelding(avslagArbeidstidVedtak, emptyList())
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
          "navn": "${TAPT_ARBEIDSTID.vilkårNavn}",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-21T11:06:58.171429",
          "hjemmel": "folketrygdloven § 4-3"
        },
        {
          "navn": "${TAPT_ARBEIDSTID_ELLER_ARBEIDSINNTEKT.vilkårNavn}",
          "status": "IkkeOppfylt",
          "vurderingstidspunkt": "2025-01-21T11:06:58.212861",
          "hjemmel": "folketrygdloven § 4-3"
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
          "id": "01948854-5e53-7bc0-81eb-5e6019c903e1",
          "opplysningTypeId": "0194881f-9435-72a8-b1ce-9575cbc2a76b",
          "navn": "Ny arbeidstid per uke",
          "verdi": "20.0",
          "status": "Faktum",
          "datatype": "desimaltall",
          "redigerbar": true,
          "synlig": true,
          "formål": "Regel",
          "gyldigFraOgMed": null,
          "gyldigTilOgMed": null,
          "kilde": {
            "type": "Saksbehandler",
            "registrert": "2025-01-21T11:06:57.90184",
            "ident": "Z994251",
            "meldingId": null
          },
          "utledetAv": null
        },
        {
          "id": "01948850-e99d-7037-9851-8b15c4b2ed28",
          "opplysningTypeId": "0194881f-9435-72a8-b1ce-9575cbc2a76a",
          "navn": "Fastsatt arbeidstid per uke før tap",
          "verdi": "37.5",
          "status": "Faktum",
          "datatype": "desimaltall",
          "redigerbar": false,
          "synlig": true,
          "formål": "Regel",
          "gyldigFraOgMed": null,
          "gyldigTilOgMed": null,
          "kilde": null,
          "utledetAv": {
            "regel": {
              "navn": "MinstAv"
            },
            "opplysninger": [
              "01948850-e75f-710b-8b4e-527662639a13",
              "01948850-e764-7663-8f83-0bf956b83249",
              "01948850-e77c-7209-b7e3-724d67f67801",
              "01948850-e99a-7a48-b1b5-f67e5c03159b"
            ]
          }
        },
        {
          "id": "01948850-e75f-710b-8b4e-527662639a27",
          "opplysningTypeId": "0194881f-9435-72a8-b1ce-9575cbc2a762",
          "navn": "Krav til prosentvis tap av arbeidstid",
          "verdi": "50.0",
          "status": "Faktum",
          "datatype": "desimaltall",
          "redigerbar": false,
          "synlig": true,
          "formål": "Regel",
          "gyldigFraOgMed": null,
          "gyldigTilOgMed": null,
          "kilde": null,
          "utledetAv": {
            "regel": {
              "navn": "Oppslag"
            },
            "opplysninger": [
              "01948850-df4a-79b7-9bd4-f81f6e629ec1"
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
