package no.nav.dagpenger.vedtaksmelding.model

import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.Mediator
import no.nav.dagpenger.vedtaksmelding.model.Vilkår.Status.IKKE_OPPFYLT
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk
import java.util.UUID

private val logger = KotlinLogging.logger {}

sealed class Vedtaksmelding(
    protected open val vedtak: Vedtak,
    protected open val mediator: Mediator,
) {
    protected abstract val brevBlokkIder: List<String>
    abstract val harBrevstøtte: Boolean

    fun brevBlokkIder(): List<String> {
        return brevBlokkIder + fasteBlokker
    }

    suspend fun hentBrevBlokker(): List<BrevBlokk> {
        return mediator.hentBrevBlokker(brevBlokkIder())
    }

    suspend fun hentOpplysninger(): List<Opplysning> {
        val opplysningstekstIder = mediator.hentOpplysningTekstIder(brevBlokkIder())
        logger.info { "Skal hente opplysninger basert på følgende tekstider: $opplysningstekstIder" }
        return opplysningstekstIder.map { opplysningstekstId -> vedtak.hentOpplysning(opplysningstekstId) }
    }

    fun hentUtvidedeBeskrivelser(behandlingId: UUID): List<UtvidetBeskrivelse> {
        return mediator.hentUtvidedeBeskrivelser(behandlingId)
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
            mediator: Mediator,
        ): Vedtaksmelding {
            return try {
                mutableSetOf<Result<Vedtaksmelding>>().apply {
                    add(kotlin.runCatching { Avslag(vedtak, mediator) })
                    add(kotlin.runCatching { Innvilgelse(vedtak, mediator) })
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

data class Avslag(
    override val vedtak: Vedtak,
    override val mediator: Mediator,
) : Vedtaksmelding(vedtak, mediator) {
    override val harBrevstøtte: Boolean =
        vedtak.utfall == Utfall.AVSLÅTT &&
            (vedtak.vilkår.avslagMinsteinntekt() || vedtak.vilkår.reellArbeidssøker() || vedtak.vilkår.avslagArbeidstid())

    init {
        require(this.harBrevstøtte) {
            throw ManglerBrevstøtte("Vedtak for behandling: ${this.vedtak.behandlingId} mangler brevstøtte")
        }
    }

    private val pre =
        listOf(
            "brev.blokk.vedtak-avslag",
        )
    override val brevBlokkIder: List<String>
        get() {
            return pre + avslagMinsteInntekt() + avslagReellArbeidssøker() + avslagTaptArbeidstid()
        }

    private fun avslagMinsteInntekt(): List<String> {
        return vedtak.vilkår.find {
            it.navn == "Oppfyller kravet til minsteinntekt eller verneplikt" && it.status == IKKE_OPPFYLT
        }
            ?.let {
                listOf("brev.blokk.begrunnelse-avslag-minsteinntekt")
            } ?: emptyList()
    }

    private fun avslagTaptArbeidstid(): List<String> {
        return vedtak.vilkår.find {
            it.navn == "Tap av arbeidstid er minst terskel" && it.status == IKKE_OPPFYLT
        }
            ?.let {
                listOf("brev.blokk.avslag-tapt-arbeidstid")
            } ?: emptyList()
    }

    private fun avslagReellArbeidssøker(): List<String> {
        val grunnerTilAvslag = mutableListOf("brev.blokk.avslag-reell-arbeidssoker-overskrift")

        vedtak.vilkår.find {
            it.navn == "Oppfyller kravet til heltid- og deltidsarbeid" && it.status == IKKE_OPPFYLT
        }?.let {
            grunnerTilAvslag.add("brev.blokk.avslag-reell-arbeidssoker-heltid-deltid")
        }

        vedtak.vilkår.find {
            it.navn == "Oppfyller kravet til mobilitet" && it.status == IKKE_OPPFYLT
        }?.let {
            grunnerTilAvslag.add("brev.blokk.avslag-reell-arbeidssoker-arbeid-i-hele-norge")
        }

        if (grunnerTilAvslag.size > 1) {
            grunnerTilAvslag.add("brev.blokk.avslag-reell-arbeidssoker-unntak-heltid-deltid-hele-norge")
        }
        vedtak.vilkår.find {
            it.navn == "Oppfyller kravet til å være arbeidsfør" && it.status == IKKE_OPPFYLT
        }?.let {
            grunnerTilAvslag.add("brev.blokk.avslag-reell-arbeidssoker-arbeidsfor")
        }

        vedtak.vilkår.find {
            it.navn == "Oppfyller kravet til å ta ethvert arbeid" && it.status == IKKE_OPPFYLT
        }?.let {
            grunnerTilAvslag.add("brev.blokk.avslag-reell-arbeidssoker-ethvert-arbeid")
        }

        vedtak.vilkår.find {
            it.navn == "Registrert som arbeidssøker på søknadstidspunktet" && it.status == IKKE_OPPFYLT
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
        return this.any { it.navn == "Oppfyller kravet til minsteinntekt eller verneplikt" && it.status == IKKE_OPPFYLT }
    }

    private fun Set<Vilkår>.avslagArbeidstid(): Boolean {
        return this.any { it.navn == "Tap av arbeidstid er minst terskel" && it.status == IKKE_OPPFYLT }
    }

    private fun Set<Vilkår>.reellArbeidssøker(): Boolean {
        return this.any {
            it.status == IKKE_OPPFYLT &&
                (it.navn == "Krav til arbeidssøker" || it.navn == "Registrert som arbeidssøker på søknadstidspunktet")
        }
    }
}

data class Innvilgelse(
    override val vedtak: Vedtak,
    override val mediator: Mediator,
) : Vedtaksmelding(vedtak, mediator) {
    override val harBrevstøtte: Boolean = vedtak.utfall == Utfall.INNVILGET

    init {
        require(this.harBrevstøtte) { "Vedtak oppfyller ikke innvilgelseskriterier" }
    }

    override val brevBlokkIder: List<String>
        get() {
            val pre =
                listOf(
                    "brev.blokk.vedtak-innvilgelse",
                    "brev.blokk.begrunnelse-innvilgelsesdato",
                    "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                    "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                )

            val post =
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

            return pre + barnetillegg() + nittiProsentRegel() + samordnet() + grunnlag() + post
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
        val samordningBlokker = mutableListOf<String>()
        val harSamordnet =
            vedtak.opplysninger.any { it.opplysningTekstId == "opplysning.har-samordnet" && it.verdi == "true" }
        val sykepengerDagsats =
            vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.sykepenger-dagsats" && it.verdi > "0" }
        val pleiepengerDagsats =
            vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.pleiepenger-dagsats" && it.verdi > "0" }
        val omsorgspengerDagsats =
            vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.omsorgspenger-dagsats" && it.verdi > "0" }
        val opplæringspengerDagsats =
            vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.opplaeringspenger-dagsats" && it.verdi > "0" }
        val uføreDagsats =
            vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.ufore-dagsats" && it.verdi > "0" }
        val foreldrepengerDagsats =
            vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.foreldrepenger-dagsats" && it.verdi > "0" }
        val svangerskapspengerDagsats =
            vedtak.opplysninger.find {
                it.opplysningTekstId == "opplysning.svangerskapspenger-dagsats" && it.verdi > "0"
            }

        if (harSamordnet) {
            samordningBlokker.add("brev.blokk.samordning")
            if (sykepengerDagsats != null) {
                samordningBlokker.add("brev.blokk.samordning-sykepenger")
            }
            if (pleiepengerDagsats != null) {
                samordningBlokker.add("brev.blokk.samordning-pleiepenger")
            }
            if (omsorgspengerDagsats != null) {
                samordningBlokker.add("brev.blokk.samordning-omsorgspenger")
            }
            if (opplæringspengerDagsats != null) {
                samordningBlokker.add("brev.blokk.samordning-opplaeringspenger")
            }
            if (uføreDagsats != null) {
                samordningBlokker.add("brev.blokk.samordning-ufore")
            }
            if (foreldrepengerDagsats != null) {
                samordningBlokker.add("brev.blokk.samordning-foreldrepenger")
            }
            if (svangerskapspengerDagsats != null) {
                samordningBlokker.add("brev.blokk.samordning-svangerskapspenger")
            }
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
