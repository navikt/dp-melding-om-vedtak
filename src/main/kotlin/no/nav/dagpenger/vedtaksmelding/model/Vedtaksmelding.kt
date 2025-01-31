package no.nav.dagpenger.vedtaksmelding.model

import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.IKKE_ANDRE_FULLE_YTELSER
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.IKKE_UTESTENGT
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.MINSTEINNTEKT_ELLER_VERNEPLIKT
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.OPPHOLD_I_NORGE
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER_ARBEIDSFØR
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER_ETHVERT_ARBEID
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER_HELTID_DELTID
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER_MOBILITET
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER_REGISTRERT_SOM_ARBEIDSSØKER
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.TAPT_ARBEIDSINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.TAPT_ARBEIDSTID
import no.nav.dagpenger.vedtaksmelding.model.Vilkår.Status.IKKE_OPPFYLT
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk
import no.nav.dagpenger.vedtaksmelding.portabletext.Child

private val logger = KotlinLogging.logger {}

sealed class Vedtaksmelding(
    protected open val vedtak: Vedtak,
) {
    abstract val harBrevstøtte: Boolean

    protected abstract val brevBlokkIder: List<String>
    protected abstract val brevBlokker: List<BrevBlokk>

    fun brevBlokkIder(): List<String> {
        return brevBlokkIder + fasteBlokker
    }

    fun hentBrevBlokker(): List<BrevBlokk> {
        return brevBlokker
    }

    fun hentOpplysninger(): List<Opplysning> {
        return brevBlokker.asSequence()
            .filter { it.textId in brevBlokkIder() }
            .flatMap { it.innhold }
            .flatMap { it.children }
            .filterIsInstance<Child.OpplysningReference>()
            .map { it.behandlingOpplysning.textId }
            .map { vedtak.hentOpplysning(it) }
            .toList()
    }

    fun hentFagsakId(): String {
        return vedtak.fagsakId
    }

    companion object {
        val fasteBlokker =
            listOf(
                "brev.blokk.sporsmaal",
                "brev.blokk.rett-til-innsyn",
                "brev.blokk.rett-til-aa-klage",
            )

        fun byggVedtaksmelding(
            vedtak: Vedtak,
            alleBrevblokker: List<BrevBlokk>,
        ): Vedtaksmelding {
            return try {
                mutableSetOf<Result<Vedtaksmelding>>().apply {
                    add(kotlin.runCatching { Avslag(vedtak, alleBrevblokker) })
                    add(kotlin.runCatching { Innvilgelse(vedtak, alleBrevblokker) })
                }
                    .single { it.isSuccess }
                    .getOrThrow()
            } catch (e: Exception) {
                logger.error(e) { "Feil ved oppbygging av vedtaksmelding. BehandlingId ${vedtak.behandlingId}" }
                when (e) {
                    is NoSuchElementException -> throw UkjentVedtakException(
                        "Kunne ikke bygge vedtaksmelding ut fra vedtak: $vedtak. Feil: ${e.message}",
                        e,
                    )

                    is IllegalArgumentException -> throw UkjentVedtakException(
                        "Alvorlig feil: Vedtak er gyldig for flere type vedtaksmeldinger: $vedtak. Feil: ${e.message}",
                        e,
                    )

                    else -> throw e
                }
            }
        }
    }

    class UkjentVedtakException(override val message: String, override val cause: Throwable? = null) :
        RuntimeException(message, cause)

    class ManglerBrevstøtte(override val message: String) : RuntimeException(message)
}

class Avslag(
    override val vedtak: Vedtak,
    alleBrevblokker: List<BrevBlokk>,
) : Vedtaksmelding(vedtak) {
    override val harBrevstøtte: Boolean =
        vedtak.utfall == Utfall.AVSLÅTT &&
            (
                vedtak.vilkår.avslagMinsteinntekt() ||
                    vedtak.vilkår.avslagReellArbeidssøker() ||
                    vedtak.vilkår.avslagTaptArbeidsinntekt() ||
                    vedtak.vilkår.avslagTaptArbeidstid() ||
                    vedtak.vilkår.avslagOppholdUtland() ||
                    vedtak.vilkår.avslagAndreFulleYtelser() ||
                    vedtak.vilkår.avslagUtestengt()
            )

    init {
        require(this.harBrevstøtte) {
            throw ManglerBrevstøtte("Vedtak for behandling: ${this.vedtak.behandlingId} mangler brevstøtte")
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
                blokkerAndreFulleYtelser()
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
        return this.any { vilkår -> vilkår.navn == MINSTEINNTEKT_ELLER_VERNEPLIKT.navn && vilkår.status == IKKE_OPPFYLT }
    }

    private fun Set<Vilkår>.avslagTaptArbeidstid(): Boolean {
        return this.any { vilkår -> vilkår.navn == TAPT_ARBEIDSTID.navn && vilkår.status == IKKE_OPPFYLT }
    }

    private fun Set<Vilkår>.avslagTaptArbeidsinntekt(): Boolean {
        return this.any { vilkår -> vilkår.navn == TAPT_ARBEIDSINNTEKT.navn && vilkår.status == IKKE_OPPFYLT }
    }

    private fun Set<Vilkår>.avslagUtestengt(): Boolean {
        return this.any { vilkår -> vilkår.navn == IKKE_UTESTENGT.navn && vilkår.status == IKKE_OPPFYLT }
    }

    private fun Set<Vilkår>.avslagOppholdUtland(): Boolean {
        return this.any { vilkår -> vilkår.navn == OPPHOLD_I_NORGE.navn && vilkår.status == IKKE_OPPFYLT }
    }

    private fun Set<Vilkår>.avslagAndreFulleYtelser(): Boolean {
        return this.any { vilkår -> vilkår.navn == IKKE_ANDRE_FULLE_YTELSER.navn && vilkår.status == IKKE_OPPFYLT }
    }

    private fun Set<Vilkår>.avslagReellArbeidssøker(): Boolean {
        return this.any { vilkår -> vilkår.navn == REELL_ARBEIDSSØKER.navn && vilkår.status == IKKE_OPPFYLT }
    }
}

class Innvilgelse(
    override val vedtak: Vedtak,
    alleBrevblokker: List<BrevBlokk>,
) : Vedtaksmelding(vedtak) {
    override val harBrevstøtte: Boolean = vedtak.utfall == Utfall.INNVILGET

    init {
        require(this.harBrevstøtte) { "Vedtak oppfyller ikke innvilgelseskriterier" }
    }

    override val brevBlokkIder: List<String>
        get() {
            val innledendeBrevblokker =
                listOf(
                    "brev.blokk.vedtak-innvilgelse",
                    "brev.blokk.begrunnelse-innvilgelsesdato",
                    "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                    "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                )

            val avsluttendeBrevblokker =
                listOf(
                    "brev.blokk.arbeidstiden-din",
                    "brev.blokk.egenandel",
                    "brev.blokk.du-maa-sende-meldekort",
                    "brev.blokk.utbetaling",
                    "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                    "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                    "brev.blokk.du-maa-melde-fra-om-endringer",
                    "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
                )

            return innledendeBrevblokker + barnetillegg() + nittiProsentRegel() + samordnet() + grunnlag() + avsluttendeBrevblokker
        }
    override val brevBlokker: List<BrevBlokk> =
        run {
            val brevBlokkMap = alleBrevblokker.associateBy { it.textId }
            brevBlokkIder().mapNotNull { id -> brevBlokkMap[id] }
        }

    private fun nittiProsentRegel(): List<String> {
        val id = "opplysning.andel-av-dagsats-med-barnetillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget"
        return vedtak.opplysninger.find {
            it.opplysningTekstId == id &&
                it.verdi.toDouble() > 0
        }
            ?.let {
                listOf("brev.blokk.nittiprosentregel")
            } ?: emptyList()
    }

    private fun samordnet(): List<String> {
        if (!vedtak.opplysninger.any { it.opplysningTekstId == "opplysning.har-samordnet" && it.verdi == "true" }) {
            return emptyList()
        }

        val samordnedeYtelser = mutableSetOf<Pair<String, Double>>()
        vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.sykepenger-dagsats" && it.verdi > "0" }?.let {
            samordnedeYtelser.add("Sykepenger" to it.verdi.toDouble())
        }
        vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.pleiepenger-dagsats" && it.verdi > "0" }?.let {
            samordnedeYtelser.add("Pleiepenger" to it.verdi.toDouble())
        }
        vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.omsorgspenger-dagsats" && it.verdi > "0" }?.let {
            samordnedeYtelser.add("Omsorgspenger" to it.verdi.toDouble())
        }
        vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.opplaeringspenger-dagsats" && it.verdi > "0" }?.let {
            samordnedeYtelser.add("Opplæringspenger" to it.verdi.toDouble())
        }
        vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.ufore-dagsats" && it.verdi > "0" }?.let {
            samordnedeYtelser.add("Uføre" to it.verdi.toDouble())
        }
        vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.foreldrepenger-dagsats" && it.verdi > "0" }?.let {
            samordnedeYtelser.add("Foreldrepenger" to it.verdi.toDouble())
        }
        vedtak.opplysninger.find {
            it.opplysningTekstId == "opplysning.svangerskapspenger-dagsats" && it.verdi > "0"
        }?.let {
            samordnedeYtelser.add("Svangerskapspenger" to it.verdi.toDouble())
        }

        val samordningBlokker = mutableListOf<String>()
        if (samordnedeYtelser.size == 1) {
            when (samordnedeYtelser.first().first) {
                "Sykepenger" -> samordningBlokker.add("brev.blokk.samordnet-med-sykepenger")
                "Pleiepenger" -> samordningBlokker.add("brev.blokk.samordnet-med-pleiepenger")
                "Omsorgspenger" -> samordningBlokker.add("brev.blokk.samordnet-med-omsorgspenger")
                "Opplæringspenger" -> samordningBlokker.add("brev.blokk.samordnet-med-opplaeringspenger")
                "Uføre" -> samordningBlokker.add("brev.blokk.samordnet-med-ufore")
                "Foreldrepenger" -> samordningBlokker.add("brev.blokk.samordnet-med-foreldrepenger")
                "Svangerskapspenger" -> samordningBlokker.add("brev.blokk.samordnet-med-svangerskapspenger")
            }
        } else {
            samordningBlokker.add("brev.blokk.samordnet-generisk")
        }
        return samordningBlokker
    }

    private fun barnetillegg(): List<String> {
        return vedtak.opplysninger.find {
            it.opplysningTekstId == "opplysning.antall-barn-som-gir-rett-til-barnetillegg" && it.verdi.toInt() > 0
        }
            ?.let {
                listOf("brev.blokk.barnetillegg")
            } ?: emptyList()
    }

    private fun grunnlag(): List<String> {
        val grunnlagBlokker = mutableListOf<String>()
        val erInnvilgetMedVerneplikt =
            vedtak.opplysninger.any { it.opplysningTekstId == "opplysning.er-innvilget-med-verneplikt" && it.verdi == "true" }
        val kravTilMinsteinntektErOppfylt =
            vedtak.opplysninger.any { it.opplysningTekstId == "opplysning.krav-til-minsteinntekt" && it.verdi == "true" }

        if (erInnvilgetMedVerneplikt) {
            grunnlagBlokker.add("brev.blokk.grunnlag-for-verneplikt")
            if (kravTilMinsteinntektErOppfylt) {
                grunnlagBlokker.add("brev.blokk.verneplikt-gunstigere-enn-inntekt")
            }
        } else {
            grunnlagBlokker.add("brev.blokk.grunnlag")
        }
        return grunnlagBlokker.toList()
    }
}
