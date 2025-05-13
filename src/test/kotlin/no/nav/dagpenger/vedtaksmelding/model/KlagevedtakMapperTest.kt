package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class KlagevedtakMapperTest() {
    @Test
    fun `skal kunne lage klagevedtak`() {
        KlagevedtakMapper(
            vedtakJson = "",
        ).vedtak().let { vedtak ->
            vedtak.behandlingId shouldBe ""
            vedtak.fagsakId shouldBe ""
            vedtak.opplysninger shouldBe emptySet()
        }
    }
}


private val klageVedtakJson =
    //language=JSON
    """
    
      {
  "behandlingId" : "0196a5b8-3b8c-7ebe-983b-d78078e51cbd",
  "saksbehandler" : {
    "ident" : "Z994030",
    "fornavn" : "F_Z994030",
    "etternavn" : "E_Z994030",
    "enhet" : {
      "navn" : "IT-avdelingen",
      "enhetNr" : "2970",
      "postadresse" : ""
    }
  },
  "behandlingOpplysninger" : [ {
    "opplysningId" : "0196a5b8-3b8c-7ebe-983b-d78078e51cbe",
    "navn" : "Hva klagen gjelder",
    "paakrevd" : false,
    "gruppe" : "KLAGESAK",
    "valgmuligheter" : [ "Avslag på søknad", "Dagpengenes størrelse", "Annet" ],
    "redigerbar" : true,
    "type" : "FLER_LISTEVALG"
  }, {
    "opplysningId" : "0196a5b8-3b8c-7ebe-983b-d78078e51cbf",
    "navn" : "Vedtak klagen gjelder",
    "paakrevd" : true,
    "gruppe" : "KLAGESAK",
    "valgmuligheter" : [ ],
    "redigerbar" : true,
    "type" : "TEKST"
  }, {
    "opplysningId" : "0196a5b8-3b8c-7ebe-983b-d78078e51cc5",
    "navn" : "Frist for å klage",
    "paakrevd" : true,
    "gruppe" : "FRIST",
    "valgmuligheter" : [ ],
    "redigerbar" : true,
    "type" : "DATO"
  }, {
    "opplysningId" : "0196a5b8-3b8c-7ebe-983b-d78078e51cc4",
    "navn" : "Klage mottatt",
    "paakrevd" : true,
    "gruppe" : "FRIST",
    "valgmuligheter" : [ ],
    "redigerbar" : true,
    "type" : "DATO"
  }, {
    "opplysningId" : "0196a5b8-3b8c-7ebe-983b-d78078e51cc6",
    "navn" : "Har klager klaget innen fristen?",
    "paakrevd" : true,
    "gruppe" : "FRIST",
    "valgmuligheter" : [ ],
    "redigerbar" : true,
    "type" : "BOOLSK"
  }, {
    "opplysningId" : "0196a5b8-3b8c-7ebe-983b-d78078e51cc0",
    "navn" : "Er klagen skriftlig?",
    "paakrevd" : true,
    "gruppe" : "FORMKRAV",
    "valgmuligheter" : [ ],
    "redigerbar" : true,
    "type" : "BOOLSK"
  }, {
    "opplysningId" : "0196a5b8-3b8c-7ebe-983b-d78078e51cc1",
    "navn" : "Er klagen underskrevet?",
    "paakrevd" : true,
    "gruppe" : "FORMKRAV",
    "valgmuligheter" : [ ],
    "redigerbar" : true,
    "type" : "BOOLSK"
  }, {
    "opplysningId" : "0196a5b8-3b8c-7ebe-983b-d78078e51cc2",
    "navn" : "Nevner klagen den endring som kreves?",
    "paakrevd" : true,
    "gruppe" : "FORMKRAV",
    "valgmuligheter" : [ ],
    "redigerbar" : true,
    "type" : "BOOLSK"
  }, {
    "opplysningId" : "0196a5b8-3b8c-7ebe-983b-d78078e51cc3",
    "navn" : "Har klager rettslig klageinteresse?",
    "paakrevd" : true,
    "gruppe" : "FORMKRAV",
    "valgmuligheter" : [ ],
    "redigerbar" : true,
    "type" : "BOOLSK"
  } ],
  "utfallOpplysninger" : [ ],
  "utfall" : {
    "verdi" : "IKKE_SATT",
    "tilgjengeligeUtfall" : [ "AVVIST", "OPPRETTHOLDELSE", "DELVIS_MEDHOLD", "MEDHOLD" ]
  },
  "meldingOmVedtak" : {
    "html" : "<html><h1>Hei</H1></html>",
    "utvidedeBeskrivelser" : [ ]
  }
}
   
    """.trimIndent()