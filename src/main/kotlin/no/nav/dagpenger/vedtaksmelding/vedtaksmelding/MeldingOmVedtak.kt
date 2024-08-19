package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.meldingOmVedtak() {
    routing {
        get("/melding-om-vedtak/{behandlingId}") {
            val behandlingId = call.parameters["behandlingId"]

            //language=JSON
            val brevblokk = """
                {
                    "tekstId": "someTekstId",
                    "opplysninger": [
                        {
                            "tekstId": "someOpplysningTekstId",
                            "verdi": "someVerdi"
                        }
                    ]
                }
            """
            val response =
                listOf(
                    brevblokk,
                )
            call.respond(status = io.ktor.http.HttpStatusCode.OK, message = response)
        }
    }
}
