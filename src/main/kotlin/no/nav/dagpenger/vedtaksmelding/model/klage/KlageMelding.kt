package no.nav.dagpenger.vedtaksmelding.model.klage

import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.FasteBrevblokker.HJELP_FRA_ANDRE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.FasteBrevblokker.RETT_TIL_INNSYN
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.FasteBrevblokker.RETT_TIL_Å_KLAGE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.FasteBrevblokker.SPØRSMÅL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.FasteBrevblokker.VEILEDNING_FRA_NAV
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_INTRO
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_KLAGEFIRST
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_KLAGEFIRST_HJEMMEL
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_NEVNER_ENDRING
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_HJEMMEL
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_SKRIFTLIG_OG_NEVNER_ENDRING_HJEMMEL
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_SKRIFTLIG_OG_SIGNERT
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_OPPRETTHOLDELSE_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_OPPRETTHOLDELSE_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.vedtak.BrevKomponenter
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk
import no.nav.dagpenger.vedtaksmelding.portabletext.Child

class KlageMelding(
    private val klagevedtak: KlageVedtak,
    val alleBrevBlokker: List<BrevBlokk>,
) : BrevKomponenter {
    private val brevBlokkIder: List<String> =
        opprettholdelse() +
            avvist()

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

    private fun opprettholdelse(): List<String> {
        return if (!klagevedtak.any<KlageOpplysning.KlageUtfall> { it.verdi.uppercase() == "OPPRETTHOLDELSE" }) {
            emptyList()
        } else {
            listOf(
                KLAGE_OPPRETTHOLDELSE_DEL_1.brevblokkId,
                KLAGE_OPPRETTHOLDELSE_DEL_2.brevblokkId,
            ) + fasteAvsluttendeBlokker
        }
    }

    private fun avvist(): List<String> {
        if (!klagevedtak.any<KlageOpplysning.KlageUtfall> {
                it.verdi.uppercase() == "AVVIST"
            }
        ) {
            return emptyList()
        }

        val brevBlokkIder = mutableListOf<String>()
        brevBlokkIder.add(KLAGE_AVVIST_INTRO.brevblokkId)

        if (
            klagevedtak.ikkeOppfylt<KlageOpplysning.ErKlagenSkriftelig>() ||
            klagevedtak.ikkeOppfylt<KlageOpplysning.ErKlagenUnderskrevet>()
        ) {
            brevBlokkIder.add(KLAGE_AVVIST_SKRIFTLIG_OG_SIGNERT.brevblokkId)
        }

        if (klagevedtak.ikkeOppfylt<KlageOpplysning.KlagenNevnerEndring>()) {
            brevBlokkIder.add(KLAGE_AVVIST_NEVNER_ENDRING.brevblokkId)
        }

        if (
            klagevedtak.ikkeOppfylt<KlageOpplysning.ErKlagenSkriftelig>() ||
            klagevedtak.ikkeOppfylt<KlageOpplysning.ErKlagenUnderskrevet>() ||
            klagevedtak.ikkeOppfylt<KlageOpplysning.KlagenNevnerEndring>()
        ) {
            brevBlokkIder.add(KLAGE_AVVIST_SKRIFTLIG_OG_NEVNER_ENDRING_HJEMMEL.brevblokkId)
        }

        if (klagevedtak.ikkeOppfylt<KlageOpplysning.KlagefristOppfylt>()) {
            brevBlokkIder.add(KLAGE_AVVIST_KLAGEFIRST.brevblokkId)
            brevBlokkIder.add(KLAGE_AVVIST_KLAGEFIRST_HJEMMEL.brevblokkId)
        }

        if (klagevedtak.ikkeOppfylt<KlageOpplysning.RettsligKlageinteresse>()) {
            brevBlokkIder.add(KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE.brevblokkId)
            brevBlokkIder.add(KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_HJEMMEL.brevblokkId)
        }
        return brevBlokkIder.toList() + fasteAvsluttendeBlokker
    }

    companion object {
        val fasteAvsluttendeBlokker =
            listOf(
                RETT_TIL_INNSYN.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
    }
}
