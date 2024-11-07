package no.nav.dagpenger.vedtaksmelding

import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.helsesjekk.helsesjekker
import no.nav.dagpenger.vedtaksmelding.sanity.SanityKlient

private val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Starter opp dp-melding-om-vedtak" }
    // runMigration()
    val behandlingKlient =
        BehandlngHttpKlient(
            dpBehandlingApiUrl = Configuration.dbBehandlingApiUrl,
            tokenProvider = Configuration.dpBehandlingOboExchanger,
        )

    val sanityKlient = SanityKlient(Configuration.sanityApiUrl)

    embeddedServer(CIO, port = 8080) {
        helsesjekker()
        meldingOmVedtakApi(Mediator(behandlingKlient, sanityKlient))
    }.start(wait = true)
}
