package no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans

import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall.STANS
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.finnOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.ikkeOppfylt
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk

class StansMelding(
    override val vedtak: Vedtak,
    alleBrevblokker: List<BrevBlokk>,
) : Vedtaksmelding(vedtak) {
    override val harBrevstøtte: Boolean =
        vedtak.utfall == STANS &&
            vedtak.automatiskBehandling &&
            setOfNotNull<DagpengerOpplysning<*, Boolean>>(
                vedtak.finnOpplysning<DagpengerOpplysning.OppfyllerVilkåretOmTapAvArbeidstid>(),
                vedtak.finnOpplysning<DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker>(),
                vedtak.finnOpplysning<DagpengerOpplysning.OppyllerMeldeplikt>(),
            ).any {
                !it.verdi
            }

    override val brevBlokkIder: List<String>
        get() {
            return listOf(StansBrevblokker.STANS_INNLEDNING.brevblokkId) +
                blokkerAutomatiskStans()
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

    private fun blokkerAutomatiskStans(): List<String> =
        if (vedtak.ikkeOppfylt<DagpengerOpplysning.OppfyllerVilkåretOmTapAvArbeidstid>()) {
            listOf(StansBrevblokker.STANS_ARBEID_OVER_TERSKEL.brevblokkId)
        } else if (vedtak.ikkeOppfylt<DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker>() &&
            vedtak.ikkeOppfylt<DagpengerOpplysning.OppyllerMeldeplikt>()
        ) {
            listOf(StansBrevblokker.STANS_IKKE_MELDT_SEG_I_TIDE.brevblokkId)
        } else if (vedtak.ikkeOppfylt<DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker>()) {
            listOf(StansBrevblokker.STANS_SVART_NEI_TIL_Å_STÅ_TILMELDT.brevblokkId)
        } else {
            emptyList()
        }
}
