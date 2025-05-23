package no.nav.dagpenger.vedtaksmelding.model.klage

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.KlageOpplysningTyper
import org.junit.jupiter.api.Test
import java.util.UUID

class KlagevedtakMapperTest {
    @Test
    fun `skal kunne lage klagevedtak`() {
        KlagevedtakMapper(
            vedtakJson = klageVedtakJson,
        ).vedtak().let { vedtak ->
            vedtak.behandlingId shouldBe UUID.fromString("0196a5b8-3dab-779f-ba9c-a116e298b2b1")
            vedtak.fagsakId shouldBe "fagsakId"
            vedtak.opplysninger.size shouldBe 7
            vedtak.opplysninger.single { it.opplysningTekstId == KlageOpplysningTyper.KlageMottattDato.opplysningTekstId }.let {
                it.formatertVerdi shouldBe "7. mai 2025"
            }
            vedtak.opplysninger.single { it.opplysningTekstId == KlageOpplysningTyper.PåklagetVedtakDato.opplysningTekstId }.let {
                it.formatertVerdi shouldBe "3. februar 2025"
            }
        }
    }
}

private val klageVedtakJson =
    //language=JSON
    """
    {
      "behandlingId" : "0196a5b8-3dab-779f-ba9c-a116e298b2b1",
      "saksbehandler" : {
        "ident" : "Z993082",
        "fornavn" : "F_Z993082",
        "etternavn" : "E_Z993082",
        "enhet" : {
          "navn" : "IT-avdelingen",
          "enhetNr" : "2970",
          "postadresse" : ""
        }
      },
      "behandlingOpplysninger" : [ {
        "opplysningId" : "0196a5b8-3dab-779f-ba9c-a116e298b2b2",
        "opplysningNavnId" : "KLAGEN_GJELDER",
        "navn" : "Hva klagen gjelder",
        "paakrevd" : false,
        "gruppe" : "KLAGESAK",
        "valgmuligheter" : [ "Avslag på søknad", "Dagpengenes størrelse", "Annet" ],
        "redigerbar" : true,
        "verdi" : [ "Avslag på søknad" ],
        "type" : "FLER_LISTEVALG"
      }, {
        "opplysningId" : "0196a5b8-3dab-779f-ba9c-a116e298b2b3",
        "opplysningNavnId" : "KLAGEN_GJELDER_VEDTAK",
        "navn" : "Vedtak klagen gjelder",
        "paakrevd" : true,
        "gruppe" : "KLAGESAK",
        "valgmuligheter" : [ ],
        "redigerbar" : true,
        "verdi" : "12",
        "type" : "TEKST"
      }, {
        "opplysningId": "0196a5b8-3dab-779f-ba9c-a116e298b4b7",
        "opplysningNavnId": "KLAGEN_GJELDER_VEDTAKSDATO",
        "navn": "Vedtaksdato",
        "paakrevd": true,
        "gruppe": "KLAGESAK",
        "valgmuligheter": [],
        "redigerbar": true,
        "verdi": "2025-02-03",
        "type": "DATO"
      }, {
        "opplysningId" : "0196a5b8-3dab-779f-ba9c-a116e298b2b9",
        "opplysningNavnId" : "KLAGEFRIST",
        "navn" : "Frist for å klage",
        "paakrevd" : true,
        "gruppe" : "FRIST",
        "valgmuligheter" : [ ],
        "redigerbar" : true,
        "verdi" : "2025-05-07",
        "type" : "DATO"
      }, {
        "opplysningId" : "0196a5b8-3dab-779f-ba9c-a116e298b2b8",
        "opplysningNavnId" : "KLAGE_MOTTATT",
        "navn" : "Klage mottatt",
        "paakrevd" : true,
        "gruppe" : "FRIST",
        "valgmuligheter" : [ ],
        "redigerbar" : true,
        "verdi" : "2025-05-07",
        "type" : "DATO"
      }, {
        "opplysningId" : "0196a5b8-3dab-779f-ba9c-a116e298b2ba",
        "opplysningNavnId" : "KLAGEFRIST_OPPFYLT",
        "navn" : "Har klager klaget innen fristen?",
        "paakrevd" : true,
        "gruppe" : "FRIST",
        "valgmuligheter" : [ ],
        "redigerbar" : true,
        "verdi" : true,
        "type" : "BOOLSK"
      }, {
        "opplysningId" : "0196a5b8-3dab-779f-ba9c-a116e298b2b4",
        "opplysningNavnId" : "ER_KLAGEN_SKRIFTLIG",
        "navn" : "Er klagen skriftlig?",
        "paakrevd" : true,
        "gruppe" : "FORMKRAV",
        "valgmuligheter" : [ ],
        "redigerbar" : true,
        "verdi" : true,
        "type" : "BOOLSK"
      }, {
        "opplysningId" : "0196a5b8-3dab-779f-ba9c-a116e298b2b5",
        "opplysningNavnId" : "ER_KLAGEN_UNDERSKREVET",
        "navn" : "Er klagen underskrevet?",
        "paakrevd" : true,
        "gruppe" : "FORMKRAV",
        "valgmuligheter" : [ ],
        "redigerbar" : true,
        "verdi" : true,
        "type" : "BOOLSK"
      }, {
        "opplysningId" : "0196a5b8-3dab-779f-ba9c-a116e298b2b6",
        "opplysningNavnId" : "KLAGEN_NEVNER_ENDRING",
        "navn" : "Nevner klagen den endring som kreves?",
        "paakrevd" : true,
        "gruppe" : "FORMKRAV",
        "valgmuligheter" : [ ],
        "redigerbar" : true,
        "verdi" : true,
        "type" : "BOOLSK"
      }, {
        "opplysningId" : "0196a5b8-3dab-779f-ba9c-a116e298b2b7",
        "opplysningNavnId" : "RETTSLIG_KLAGEINTERESSE",
        "navn" : "Har klager rettslig klageinteresse?",
        "paakrevd" : true,
        "gruppe" : "FORMKRAV",
        "valgmuligheter" : [ ],
        "redigerbar" : true,
        "verdi" : true,
        "type" : "BOOLSK"
      } ],
      "utfallOpplysninger" : [ {
        "opplysningId" : "0196a5b8-3dab-779f-ba9c-a116e298b2bd",
        "opplysningNavnId" : "UTFALL",
        "navn" : "Utfall",
        "paakrevd" : true,
        "gruppe" : "KLAGE_ANKE",
        "valgmuligheter" : [ "OPPRETTHOLDELSE", "MEDHOLD", "DELVIS_MEDHOLD", "AVVIST" ],
        "redigerbar" : true,
        "verdi" : "OPPRETTHOLDELSE",
        "type" : "LISTEVALG"
      }, {
        "opplysningId" : "0196a5b8-3dab-779f-ba9c-a116e298b2be",
        "opplysningNavnId" : "VURDERIG_AV_KLAGEN",
        "navn" : "Vurdering av klagen",
        "paakrevd" : true,
        "gruppe" : "KLAGE_ANKE",
        "valgmuligheter" : [ ],
        "redigerbar" : true,
        "verdi" : "adf",
        "type" : "TEKST"
      }, {
        "opplysningId" : "0196a5b8-3dab-779f-ba9c-a116e298b2bf",
        "opplysningNavnId" : "HVEM_KLAGER",
        "navn" : "Hvem er klager i saken?",
        "paakrevd" : true,
        "gruppe" : "KLAGE_ANKE",
        "valgmuligheter" : [ "BRUKER", "FULLMEKTIG" ],
        "redigerbar" : true,
        "verdi" : "BRUKER",
        "type" : "LISTEVALG"
      }, {
        "opplysningId" : "0196a5b8-3dab-779f-ba9c-a116e298b2c0",
        "opplysningNavnId" : "HJEMLER",
        "navn" : "Hvilke hjemler gjelder klagen?",
        "paakrevd" : true,
        "gruppe" : "KLAGE_ANKE",
        "valgmuligheter" : [ "§ 4-1", "§ 4-2", "§ 4-3", "§ 4-4", "§ 4-5" ],
        "redigerbar" : true,
        "verdi" : [ "§ 4-1" ],
        "type" : "FLER_LISTEVALG"
      }, {
        "opplysningId" : "0196a5b8-3dab-779f-ba9c-a116e298b2c1",
        "opplysningNavnId" : "INTERN_MELDING",
        "navn" : "Intern melding",
        "paakrevd" : false,
        "gruppe" : "KLAGE_ANKE",
        "valgmuligheter" : [ ],
        "redigerbar" : true,
        "verdi" : "qasdas",
        "type" : "TEKST"
      } ],
      "utfall" : {
        "verdi" : "OPPRETTHOLDELSE",
        "tilgjengeligeUtfall" : [ "AVVIST", "OPPRETTHOLDELSE", "DELVIS_MEDHOLD", "MEDHOLD" ]
      },
      "meldingOmVedtak" : {
        "html" : "<html><h1>Hei</H1></html>",
        "utvidedeBeskrivelser" : [ ]
      }
    }
    """.trimIndent()
