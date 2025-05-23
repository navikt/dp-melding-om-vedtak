package no.nav.dagpenger.vedtaksmelding.model.klage

import no.nav.dagpenger.vedtaksmelding.model.KlageOpplysningTyper.ErKlagenSkriftelig
import no.nav.dagpenger.vedtaksmelding.model.KlageOpplysningTyper.ErKlagenUnderskrevet
import no.nav.dagpenger.vedtaksmelding.model.KlageOpplysningTyper.KlageUtfall
import no.nav.dagpenger.vedtaksmelding.model.KlageOpplysningTyper.KlagenNevnerEndring
import no.nav.dagpenger.vedtaksmelding.model.KlageOpplysningTyper.RettsligKlageinteresse
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding.FasteBrevblokker.HJELP_FRA_ANDRE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding.FasteBrevblokker.PERSONOPPLYSNINGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding.FasteBrevblokker.RETT_TIL_INNSYN
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding.FasteBrevblokker.RETT_TIL_Å_KLAGE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding.FasteBrevblokker.SPØRSMÅL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding.FasteBrevblokker.VEILEDNING_FRA_NAV
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
        if (!klagevedtak.opplysninger.any {
                it.opplysningTekstId == KlageUtfall.opplysningTekstId && it.råVerdi() == "AVVIST"
            }
        ) {
            return emptyList()
        }
        val brevBlokkIder = mutableListOf<String>()
        brevBlokkIder.add(KlageBrevBlokker.KLAGE_AVVIST_DEL_1.brevblokkId)

        if ((
                klagevedtak.opplysninger.any {
                    it.opplysningTekstId == ErKlagenSkriftelig.opplysningTekstId && !it.råVerdi().toBoolean()
                }
            ) ||
            (
                klagevedtak.opplysninger.any {
                    it.opplysningTekstId == ErKlagenUnderskrevet.opplysningTekstId && !it.råVerdi().toBoolean()
                }
            )
        ) {
            brevBlokkIder.add(KlageBrevBlokker.KLAGE_AVVIST_SKRIFTLIG_DEL_2.brevblokkId)
        }
        if (klagevedtak.opplysninger.any {
                it.opplysningTekstId == KlagenNevnerEndring.opplysningTekstId && !it.råVerdi().toBoolean()
            }
        ) {
            brevBlokkIder.add(KlageBrevBlokker.KLAGE_AVVIST_NEVNER_ENDRING_DEL_3.brevblokkId)
        }
        if ((
                klagevedtak.opplysninger.any {
                    it.opplysningTekstId == ErKlagenSkriftelig.opplysningTekstId && !it.råVerdi().toBoolean()
                }
            ) ||
            (
                klagevedtak.opplysninger.any {
                    it.opplysningTekstId == ErKlagenUnderskrevet.opplysningTekstId && !it.råVerdi().toBoolean()
                }
            ) ||
            (
                klagevedtak.opplysninger.any {
                    it.opplysningTekstId == KlagenNevnerEndring.opplysningTekstId && !it.råVerdi().toBoolean()
                }
            )
        ) {
            brevBlokkIder.add(KlageBrevBlokker.KLAGE_AVVIST_SKRIFTLIG_OG_NEVNER_ENDRING_HJEMMEL_DEL_4.brevblokkId)
        }
        if (klagevedtak.opplysninger.any {
                it.opplysningTekstId == RettsligKlageinteresse.opplysningTekstId && !it.råVerdi().toBoolean()
            }
        ) {
            brevBlokkIder.add(KlageBrevBlokker.KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_DEL_5.brevblokkId)
            brevBlokkIder.add(KlageBrevBlokker.KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_HJEMMEL_DEL_6.brevblokkId)
        }

        return brevBlokkIder.toList() + fasteAvsluttendeBlokkerForVedtak
    }

    companion object {
        val fasteAvsluttendeBlokkerForVedtak =
            listOf(
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
            )
    }
}
