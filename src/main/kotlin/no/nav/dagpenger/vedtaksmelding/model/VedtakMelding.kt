package no.nav.dagpenger.vedtaksmelding.model

import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagMelding
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseMelding
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk
import no.nav.dagpenger.vedtaksmelding.portabletext.Child

private val logger = KotlinLogging.logger {}

abstract class VedtakMelding(
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
        ): VedtakMelding {
            return try {
                mutableSetOf<Result<VedtakMelding>>().apply {
                    add(kotlin.runCatching { AvslagMelding(vedtak, alleBrevblokker) })
                    add(kotlin.runCatching { InnvilgelseMelding(vedtak, alleBrevblokker) })
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
