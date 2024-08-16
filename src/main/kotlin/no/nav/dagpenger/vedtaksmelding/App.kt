package no.nav.dagpenger.vedtaksmelding

import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import no.nav.dagpenger.vedtaksmelding.helsesjekk.helsesjekker

fun main() {
    embeddedServer(CIO, port = 8080) {
        helsesjekker()
    }.start(wait = true)
}
