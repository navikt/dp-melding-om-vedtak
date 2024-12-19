package no.nav.dagpenger.vedtaksmelding.model

import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.Mediator
import java.util.UUID

private val logger = KotlinLogging.logger {}
private val sikkerlogger = KotlinLogging.logger("tjenestekall")

class VedtaksMelding(private val behandling: Behandling, private val mediator: Mediator) {
    companion object {
        val FASTE_BLOKKER =
            listOf(
                "brev.blokk.sporsmaal",
                "brev.blokk.rett-til-innsyn",
                "brev.blokk.rett-til-aa-klage",
            )
    }

    suspend fun hentOpplysninger(): List<Opplysning> {
        val opplysningstekstIder = mediator.hentOpplysningTekstIder(hentBrevBlokkIder())
        logger.info { "Skal hente opplysninger basert på følgende tekstider: $opplysningstekstIder" }
        return opplysningstekstIder.map { behandling.hentOpplysning(it) }
    }

    fun hentUtvidedeBeskrivelser(): List<UtvidetBeskrivelse> {
        return mediator.hentUtvidedeBeskrivelser(behandling.id)
    }

    fun hentBrevBlokkIder(): List<String> {
        try {
            val blokker = mutableListOf<String>()

            if (oppfyllerMinsteinntekt?.verdi == "false") {
                blokker.add("brev.blokk.vedtak-avslag")
                blokker.add("brev.blokk.begrunnelse-avslag-minsteinntekt")
            }

            if (oppfyllerKravTilDagpenger?.verdi == "true") {
                blokker.add("brev.blokk.vedtak-innvilgelse")
                blokker.add("brev.blokk.hvor-lenge-kan-du-faa-dagpenger")
                blokker.add("brev.blokk.slik-har-vi-beregnet-dagpengene-dine")
                if (antallBarn != null && antallBarn.verdi.toInt() > 0) {
                    blokker.add("brev.blokk.barnetillegg")
                }
                if (nittiProsentRegelBrukt) {
                    blokker.add("brev.blokk.nittiprosentregel")
                }
                if (utførtSammordning) {
                    blokker.add("brev.blokk.samordning")
                }
                blokker.add("brev.blokk.grunnlag")
                blokker.add("brev.blokk.arbeidstiden-din")
                blokker.add("brev.blokk.egenandel")
                blokker.add("brev.blokk.du-maa-sende-meldekort")
                blokker.add("brev.blokk.utbetaling")
                blokker.add("brev.blokk.husk-aa-sjekke-skattekortet-ditt")
                blokker.add("brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du")
                blokker.add("brev.blokk.du-maa-melde-fra-om-endringer")
                blokker.add("brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger")
            }

            val alleBlokker = (blokker + FASTE_BLOKKER).toList()
            return alleBlokker
        } catch (e: Exception) {
            sikkerlogger.error(e) { "Ugyldig vedtak for behandling ${behandling.id}: ${e.message}. Hele behandlingen: $behandling." }
            logger.error(e) { "Ugyldig vedtak for behandling ${behandling.id}: ${e.message}" }
            throw UgyldigVedtakException(behandling.id, e)
        }
    }

    private val oppfyllerMinsteinntekt: Opplysning? =
        behandling.opplysninger.firstOrNull {
            it.opplysningTekstId == "opplysning.krav-til-minsteinntekt"
        }

    private val oppfyllerKravTilDagpenger: Opplysning? =
        behandling.opplysninger.find {
            it.opplysningTekstId == "opplysning.krav-paa-dagpenger"
        }

    private val antallBarn: Opplysning? =
        behandling.opplysninger.firstOrNull {
            it.opplysningTekstId == "opplysning.antall-barn-som-gir-rett-til-barnetillegg"
        }

    private val utførtSammordning: Boolean =
        behandling.opplysninger.find {
            it.opplysningTekstId == "opplysning.har-samordnet"
        }?.verdi.toBoolean()

    private val nittiProsentRegelBrukt: Boolean =
        behandling.opplysninger.any {
            it.opplysningTekstId == "opplysning.andel-av-dagsats-med-barnetillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget" &&
                it.verdi.toDouble() > 0.0
        }
}

internal class UgyldigVedtakException(private val behandlingId: UUID, e: Exception) : RuntimeException(e) {
    override val message: String
        get() = "Ugyldig vedtak for behandling $behandlingId"
}
