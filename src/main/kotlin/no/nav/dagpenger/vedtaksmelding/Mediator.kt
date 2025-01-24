package no.nav.dagpenger.vedtaksmelding

import mu.KotlinLogging
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.vedtaksmelding.db.VedtaksmeldingRepository
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import no.nav.dagpenger.vedtaksmelding.model.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk
import no.nav.dagpenger.vedtaksmelding.portabletext.HtmlConverter
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
        // val sanityInnholdDatabase = vedtaksmeldingRepository.hentSanityInnhold(behandlingId)
        // val alleBrevblokker = Json.decodeFromString<ResultDTO>(sanityInnhold).result //Todo: dette mikker i testen

        val alleBrevblokker = sanityKlient.hentBrevBlokker()
        return behandlingKlient.hentVedtak(
            behandlingId = behandlingId,
            saksbehandler = saksbehandler,
        ).onFailure { throwable ->
            logger.error { "Fikk ikke hentet vedtak for behandling $behandlingId: $throwable" }
        }.map { Vedtaksmelding.byggVedtaksmelding(it, alleBrevblokker) }
        // bygg vetakmelding kan ha sanityInnhold som parameter og utvidet beskrivelse som inputtparametere
    }

/*    suspend fun hentVedtaksmelding2(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
    ): Result<Vedtaksmelding> {
        val vedtak = behandlingKlient.hentVedtak(behandlingId, saksbehandler)
        val sanityInnhold = sanityKlient.hentBrevBlokkerJson()
        // lagre sanituInnhold i database
        // bygg vetakmelding kan ha sanityInnhold som parameter og utvidet beskrivelse som inputtparametere
    }*/

    suspend fun hentOpplysningTekstIder(brevbklokkIder: List<String>): List<String> {
        return sanityKlient.hentOpplysningTekstIder(brevbklokkIder)
    }

    suspend fun hentBrevBlokker(brevbklokkIder: List<String>): List<BrevBlokk> {
        val brevblokkInnhold = sanityKlient.hentBrevBlokker().associateBy { it.textId }
        return brevbklokkIder.map { brevblokkInnhold[it] ?: throw RuntimeException("Fant ikke brevblokk med id $it") }
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
        val tekstmapping = vedtaksmeldingRepository.hentUtvidedeBeskrivelserFor(behandlingId).associateBy { it.brevblokkId }
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
        return hentVedtaksmelding(behandlingId, behandler).map { vedtak ->
            HtmlConverter.toHtml(vedtak.hentBrevBlokker(), vedtak.hentOpplysninger(), meldingOmVedtakData)
        }.getOrThrow()
    }
}
