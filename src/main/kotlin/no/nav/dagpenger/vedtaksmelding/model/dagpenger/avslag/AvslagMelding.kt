package no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag

import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.HarBruktBeregningsregelArbeidstidSiste12Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.HarBruktBeregningsregelArbeidstidSiste36Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.HarBruktBeregningsregelArbeidstidSiste6Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall.AVSLÅTT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vilkår
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vilkår.Status.IKKE_OPPFYLT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.IKKE_ANDRE_FULLE_YTELSER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.IKKE_PASSERT_ALDERSGRENSE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.IKKE_PÅVIRKET_AV_STREIK_ELLER_LOCKOUT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.IKKE_UTDANNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.IKKE_UTESTENGT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.MEDLEMSKAP
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.MINSTEINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.OPPHOLD_I_NORGE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.PERMITTERING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.PERMITTERING_FISK
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.REELL_ARBEIDSSØKER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.REELL_ARBEIDSSØKER_ARBEIDSFØR
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.REELL_ARBEIDSSØKER_ETHVERT_ARBEID
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.REELL_ARBEIDSSØKER_HELTID_DELTID
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.REELL_ARBEIDSSØKER_MOBILITET
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.REELL_ARBEIDSSØKER_REGISTRERT_SOM_ARBEIDSSØKER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.TAPT_ARBEIDSINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.TAPT_ARBEIDSTID
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.TAPT_ARBEIDSTID_ELLER_ARBEIDSINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_ALDER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_ANDRE_FULLE_YTELSER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING_PERMITTERT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING_PERMITTERT_FISK
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_MEDLEMSKAP_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_MEDLEMSKAP_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_MINSTEINNTEKT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_MINSTEINNTEKT_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_OPPHOLD_UTLAND_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_OPPHOLD_UTLAND_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_PERMITTERT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_PERMITTERT_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_REGISTRERT_ARBEIDSSOKER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_STREIK_LOCKOUT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_STREIK_LOCKOUT_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_12_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_36_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_6_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_12_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_36_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_6_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_UTDANNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_UTESTENGT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_UTESTENGT_HJEMMEL
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
                REELL_ARBEIDSSØKER_REGISTRERT_SOM_ARBEIDSSØKER,
                OPPHOLD_I_NORGE,
                IKKE_ANDRE_FULLE_YTELSER,
                IKKE_PÅVIRKET_AV_STREIK_ELLER_LOCKOUT,
                MEDLEMSKAP,
                PERMITTERING,
                PERMITTERING_FISK,
            ).any { vilkår ->
                vedtak.vilkår.ikkeOppfylt(vilkår)
            }

    init {
        require(this.harBrevstøtte) {
            throw ManglerBrevstøtte("Avslag for behandling ${this.vedtak.behandlingId}. Mangler brevstøtte.")
        }
    }

    private fun Set<Vilkår>.ikkeOppfylt(avslagsvilkår: VilkårTyper): Boolean =
        any { vilkår -> vilkår.navn == avslagsvilkår.vilkårNavn && vilkår.status == IKKE_OPPFYLT }

    private val innledendeBrevblokk =
        when {
            avslåttPermittering() -> listOf(AVSLAG_INNLEDNING_PERMITTERT.brevblokkId)
            avslåttPermitteringFisk() -> listOf(AVSLAG_INNLEDNING_PERMITTERT_FISK.brevblokkId)
            else -> listOf(AVSLAG_INNLEDNING.brevblokkId)
        }
    override val brevBlokkIder: List<String>
        get() {
            return innledendeBrevblokk +
                blokkerAvslagPermittering() +
                blokkerAvslagPermitteringFisk() +
                blokkerAvslagMinsteinntekt() +
                blokkerAvslagAlder() +
                blokkerAvslagTaptArbeidsinntekt() +
                blokkerAvslagTaptArbeidstid() +
                blokkerAvslagMedlemskap() +
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

    private fun blokkerAvslagMinsteinntekt(): List<String> =
        vedtak.vilkår
            .find { vilkår ->
                vilkår.navn == MINSTEINNTEKT.vilkårNavn && vilkår.status == IKKE_OPPFYLT
            }?.let {
                listOf(AVSLAG_MINSTEINNTEKT_DEL_1.brevblokkId, AVSLAG_MINSTEINNTEKT_DEL_2.brevblokkId)
            } ?: emptyList()

    private fun blokkerAvslagAlder(): List<String> =
        vedtak.vilkår
            .find { vilkår ->
                vilkår.navn == IKKE_PASSERT_ALDERSGRENSE.vilkårNavn && vilkår.status == IKKE_OPPFYLT
            }?.let {
                listOf(
                    AVSLAG_ALDER.brevblokkId,
                )
            } ?: emptyList()

    private fun blokkerAvslagTaptArbeidsinntekt(): List<String> =
        vedtak.vilkår
            .find { vilkår ->
                vilkår.navn == TAPT_ARBEIDSINNTEKT.vilkårNavn && vilkår.status == IKKE_OPPFYLT
            }?.let {
                listOf(AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_1.brevblokkId, AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_2.brevblokkId)
            } ?: emptyList()

    private fun blokkerAvslagTaptArbeidstid(): List<String> =
        when (
            vedtak.vilkår.any { vilkår ->
                vilkår.navn == TAPT_ARBEIDSTID.vilkårNavn && vilkår.status == IKKE_OPPFYLT
            }
        ) {
            true ->
                when (gjelderPermitteringFisk()) {
                    true ->
                        listOf(
                            AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_1.brevblokkId,
                            AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_2.brevblokkId,
                        ) + avslagTaptArbeidstidPermittertFiskHjemmelBlokk()

                    false ->
                        listOf(
                            AVSLAG_TAPT_ARBEIDSTID_DEL_1.brevblokkId,
                            AVSLAG_TAPT_ARBEIDSTID_DEL_2.brevblokkId,
                        ) + avslagTaptArbeidstidHjemmelBlokk()
                }
            else -> emptyList()
        }

    private fun blokkerAvslagUtestengt(): List<String> =
        vedtak.vilkår
            .find { vilkår ->
                vilkår.navn == IKKE_UTESTENGT.vilkårNavn && vilkår.status == IKKE_OPPFYLT
            }?.let {
                listOf(AVSLAG_UTESTENGT.brevblokkId, AVSLAG_UTESTENGT_HJEMMEL.brevblokkId)
            } ?: emptyList()

    private fun blokkerAvslagUtdanning(): List<String> =
        vedtak.vilkår
            .find { vilkår ->
                vilkår.navn == IKKE_UTDANNING.vilkårNavn && vilkår.status == IKKE_OPPFYLT
            }?.let {
                listOf(
                    AVSLAG_UTDANNING.brevblokkId,
                )
            } ?: emptyList()

    private fun blokkerAvslagReellArbeidssøker(): List<String> {
        val grunnerTilAvslag = mutableListOf(AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId)

        vedtak.vilkår
            .find { vilkår ->
                vilkår.navn == REELL_ARBEIDSSØKER_HELTID_DELTID.vilkårNavn && vilkår.status == IKKE_OPPFYLT
            }?.let {
                grunnerTilAvslag.add(AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId)
            }

        vedtak.vilkår
            .find { vilkår ->
                vilkår.navn == REELL_ARBEIDSSØKER_MOBILITET.vilkårNavn && vilkår.status == IKKE_OPPFYLT
            }?.let {
                grunnerTilAvslag.add(AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE.brevblokkId)
            }

        if (grunnerTilAvslag.contains(AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId) ||
            grunnerTilAvslag.contains(AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE.brevblokkId)
        ) {
            grunnerTilAvslag.add(AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE.brevblokkId)
        }
        vedtak.vilkår
            .find { vilkår ->
                vilkår.navn == REELL_ARBEIDSSØKER_ARBEIDSFØR.vilkårNavn && vilkår.status == IKKE_OPPFYLT
            }?.let {
                grunnerTilAvslag.add(AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR.brevblokkId)
            }

        vedtak.vilkår
            .find { vilkår ->
                vilkår.navn == REELL_ARBEIDSSØKER_ETHVERT_ARBEID.vilkårNavn && vilkår.status == IKKE_OPPFYLT
            }?.let {
                grunnerTilAvslag.add(AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID.brevblokkId)
            }

        vedtak.vilkår
            .find { vilkår ->
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

    private fun blokkerAvslagOppholdUtland(): List<String> =
        vedtak.vilkår
            .find { vilkår ->
                vilkår.navn == OPPHOLD_I_NORGE.vilkårNavn && vilkår.status == IKKE_OPPFYLT
            }?.let {
                listOf(
                    AVSLAG_OPPHOLD_UTLAND_DEL_1.brevblokkId,
                    AVSLAG_OPPHOLD_UTLAND_DEL_2.brevblokkId,
                )
            } ?: emptyList()

    private fun blokkerAndreFulleYtelser(): List<String> =
        vedtak.vilkår
            .find { vilkår ->
                vilkår.navn == IKKE_ANDRE_FULLE_YTELSER.vilkårNavn && vilkår.status == IKKE_OPPFYLT
            }?.let {
                listOf(AVSLAG_ANDRE_FULLE_YTELSER.brevblokkId)
            } ?: emptyList()

    private fun blokkerStreikLockout(): List<String> =
        vedtak.vilkår
            .find { vilkår ->
                vilkår.navn == IKKE_PÅVIRKET_AV_STREIK_ELLER_LOCKOUT.vilkårNavn && vilkår.status == IKKE_OPPFYLT
            }?.let {
                listOf(
                    AVSLAG_STREIK_LOCKOUT_DEL_1.brevblokkId,
                    AVSLAG_STREIK_LOCKOUT_DEL_2.brevblokkId,
                )
            } ?: emptyList()

    private fun blokkerAvslagMedlemskap(): List<String> =
        vedtak.vilkår
            .find { vilkår ->
                vilkår.navn == MEDLEMSKAP.vilkårNavn && vilkår.status == IKKE_OPPFYLT
            }?.let {
                listOf(
                    AVSLAG_MEDLEMSKAP_DEL_1.brevblokkId,
                    AVSLAG_MEDLEMSKAP_DEL_2.brevblokkId,
                )
            } ?: emptyList()

    private fun blokkerAvslagPermittering(): List<String> =
        when (avslåttPermittering()) {
            true ->
                listOf(
                    AVSLAG_PERMITTERT_DEL_1.brevblokkId,
                    AVSLAG_PERMITTERT_DEL_2.brevblokkId,
                )

            false -> emptyList()
        }

    private fun blokkerAvslagPermitteringFisk(): List<String> =
        when (avslåttPermitteringFisk()) {
            true ->
                listOf(
                    AVSLAG_PERMITTERT_DEL_1.brevblokkId,
                    AVSLAG_PERMITTERT_DEL_2.brevblokkId,
                )

            false -> emptyList()
        }

    private fun avslåttPermittering(): Boolean =
        vedtak.vilkår.any { vilkår -> vilkår.navn == PERMITTERING.vilkårNavn && vilkår.status == IKKE_OPPFYLT }

    private fun avslåttPermitteringFisk(): Boolean =
        vedtak.vilkår.any { vilkår -> vilkår.navn == PERMITTERING_FISK.vilkårNavn && vilkår.status == IKKE_OPPFYLT }

    private fun gjelderPermitteringFisk(): Boolean = vedtak.vilkår.any { vilkår -> vilkår.navn == PERMITTERING_FISK.vilkårNavn }

    private fun harBrukBeregningsregelArbeidstidSiste6Mnd(): Boolean =
        vedtak.opplysninger.any { opplysning ->
            opplysning.opplysningTekstId == HarBruktBeregningsregelArbeidstidSiste6Måneder.opplysningTekstId &&
                opplysning.råVerdi().toBoolean()
        }

    private fun harBrukBeregningsregelArbeidstidSiste12Mnd(): Boolean =
        vedtak.opplysninger.any { opplysning ->
            opplysning.opplysningTekstId == HarBruktBeregningsregelArbeidstidSiste12Måneder.opplysningTekstId &&
                opplysning.råVerdi().toBoolean()
        }

    private fun harBrukBeregningsregelArbeidstidSiste36Mnd(): Boolean =
        vedtak.opplysninger.any { opplysning ->
            opplysning.opplysningTekstId == HarBruktBeregningsregelArbeidstidSiste36Måneder.opplysningTekstId &&
                opplysning.råVerdi().toBoolean()
        }

    private fun avslagTaptArbeidstidHjemmelBlokk(): List<String> =
        when {
            harBrukBeregningsregelArbeidstidSiste6Mnd() -> listOf(AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_6_MND.brevblokkId)
            harBrukBeregningsregelArbeidstidSiste12Mnd() -> listOf(AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_12_MND.brevblokkId)
            harBrukBeregningsregelArbeidstidSiste36Mnd() -> listOf(AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_36_MND.brevblokkId)
            else -> emptyList()
        }

    private fun avslagTaptArbeidstidPermittertFiskHjemmelBlokk(): List<String> =
        when {
            harBrukBeregningsregelArbeidstidSiste6Mnd() ->
                listOf(
                    AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_6_MND.brevblokkId,
                )

            harBrukBeregningsregelArbeidstidSiste12Mnd() ->
                listOf(
                    AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_12_MND.brevblokkId,
                )

            harBrukBeregningsregelArbeidstidSiste36Mnd() ->
                listOf(
                    AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_36_MND.brevblokkId,
                )

            else -> emptyList()
        }
}
