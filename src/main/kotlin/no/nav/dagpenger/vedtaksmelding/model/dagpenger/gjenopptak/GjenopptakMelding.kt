package no.nav.dagpenger.vedtaksmelding.model.dagpenger.gjenopptak

import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall.INNVILGET
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.finnOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_DAGPENGEPERIODE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_EGENANDEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ORDINÆR
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ORDINÆR_FOM_TOM
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_PERMITTERT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_PERMITTERT_FISK
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_UTBETALING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT_FISK
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.oppfylt
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk

class GjenopptakMelding(
    override val vedtak: Vedtak,
    alleBrevblokker: List<BrevBlokk>,
) : Vedtaksmelding(vedtak) {
    override val harBrevstøtte: Boolean = vedtak.utfall == INNVILGET

    init {
        require(this.harBrevstøtte) {
            throw ManglerBrevstøtte("Innvilgelse for behandling ${this.vedtak.behandlingId} mangler brevstøtte")
        }
    }

    override val brevBlokkIder: List<String>
        get() {
            val avsluttendeBrevblokker =
                listOf(
                    INNVILGELSE_MELDEKORT.brevblokkId,
                    INNVILGELSE_UTBETALING.brevblokkId,
                    INNVILGELSE_SKATTEKORT.brevblokkId,
                    INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                    INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                    INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                )
            return innledningBlokker() +
                gjenståendeEgenandel() +
                virkningsdatoBlokker() +
                dagpengeperiodeBlokker() +
                avsluttendeBrevblokker
        }
    override val brevBlokker: List<BrevBlokk> =
        run {
            val brevBlokkMap = alleBrevblokker.associateBy { it.textId }
            brevBlokkIder().mapNotNull { id -> brevBlokkMap[id] }
        }

    private fun innledningBlokker(): List<String> =
        when {
            erInnvilgetSomPermittert() -> listOf(INNVILGELSE_PERMITTERT.brevblokkId, GJENOPPTAK_INNLEDNING.brevblokkId)
            erInnvilgetSomPermittertIFiskeindustri() -> listOf(INNVILGELSE_PERMITTERT_FISK.brevblokkId, GJENOPPTAK_INNLEDNING.brevblokkId)
            else -> {
                when (vedtak.finnOpplysning("opplysning.siste-dag-med-rett")) {
                    null -> listOf(INNVILGELSE_ORDINÆR.brevblokkId, GJENOPPTAK_INNLEDNING.brevblokkId)
                    else -> listOf(INNVILGELSE_ORDINÆR_FOM_TOM.brevblokkId, GJENOPPTAK_INNLEDNING.brevblokkId)
                }
            }
        }

    private fun gjenståendeEgenandel(): List<String> =
        vedtak.finnOpplysning<DagpengerOpplysning.EgenandelGjenstående> { it.toDouble() > 0.0 }?.let {
            listOf(INNVILGELSE_EGENANDEL.brevblokkId)
        } ?: emptyList()

    private fun virkningsdatoBlokker(): List<String> =
        when {
            erInnvilgetSomPermittert() -> listOf(INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT.brevblokkId)
            erInnvilgetSomPermittertIFiskeindustri() -> listOf(INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT_FISK.brevblokkId)
            else -> listOf(INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId)
        }

    // TODO: legg til funksjonalitet for til-og-med-dato.
    // TODO: legg til funksjonalitet for verneplikt, permittering og fisk.
    private fun dagpengeperiodeBlokker(): List<String> = listOf(GJENOPPTAK_DAGPENGEPERIODE.brevblokkId)

    private fun erInnvilgetSomPermittert() = vedtak.oppfylt<DagpengerOpplysning.OppfyllerKravetTilPermittering>()

    private fun erInnvilgetSomPermittertIFiskeindustri() = vedtak.oppfylt<DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri>()
}
