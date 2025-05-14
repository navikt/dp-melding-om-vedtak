package no.nav.dagpenger.vedtaksmelding.model

import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker
import no.nav.dagpenger.vedtaksmelding.model.vedtak.KlageVedtak
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk
import no.nav.dagpenger.vedtaksmelding.portabletext.Child

class KlagevedtakMelding(
    private val klagevedtak: KlageVedtak,
    alleBrevBlokker: List<BrevBlokk>,
) : Brev {
    private val brevBlokkIder: List<String> =
        listOf(KlageBrevBlokker.KLAGE_OPPRETTHOLDELSE_DEL_1.brevblokkId) + fasteAvsluttendeBlokker

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

    override fun hentFagsakId(): String {
        return klagevedtak.fagsakId
    }

    companion object {
        val fasteAvsluttendeBlokker = emptyList<String>()
    }
}
