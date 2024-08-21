package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.intellij.lang.annotations.Language

fun Application.meldingOmVedtakApi() {
    apiConfig()
    routing {
        authenticate("azureAd") {
            get("/melding-om-vedtak/{behandlingId}") {
                call.respond(status = OK, message = objectMapper.readValue(hubba))
            }
        }
    }
}

@Language("JSON")
internal val hubba =
    """
    [
      {
        "tekstId": "brev.blokk.vedtak-avslag",
        "opplysninger": [{ "tekstId": "Søknadsdato", "type": "dato", "verdi": "12-05-2024" }]
      },
      {
        "tekstId": "brev.blokk.begrunnelse-avslag-minsteinntekt",
        "opplysninger": [{ "tekstId": "Søknadsdato", "type": "dato", "verdi": "12-05-2024" }]
      },
      {
        "tekstId": "brev.blokk.rett-til-aa-klaage",
        "opplysninger": [
          {
            "tekstId": "Arbeidsinntekt siste 12 mnd",
            "type": "penger",
            "verdi": "0"
          },
          { "tekstId": "Inntektskrav for siste 12 mnd", "type": "penger", "verdi": "176000" },
          {
            "tekstId": "Arbeidsinntekt siste 36 mnd",
            "type": "penger",
            "verdi": "58000"
          },
          { "tekstId": "Inntektskrav for siste 36 mnd", "type": "penger", "verdi": "528000" }
        ]
      },
      { "tekstId": "brev.blokk.rett-til-innsyn", "opplysninger": [] },
      { "tekstId": "brev.blokk.sporsmaal", "opplysninger": [] }
    ]

    """.trimIndent()
