package no.nav.dagpenger.vedtaksmelding.helsesjekk

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.helsesjekker() {
    routing {
        get("/isalive") {
            call.respondText("I'm alive!")
        }
        get("/isready") {
            call.respondText("I'm ready!")
        }
    }
}
