package no.nav.dagpenger.vedtaksmelding.model.dagpenger.omgjøring

import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall.OMGJØRING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.finnOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ARBEIDSFORHOLD_AVSLUTT_PERMITTERT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ARBEIDSTIDEN_DIN
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ARBEIDSTIDEN_DIN_VERNEPLIKT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_BARNETILLEGG
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE_PERMITTERT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE_PERMITTERT_FISK_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE_PERMITTERT_FISK_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE_VERNEPLIKT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_EGENANDEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG_VERNEPLIKT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_HVA_SKJER_ETTER_PERMITTERINGEN
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_NITTI_PROSENT_REGEL
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
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_VERNEPLIKT_GUNSTIGEST
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.OMGJØRING_BEGRUNNELSE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.OMGJØRING_OVERSKRIFT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.oppfylt
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk

class OmgjøringMelding(
    override val vedtak: Vedtak,
    alleBrevblokker: List<BrevBlokk>,
) : Vedtaksmelding(vedtak) {
    override val harBrevstøtte: Boolean =
        vedtak.utfall == OMGJØRING &&
            (
                vedtak.oppfylt<DagpengerOpplysning.UnderretningOmVedtaketIkkeErKommetFram>() ||
                    vedtak.oppfylt<DagpengerOpplysning.VedtaketMåAnsesUgyldig>() ||
                    vedtak.oppfylt<DagpengerOpplysning.VedtaketErIkkeTilSkade>()
            )

    init {
        require(this.harBrevstøtte) {
            throw ManglerBrevstøtte("Omgjøring for behandling ${this.vedtak.behandlingId} mangler brevstøtte")
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
//                medEllerUtenEgenandelBlokker() +
                dagpengeperiodeBlokker() +
                permittertOgPermittertFiskBlokker() +
                dagsatsBlokker() +
                barnetilleggBlokker() +
                nittiProsentRegelBlokker() +
                samordnetBlokker() +
                grunnlagBlokker() +
                arbeidstidenDinBlokker() +
                reellArbeidssøkerBlokker() +
                egenandelBlokker() +
                avsluttendeBrevblokker
        }
    override val brevBlokker: List<BrevBlokk> =
        run {
            val brevBlokkMap = alleBrevblokker.associateBy { it.textId }
            brevBlokkIder().mapNotNull { id -> brevBlokkMap[id] }
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

    private fun dagsatsBlokker(): List<String> {
        if (vedtak.oppfylt<DagpengerOpplysning.DagsatsErEndret>()) {
            return listOf(INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId)
        } else {
            return emptyList()
        }
    }

    private fun barnetilleggBlokker(): List<String> =
        if (vedtak.oppfylt<DagpengerOpplysning.DagsatsErEndret>()) {
            vedtak.opplysninger
                .find {
                    it is DagpengerOpplysning.AntallBarnSomGirRettTilBarnetillegg && it.verdi > 0
                }?.let {
                    listOf(INNVILGELSE_BARNETILLEGG.brevblokkId)
                } ?: emptyList()
        } else {
            emptyList()
        }

    private fun grunnlagBlokker(): List<String> {
        if (vedtak.oppfylt<DagpengerOpplysning.DagsatsErEndret>()) {
            val grunnlagBlokker = mutableListOf<String>()
            val kravTilMinsteinntektOppfylt = vedtak.oppfylt<DagpengerOpplysning.OppfyllerKravTilMinsteinntekt>()

            when {
                erInnvilgetMedVerneplikt() -> {
                    grunnlagBlokker.add(INNVILGELSE_GRUNNLAG_VERNEPLIKT.brevblokkId)
                    if (kravTilMinsteinntektOppfylt) {
                        grunnlagBlokker.add(INNVILGELSE_VERNEPLIKT_GUNSTIGEST.brevblokkId)
                    }
                }

                else -> grunnlagBlokker.add(INNVILGELSE_GRUNNLAG.brevblokkId)
            }
            return grunnlagBlokker.toList()
        } else {
            return emptyList()
        }
    }

    private fun arbeidstidenDinBlokker(): List<String> =
        if (vedtak.oppfylt<DagpengerOpplysning.FastsattVanligArbeidstidPerUkeErEndret>()) {
            if (erInnvilgetMedVerneplikt()) {
                listOf(INNVILGELSE_ARBEIDSTIDEN_DIN_VERNEPLIKT.brevblokkId)
            } else {
                listOf(INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId)
            }
        } else {
            emptyList()
        }

    private fun innledningBlokker(): List<String> =
        listOf(
            OMGJØRING_OVERSKRIFT.brevblokkId,
            OMGJØRING_BEGRUNNELSE.brevblokkId,
        )

    private fun reellArbeidssøkerBlokker(): List<String> =
        if (godkjentLokalArbeidssøker() || godkjentKunDeltidssøker()) {
            listOf(
                INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_1.brevblokkId,
                INNVILGELSE_GODKJENT_LOKAL_ELLER_DELTID_ARBEIDSSØKER_DEL_2.brevblokkId,
            )
        } else {
            emptyList()
        }

//    private fun medEllerUtenEgenandelBlokker(): List<String> {
//        vedtak.finnOpplysning<DagpengerOpplysning.EgenandelGjenstående> {
//            return when (it.toDouble()) {
//                0.0 -> emptyList()
//                else -> listOf(INNVILGELSE_MED_EGENANDEL.brevblokkId)
//            }
//        }
//        return listOf(INNVILGELSE_MED_EGENANDEL.brevblokkId)
//    }

    private fun egenandelBlokker(): List<String> {
        vedtak.finnOpplysning<DagpengerOpplysning.EgenandelGjenstående>
            {
                return when (it.toDouble()) {
                    0.0 -> emptyList()
                    else ->
                        vedtak.finnOpplysning<DagpengerOpplysning.Egenandel> { it.toDouble() > 0.0 }?.let {
                            listOf(INNVILGELSE_EGENANDEL.brevblokkId)
                        } ?: emptyList()
                }
            }
        return vedtak.finnOpplysning<DagpengerOpplysning.Egenandel> { it.toDouble() > 0.0 }?.let {
            listOf(INNVILGELSE_EGENANDEL.brevblokkId)
        } ?: emptyList()
    }

    private fun dagpengeperiodeBlokker(): List<String> =
        when {
            erInnvilgetMedVerneplikt() -> {
                if (vedtak.oppfylt<DagpengerOpplysning.AntallStønadsukerErEndret>()) {
                    listOf(INNVILGELSE_DAGPENGEPERIODE_VERNEPLIKT.brevblokkId)
                } else {
                    emptyList()
                }
            }

            erInnvilgetSomPermittert() -> {
                if (vedtak.oppfylt<DagpengerOpplysning.AntallStønadsukerErEndret>()) {
                    listOf(INNVILGELSE_DAGPENGEPERIODE_PERMITTERT.brevblokkId)
                } else {
                    emptyList()
                }
            }

            erInnvilgetSomPermittertIFiskeindustri() -> {
                if (vedtak.oppfylt<DagpengerOpplysning.AntallStønadsukerErEndret>()) {
                    listOf(
                        INNVILGELSE_DAGPENGEPERIODE_PERMITTERT_FISK_DEL_1.brevblokkId,
                        INNVILGELSE_DAGPENGEPERIODE_PERMITTERT_FISK_DEL_2.brevblokkId,
                    )
                } else {
                    emptyList()
                }
            }

            else -> {
                when (vedtak.finnOpplysning("opplysning.siste-dag-med-rett")) {
                    null -> {
                        if (vedtak.oppfylt<DagpengerOpplysning.AntallStønadsukerErEndret>()) {
                            listOf(INNVILGELSE_DAGPENGEPERIODE.brevblokkId)
                        } else {
                            emptyList()
                        }
                    }

                    else ->
                        listOf(
                            INNVILGELSE_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_1.brevblokkId,
                            INNVILGELSE_DAGPENGEPERIODE_HVIS_TOM_DATO_DEL_2.brevblokkId,
                        )
                }
            }
        }

    private fun permittertOgPermittertFiskBlokker(): List<String> =
        when {
            erInnvilgetSomPermittert() || erInnvilgetSomPermittertIFiskeindustri() ->
                listOf(
                    INNVILGELSE_ARBEIDSFORHOLD_AVSLUTT_PERMITTERT.brevblokkId,
                    INNVILGELSE_HVA_SKJER_ETTER_PERMITTERINGEN.brevblokkId,
                )

            else -> emptyList()
        }

    private fun godkjentLokalArbeidssøker() = vedtak.oppfylt<DagpengerOpplysning.GodkjentLokalArbeidssøker>()

    private fun godkjentKunDeltidssøker() = vedtak.oppfylt<DagpengerOpplysning.GodkjentKunDeltidssøker>()

    private fun erInnvilgetMedVerneplikt() = vedtak.oppfylt<DagpengerOpplysning.GrunnlagetForVernepliktErHøyereEnnDagpengegrunnlaget>()

    private fun erInnvilgetSomPermittert() = vedtak.oppfylt<DagpengerOpplysning.OppfyllerKravetTilPermittering>()

    private fun erInnvilgetSomPermittertIFiskeindustri() = vedtak.oppfylt<DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri>()
}
