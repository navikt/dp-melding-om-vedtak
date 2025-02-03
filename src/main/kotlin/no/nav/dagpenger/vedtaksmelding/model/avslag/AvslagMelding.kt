package no.nav.dagpenger.vedtaksmelding.model.avslag

import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.IKKE_ANDRE_FULLE_YTELSER
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.IKKE_UTESTENGT
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.MEDLEM_PÅVIRKET_AV_STREIK_ELLER_LOCKOUT
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.MINSTEINNTEKT_ELLER_VERNEPLIKT
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.OPPHOLD_I_NORGE
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER_ARBEIDSFØR
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER_ETHVERT_ARBEID
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER_HELTID_DELTID
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER_MOBILITET
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER_REGISTRERT_SOM_ARBEIDSSØKER
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.TAPT_ARBEIDSINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.TAPT_ARBEIDSTID
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak.Utfall.AVSLÅTT
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vilkår
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vilkår.Status.IKKE_OPPFYLT
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk

class AvslagMelding(
    override val vedtak: Vedtak,
    alleBrevblokker: List<BrevBlokk>,
) : VedtakMelding(vedtak) {
    override val harBrevstøtte: Boolean =
        vedtak.utfall == AVSLÅTT &&
            (
                vedtak.vilkår.avslagMinsteinntekt() ||
                    vedtak.vilkår.avslagReellArbeidssøker() ||
                    vedtak.vilkår.avslagTaptArbeidsinntekt() ||
                    vedtak.vilkår.avslagTaptArbeidstid() ||
                    vedtak.vilkår.avslagOppholdUtland() ||
                    vedtak.vilkår.avslagAndreFulleYtelser() ||
                    vedtak.vilkår.avslagUtestengt() ||
                    vedtak.vilkår.avslagStreikEllerLockout()
            )

    init {
        require(this.harBrevstøtte) {
            throw ManglerBrevstøtte("Avslag for behandling ${this.vedtak.behandlingId} mangler brevstøtte")
        }
    }

    private val innledendeBrevblokker = listOf("brev.blokk.vedtak-avslag")

    override val brevBlokkIder: List<String>
        get() {
            return innledendeBrevblokker +
                blokkerAvslagMinsteinntekt() +
                blokkerAvslagTaptArbeidsinntekt() +
                blokkerAvslagTaptArbeidstid() +
                blokkerAvslagUtestengt() +
                blokkerAvslagReellArbeidssøker() +
                blokkerAvslagOppholdUtland() +
                blokkerAndreFulleYtelser() +
                blokkerStreikLockout()
        }
    override val brevBlokker: List<BrevBlokk> =
        run {
            val brevBlokkMap = alleBrevblokker.associateBy { it.textId }
            brevBlokkIder().mapNotNull { id -> brevBlokkMap[id] }
        }

    private fun blokkerAvslagMinsteinntekt(): List<String> {
        return vedtak.vilkår.find { vilkår ->
            vilkår.navn == MINSTEINNTEKT_ELLER_VERNEPLIKT.navn && vilkår.status == IKKE_OPPFYLT
        }
            ?.let {
                listOf("brev.blokk.begrunnelse-avslag-minsteinntekt")
            } ?: emptyList()
    }

    private fun blokkerAvslagOppholdUtland(): List<String> {
        return vedtak.vilkår.find { vilkår ->
            vilkår.navn == OPPHOLD_I_NORGE.navn && vilkår.status == IKKE_OPPFYLT
        }
            ?.let {
                listOf(
                    "brev.blokk.avslag-opphold-utlandet-del-1",
                    "brev.blokk.avslag-opphold-utlandet-del-2",
                )
            } ?: emptyList()
    }

    private fun blokkerAndreFulleYtelser(): List<String> {
        return vedtak.vilkår.find { vilkår ->
            vilkår.navn == IKKE_ANDRE_FULLE_YTELSER.navn && vilkår.status == IKKE_OPPFYLT
        }
            ?.let {
                listOf("brev.blokk.avslag-andre-fulle-ytelser")
            } ?: emptyList()
    }

    private fun blokkerStreikLockout(): List<String> {
        return vedtak.vilkår.find { vilkår ->
            vilkår.navn == MEDLEM_PÅVIRKET_AV_STREIK_ELLER_LOCKOUT.navn && vilkår.status == IKKE_OPPFYLT
        }
            ?.let {
                listOf(
                    "brev.blokk.avslag-streik-lockout-del-1",
                    "brev.blokk.avslag-streik-lockout-del-2",
                )
            } ?: emptyList()
    }

    private fun blokkerAvslagTaptArbeidstid(): List<String> {
        return vedtak.vilkår.find { vilkår ->
            vilkår.navn == TAPT_ARBEIDSTID.navn && vilkår.status == IKKE_OPPFYLT
        }
            ?.let {
                listOf("brev.blokk.avslag-tapt-arbeidstid")
            } ?: emptyList()
    }

    private fun blokkerAvslagTaptArbeidsinntekt(): List<String> {
        return vedtak.vilkår.find { vilkår ->
            vilkår.navn == TAPT_ARBEIDSINNTEKT.navn && vilkår.status == IKKE_OPPFYLT
        }
            ?.let {
                listOf("brev.blokk.avslag-tapt-arbeidsinntekt")
            } ?: emptyList()
    }

    private fun blokkerAvslagUtestengt(): List<String> {
        return vedtak.vilkår.find { vilkår ->
            vilkår.navn == IKKE_UTESTENGT.navn && vilkår.status == IKKE_OPPFYLT
        }
            ?.let {
                listOf("brev.blokk.avslag-utestengt", "brev.blokk.avslag-utestengt-hjemmel")
            } ?: emptyList()
    }

    private fun blokkerAvslagReellArbeidssøker(): List<String> {
        val grunnerTilAvslag = mutableListOf("brev.blokk.avslag-reell-arbeidssoker-overskrift")

        vedtak.vilkår.find { vilkår ->
            vilkår.navn == REELL_ARBEIDSSØKER_HELTID_DELTID.navn && vilkår.status == IKKE_OPPFYLT
        }?.let {
            grunnerTilAvslag.add("brev.blokk.avslag-reell-arbeidssoker-heltid-deltid")
        }

        vedtak.vilkår.find { vilkår ->
            vilkår.navn == REELL_ARBEIDSSØKER_MOBILITET.navn && vilkår.status == IKKE_OPPFYLT
        }?.let {
            grunnerTilAvslag.add("brev.blokk.avslag-reell-arbeidssoker-arbeid-i-hele-norge")
        }

        if (grunnerTilAvslag.size > 1) {
            grunnerTilAvslag.add("brev.blokk.avslag-reell-arbeidssoker-unntak-heltid-deltid-hele-norge")
        }
        vedtak.vilkår.find { vilkår ->
            vilkår.navn == REELL_ARBEIDSSØKER_ARBEIDSFØR.navn && vilkår.status == IKKE_OPPFYLT
        }?.let {
            grunnerTilAvslag.add("brev.blokk.avslag-reell-arbeidssoker-arbeidsfor")
        }

        vedtak.vilkår.find { vilkår ->
            vilkår.navn == REELL_ARBEIDSSØKER_ETHVERT_ARBEID.navn && vilkår.status == IKKE_OPPFYLT
        }?.let {
            grunnerTilAvslag.add("brev.blokk.avslag-reell-arbeidssoker-ethvert-arbeid")
        }

        vedtak.vilkår.find { vilkår ->
            vilkår.navn == REELL_ARBEIDSSØKER_REGISTRERT_SOM_ARBEIDSSØKER.navn && vilkår.status == IKKE_OPPFYLT
        }?.let {
            grunnerTilAvslag.add("brev.blokk.avslag-reell-arbeidssoker-registrert-arbeidssoker")
        }

        if (grunnerTilAvslag.size > 1) {
            grunnerTilAvslag.add("brev.blokk.avslag-reell-arbeidssoker-hjemmel")
        } else {
            grunnerTilAvslag.removeFirst()
        }

        return grunnerTilAvslag.toList()
    }

    private fun Set<Vilkår>.avslagMinsteinntekt(): Boolean {
        return this.any {
                vilkår ->
            vilkår.navn == MINSTEINNTEKT_ELLER_VERNEPLIKT.navn && vilkår.status == IKKE_OPPFYLT
        }
    }

    private fun Set<Vilkår>.avslagTaptArbeidstid(): Boolean {
        return this.any {
                vilkår ->
            vilkår.navn == TAPT_ARBEIDSTID.navn && vilkår.status == IKKE_OPPFYLT
        }
    }

    private fun Set<Vilkår>.avslagTaptArbeidsinntekt(): Boolean {
        return this.any {
                vilkår ->
            vilkår.navn == TAPT_ARBEIDSINNTEKT.navn && vilkår.status == IKKE_OPPFYLT
        }
    }

    private fun Set<Vilkår>.avslagUtestengt(): Boolean {
        return this.any {
                vilkår ->
            vilkår.navn == IKKE_UTESTENGT.navn && vilkår.status == IKKE_OPPFYLT
        }
    }

    private fun Set<Vilkår>.avslagStreikEllerLockout(): Boolean {
        return this.any {
                vilkår ->
            vilkår.navn == MEDLEM_PÅVIRKET_AV_STREIK_ELLER_LOCKOUT.navn && vilkår.status == IKKE_OPPFYLT
        }
    }

    private fun Set<Vilkår>.avslagOppholdUtland(): Boolean {
        return this.any {
                vilkår ->
            vilkår.navn == OPPHOLD_I_NORGE.navn && vilkår.status == IKKE_OPPFYLT
        }
    }

    private fun Set<Vilkår>.avslagAndreFulleYtelser(): Boolean {
        return this.any {
                vilkår ->
            vilkår.navn == IKKE_ANDRE_FULLE_YTELSER.navn && vilkår.status == IKKE_OPPFYLT
        }
    }

    private fun Set<Vilkår>.avslagReellArbeidssøker(): Boolean {
        return this.any {
                vilkår ->
            vilkår.navn == REELL_ARBEIDSSØKER.navn && vilkår.status == IKKE_OPPFYLT
        }
    }
}
