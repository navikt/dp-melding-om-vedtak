package no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans

import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.VedtakType.STANS_DAGPENGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.finnOpplysning
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk

class StansMelding(
    override val vedtak: Vedtak,
    alleBrevblokker: List<BrevBlokk>,
) : Vedtaksmelding(vedtak) {
    // TODO: Dette er ikke godt nok. Må trolig se på om de enkelte vilkårene er ikke-oppfylt fra
    // stans-dato. Kanskje opprinnelse må være "Ny" for at den skal være interessant?
    // Det kan også hende at det ikke er de samme vilkårene
    override val harBrevstøtte: Boolean =
        vedtak.vedtakType == STANS_DAGPENGER &&
            setOfNotNull<DagpengerOpplysning<*, Boolean>>(
                vedtak.finnOpplysning<DagpengerOpplysning.KravTilAlder>(),
                vedtak.finnOpplysning<DagpengerOpplysning.KravTilTapAvArbeidsinntektOgArbeidstid>(),
                vedtak.finnOpplysning<DagpengerOpplysning.KravTilTapAvArbeidsinntekt>(),
                vedtak.finnOpplysning<DagpengerOpplysning.KravTilTaptArbeidstid>(),
                vedtak.finnOpplysning<DagpengerOpplysning.OppfyllerKravetTilIkkeUtestengt>(),
                vedtak.finnOpplysning<DagpengerOpplysning.KravTilUtdanning>(),
                vedtak.finnOpplysning<DagpengerOpplysning.KravTilArbeidssøker>(),
                vedtak.finnOpplysning<DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker>(),
                vedtak.finnOpplysning<DagpengerOpplysning.OppfyllerKravetTilOpphold>(),
                vedtak.finnOpplysning<DagpengerOpplysning.IkkeFulleYtelser>(),
                vedtak.finnOpplysning<DagpengerOpplysning.IkkeStreikEllerLockout>(),
                vedtak.finnOpplysning<DagpengerOpplysning.OppfyllerMedlemskap>(),
                vedtak.finnOpplysning<DagpengerOpplysning.OppfyllerKravetTilPermittering>(),
                vedtak.finnOpplysning<DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri>(),
            ).any {
                !it.verdi
            }

    init {
        require(this.harBrevstøtte) {
            throw ManglerBrevstøtte("Avslag for behandling ${this.vedtak.behandlingId}. Mangler brevstøtte.")
        }
    }

    override val brevBlokkIder: List<String>
        get() {
            return innledendeBrevblokk
        }

    override val brevBlokker: List<BrevBlokk> =
        run {
            val brevBlokkMap = alleBrevblokker.associateBy { it.textId }
            brevBlokkIder().mapNotNull { id -> brevBlokkMap[id] }
        }
    private val innledendeBrevblokk = listOf(StansBrevblokker.STANS_INNLEDNING.brevblokkId)
}
