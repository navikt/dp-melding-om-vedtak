package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.VedtaksMelding
import java.util.UUID

private val logger = KotlinLogging.logger {}

class Mediator(private val behandlingKlient: BehandlingKlient) {
    suspend fun sendVedtak(
        behandling: UUID,
        saksbehandler: Saksbehandler,
    ): Pair<List<String>, Set<Opplysning>> {
        val behandling =
            behandlingKlient.hentBehandling(behandling, saksbehandler).onFailure { throwable ->
                logger.error { "Fikk ikke hentet opplysninger for $behandling: $throwable" }
            }.getOrThrow()

        return VedtaksMelding(behandling).hubba()
    }
}
