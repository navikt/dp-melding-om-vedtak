package no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans

import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall.STANS
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.finnOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.ikkOppfyltBlokker
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_ANDRE_FULLE_YTELSER_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_ANDRE_FULLE_YTELSER_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_IKKE_REGISTRERT_ARBEIDSSØKER_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_IKKE_REGISTRERT_ARBEIDSSØKER_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_OPPHOLD_UTLANDET_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans.StansBrevblokker.STANS_OPPHOLD_UTLANDET_DEL_2
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk

class StansMelding(
    override val vedtak: Vedtak,
    alleBrevblokker: List<BrevBlokk>,
) : Vedtaksmelding(vedtak) {
    override val harBrevstøtte: Boolean =
        vedtak.utfall == STANS &&
            setOfNotNull<DagpengerOpplysning<*, Boolean>>(
                vedtak.finnOpplysning<DagpengerOpplysning.OppfyllerKravetTilOpphold>(),
                vedtak.finnOpplysning<DagpengerOpplysning.IkkeFulleYtelser>(),
                vedtak.finnOpplysning<DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker>(),
            ).any {
                !it.verdi
            }

    init {
        require(this.harBrevstøtte) {
            throw ManglerBrevstøtte("Stans for behandling ${this.vedtak.behandlingId}. Mangler brevstøtte.")
        }
    }

    override val brevBlokkIder: List<String>
        get() {
            return listOf(StansBrevblokker.STANS_INNLEDNING.brevblokkId) +
                blokkerOppholdUtland() +
                blokkerAndreFulleYtelser() +
                blokkerIkkeRegistrertArbeidssøker()
        }

    override val brevBlokker: List<BrevBlokk> =
        run {
            val brevBlokkMap = alleBrevblokker.associateBy { it.textId }
            brevBlokkIder().mapNotNull { id -> brevBlokkMap[id] }
        }

    private fun blokkerOppholdUtland(): List<String> =
        vedtak.ikkOppfyltBlokker<DagpengerOpplysning.OppfyllerKravetTilOpphold> {
            listOf(
                STANS_OPPHOLD_UTLANDET_DEL_1.brevblokkId,
                STANS_OPPHOLD_UTLANDET_DEL_2.brevblokkId,
            )
        }

    private fun blokkerAndreFulleYtelser(): List<String> =
        vedtak.ikkOppfyltBlokker<DagpengerOpplysning.IkkeFulleYtelser> {
            listOf(
                STANS_ANDRE_FULLE_YTELSER_DEL_1.brevblokkId,
                STANS_ANDRE_FULLE_YTELSER_DEL_2.brevblokkId,
            )
        }

    private fun blokkerIkkeRegistrertArbeidssøker(): List<String> =
        vedtak.ikkOppfyltBlokker<DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker> {
            listOf(
                STANS_IKKE_REGISTRERT_ARBEIDSSØKER_DEL_1.brevblokkId,
                STANS_IKKE_REGISTRERT_ARBEIDSSØKER_DEL_2.brevblokkId,
            )
        }
// TODO
//    private fun blokkerStansDødsfall(): List<String> =
//        vedtak.ikkOppfyltBlokker<DagpengerOpplysning.ErIkkeDød> {
//            listOf(
//                STANS_DØDSFALL_DEL_1.brevblokkId,
//                STANS_DØDSFALL_DEL_2.brevblokkId,
//            )
//        }
}
