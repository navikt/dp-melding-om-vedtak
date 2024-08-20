package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import no.nav.dagpenger.saksbehandling.api.models.BrevblokkDTO
import no.nav.dagpenger.saksbehandling.api.models.OpplysningDTO

fun Application.meldingOmVedtakApi() {
    apiConfig()
    routing {
        authenticate("azureAd") {
            get("/melding-om-vedtak/{behandlingId}") {
                val behandlingId = call.parameters["behandlingId"]

                val brevblokkDTO =
                    BrevblokkDTO(
                        tekstId = "someTekstId",
                        opplysninger =
                            listOf(
                                OpplysningDTO(
                                    tekstId = "someOpplysningTekstId",
                                    verdi = "someVerdi",
                                ),
                            ),
                    )
                val response = listOf(brevblokkDTO)
                call.respond(status = OK, message = response)
            }
        }
    }
}
