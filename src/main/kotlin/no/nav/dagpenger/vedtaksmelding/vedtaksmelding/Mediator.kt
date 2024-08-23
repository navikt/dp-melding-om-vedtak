package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.VedtaksMelding
import java.util.UUID

private val logger = KotlinLogging.logger {}

class Mediator(private val behandlingKlient: BehandlingKlient) {
    suspend fun sendVedtak(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
    ): List<String> {
        val behandling =
            behandlingKlient.hentBehandling(behandlingId, saksbehandler).onFailure { throwable ->
                logger.error { "Fikk ikke hentet opplysninger for $behandlingId: $throwable" }
            }.getOrThrow()

        return VedtaksMelding(behandling).blokker()
    }
}
