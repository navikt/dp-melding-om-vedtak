package no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans

import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall.STANS
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.finnOpplysning
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk

class StansMelding(
    override val vedtak: Vedtak,
    alleBrevblokker: List<BrevBlokk>,
) : Vedtaksmelding(vedtak) {
    // TODO: Sjekk at automatiskBehandlet er true
    override val harBrevstøtte: Boolean =
        vedtak.utfall == STANS &&
            setOfNotNull<DagpengerOpplysning<*, Boolean>>(
                vedtak.finnOpplysning<DagpengerOpplysning.OppfyllerVilkåretOmTapAvArbeidstid>(),
                vedtak.finnOpplysning<DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker>(),
                vedtak.finnOpplysning<DagpengerOpplysning.OppyllerMeldeplikt>(),
            ).any {
                !it.verdi
            }

    override val brevBlokkIder: List<String>
        get() {
            return listOf(StansBrevblokker.STANS_INNLEDNING.brevblokkId)
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
}
