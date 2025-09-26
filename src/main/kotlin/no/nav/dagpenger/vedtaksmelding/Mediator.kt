package no.nav.dagpenger.vedtaksmelding

import com.fasterxml.jackson.core.type.TypeReference
import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.dagpenger.saksbehandling.api.models.BrevVariantDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakResponseDTO
import no.nav.dagpenger.saksbehandling.api.models.UtvidetBeskrivelseDTO
import no.nav.dagpenger.vedtaksmelding.Configuration.objectMapper
import no.nav.dagpenger.vedtaksmelding.apiconfig.Klient
import no.nav.dagpenger.vedtaksmelding.apiconfig.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.db.VedtaksmeldingRepository
import no.nav.dagpenger.vedtaksmelding.model.Behandlingstype
import no.nav.dagpenger.vedtaksmelding.model.Behandlingstype.Companion.tilBehandlingstype
import no.nav.dagpenger.vedtaksmelding.model.Behandlingstype.KLAGE
import no.nav.dagpenger.vedtaksmelding.model.Behandlingstype.MANUELL
import no.nav.dagpenger.vedtaksmelding.model.Behandlingstype.MELDEKORT
import no.nav.dagpenger.vedtaksmelding.model.Behandlingstype.RETT_TIL_DAGPENGER
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.TomtVedtak
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageMelding
import no.nav.dagpenger.vedtaksmelding.model.vedtak.BrevKomponenter
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
    fun lagreBrevVariant(
        behandlingId: UUID,
        brevVariant: BrevVariantDTO,
    ) {
        vedtaksmeldingRepository.lagreBrevVariant(
            behandlingId = behandlingId,
            brevVariant = brevVariant,
        )
    }

    fun lagreUtvidetBeskrivelse(utvidetBeskrivelse: UtvidetBeskrivelse): LocalDateTime = vedtaksmeldingRepository.lagre(utvidetBeskrivelse)

    suspend fun hentForhåndsvisning(
        behandlingId: UUID,
        klient: Klient,
        meldingOmVedtakData: MeldingOmVedtakDataDTO,
    ): MeldingOmVedtakResponseDTO {
        return when (vedtaksmeldingRepository.hentBrevVariant(behandlingId)) {
            BrevVariantDTO.GENERERT -> hentGenerertForhåndsvisning(behandlingId, klient, meldingOmVedtakData)
            BrevVariantDTO.EGENDEFINERT -> hentEgendefinertBrev(behandlingId, meldingOmVedtakData)
        }
    }

    suspend fun hentEndeligBrev(
        behandlingId: UUID,
        klient: Klient,
        meldingOmVedtakData: MeldingOmVedtakDataDTO,
    ): String {
        return when (vedtaksmeldingRepository.hentBrevVariant(behandlingId)) {
            BrevVariantDTO.GENERERT -> hentGenerertEndeligBrev(behandlingId, klient, meldingOmVedtakData)
            BrevVariantDTO.EGENDEFINERT -> {
                hentEgendefinertBrev(behandlingId, meldingOmVedtakData).html.also {
                    vedtaksmeldingRepository.lagreVedaksmeldingHtml(behandlingId, it)
                }
            }
        }
    }

    suspend fun hentBrevKomponenterOgLagre(
        behandlingId: UUID,
        klient: Klient,
        behanldingstype: Behandlingstype,
    ): BrevKomponenter {
        val sanityInnhold = sanityKlient.hentBrevBlokkerJson()
        vedtaksmeldingRepository.lagreSanityInnhold(behandlingId, sanityInnhold)
        return hentBrevKomponenter(
            behandlingId = behandlingId,
            klient = klient,
            behandlingstype = behanldingstype,
        ) {
            sanityInnhold
        }
    }

    suspend fun hentEndeligeBrevKomponenter(
        behandlingId: UUID,
        klient: Klient,
        behanldingstype: Behandlingstype,
    ): BrevKomponenter =
        hentBrevKomponenter(
            behandlingId = behandlingId,
            klient = klient,
            behandlingstype = behanldingstype,
        ) {
            runCatching {
                vedtaksmeldingRepository.hentSanityInnhold(behandlingId)
            }.onFailure {
                logger.warn { "Fikk ikke hentet brevtekster fra database for behandling $behandlingId - henter på nytt fra Sanity" }
            }.getOrDefault(
                sanityKlient.hentBrevBlokkerJson(),
            )
        }

    private suspend fun hentBrevKomponenter(
        behandlingId: UUID,
        klient: Klient,
        behandlingstype: Behandlingstype,
        sanitySupplier: suspend () -> String,
    ): BrevKomponenter {
        val sanityInnhold = sanitySupplier.invoke()

        val alleBrevblokker: List<BrevBlokk> =
            objectMapper
                .readValue(
                    sanityInnhold,
                    object : TypeReference<ResultDTO>() {},
                ).result

        logger.info { "Henter brevkKomponenter for behandlingtype: $behandlingstype" }

        return when (behandlingstype) {
            RETT_TIL_DAGPENGER -> {
                val vedtak =
                    behandlingKlient
                        .hentBehandlingResultat(
                            behandlingId = behandlingId,
                            klient = klient,
                        ).onFailure { throwable ->
                            logger.error { "Feil ved henting av behandlingsresultat for behandlingId $behandlingId: $throwable" }
                        }.getOrThrow()

                runCatching {
                    Vedtaksmelding.byggVedtaksmelding(vedtak, alleBrevblokker)
                }.getOrElse {
                    when {
                        Configuration.isDev -> {
                            logger.error(it) { "Lager tomt vedtak i dev, men feil er: ${it.message}" }
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
                            // todo unsafe cast?
                            klient = klient as Saksbehandler,
                        ).onFailure { throwable ->
                            logger.error { "Fikk ikke hentet vedtak for behandling $behandlingId: $throwable" }
                        }.getOrThrow()

                KlageMelding(
                    klagevedtak = vedtak,
                    alleBrevBlokker = alleBrevblokker,
                )
            }

            // TODO: Må kunne tilby egendefinert brev
            MELDEKORT -> throw NotImplementedError("Meldekortbehandling har ikke støtte for vedtaksmelding")
            // TODO: Må kunne tilby egendefinert brev
            MANUELL -> throw NotImplementedError("Manuell behandling har ikke støtte for vedtaksmelding")
        }
    }

    private fun hentUtvidedeBeskrivelser(
        behandlingId: UUID,
        brevKomponenter: BrevKomponenter,
    ): List<UtvidetBeskrivelse> {
        val tekstMapping =
            vedtaksmeldingRepository.hentUtvidedeBeskrivelserFor(behandlingId).associateBy { it.brevblokkId }
        return brevKomponenter
            .hentBrevBlokker()
            .filter { it.utvidetBeskrivelse }
            .map {
                UtvidetBeskrivelse(
                    behandlingId = behandlingId,
                    brevblokkId = it.textId,
                    tekst = tekstMapping[it.textId]?.tekst,
                    sistEndretTidspunkt = LocalDateTime.now(),
                    tittel = it.title,
                )
            }.also {
                sikkerlogger.info { "Hentet utvidede beskrivelser for behandlingId=$behandlingId: $it" }
            }
    }

    private fun hentEgendefinertBrev(
        behandlingId: UUID,
        meldingOmVedtakData: MeldingOmVedtakDataDTO,
    ): MeldingOmVedtakResponseDTO {
        val utvidetBeskrivelse =
            vedtaksmeldingRepository.hentUtvidedeBeskrivelserFor(behandlingId)
                .singleOrNull { it.brevblokkId == "brev.blokk.egendefinert" }
                ?: UtvidetBeskrivelse(
                    behandlingId = behandlingId,
                    brevblokkId = "brev.blokk.egendefinert",
                    tekst = "",
                    sistEndretTidspunkt = null,
                    tittel = "Egendefinert",
                )

        val utvidedeBeskrivelser = setOf(utvidetBeskrivelse)

        return MeldingOmVedtakResponseDTO(
            html = HtmlConverter.toHtml(meldingOmVedtakData, utvidedeBeskrivelser),
            utvidedeBeskrivelser =
                utvidedeBeskrivelser.map {
                    UtvidetBeskrivelseDTO(
                        brevblokkId = it.brevblokkId,
                        tekst = it.tekst ?: "",
                        sistEndretTidspunkt = it.sistEndretTidspunkt ?: LocalDateTime.now(),
                        tittel = it.tittel,
                    )
                },
            brevVariant = BrevVariantDTO.EGENDEFINERT,
        )
    }

    private suspend fun hentGenerertForhåndsvisning(
        behandlingId: UUID,
        klient: Klient,
        meldingOmVedtakData: MeldingOmVedtakDataDTO,
    ): MeldingOmVedtakResponseDTO {
        val brevKomponenter =
            hentBrevKomponenterOgLagre(
                behandlingId = behandlingId,
                klient = klient,
                behanldingstype = meldingOmVedtakData.behandlingstype.tilBehandlingstype(),
            )

        val html =
            HtmlConverter.toHtml(
                brevBlokker = brevKomponenter.hentBrevBlokker(),
                opplysninger = brevKomponenter.hentOpplysninger(),
                meldingOmVedtakData = meldingOmVedtakData,
            )

        return MeldingOmVedtakResponseDTO(
            html = html,
            utvidedeBeskrivelser =
                hentUtvidedeBeskrivelser(behandlingId, brevKomponenter).map {
                    UtvidetBeskrivelseDTO(
                        brevblokkId = it.brevblokkId,
                        tekst = it.tekst ?: "",
                        sistEndretTidspunkt = it.sistEndretTidspunkt ?: LocalDateTime.now(),
                        tittel = it.tittel,
                    )
                },
            brevVariant = BrevVariantDTO.GENERERT,
        )
    }

    private suspend fun hentGenerertEndeligBrev(
        behandlingId: UUID,
        klient: Klient,
        meldingOmVedtakData: MeldingOmVedtakDataDTO,
    ): String {
        val html =
            hentEndeligeBrevKomponenter(
                behandlingId = behandlingId,
                klient = klient,
                behanldingstype = meldingOmVedtakData.behandlingstype.tilBehandlingstype(),
            ).let { vedtak ->
                HtmlConverter.toHtml(
                    brevBlokker = vedtak.hentBrevBlokker(),
                    opplysninger = vedtak.hentOpplysninger(),
                    meldingOmVedtakData = meldingOmVedtakData,
                    utvidetBeskrivelse = hentUtvidedeBeskrivelser(behandlingId, vedtak).toSet(),
                )
            }
        vedtaksmeldingRepository.lagreVedaksmeldingHtml(behandlingId, html)
        return html
    }
}
