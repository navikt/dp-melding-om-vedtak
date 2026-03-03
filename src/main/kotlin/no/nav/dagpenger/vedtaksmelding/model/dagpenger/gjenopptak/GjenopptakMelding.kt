package no.nav.dagpenger.vedtaksmelding.model.dagpenger.gjenopptak

import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall.GJENOPPTAK
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.finnOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_ARBEIDSTIDEN_DIN
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_DAGPENGEPERIODE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_3
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_EGENANDEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_EGENANDEL_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_REBEREGNING_IKKE_RETT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_REBEREGNING_IKKE_RETT_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_REBEREGNING_UGUNST
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.GJENOPPTAK_REBEREGNING_UTFØRT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_BARNETILLEGG
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG_VERNEPLIKT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_NITTI_PROSENT_REGEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ORDINÆR
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ORDINÆR_FOM_TOM
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_PERMITTERT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_PERMITTERT_FISK
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_FORELDREPENGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_GENERISK
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_OMSORGSPENGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_OPPLÆRINGSPENGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_PLEIEPENGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_SVANGERSKAPSPENGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_SYKEPENGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_UFØRE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE
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
    override val harBrevstøtte: Boolean = vedtak.utfall == GJENOPPTAK

    init {
        require(this.harBrevstøtte) {
            throw ManglerBrevstøtte("Innvilgelse av gjenopptak for behandling ${this.vedtak.behandlingId} mangler brevstøtte")
        }
    }

    override val brevBlokkIder: List<String>
        get() {
            val fasteBrevblokker =
                listOf(
                    INNVILGELSE_MELDEKORT.brevblokkId,
                    INNVILGELSE_UTBETALING.brevblokkId,
                    INNVILGELSE_SKATTEKORT.brevblokkId,
                    INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                    INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                    INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                )
            return innledningBlokker() +
                    gjenståendeEgenandelInnledning() +
                    virkningsdatoBlokker() +
                    dagpengeperiodeBlokker() +
                    reberegningBlokker() +

                    // TODO: Skal bare vises hvis en av beregningsopplysningene under har endret seg (opprinnelse Ny)
                    listOf(INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId) +
                    // TODO: Skal bare vises hvis antall barn med barnetillegg har endret seg (opprinnelse Ny)
                    barnetilleggBlokker() +
                    // TODO: Skal bare vises hvis 90%-opplysningen har endret seg (opprinnelse Ny)
                    nittiProsentRegelBlokker() +
                    // TODO: Skal bare vises hvis noen av samordningsopplysningene har endret seg (opprinnelse Ny)
                    samordnetBlokker() +

                    grunnlagBlokker() +
                    listOf(GJENOPPTAK_ARBEIDSTIDEN_DIN.brevblokkId) +
                    gjenståendeEgenandelBlokker() +

                    fasteBrevblokker +

                    // TODO: Skal denne blokka inn her, eller burde den vært høyere opp?
                    reellArbeidssøkerBlokker()
        }
    override val brevBlokker: List<BrevBlokk> =
        run {
            val brevBlokkMap = alleBrevblokker.associateBy { it.textId }
            brevBlokkIder().mapNotNull { id -> brevBlokkMap[id] }
        }

    private fun innledningBlokker(): List<String> =
        when {
            erInnvilgetSomPermittert() -> listOf(INNVILGELSE_PERMITTERT.brevblokkId, GJENOPPTAK_INNLEDNING.brevblokkId)
            erInnvilgetSomPermittertIFiskeindustri() ->
                listOf(
                    INNVILGELSE_PERMITTERT_FISK.brevblokkId,
                    GJENOPPTAK_INNLEDNING.brevblokkId,
                )

            else -> {
                when (vedtak.finnOpplysning("opplysning.siste-dag-med-rett")) {
                    null -> listOf(INNVILGELSE_ORDINÆR.brevblokkId, GJENOPPTAK_INNLEDNING.brevblokkId)
                    else -> listOf(INNVILGELSE_ORDINÆR_FOM_TOM.brevblokkId, GJENOPPTAK_INNLEDNING.brevblokkId)
                }
            }
        }

    private fun gjenståendeEgenandelInnledning(): List<String> =
        vedtak.finnOpplysning<DagpengerOpplysning.EgenandelGjenstående> { it.toDouble() > 0.0 }?.let {
            listOf(GJENOPPTAK_EGENANDEL_INNLEDNING.brevblokkId)
        } ?: emptyList()

    private fun virkningsdatoBlokker(): List<String> =
        when {
            erInnvilgetSomPermittert() -> listOf(INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT.brevblokkId)
            erInnvilgetSomPermittertIFiskeindustri() -> listOf(INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT_FISK.brevblokkId)
            else -> listOf(INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId)
        }

    // TODO: legg til funksjonalitet for verneplikt, permittering og fisk.
    private fun dagpengeperiodeBlokker(): List<String> =
        when (vedtak.finnOpplysning("opplysning.siste-dag-med-rett")) {
            null -> listOf(GJENOPPTAK_DAGPENGEPERIODE.brevblokkId)
            else ->
                listOf(
                    GJENOPPTAK_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_1.brevblokkId,
                    GJENOPPTAK_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_2.brevblokkId,
                    GJENOPPTAK_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_3.brevblokkId,
                )
        }

    // TODO: Avklare med PJ's hvordan vi skal hente ut info om reberegning.
    // Reberegning utført: Grunnlag-opplysningen (typeid 0194881f-9410-7481-b263-4606fdd10cbd) har en periode med opprinnelse "Ny"
    // Hva med info ang. rett til reberegning?
    private fun reberegningBlokker(): List<String> =
        when (1 == 1) {
            true -> listOf(
                GJENOPPTAK_REBEREGNING_IKKE_RETT_DEL_1.brevblokkId,
                GJENOPPTAK_REBEREGNING_IKKE_RETT_DEL_2.brevblokkId,
            )

            false -> listOf(GJENOPPTAK_REBEREGNING_UGUNST.brevblokkId)
            else -> listOf(GJENOPPTAK_REBEREGNING_UTFØRT.brevblokkId)
        }

    private fun nittiProsentRegelBlokker(): List<String> =
        vedtak
            .finnOpplysning<AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget> {
                it.toDouble() > 0
            }?.let {
                listOf(INNVILGELSE_NITTI_PROSENT_REGEL.brevblokkId)
            } ?: emptyList()

    private fun samordnetBlokker(): List<String> {
        if (!vedtak.oppfylt<DagpengerOpplysning.HarSamordnet>()) {
            return emptyList()
        }

        val samordnedeYtelser = mutableSetOf<Pair<String, Double>>()
        vedtak.finnOpplysning<DagpengerOpplysning.SykepengerDagsats> { it.toDouble() > 0 }?.let {
            samordnedeYtelser.add("Sykepenger" to it.verdi.toDouble())
        }

        vedtak.finnOpplysning<DagpengerOpplysning.PleiepengerDagsats> { it.toDouble() > 0 }?.let {
            samordnedeYtelser.add("Pleiepenger" to it.verdi.toDouble())
        }

        vedtak.finnOpplysning<DagpengerOpplysning.OmsorgspengerDagsats> { it.toDouble() > 0 }?.let {
            samordnedeYtelser.add("Omsorgspenger" to it.verdi.toDouble())
        }

        vedtak.finnOpplysning<DagpengerOpplysning.OpplæringspengerDagsats> { it.toDouble() > 0 }?.let {
            samordnedeYtelser.add("Opplæringspenger" to it.verdi.toDouble())
        }

        vedtak.finnOpplysning<DagpengerOpplysning.UføreDagsats> { it.toDouble() > 0 }?.let {
            samordnedeYtelser.add("Uføre" to it.verdi.toDouble())
        }

        vedtak.finnOpplysning<DagpengerOpplysning.ForeldrepengerDagsats> { it.toDouble() > 0 }?.let {
            samordnedeYtelser.add("Foreldrepenger" to it.verdi.toDouble())
        }

        vedtak.finnOpplysning<DagpengerOpplysning.SvangerskapspengerDagsats> { it.toDouble() > 0 }?.let {
            samordnedeYtelser.add("Svangerskapspenger" to it.verdi.toDouble())
        }

        val samordningBlokker = mutableListOf<String>()
        if (samordnedeYtelser.size == 1) {
            when (samordnedeYtelser.first().first) {
                "Sykepenger" -> samordningBlokker.add(INNVILGELSE_SAMORDNET_SYKEPENGER.brevblokkId)
                "Pleiepenger" -> samordningBlokker.add(INNVILGELSE_SAMORDNET_PLEIEPENGER.brevblokkId)
                "Omsorgspenger" -> samordningBlokker.add(INNVILGELSE_SAMORDNET_OMSORGSPENGER.brevblokkId)
                "Opplæringspenger" -> samordningBlokker.add(INNVILGELSE_SAMORDNET_OPPLÆRINGSPENGER.brevblokkId)
                "Uføre" -> samordningBlokker.add(INNVILGELSE_SAMORDNET_UFØRE.brevblokkId)
                "Foreldrepenger" -> samordningBlokker.add(INNVILGELSE_SAMORDNET_FORELDREPENGER.brevblokkId)
                "Svangerskapspenger" -> samordningBlokker.add(INNVILGELSE_SAMORDNET_SVANGERSKAPSPENGER.brevblokkId)
            }
        } else {
            samordningBlokker.add(INNVILGELSE_SAMORDNET_GENERISK.brevblokkId)
        }
        return samordningBlokker
    }

    private fun barnetilleggBlokker(): List<String> =
        vedtak.opplysninger
            .find {
                it is DagpengerOpplysning.AntallBarnSomGirRettTilBarnetillegg && it.verdi > 0
            }?.let {
                listOf(INNVILGELSE_BARNETILLEGG.brevblokkId)
            } ?: emptyList()

    private fun grunnlagBlokker(): List<String> {
        // TODO: hvorfor funker ikke dette??
        vedtak.finnOpplysning<DagpengerOpplysning.GrunnlagErReberegnet> { it.verdi }?.let {
            return when {
                erInnvilgetMedVerneplikt() -> listOf(INNVILGELSE_GRUNNLAG_VERNEPLIKT.brevblokkId)
                else -> listOf(INNVILGELSE_GRUNNLAG.brevblokkId)
            }
        }
        return emptyList()
    }

    private fun gjenståendeEgenandelBlokker(): List<String> =
        vedtak.finnOpplysning<DagpengerOpplysning.EgenandelGjenstående> { it.toDouble() > 0.0 }?.let {
            listOf(GJENOPPTAK_EGENANDEL.brevblokkId)
        } ?: emptyList()

    private fun reellArbeidssøkerBlokker(): List<String> =
        if (godkjentLokalArbeidssøker() || godkjentKunDeltidssøker()) {
            listOf(
                INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_1.brevblokkId,
                INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_2.brevblokkId,
            )
        } else {
            emptyList()
        }

    private fun godkjentLokalArbeidssøker() = vedtak.oppfylt<DagpengerOpplysning.GodkjentLokalArbeidssøker>()

    private fun godkjentKunDeltidssøker() = vedtak.oppfylt<DagpengerOpplysning.GodkjentKunDeltidssøker>()

    private fun erInnvilgetMedVerneplikt() = vedtak.oppfylt<DagpengerOpplysning.ErInnvilgetMedVerneplikt>()

    private fun erInnvilgetSomPermittert() = vedtak.oppfylt<DagpengerOpplysning.OppfyllerKravetTilPermittering>()

    private fun erInnvilgetSomPermittertIFiskeindustri() =
        vedtak.oppfylt<DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri>()
}
