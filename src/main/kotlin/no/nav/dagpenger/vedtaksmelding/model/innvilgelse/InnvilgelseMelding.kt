package no.nav.dagpenger.vedtaksmelding.model.innvilgelse

import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.AntallBarnSomGirRettTilBarnetillegg
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.ErInnvilgetMedVerneplikt
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.ForeldrepengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.HarSamordnet
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.KravTilMinsteinntekt
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.OmsorgspengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.OpplæringspengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.PleiepengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.SvangerskapspengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.SykepengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.UføreDagsats
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.MINSTEINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ARBEIDSFORHOLD_AVSLUTT_PERMITTERT
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ARBEIDSTIDEN_DIN
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ARBEIDSTIDEN_DIN_VERNEPLIKT
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_BARNETILLEGG
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE_PERMITTERT
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE_VERNEPLIKT
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_EGENANDEL
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG_VERNEPLIKT
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_HVA_SKJER_ETTER_PERMITTERINGEN
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_INNLEDNING_PERMITTERT
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_NITTI_PROSENT_REGEL
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_FORELDREPENGER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_GENERISK
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_OMSORGSPENGER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_OPPLÆRINGSPENGER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_PLEIEPENGER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_SVANGERSKAPSPENGER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_SYKEPENGER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SAMORDNET_UFØRE
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_UTBETALING
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_VERNEPLIKT_GUNSTIGEST
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseVilkårMedBrevstøtte.OPPFYLLER_KRAVET_TIL_PERMITTERING
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak.Utfall.INNVILGET
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vilkår.Status.IKKE_OPPFYLT
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vilkår.Status.OPPFYLT
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk

private val logger = KotlinLogging.logger {}

class InnvilgelseMelding(
    override val vedtak: Vedtak,
    alleBrevblokker: List<BrevBlokk>,
) : VedtakMelding(vedtak) {
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
                    INNVILGELSE_EGENANDEL.brevblokkId,
                    INNVILGELSE_MELDEKORT.brevblokkId,
                    INNVILGELSE_UTBETALING.brevblokkId,
                    INNVILGELSE_SKATTEKORT.brevblokkId,
                    INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                    INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                    INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                )
            return innledning() +
                virkningsdato() +
                dagpengeperiode() +
                ekstrablokkerPermittert() +
                listOf(INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId) +
                barnetillegg() +
                nittiProsentRegel() +
                samordnet() +
                grunnlag() +
                arbeidstidenDin() +
                avsluttendeBrevblokker
        }
    override val brevBlokker: List<BrevBlokk> =
        run {
            val brevBlokkMap = alleBrevblokker.associateBy { it.textId }
            brevBlokkIder().mapNotNull { id -> brevBlokkMap[id] }
        }

    private fun nittiProsentRegel(): List<String> {
        val id = AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget.opplysningTekstId
        return vedtak.opplysninger.find {
            it.opplysningTekstId == id &&
                it.råVerdi().toDouble() > 0
        }
            ?.let {
                listOf(INNVILGELSE_NITTI_PROSENT_REGEL.brevblokkId)
            } ?: emptyList()
    }

    private fun samordnet(): List<String> {
        if (!vedtak.opplysninger.any { it.opplysningTekstId == HarSamordnet.opplysningTekstId && it.formatertVerdi == "true" }) {
            return emptyList()
        }

        val samordnedeYtelser = mutableSetOf<Pair<String, Double>>()
        vedtak.opplysninger.find {
            it.opplysningTekstId == SykepengerDagsats.opplysningTekstId && it.råVerdi().toDouble() > 0
        }?.let {
            samordnedeYtelser.add("Sykepenger" to it.råVerdi().toDouble())
        }
        vedtak.opplysninger.find {
            it.opplysningTekstId == PleiepengerDagsats.opplysningTekstId && it.råVerdi().toDouble() > 0
        }?.let {
            samordnedeYtelser.add("Pleiepenger" to it.råVerdi().toDouble())
        }
        vedtak.opplysninger.find {
            it.opplysningTekstId == OmsorgspengerDagsats.opplysningTekstId && it.råVerdi().toDouble() > 0
        }?.let {
            samordnedeYtelser.add("Omsorgspenger" to it.råVerdi().toDouble())
        }
        vedtak.opplysninger.find {
            it.opplysningTekstId == OpplæringspengerDagsats.opplysningTekstId && it.råVerdi().toDouble() > 0
        }?.let {
            samordnedeYtelser.add("Opplæringspenger" to it.råVerdi().toDouble())
        }
        vedtak.opplysninger.find {
            it.opplysningTekstId == UføreDagsats.opplysningTekstId && it.råVerdi().toDouble() > 0
        }?.let {
            samordnedeYtelser.add("Uføre" to it.råVerdi().toDouble())
        }
        vedtak.opplysninger.find {
            it.opplysningTekstId == ForeldrepengerDagsats.opplysningTekstId && it.råVerdi().toDouble() > 0
        }?.let {
            samordnedeYtelser.add("Foreldrepenger" to it.råVerdi().toDouble())
        }
        vedtak.opplysninger.find {
            it.opplysningTekstId == SvangerskapspengerDagsats.opplysningTekstId && it.råVerdi().toDouble() > 0
        }?.let {
            samordnedeYtelser.add("Svangerskapspenger" to it.råVerdi().toDouble())
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

    private fun barnetillegg(): List<String> {
        return vedtak.opplysninger.find {
            it.opplysningTekstId == AntallBarnSomGirRettTilBarnetillegg.opplysningTekstId && it.råVerdi().toInt() > 0
        }
            ?.let {
                listOf(INNVILGELSE_BARNETILLEGG.brevblokkId)
            } ?: emptyList()
    }

    private fun grunnlag(): List<String> {
        val grunnlagBlokker = mutableListOf<String>()
        val kravTilMinsteinntektErOppfyltOld =
            vedtak.opplysninger.any { it.opplysningTekstId == KravTilMinsteinntekt.opplysningTekstId && it.formatertVerdi == "true" }
        val kravTilMinsteinntektOppfylt =
            vedtak.vilkår.any { vilkår ->
                vilkår.navn == MINSTEINNTEKT.navn && vilkår.status == IKKE_OPPFYLT
            }
        when {
            erInnvilgetMedVerneplikt() -> {
                grunnlagBlokker.add(INNVILGELSE_GRUNNLAG_VERNEPLIKT.brevblokkId)
                if (kravTilMinsteinntektErOppfyltOld || kravTilMinsteinntektOppfylt) {
                    grunnlagBlokker.add(INNVILGELSE_VERNEPLIKT_GUNSTIGEST.brevblokkId)
                }
            }

            else -> grunnlagBlokker.add(INNVILGELSE_GRUNNLAG.brevblokkId)
        }
        return grunnlagBlokker.toList()
    }

    private fun arbeidstidenDin(): List<String> {
        return if (erInnvilgetMedVerneplikt()) {
            listOf(INNVILGELSE_ARBEIDSTIDEN_DIN_VERNEPLIKT.brevblokkId)
        } else {
            listOf(INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId)
        }
    }

    private fun innledning(): List<String> {
        return when {
            erInnvilgetSomPermittert() -> listOf(INNVILGELSE_INNLEDNING_PERMITTERT.brevblokkId)
            else -> listOf(INNVILGELSE_INNLEDNING.brevblokkId)
        }
    }

    private fun virkningsdato(): List<String> {
        return when {
            erInnvilgetSomPermittert() -> listOf(INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT.brevblokkId)
            else -> listOf(INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId)
        }
    }

    private fun dagpengeperiode(): List<String> {
        return when {
            erInnvilgetMedVerneplikt() -> listOf(INNVILGELSE_DAGPENGEPERIODE_VERNEPLIKT.brevblokkId)
            erInnvilgetSomPermittert() -> listOf(INNVILGELSE_DAGPENGEPERIODE_PERMITTERT.brevblokkId)
            else -> listOf(INNVILGELSE_DAGPENGEPERIODE.brevblokkId)
        }
    }

    private fun ekstrablokkerPermittert(): List<String> {
        return when {
            erInnvilgetSomPermittert() ->
                listOf(
                    INNVILGELSE_ARBEIDSFORHOLD_AVSLUTT_PERMITTERT.brevblokkId,
                    INNVILGELSE_HVA_SKJER_ETTER_PERMITTERINGEN.brevblokkId,
                )

            else -> emptyList()
        }
    }

    private fun erInnvilgetMedVerneplikt() =
        vedtak.opplysninger.any {
            it.opplysningTekstId == ErInnvilgetMedVerneplikt.opplysningTekstId && it.formatertVerdi == "true"
        }

    private fun erInnvilgetSomPermittert(): Boolean {
        val gjelderPermittering = vedtak.vilkår.any { it.navn == OPPFYLLER_KRAVET_TIL_PERMITTERING.vilkårNavn && it.status == OPPFYLT }
        if (gjelderPermittering) {
            logger.info { "Behandling ${vedtak.behandlingId} er innvilget som permittert" }
        } else {
            logger.info { "Behandling ${vedtak.behandlingId} er ikke innvilget som permittert" }
        }
        return gjelderPermittering
    }
}
