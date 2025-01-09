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
    abstract val isApplicable: Boolean

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

    suspend fun doStuff() {
        val opplysningstekstIder = mediator.hentOpplysningTekstIder(brevBlokkIder.toList())
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
                    add(kotlin.runCatching { AvslagMinsteInntekt(vedtak, mediator) })
                    add(kotlin.runCatching { Innvilgelse(vedtak, mediator) })
                }
                    .single { it.isSuccess }
                    .getOrThrow()
            } catch (e: Exception) {
                logger.error(e) { "Feil ved oppbygging vedtaksmelding" }
                when (e) {
                    is NoSuchElementException -> throw UkjentVedtakException(
                        "Kunne ikke bygge vedtaksmelding utifra vedtak: $vedtak",
                        e,
                    )

                    is IllegalArgumentException -> throw UkjentVedtakException(
                        "Vedtak er gyldig for flere vedtaksmeldinger. Dette er ikke lovlig: $vedtak",
                        e,
                    )

                    else -> throw e
                }
            }
        }
    }

    class UkjentVedtakException(override val message: String, override val cause: Throwable? = null) :
        RuntimeException(message, cause)
}

data class AvslagMinsteInntekt(
    override val vedtak: Vedtak,
    override val mediator: Mediator,
) : Vedtaksmelding(vedtak, mediator) {
    override val isApplicable: Boolean = vedtak.utfall == Utfall.AVSLÅTT && vedtak.vilkår.avslagMinsteinntekt()
    override val brevBlokkIder: List<String> =
        listOf("brev.blokk.vedtak-avslag", "brev.blokk.begrunnelse-avslag-minsteinntekt")

    init {
        require(this.isApplicable) { "Vedtak oppfyller ikke avslagskriterier" }
    }

    fun Set<Vilkår>.avslagMinsteinntekt(): Boolean {
        return this.any { it.navn == "Oppfyller kravet til minsteinntekt eller verneplikt" && it.status == IKKE_OPPFYLT }
    }
}

data class Innvilgelse(
    override val vedtak: Vedtak,
    override val mediator: Mediator,
) : Vedtaksmelding(vedtak, mediator) {
    override val isApplicable: Boolean = vedtak.utfall == Utfall.INNVILGET

    init {
        require(this.isApplicable) { "Vedtak oppfyller ikke innvilgelseskriterier" }
    }

    override val brevBlokkIder: List<String>
        get() {
            val pre =
                listOf(
                    "brev.blokk.vedtak-innvilgelse",
                    "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                    "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                )

            val post =
                listOf(
                    "brev.blokk.grunnlag",
                    "brev.blokk.arbeidstiden-din",
                    "brev.blokk.egenandel",
                    "brev.blokk.du-maa-sende-meldekort",
                    "brev.blokk.utbetaling",
                    "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                    "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                    "brev.blokk.du-maa-melde-fra-om-endringer",
                    "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
                )

            return pre + barnetillegg() + nittiProsentRegel() + samordnet() + post
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
        return vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.har-samordnet" && it.verdi == "true" }
            ?.let {
                listOf("brev.blokk.samordning")
            } ?: emptyList()
    }

    private fun barnetillegg(): List<String> {
        return vedtak.opplysninger.find {
            it.opplysningTekstId == "opplysning.antall-barn-som-gir-rett-til-barnetillegg" && it.verdi.toInt() > 0
        }
            ?.let {
                listOf("brev.blokk.barnetillegg")
            } ?: emptyList()
    }
}
