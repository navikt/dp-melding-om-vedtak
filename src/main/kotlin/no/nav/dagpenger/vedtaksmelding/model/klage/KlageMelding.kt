package no.nav.dagpenger.vedtaksmelding.model.klage

import no.nav.dagpenger.vedtaksmelding.model.KlageOpplysningTyper
import no.nav.dagpenger.vedtaksmelding.model.KlageOpplysningTyper.ErKlagenSkriftelig
import no.nav.dagpenger.vedtaksmelding.model.KlageOpplysningTyper.ErKlagenUnderskrevet
import no.nav.dagpenger.vedtaksmelding.model.KlageOpplysningTyper.KlageUtfall
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_OPPRETTHOLDELSE_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_OPPRETTHOLDELSE_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Brev
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk
import no.nav.dagpenger.vedtaksmelding.portabletext.Child

class KlageMelding(
    private val klagevedtak: KlageVedtak,
    val alleBrevBlokker: List<BrevBlokk>,
) : Brev {
    private val brevBlokkIder: List<String> =
        opprettholdelse() +
            avvist() +
            fasteAvsluttendeBlokker

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

    private fun opprettholdelse(): List<String> {
        if (!klagevedtak.opplysninger.any { it.opplysningTekstId == KlageUtfall.opplysningTekstId && it.råVerdi() == "OPPRETTHOLDELSE" }) {
            return emptyList()
        }
        return listOf(
            KLAGE_OPPRETTHOLDELSE_DEL_1.brevblokkId,
            KLAGE_OPPRETTHOLDELSE_DEL_2.brevblokkId,
        )
    }

    private fun avvist(): List<String> {
        if (!klagevedtak.opplysninger.any { it.opplysningTekstId == KlageUtfall.opplysningTekstId && it.råVerdi() == "AVVIST" }) {
            return emptyList()
        }
        val brevBlokkIder = mutableListOf<String>()
        if (!klagevedtak.opplysninger.any { it.opplysningTekstId == ErKlagenSkriftelig.opplysningTekstId && !it.råVerdi().toBoolean() }) {
            // todo legg til rett brevblokk
        }
        if (!klagevedtak.opplysninger.any { it.opplysningTekstId == ErKlagenUnderskrevet.opplysningTekstId && !it.råVerdi().toBoolean() }) {
            // todo legg til rett brevblokk
        }
        if (!klagevedtak.opplysninger.any {
                it.opplysningTekstId == KlageOpplysningTyper.KlagenNevnerEndring.opplysningTekstId && !it.råVerdi().toBoolean()
            }
        ) {
            // todo legg til rett brevblokk
        }
        if (!klagevedtak.opplysninger.any {
                it.opplysningTekstId == KlageOpplysningTyper.RettsligKlageinteresse.opplysningTekstId && !it.råVerdi().toBoolean()
            }
        ) {
            // todo legg til rett brevblokk
        }

        return brevBlokkIder.toList()
    }

    // TODO vurder om vi trenger denne for avvist klage
    companion object {
        val fasteAvsluttendeBlokker = emptyList<String>()
    }
}
