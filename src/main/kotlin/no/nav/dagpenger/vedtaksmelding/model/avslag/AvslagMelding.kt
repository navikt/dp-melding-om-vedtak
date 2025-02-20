package no.nav.dagpenger.vedtaksmelding.model.avslag

import no.nav.dagpenger.vedtaksmelding.model.OpplysningTyper.FastsattVanligArbeidstidPerUke
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.IKKE_ANDRE_FULLE_YTELSER
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.IKKE_PASSERT_ALDERSGRENSE
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.IKKE_PÅVIRKET_AV_STREIK_ELLER_LOCKOUT
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.IKKE_UTDANNING
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.IKKE_UTESTENGT
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.MINSTEINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.OPPHOLD_I_NORGE
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.REELL_ARBEIDSSØKER
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.REELL_ARBEIDSSØKER_ARBEIDSFØR
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.REELL_ARBEIDSSØKER_ETHVERT_ARBEID
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.REELL_ARBEIDSSØKER_HELTID_DELTID
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.REELL_ARBEIDSSØKER_MOBILITET
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.REELL_ARBEIDSSØKER_REGISTRERT_SOM_ARBEIDSSØKER
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.TAPT_ARBEIDSINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.TAPT_ARBEIDSTID
import no.nav.dagpenger.vedtaksmelding.model.VilkårTyper.TAPT_ARBEIDSTID_ELLER_ARBEIDSINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_ALDER
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_ANDRE_FULLE_YTELSER
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_MINSTEINNTEKT_BEGRUNNELSE
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_OPPHOLD_UTLAND_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_OPPHOLD_UTLAND_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ARBEID_NORGE
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_REGISTRERT_ARBEIDSSOKER
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_STREIK_LOCKOUT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_STREIK_LOCKOUT_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_FASTSATT_VANLIG_ARBEDSTID_0
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_UTDANNING
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_UTESTENGT
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_UTESTENGT_HJEMMEL
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
            listOf(
                MINSTEINNTEKT,
                IKKE_PASSERT_ALDERSGRENSE,
                TAPT_ARBEIDSTID_ELLER_ARBEIDSINNTEKT,
                TAPT_ARBEIDSINNTEKT,
                TAPT_ARBEIDSTID,
                IKKE_UTESTENGT,
                IKKE_UTDANNING,
                REELL_ARBEIDSSØKER,
                OPPHOLD_I_NORGE,
                IKKE_ANDRE_FULLE_YTELSER,
                IKKE_PÅVIRKET_AV_STREIK_ELLER_LOCKOUT,
// TODO               MEDLEMSKAP,
// TODO               PERMITTERING,
            ).any { vilkår ->
                vedtak.vilkår.ikkeOppfylt(vilkår)
            }

    init {
        require(this.harBrevstøtte) {
            throw ManglerBrevstøtte("Avslag for behandling ${this.vedtak.behandlingId} mangler brevstøtte")
        }
    }

    private fun Set<Vilkår>.ikkeOppfylt(avslagsvilkår: VilkårTyper): Boolean =
        any { vilkår -> vilkår.navn == avslagsvilkår.vilkårNavn && vilkår.status == IKKE_OPPFYLT }

    private val innledendeBrevblokker = listOf(AVSLAG_INNLEDNING.brevblokkId)
    override val brevBlokkIder: List<String>
        get() {
            return innledendeBrevblokker +
                blokkerAvslagMinsteinntekt() +
                blokkerAvslagAlder() +
                blokkerAvslagTaptArbeidsinntekt() +
                blokkerAvslagTaptArbeidstid() +
                blokkerAvslagUtestengt() +
                blokkerAvslagUtdanning() +
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
            vilkår.navn == MINSTEINNTEKT.vilkårNavn && vilkår.status == IKKE_OPPFYLT
        }
            ?.let {
                listOf(AVSLAG_MINSTEINNTEKT_BEGRUNNELSE.brevblokkId)
            } ?: emptyList()
    }

    private fun blokkerAvslagAlder(): List<String> {
        return vedtak.vilkår.find { vilkår ->
            vilkår.navn == IKKE_PASSERT_ALDERSGRENSE.vilkårNavn && vilkår.status == IKKE_OPPFYLT
        }
            ?.let {
                listOf(
                    AVSLAG_ALDER.brevblokkId,
                )
            } ?: emptyList()
    }

    private fun blokkerAvslagTaptArbeidsinntekt(): List<String> {
        return vedtak.vilkår.find { vilkår ->
            vilkår.navn == TAPT_ARBEIDSINNTEKT.vilkårNavn && vilkår.status == IKKE_OPPFYLT
        }
            ?.let {
                listOf(AVSLAG_TAPT_ARBEIDSINNTEKT.brevblokkId)
            } ?: emptyList()
    }

    private fun blokkerAvslagTaptArbeidstid(): List<String> {
        return when (
            vedtak.vilkår.any { vilkår ->
                vilkår.navn == TAPT_ARBEIDSTID.vilkårNavn && vilkår.status == IKKE_OPPFYLT
            }
        ) {
            true ->
                when (
                    vedtak.opplysninger.any { opplysning ->
                        opplysning.opplysningTekstId == FastsattVanligArbeidstidPerUke.opplysningTekstId &&
                            opplysning.råVerdi().toDouble() > 0.0
                    }
                ) {
                    true -> listOf(AVSLAG_TAPT_ARBEIDSTID.brevblokkId)
                    false -> listOf(AVSLAG_TAPT_ARBEIDSTID_FASTSATT_VANLIG_ARBEDSTID_0.brevblokkId)
                }
            else -> emptyList()
        }
    }

    private fun blokkerAvslagUtestengt(): List<String> {
        return vedtak.vilkår.find { vilkår ->
            vilkår.navn == IKKE_UTESTENGT.vilkårNavn && vilkår.status == IKKE_OPPFYLT
        }
            ?.let {
                listOf(AVSLAG_UTESTENGT.brevblokkId, AVSLAG_UTESTENGT_HJEMMEL.brevblokkId)
            } ?: emptyList()
    }

    private fun blokkerAvslagUtdanning(): List<String> {
        return vedtak.vilkår.find { vilkår ->
            vilkår.navn == IKKE_UTDANNING.vilkårNavn && vilkår.status == IKKE_OPPFYLT
        }
            ?.let {
                listOf(
                    AVSLAG_UTDANNING.brevblokkId,
                )
            } ?: emptyList()
    }

    private fun blokkerAvslagReellArbeidssøker(): List<String> {
        val grunnerTilAvslag = mutableListOf(AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId)

        vedtak.vilkår.find { vilkår ->
            vilkår.navn == REELL_ARBEIDSSØKER_HELTID_DELTID.vilkårNavn && vilkår.status == IKKE_OPPFYLT
        }?.let {
            grunnerTilAvslag.add(AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId)
        }

        vedtak.vilkår.find { vilkår ->
            vilkår.navn == REELL_ARBEIDSSØKER_MOBILITET.vilkårNavn && vilkår.status == IKKE_OPPFYLT
        }?.let {
            grunnerTilAvslag.add(AVSLAG_REELL_ARBEIDSSØKER_ARBEID_NORGE.brevblokkId)
        }

        if (grunnerTilAvslag.contains(AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId) ||
            grunnerTilAvslag.contains(AVSLAG_REELL_ARBEIDSSØKER_ARBEID_NORGE.brevblokkId)
        ) {
            grunnerTilAvslag.add(AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE.brevblokkId)
        }
        vedtak.vilkår.find { vilkår ->
            vilkår.navn == REELL_ARBEIDSSØKER_ARBEIDSFØR.vilkårNavn && vilkår.status == IKKE_OPPFYLT
        }?.let {
            grunnerTilAvslag.add(AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR.brevblokkId)
        }

        vedtak.vilkår.find { vilkår ->
            vilkår.navn == REELL_ARBEIDSSØKER_ETHVERT_ARBEID.vilkårNavn && vilkår.status == IKKE_OPPFYLT
        }?.let {
            grunnerTilAvslag.add(AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID.brevblokkId)
        }

        vedtak.vilkår.find { vilkår ->
            vilkår.navn == REELL_ARBEIDSSØKER_REGISTRERT_SOM_ARBEIDSSØKER.vilkårNavn && vilkår.status == IKKE_OPPFYLT
        }?.let {
            grunnerTilAvslag.add(AVSLAG_REELL_ARBEIDSSØKER_REGISTRERT_ARBEIDSSOKER.brevblokkId)
        }

        if (grunnerTilAvslag.size > 1) {
            grunnerTilAvslag.add(AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId)
        } else {
            grunnerTilAvslag.removeFirst()
        }

        return grunnerTilAvslag.toList()
    }

    private fun blokkerAvslagOppholdUtland(): List<String> {
        return vedtak.vilkår.find { vilkår ->
            vilkår.navn == OPPHOLD_I_NORGE.vilkårNavn && vilkår.status == IKKE_OPPFYLT
        }
            ?.let {
                listOf(
                    AVSLAG_OPPHOLD_UTLAND_DEL_1.brevblokkId,
                    AVSLAG_OPPHOLD_UTLAND_DEL_2.brevblokkId,
                )
            } ?: emptyList()
    }

    private fun blokkerAndreFulleYtelser(): List<String> {
        return vedtak.vilkår.find { vilkår ->
            vilkår.navn == IKKE_ANDRE_FULLE_YTELSER.vilkårNavn && vilkår.status == IKKE_OPPFYLT
        }
            ?.let {
                listOf(AVSLAG_ANDRE_FULLE_YTELSER.brevblokkId)
            } ?: emptyList()
    }

    private fun blokkerStreikLockout(): List<String> {
        return vedtak.vilkår.find { vilkår ->
            vilkår.navn == IKKE_PÅVIRKET_AV_STREIK_ELLER_LOCKOUT.vilkårNavn && vilkår.status == IKKE_OPPFYLT
        }
            ?.let {
                listOf(
                    AVSLAG_STREIK_LOCKOUT_DEL_1.brevblokkId,
                    AVSLAG_STREIK_LOCKOUT_DEL_2.brevblokkId,
                )
            } ?: emptyList()
    }
}
