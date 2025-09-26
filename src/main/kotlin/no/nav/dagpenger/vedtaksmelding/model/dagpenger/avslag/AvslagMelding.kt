package no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag

import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall.AVSLÅTT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding
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
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.finnOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.ikkOppfyltBlokker
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.ikkeOppfylt
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk

class AvslagMelding(
    override val vedtak: Vedtak,
    alleBrevblokker: List<BrevBlokk>,
) : Vedtaksmelding(vedtak) {
    override val harBrevstøtte: Boolean =
        vedtak.utfall == AVSLÅTT &&
            setOfNotNull<DagpengerOpplysning<*, Boolean>>(
                vedtak.finnOpplysning<DagpengerOpplysning.OppfyllerKravTilMinsteinntekt>(),
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
        vedtak.ikkOppfyltBlokker<DagpengerOpplysning.OppfyllerKravTilMinsteinntekt> {
            listOf(AVSLAG_MINSTEINNTEKT_DEL_1.brevblokkId, AVSLAG_MINSTEINNTEKT_DEL_2.brevblokkId)
        }

    private fun blokkerAvslagAlder(): List<String> =
        vedtak.ikkOppfyltBlokker<DagpengerOpplysning.KravTilAlder> {
            listOf(AVSLAG_ALDER.brevblokkId)
        }

    private fun blokkerAvslagTaptArbeidsinntekt(): List<String> =
        vedtak.ikkOppfyltBlokker<DagpengerOpplysning.KravTilTapAvArbeidsinntekt> {
            listOf(AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_1.brevblokkId, AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_2.brevblokkId)
        }

    private fun blokkerAvslagTaptArbeidstid(): List<String> =
        when (vedtak.ikkeOppfylt<DagpengerOpplysning.KravTilTaptArbeidstid>()) {
            true ->
                when (gjelderPermitteringFisk()) {
                    true ->
                        listOf(
                            AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_1.brevblokkId,
                            AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_2.brevblokkId,
                        ) + avslagTaptArbeidstidPermittertFiskHjemmelBlokk()

                    false ->
                        listOf(
                            AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_1.brevblokkId,
                            AVSLAG_TAPT_ARBEIDSTID_DEL_2.brevblokkId,
                        ) + avslagTaptArbeidstidHjemmelBlokk()
                }

            false -> emptyList()
        }

    private fun blokkerAvslagUtestengt(): List<String> =
        vedtak.ikkOppfyltBlokker<DagpengerOpplysning.OppfyllerKravetTilIkkeUtestengt> {
            listOf(AVSLAG_UTESTENGT.brevblokkId, AVSLAG_UTESTENGT_HJEMMEL.brevblokkId)
        }

    private fun blokkerAvslagUtdanning(): List<String> =
        vedtak.ikkOppfyltBlokker<DagpengerOpplysning.KravTilUtdanning> {
            listOf(AVSLAG_UTDANNING.brevblokkId)
        }

    private fun blokkerAvslagReellArbeidssøker(): List<String> {
        val grunnerTilAvslag = mutableListOf(AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId)

        if (vedtak.ikkeOppfylt<DagpengerOpplysning.OppfyllerKravTilArbeidssøker>()) {
            grunnerTilAvslag.add(AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId)
        }

        if (vedtak.ikkeOppfylt<DagpengerOpplysning.OppfyllerKravTilMobilitet>()) {
            grunnerTilAvslag.add(AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE.brevblokkId)
        }

        if (grunnerTilAvslag.contains(AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId) ||
            grunnerTilAvslag.contains(AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE.brevblokkId)
        ) {
            grunnerTilAvslag.add(AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE.brevblokkId)
        }

        if (vedtak.ikkeOppfylt<DagpengerOpplysning.OppfyllerKravTilArbeidsfør>()) {
            grunnerTilAvslag.add(AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR.brevblokkId)
        }

        if (vedtak.ikkeOppfylt<DagpengerOpplysning.OppfyllerKravetTilEthvertArbeid>()) {
            grunnerTilAvslag.add(AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID.brevblokkId)
        }

        if (vedtak.ikkeOppfylt<DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker>()) {
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
        vedtak.ikkOppfyltBlokker<DagpengerOpplysning.OppfyllerKravetTilOpphold> {
            listOf(
                AVSLAG_OPPHOLD_UTLAND_DEL_1.brevblokkId,
                AVSLAG_OPPHOLD_UTLAND_DEL_2.brevblokkId,
            )
        }

    private fun blokkerAndreFulleYtelser(): List<String> =
        vedtak.ikkOppfyltBlokker<DagpengerOpplysning.IkkeFulleYtelser> {
            listOf(AVSLAG_ANDRE_FULLE_YTELSER.brevblokkId)
        }

    private fun blokkerStreikLockout(): List<String> =
        vedtak.ikkOppfyltBlokker<DagpengerOpplysning.IkkeStreikEllerLockout> {
            listOf(
                AVSLAG_STREIK_LOCKOUT_DEL_1.brevblokkId,
                AVSLAG_STREIK_LOCKOUT_DEL_2.brevblokkId,
            )
        }

    private fun blokkerAvslagMedlemskap(): List<String> =
        vedtak.ikkOppfyltBlokker<DagpengerOpplysning.OppfyllerMedlemskap> {
            listOf(
                AVSLAG_MEDLEMSKAP_DEL_1.brevblokkId,
                AVSLAG_MEDLEMSKAP_DEL_2.brevblokkId,
            )
        }

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

    private fun avslåttPermittering(): Boolean = vedtak.ikkeOppfylt<DagpengerOpplysning.OppfyllerKravetTilPermittering>()

    private fun avslåttPermitteringFisk(): Boolean = vedtak.ikkeOppfylt<DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri>()

    private fun gjelderPermitteringFisk(): Boolean =
        vedtak.finnOpplysning<DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri>() != null

    private fun harBrukBeregningsregelArbeidstidSiste6Mnd(): Boolean {
        return vedtak.opplysninger.any {
            it is DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder && it.verdi
        }
    }

    private fun harBrukBeregningsregelArbeidstidSiste12Mnd(): Boolean =
        vedtak.opplysninger.any {
            it is DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste12Måneder && it.verdi
        }

    private fun harBrukBeregningsregelArbeidstidSiste36Mnd(): Boolean =
        vedtak.opplysninger.any {
            it is DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste36Måneder && it.verdi
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
