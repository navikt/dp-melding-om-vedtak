package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import java.util.UUID

class Mediator {
    suspend fun sendVedtak(
        behandling: UUID,
        saksbehandler: Saksbehandler,
    ) {
        // hente ut opplysning basert på behandlingId
        // må veksle obo token som ligger i saksbehandler
        // kalle dp-behandling med nytt token og behandlingId
        // ......
        // .....
    }
}

private val logger = KotlinLogging.logger { }
