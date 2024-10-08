package no.nav.dagpenger.vedtaksmelding

import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.VedtaksMelding
import no.nav.dagpenger.vedtaksmelding.sanity.SanityKlient
import java.util.UUID

private val logger = KotlinLogging.logger {}

class Mediator(
    private val behandlingKlient: BehandlingKlient,
    private val sanityKlient: SanityKlient,
) {
    suspend fun hentVedtaksmelding(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
    ): VedtaksMelding {
        val behandling =
            behandlingKlient.hentBehandling(behandlingId, saksbehandler).onFailure { throwable ->
                logger.error { "Fikk ikke hentet opplysninger for $behandlingId: $throwable" }
            }.getOrThrow()
        return VedtaksMelding(behandling, this)
    }

    suspend fun hentOpplysningTekstIder(brevbklokkIder: List<String>): List<String> {
        return sanityKlient.hentOpplysningTekstIder(brevbklokkIder)
    }
}
