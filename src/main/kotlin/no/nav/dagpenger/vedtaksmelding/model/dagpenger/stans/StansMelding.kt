package no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans

import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Opprinnelse
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Periode
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall.STANS
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.finnOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.hentOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.ikkOppfyltBlokker
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.ikkeOppfylt
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_ALDER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_ARBEID_OVER_TERSKEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_IKKE_MELDT_SEG_I_TIDE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_REELL_ARBEIDSSØKER_GENERELL_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_REELL_ARBEIDSSØKER_GENERELL_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_TRENGER_DU_FORTSATT_DAGPENGER
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk

class StansMelding(
    override val vedtak: Vedtak,
    alleBrevblokker: List<BrevBlokk>,
) : Vedtaksmelding(vedtak) {
    companion object {
        private fun List<Periode<Boolean>>.ikkeOppfyltNy(): Boolean =
            this.any { periode -> periode.opprinnelse == Opprinnelse.NY && !periode.verdi }
    }

    override val harBrevstøtte: Boolean =
        vedtak.utfall == STANS &&
            vedtak.behandletHendelseType in setOf("MELDEKORT", "ARBEIDSSØKERPERIODE") &&
            setOfNotNull<DagpengerOpplysning.Periodisert<*, Boolean>>(
                vedtak.finnOpplysning<DagpengerOpplysning.OppfyllerVilkåretOmTapAvArbeidstid>(),
                vedtak.finnOpplysning<DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker>(),
                vedtak.finnOpplysning<DagpengerOpplysning.OppyllerMeldeplikt>(),
                vedtak.finnOpplysning<DagpengerOpplysning.KravTilAlder>(),
            ).any {
                it.perioder.ikkeOppfyltNy()
            }

    override val brevBlokkIder: List<String>
        get() {
            val fasteBrevblokkerStans =
                if (vedtak.ikkeOppfylt<DagpengerOpplysning.KravTilAlder>()) {
                    emptyList()
                } else {
                    listOf(STANS_TRENGER_DU_FORTSATT_DAGPENGER.brevblokkId)
                }

            return listOf(StansBrevblokker.STANS_INNLEDNING.brevblokkId) +
                blokkerReellArbeidssøkerOgMeldeplikt() +
                blokkerArbeidOverTerskel() +
                blokkerAlder() +
                fasteBrevblokkerStans
        }

    override val brevBlokker: List<BrevBlokk> =
        run {
            val brevBlokkMap = alleBrevblokker.associateBy { it.textId }
            brevBlokkIder().mapNotNull { id -> brevBlokkMap[id] }
        }

    init {
        require(this.harBrevstøtte) {
            throw ManglerBrevstøtte("Stans for behandling ${this.vedtak.behandlingId}. Mangler brevstøtte.")
        }
    }

    private fun blokkerReellArbeidssøkerOgMeldeplikt(): List<String> =

        if (vedtak.ikkeOppfylt<DagpengerOpplysning.OppyllerMeldeplikt>() &&
            vedtak.hentOpplysning<DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker>().perioder.ikkeOppfyltNy()
        ) {
            listOf(STANS_IKKE_MELDT_SEG_I_TIDE.brevblokkId)
//        } else if (vedtak.ikkeOppfylt<DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker>()
//            && TODO - vi trenger å få noe ekstra fra PJ for å avdekke dette
//        ) {
//            listOf(StansBrevblokker.STANS_REELL_ARBEIDSSØKER_SVART_NEI_TIL_Å_STÅ_TILMELDT.brevblokkId) }
        } else if (vedtak.ikkeOppfylt<DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker>()) {
            listOf(
                STANS_REELL_ARBEIDSSØKER_GENERELL_DEL_1.brevblokkId,
                STANS_REELL_ARBEIDSSØKER_GENERELL_DEL_2.brevblokkId,
            )
        } else {
            emptyList()
        }

    private fun blokkerArbeidOverTerskel(): List<String> =
        vedtak.ikkOppfyltBlokker<DagpengerOpplysning.OppfyllerVilkåretOmTapAvArbeidstid> {
            listOf(STANS_ARBEID_OVER_TERSKEL.brevblokkId)
        }

    private fun blokkerAlder(): List<String> =
        vedtak.ikkOppfyltBlokker<DagpengerOpplysning.KravTilAlder> {
            listOf(STANS_ALDER.brevblokkId)
        }
}
