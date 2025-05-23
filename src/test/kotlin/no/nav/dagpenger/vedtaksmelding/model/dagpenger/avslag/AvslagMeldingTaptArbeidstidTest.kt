package no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.FastsattNyArbeidstidPerUke
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.FastsattVanligArbeidstidPerUke
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.HarBruktBeregningsregelArbeidstidSiste12Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.HarBruktBeregningsregelArbeidstidSiste36Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.HarBruktBeregningsregelArbeidstidSiste6Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.KravTilProsentvisTapAvArbeidstid
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vilkår
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.PERMITTERING_FISK
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.TAPT_ARBEIDSTID
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.TAPT_ARBEIDSTID_ELLER_ARBEIDSINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_12_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_36_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_6_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_12_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_36_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_6_MND
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.TIMER
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
        avslagArbeidstidVedtak.finnOpplysning(HarBruktBeregningsregelArbeidstidSiste6Måneder.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = HarBruktBeregningsregelArbeidstidSiste6Måneder.opplysningTekstId,
                råVerdi = "false",
                datatype = BOOLSK,
            )
        avslagArbeidstidVedtak.finnOpplysning(HarBruktBeregningsregelArbeidstidSiste12Måneder.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = HarBruktBeregningsregelArbeidstidSiste12Måneder.opplysningTekstId,
                råVerdi = "true",
                datatype = BOOLSK,
            )
        avslagArbeidstidVedtak.finnOpplysning(HarBruktBeregningsregelArbeidstidSiste36Måneder.opplysningTekstId) shouldBe
            Opplysning(
                opplysningTekstId = HarBruktBeregningsregelArbeidstidSiste36Måneder.opplysningTekstId,
                råVerdi = "false",
                datatype = BOOLSK,
            )
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid ved ordinær rettighet og 6 måneders beregning`() {
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
                            Opplysning(
                                opplysningTekstId = FastsattNyArbeidstidPerUke.opplysningTekstId,
                                råVerdi = "20.5",
                                datatype = FLYTTALL,
                                enhet = TIMER,
                            ),
                            Opplysning(
                                opplysningTekstId = HarBruktBeregningsregelArbeidstidSiste6Måneder.opplysningTekstId,
                                råVerdi = "true",
                                datatype = BOOLSK,
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
                AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_6_MND.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid ved ordinær rettighet og 12 måneders gjennomsnittsberegning`() {
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
                            Opplysning(
                                opplysningTekstId = FastsattNyArbeidstidPerUke.opplysningTekstId,
                                råVerdi = "20.5",
                                datatype = FLYTTALL,
                                enhet = TIMER,
                            ),
                            Opplysning(
                                opplysningTekstId = HarBruktBeregningsregelArbeidstidSiste12Måneder.opplysningTekstId,
                                råVerdi = "true",
                                datatype = BOOLSK,
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
                AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_12_MND.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid ved ordinær rettighet og 36 måneders gjennomsnittsberegning`() {
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
                            Opplysning(
                                opplysningTekstId = FastsattNyArbeidstidPerUke.opplysningTekstId,
                                råVerdi = "20.5",
                                datatype = FLYTTALL,
                                enhet = TIMER,
                            ),
                            Opplysning(
                                opplysningTekstId = HarBruktBeregningsregelArbeidstidSiste36Måneder.opplysningTekstId,
                                råVerdi = "true",
                                datatype = BOOLSK,
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
                AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_36_MND.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid ved rettighet permittering fisk og 6 måneders beregning`() {
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
                            Opplysning(
                                opplysningTekstId = FastsattNyArbeidstidPerUke.opplysningTekstId,
                                råVerdi = "20.5",
                                datatype = FLYTTALL,
                                enhet = TIMER,
                            ),
                            Opplysning(
                                opplysningTekstId = HarBruktBeregningsregelArbeidstidSiste6Måneder.opplysningTekstId,
                                råVerdi = "true",
                                datatype = BOOLSK,
                            ),
                        ),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_6_MND.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid ved rettighet permittering fisk og 12 måneders gjennomsnittsberegning`() {
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
                            Opplysning(
                                opplysningTekstId = FastsattNyArbeidstidPerUke.opplysningTekstId,
                                råVerdi = "20.5",
                                datatype = FLYTTALL,
                                enhet = TIMER,
                            ),
                            Opplysning(
                                opplysningTekstId = HarBruktBeregningsregelArbeidstidSiste12Måneder.opplysningTekstId,
                                råVerdi = "true",
                                datatype = BOOLSK,
                            ),
                        ),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_12_MND.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid ved rettighet permittering fisk og 36 måneders gjennomsnittsberegning`() {
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
                            Opplysning(
                                opplysningTekstId = FastsattNyArbeidstidPerUke.opplysningTekstId,
                                råVerdi = "20.5",
                                datatype = FLYTTALL,
                                enhet = TIMER,
                            ),
                            Opplysning(
                                opplysningTekstId = HarBruktBeregningsregelArbeidstidSiste36Måneder.opplysningTekstId,
                                råVerdi = "true",
                                datatype = BOOLSK,
                            ),
                        ),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_36_MND.brevblokkId,
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
        },
        {
          "id": "01957010-95c8-7630-b47a-0fd80e0613b8",
          "opplysningTypeId": "0194881f-9435-72a8-b1ce-9575cbc2a764",
          "navn": "Beregningsregel: Arbeidstid siste 6 måneder",
          "verdi": "false",
          "status": "Faktum",
          "datatype": "boolsk",
          "redigerbar": true,
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
              "01957010-858c-757b-84dd-57c591684ad2"
            ]
          }
        },
        {
          "id": "01957010-95c8-7630-b47a-0fd80e0613ba",
          "opplysningTypeId": "0194881f-9435-72a8-b1ce-9575cbc2a765",
          "navn": "Beregningsregel: Arbeidstid siste 12 måneder",
          "verdi": "true",
          "status": "Faktum",
          "datatype": "boolsk",
          "redigerbar": true,
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
              "01957010-858c-757b-84dd-57c591684ad2"
            ]
          }
        },
        {
          "id": "01957010-95c8-7630-b47a-0fd80e0613bc",
          "opplysningTypeId": "0194881f-9435-72a8-b1ce-9575cbc2a766",
          "navn": "Beregningsregel: Arbeidstid siste 36 måneder",
          "verdi": "false",
          "status": "Faktum",
          "datatype": "boolsk",
          "redigerbar": true,
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
              "01957010-858c-757b-84dd-57c591684ad2"
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
