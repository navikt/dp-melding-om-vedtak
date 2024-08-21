package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import org.intellij.lang.annotations.Language
import java.util.UUID

fun Application.meldingOmVedtakApi(mediator: Mediator) {
    apiConfig()
    routing {
        authenticate("azureAd") {
            get("/melding-om-vedtak/{behandlingId}") {
                val behandlingId = call.parseUUID("behandlingId")
                val saksbehandler = call.parseSaksbehandler()
                mediator.sendVedtak(behandlingId, saksbehandler)
                call.respond(status = OK, message = objectMapper.readValue(hubba))
            }
        }
    }
}

private fun ApplicationCall.parseSaksbehandler(): Saksbehandler = Saksbehandler(this.request.jwt())

private fun ApplicationCall.parseUUID(s: String): UUID {
    return this.parameters["behandlingId"]?.let {
        UUID.fromString(it)
    } ?: throw IllegalArgumentException("")
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
