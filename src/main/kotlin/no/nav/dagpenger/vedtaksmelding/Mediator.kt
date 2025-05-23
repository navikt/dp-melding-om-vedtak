package no.nav.dagpenger.vedtaksmelding

import com.fasterxml.jackson.core.type.TypeReference
import mu.KotlinLogging
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakResponseDTO
import no.nav.dagpenger.saksbehandling.api.models.UtvidetBeskrivelseDTO
import no.nav.dagpenger.vedtaksmelding.Configuration.objectMapper
import no.nav.dagpenger.vedtaksmelding.db.VedtaksmeldingRepository
import no.nav.dagpenger.vedtaksmelding.model.Behandlingstype
import no.nav.dagpenger.vedtaksmelding.model.Behandlingstype.Companion.tilBehandlingstype
import no.nav.dagpenger.vedtaksmelding.model.Behandlingstype.KLAGE
import no.nav.dagpenger.vedtaksmelding.model.Behandlingstype.RETT_TIL_DAGPENGER
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.TomtVedtak
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageMelding
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Brev
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk
import no.nav.dagpenger.vedtaksmelding.portabletext.HtmlConverter
import no.nav.dagpenger.vedtaksmelding.sanity.ResultDTO
import no.nav.dagpenger.vedtaksmelding.sanity.SanityKlient
import java.time.LocalDateTime
import java.util.UUID

private val logger = KotlinLogging.logger {}
private val sikkerlogger = KotlinLogging.logger("tjenestekall")

class Mediator(
    private val behandlingKlient: BehandlingKlient,
    private val klageBehandlingKlient: KlageBehandlingKlient,
    private val sanityKlient: SanityKlient,
    private val vedtaksmeldingRepository: VedtaksmeldingRepository,
) {
    suspend fun hentVedtaksmelding(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
        behanldingstype: Behandlingstype,
    ): Brev {
        val sanityInnhold = sanityKlient.hentBrevBlokkerJson()
        vedtaksmeldingRepository.lagreSanityInnhold(behandlingId, sanityInnhold)
        return hentVedtakOgByggVedtaksMelding(
            behandlingId = behandlingId,
            saksbehandler = saksbehandler,
            behandlingstype = behanldingstype,
        ) {
            sanityInnhold
        }
    }

    suspend fun hentEnderligVedtaksmelding(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
        behanldingstype: Behandlingstype,
    ): Brev =
        hentVedtakOgByggVedtaksMelding(
            behandlingId = behandlingId,
            saksbehandler = saksbehandler,
            behandlingstype = behanldingstype,
        ) {
            vedtaksmeldingRepository.hentSanityInnhold(behandlingId)
        }

    private suspend fun hentVedtakOgByggVedtaksMelding(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
        behandlingstype: Behandlingstype,
        sanitySupplier: suspend () -> String,
    ): Brev {
        val sanityInnhold = sanitySupplier.invoke()

        val alleBrevblokker: List<BrevBlokk> =
            objectMapper
                .readValue(
                    sanityInnhold,
                    object : TypeReference<ResultDTO>() {},
                ).result
        return when (behandlingstype) {
            RETT_TIL_DAGPENGER -> {
                val vedtak =
                    behandlingKlient
                        .hentVedtak(
                            behandlingId = behandlingId,
                            saksbehandler = saksbehandler,
                        ).onFailure { throwable ->
                            logger.error { "Fikk ikke hentet vedtak for behandling $behandlingId: $throwable" }
                        }.getOrThrow()

                runCatching {
                    VedtakMelding.byggVedtaksmelding(vedtak, alleBrevblokker)
                }.getOrElse {
                    when {
                        Configuration.isDev -> {
                            logger.error(it) { "Lager tomt vedtak i dev men feil er: ${it.message}" }
                            TomtVedtak(
                                vedtak = vedtak,
                                alleBrevblokker = alleBrevblokker,
                            )
                        }
                        else -> {
                            throw it
                        }
                    }
                }
            }

            KLAGE -> {
                val vedtak =
                    klageBehandlingKlient
                        .hentVedtak(
                            behandlingId = behandlingId,
                            saksbehandler = saksbehandler,
                        ).onFailure { throwable ->
                            logger.error { "Fikk ikke hentet vedtak for behandling $behandlingId: $throwable" }
                        }.getOrThrow()

                KlageMelding(
                    klagevedtak = vedtak,
                    alleBrevBlokker = alleBrevblokker,
                )
            }
        }
    }

    fun lagreUtvidetBeskrivelse(utvidetBeskrivelse: UtvidetBeskrivelse): LocalDateTime = vedtaksmeldingRepository.lagre(utvidetBeskrivelse)

    private fun hentUtvidedeBeskrivelser(
        behandlingId: UUID,
        vedtaksMelding: Brev,
    ): List<UtvidetBeskrivelse> {
        val tekstmapping =
            vedtaksmeldingRepository.hentUtvidedeBeskrivelserFor(behandlingId).associateBy { it.brevblokkId }
        return vedtaksMelding
            .hentBrevBlokker()
            .filter { it.utvidetBeskrivelse }
            .map {
                UtvidetBeskrivelse(
                    behandlingId = behandlingId,
                    brevblokkId = it.textId,
                    tekst = tekstmapping[it.textId]?.tekst,
                    sistEndretTidspunkt = LocalDateTime.now(),
                    tittel = it.title,
                )
            }.also {
                sikkerlogger.info { "Hentet utvidede beskrivelser for behandlingId=$behandlingId: $it" }
            }
    }

    suspend fun hentVedtak(
        behandlingId: UUID,
        behandler: Saksbehandler,
        meldingOmVedtakData: MeldingOmVedtakDataDTO,
    ): MeldingOmVedtakResponseDTO {
        val vedtak =
            hentVedtaksmelding(
                behandlingId = behandlingId,
                saksbehandler = behandler,
                behanldingstype = meldingOmVedtakData.behandlingstype.tilBehandlingstype(),
            )

        val html =
            HtmlConverter.toHtml(
                vedtak.hentBrevBlokker(),
                vedtak.hentOpplysninger(),
                meldingOmVedtakData,
                vedtak.hentFagsakId(),
            )

        return MeldingOmVedtakResponseDTO(
            html = html,
            utvidedeBeskrivelser =
                hentUtvidedeBeskrivelser(behandlingId, vedtak).map {
                    UtvidetBeskrivelseDTO(
                        brevblokkId = it.brevblokkId,
                        tekst = it.tekst ?: "",
                        sistEndretTidspunkt = it.sistEndretTidspunkt ?: LocalDateTime.now(),
                        tittel = it.tittel,
                    )
                },
        )
    }

    suspend fun hentEndeligVedtak(
        behandlingId: UUID,
        behandler: Saksbehandler,
        meldingOmVedtakData: MeldingOmVedtakDataDTO,
    ): String {
        val html =
            hentEnderligVedtaksmelding(
                behandlingId,
                behandler,
                meldingOmVedtakData.behandlingstype.tilBehandlingstype(),
            ).let { vedtak ->

                HtmlConverter.toHtml(
                    vedtak.hentBrevBlokker(),
                    vedtak.hentOpplysninger(),
                    meldingOmVedtakData,
                    vedtak.hentFagsakId(),
                    hentUtvidedeBeskrivelser(behandlingId, vedtak).toSet(),
                )
            }
        vedtaksmeldingRepository.lagreVedaksmeldingHtml(behandlingId, html)
        return html
    }
}
