package no.nav.dagpenger.vedtaksmelding

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.vedtaksmelding.Configuration.objectMapper
import no.nav.dagpenger.vedtaksmelding.db.VedtaksmeldingRepository
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import no.nav.dagpenger.vedtaksmelding.model.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk
import no.nav.dagpenger.vedtaksmelding.portabletext.HtmlConverter
import no.nav.dagpenger.vedtaksmelding.sanity.ResultDTO
import no.nav.dagpenger.vedtaksmelding.sanity.SanityKlient
import java.time.LocalDateTime
import java.util.UUID

private val logger = KotlinLogging.logger {}

class Mediator(
    private val behandlingKlient: BehandlingKlient,
    private val sanityKlient: SanityKlient,
    private val vedtaksmeldingRepository: VedtaksmeldingRepository,
) {
    suspend fun hentVedtaksmelding(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
    ): Result<Vedtaksmelding> {
        val sanityInnhold = sanityKlient.hentBrevBlokkerJson()
        vedtaksmeldingRepository.lagreSanityInnhold(behandlingId, sanityInnhold)
        return hentVedtakOgByggVedtaksMelding(behandlingId, saksbehandler) { sanityInnhold }
    }

    suspend fun hentEnderligVedtaksmelding(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
    ): Result<Vedtaksmelding> {
        return hentVedtakOgByggVedtaksMelding(behandlingId, saksbehandler) {
            vedtaksmeldingRepository.hentSanityInnhold(behandlingId)
        }
    }

    private suspend fun hentVedtakOgByggVedtaksMelding(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
        sanitySupplier: suspend () -> String,
    ): Result<Vedtaksmelding> {
        val sanityInnhold = sanitySupplier.invoke()

        val alleBrevblokker: List<BrevBlokk> =
            objectMapper.readValue(
                sanityInnhold,
                object : TypeReference<ResultDTO>() {},
            ).result
        return behandlingKlient.hentVedtak(
            behandlingId = behandlingId,
            saksbehandler = saksbehandler,
        ).onFailure { throwable ->
            logger.error { "Fikk ikke hentet vedtak for behandling $behandlingId: $throwable" }
        }.map { Vedtaksmelding.byggVedtaksmelding(it, alleBrevblokker) }
    }

    fun lagreUtvidetBeskrivelse(utvidetBeskrivelse: UtvidetBeskrivelse): LocalDateTime {
        return vedtaksmeldingRepository.lagre(utvidetBeskrivelse)
    }

    fun hentUtvidedeBeskrivelser(behandlingId: UUID): List<UtvidetBeskrivelse> {
        return vedtaksmeldingRepository.hentUtvidedeBeskrivelserFor(behandlingId)
    }

    suspend fun hentUtvidedeBeskrivelser(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
    ): List<UtvidetBeskrivelse> {
        val tekstmapping =
            vedtaksmeldingRepository.hentUtvidedeBeskrivelserFor(behandlingId).associateBy { it.brevblokkId }
        return hentVedtaksmelding(behandlingId, saksbehandler).map { vedtaksmelding ->
            vedtaksmelding.hentBrevBlokker().filter { it.utvidetBeskrivelse }.map {
                UtvidetBeskrivelse(
                    behandlingId = behandlingId,
                    brevblokkId = it.textId,
                    tekst = tekstmapping[it.textId]?.tekst,
                    sistEndretTidspunkt = LocalDateTime.now(),
                    tittel = it.title,
                )
            }
        }.getOrThrow()
    }

    suspend fun hentVedtaksHtml(
        behandlingId: UUID,
        behandler: Saksbehandler,
        meldingOmVedtakData: MeldingOmVedtakDataDTO,
    ): String {
        val html =
            hentVedtaksmelding(behandlingId, behandler).map { vedtak ->
                HtmlConverter.toHtml(vedtak.hentBrevBlokker(), vedtak.hentOpplysninger(), meldingOmVedtakData)
            }.getOrThrow()
        return html
    }

    suspend fun hentEndeligVedtak(
        behandlingId: UUID,
        behandler: Saksbehandler,
        meldingOmVedtakData: MeldingOmVedtakDataDTO,
    ): String {
        val html =
            hentEnderligVedtaksmelding(behandlingId, behandler).map { vedtak ->
                HtmlConverter.toHtml(vedtak.hentBrevBlokker(), vedtak.hentOpplysninger(), meldingOmVedtakData)
            }.getOrThrow()
        vedtaksmeldingRepository.lagreVedaksmeldingHtml(behandlingId, html)
        return html
    }
}
