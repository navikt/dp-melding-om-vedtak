package no.nav.dagpenger.vedtaksmelding

import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import no.nav.dagpenger.vedtaksmelding.helsesjekk.helsesjekker
import no.nav.dagpenger.vedtaksmelding.vedtaksmelding.meldingOmVedtakApi

fun main() {
    embeddedServer(CIO, port = 8080) {
        helsesjekker()
        meldingOmVedtakApi()
    }.start(wait = true)
}
