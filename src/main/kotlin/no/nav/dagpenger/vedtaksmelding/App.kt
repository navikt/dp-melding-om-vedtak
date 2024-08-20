package no.nav.dagpenger.vedtaksmelding

import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.helsesjekk.helsesjekker
import no.nav.dagpenger.vedtaksmelding.vedtaksmelding.meldingOmVedtakApi

private val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Starter opp dp-melding-om-vedtak" }
    embeddedServer(CIO, port = 8080) {
        helsesjekker()
        meldingOmVedtakApi()
    }.start(wait = true)
}
