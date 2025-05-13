package no.nav.dagpenger.vedtaksmelding

import com.fasterxml.jackson.core.type.TypeReference
import mu.KotlinLogging
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakResponseDTO
import no.nav.dagpenger.saksbehandling.api.models.UtvidetBeskrivelseDTO
import no.nav.dagpenger.vedtaksmelding.Configuration.objectMapper
import no.nav.dagpenger.vedtaksmelding.db.VedtaksmeldingRepository
import no.nav.dagpenger.vedtaksmelding.model.Brev
import no.nav.dagpenger.vedtaksmelding.model.KlagevedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.avslag.TomtVedtak
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
    ): VedtakMelding {
        val sanityInnhold = sanityKlient.hentBrevBlokkerJson()
        vedtaksmeldingRepository.lagreSanityInnhold(behandlingId, sanityInnhold)
        return hentVedtakOgByggVedtaksMelding(behandlingId, saksbehandler) { sanityInnhold }
    }

    suspend fun hentKlageVedtaksmelding(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
    ): KlagevedtakMelding {
        val sanityInnhold = sanityKlient.hentBrevBlokkerJson()
        vedtaksmeldingRepository.lagreSanityInnhold(behandlingId, sanityInnhold)
        return hentVedtakOgByggKlageVedtaksMelding(behandlingId, saksbehandler) { sanityInnhold }
    }

    suspend fun hentEnderligVedtaksmelding(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
    ): VedtakMelding {
        return hentVedtakOgByggVedtaksMelding(behandlingId, saksbehandler) {
            vedtaksmeldingRepository.hentSanityInnhold(behandlingId)
        }
    }

    private suspend fun hentVedtakOgByggKlageVedtaksMelding(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
        sanitySupplier: suspend () -> String,
    ): KlagevedtakMelding {
        val sanityInnhold = sanitySupplier.invoke()

        val alleBrevblokker: List<BrevBlokk> =
            objectMapper.readValue(
                sanityInnhold,
                object : TypeReference<ResultDTO>() {},
            ).result
        val vedtak =
            klageBehandlingKlient.hentVedtak(
                behandlingId = behandlingId,
                saksbehandler = saksbehandler,
            ).onFailure { throwable ->
                logger.error { "Fikk ikke hentet vedtak for behandling $behandlingId: $throwable" }
            }.getOrThrow()
        return KlagevedtakMelding(
            klagevedtak = vedtak,
            alleBrevBlokker = alleBrevblokker,
        )
    }

    private suspend fun hentVedtakOgByggVedtaksMelding(
        behandlingId: UUID,
        saksbehandler: Saksbehandler,
        sanitySupplier: suspend () -> String,
    ): VedtakMelding {
        val sanityInnhold = sanitySupplier.invoke()

        val alleBrevblokker: List<BrevBlokk> =
            objectMapper.readValue(
                sanityInnhold,
                object : TypeReference<ResultDTO>() {},
            ).result
        val vedtak =
            behandlingKlient.hentVedtak(
                behandlingId = behandlingId,
                saksbehandler = saksbehandler,
            ).onFailure { throwable ->
                logger.error { "Fikk ikke hentet vedtak for behandling $behandlingId: $throwable" }
            }.getOrThrow()

        return runCatching {
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

    fun lagreUtvidetBeskrivelse(utvidetBeskrivelse: UtvidetBeskrivelse): LocalDateTime {
        return vedtaksmeldingRepository.lagre(utvidetBeskrivelse)
    }

    private fun hentUtvidedeBeskrivelser(
        behandlingId: UUID,
        vedtaksMelding: Brev,
    ): List<UtvidetBeskrivelse> {
        val tekstmapping =
            vedtaksmeldingRepository.hentUtvidedeBeskrivelserFor(behandlingId).associateBy { it.brevblokkId }
        return vedtaksMelding.hentBrevBlokker().filter { it.utvidetBeskrivelse }.map {
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
        type: String = "rettTilDagpenger",
    ): MeldingOmVedtakResponseDTO {
        return when (type) {
            "rettTilDagpenger" -> {
                hentVedtaksmelding(behandlingId, behandler).let { vedtak ->
                    val html =
                        HtmlConverter.toHtml(
                            vedtak.hentBrevBlokker(),
                            vedtak.hentOpplysninger(),
                            meldingOmVedtakData,
                            vedtak.hentFagsakId(),
                        )

                    MeldingOmVedtakResponseDTO(
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
            }
            "klage" -> {
                hentKlageVedtaksmelding(behandlingId, behandler).let { vedtak ->
                    val html =
                        HtmlConverter.toHtml(
                            brevBlokker = vedtak.hentBrevBlokker(),
                            opplysninger = vedtak.hentOpplysninger(),
                            meldingOmVedtakData = meldingOmVedtakData,
                            fagsakId = vedtak.hentFagsakId(),
                        )

                    MeldingOmVedtakResponseDTO(
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
            }
            else -> {
                throw IllegalArgumentException("Ugyldig type: $type")
            }
        }
    }

    suspend fun hentEndeligVedtak(
        behandlingId: UUID,
        behandler: Saksbehandler,
        meldingOmVedtakData: MeldingOmVedtakDataDTO,
    ): String {
        val html =
            hentEnderligVedtaksmelding(behandlingId, behandler).let { vedtak ->

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
