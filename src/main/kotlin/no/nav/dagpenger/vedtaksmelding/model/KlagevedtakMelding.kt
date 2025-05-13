package no.nav.dagpenger.vedtaksmelding.model

import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding.FasteBrevblokker
import no.nav.dagpenger.vedtaksmelding.model.vedtak.KlageVedtak
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk
import no.nav.dagpenger.vedtaksmelding.portabletext.Child

class KlagevedtakMelding(
    private val klagevedtak: KlageVedtak,
    alleBrevBlokker: List<BrevBlokk>,
) : Brev {
    private val brevBlokkIder: List<String> = fasteAvsluttendeBlokker
    private val brevBlokker: List<BrevBlokk> =
        run {
            val brevBlokkMap = alleBrevBlokker.associateBy { it.textId }
            brevBlokkIder().mapNotNull { id -> brevBlokkMap[id] }
        }

    override fun brevBlokkIder(): List<String> = brevBlokkIder

    override fun hentBrevBlokker(): List<BrevBlokk> = brevBlokker

    override fun hentOpplysninger(): List<Opplysning> {
        return brevBlokker.asSequence()
            .filter { it.textId in brevBlokkIder() }
            .flatMap { it.innhold }
            .flatMap { it.children }
            .filterIsInstance<Child.OpplysningReference>()
            .map { it.behandlingOpplysning.textId }
            .map { klagevedtak.hentOpplysning(it) }
            .toList()
    }

    fun hentFagsakId(): String {
        return klagevedtak.fagsakId
    }

    companion object {
        val fasteAvsluttendeBlokker =
            listOf(
                FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
    }
}
