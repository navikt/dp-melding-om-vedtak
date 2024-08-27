package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.VedtaksMelding
import java.util.UUID

private val logger = KotlinLogging.logger {}

class Mediator(
    private val behandlingKlient: BehandlingKlient,
    private val sanity: Sanity,
) {
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

//    suspend fun sendVedtak2(
//        behandlingId: UUID,
//        saksbehandler: Saksbehandler,
//    ): List<BrevBlokk> {
//        val behandling =
//            behandlingKlient.hentBehandling(behandlingId, saksbehandler).onFailure { throwable ->
//                logger.error { "Fikk ikke hentet opplysninger for $behandlingId: $throwable" }
//            }.getOrThrow()
//
//        val brevBlokkIder: List<String> = VedtaksMelding(behandling).blokker()
//
//        val opplysningMetadata = sanity.hentOpplysningTekstId(brevBlokkIder)
//
//        opplysningMetadata.map {
//            Opplysning(
//                opplysningTekstId = opplysningMetadata.opplysningTekstId,
//                navn = behandling.navn(metaData.opplysningTekstId),
//                verdi = behandling.hentVerdi(metaData.opplysningTekstId),
//                datatype = behandling.hentType(metaData.opplysningTekstId),
//                opplysningId = behandling.hentOpplysningId(metaData.opplysningTekstId),
//            )
//        }
//    }
}

data class OpenApiVedttak(
    val brevBlokkId: List<String>,
    val opplysninger: List<Opplysning>,
)
