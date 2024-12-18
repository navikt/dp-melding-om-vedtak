package no.nav.dagpenger.vedtaksmelding

import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.db.VedtaksmeldingRepository
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import no.nav.dagpenger.vedtaksmelding.model.VedtaksMelding
import no.nav.dagpenger.vedtaksmelding.model.VedtaksMelding2
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk
import no.nav.dagpenger.vedtaksmelding.sanity.SanityKlient
import java.time.LocalDateTime
import java.util.UUID

private val logger = KotlinLogging.logger {}

class Mediator(
    private val behandlingKlient: BehandlingKlient,
    private val sanityKlient: SanityKlient,
    private val vedtaksmeldingRepository: VedtaksmeldingRepository,
) {
    suspend fun hentVedtaksmelding2(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
    ): VedtaksMelding2 {
        return behandlingKlient.hentVedtak(
            behandlingId = behandlingId,
            saksbehandler = saksbehandler,
        ).onFailure { throwable ->
            logger.error { "Fikk ikke hentet vedtak for behandling $behandlingId: $throwable" }
        }.map { VedtaksMelding2.byggVedtaksMelding(it, this) }.getOrThrow()
    }

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

    suspend fun hentBrevBlokker(brevbklokkIder: List<String>): List<BrevBlokk> {
        return sanityKlient.hentBrevBlokker().filter { brevbklokkIder.contains(it.textId) }
    }

    fun lagreUtvidetBeskrivelse(utvidetBeskrivelse: UtvidetBeskrivelse): LocalDateTime {
        return vedtaksmeldingRepository.lagre(utvidetBeskrivelse)
    }

    fun hentUtvidedeBeskrivelser(behandlingId: UUID): List<UtvidetBeskrivelse> {
        return vedtaksmeldingRepository.hentUtvidedeBeskrivelserFor(behandlingId)
    }
}
