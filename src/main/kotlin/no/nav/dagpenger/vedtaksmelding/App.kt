package no.nav.dagpenger.vedtaksmelding

import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.db.PostgresDataSourceBuilder.runMigration
import no.nav.dagpenger.vedtaksmelding.helsesjekk.helsesjekker
import no.nav.dagpenger.vedtaksmelding.sanity.SanityKlient
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Starter opp dp-melding-om-vedtak" }
    runCatching { runMigration() }
        .onFailure { logger.error(it) { "Klarte ikke å migrere databasen. Stanser appen" } }
        .onSuccess { logger.info { "Databasen migrert med $it migreringer" } }
        .getOrElse { exitProcess(1) }

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
