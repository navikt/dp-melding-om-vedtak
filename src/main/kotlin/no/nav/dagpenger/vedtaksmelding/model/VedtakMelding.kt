package no.nav.dagpenger.vedtaksmelding.model

import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding.FasteBrevblokker.HJELP_FRA_ANDRE
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding.FasteBrevblokker.PERSONOPPLYSNINGER
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding.FasteBrevblokker.RETT_TIL_INNSYN
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding.FasteBrevblokker.RETT_TIL_Å_KLAGE
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding.FasteBrevblokker.SPØRSMÅL
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding.FasteBrevblokker.VEILEDNING_FRA_NAV
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagMelding
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseMelding
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk
import no.nav.dagpenger.vedtaksmelding.portabletext.Child

private val logger = KotlinLogging.logger {}

interface Brev {
    fun brevBlokkIder(): List<String>

    fun hentBrevBlokker(): List<BrevBlokk>

    fun hentOpplysninger(): List<Opplysning>

    fun hentFagsakId(): String
}

abstract class VedtakMelding(
    protected open val vedtak: Vedtak,
) : Brev {
    abstract val harBrevstøtte: Boolean

    protected abstract val brevBlokkIder: List<String>
    protected abstract val brevBlokker: List<BrevBlokk>

    override fun brevBlokkIder(): List<String> {
        return brevBlokkIder + fasteAvsluttendeBlokker
    }

    override fun hentBrevBlokker(): List<BrevBlokk> {
        return brevBlokker
    }

    override fun hentOpplysninger(): List<Opplysning> {
        return brevBlokker.asSequence()
            .filter { it.textId in brevBlokkIder() }
            .flatMap { it.innhold }
            .flatMap { it.children }
            .filterIsInstance<Child.OpplysningReference>()
            .map { it.behandlingOpplysning.textId }
            .map { vedtak.hentOpplysning(it) }
            .toList()
    }

    override fun hentFagsakId(): String {
        return vedtak.fagsakId
    }

    companion object {
        val fasteAvsluttendeBlokker =
            listOf(
                RETT_TIL_INNSYN.brevBlokkId,
                PERSONOPPLYSNINGER.brevBlokkId,
                HJELP_FRA_ANDRE.brevBlokkId,
                VEILEDNING_FRA_NAV.brevBlokkId,
                RETT_TIL_Å_KLAGE.brevBlokkId,
                SPØRSMÅL.brevBlokkId,
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

    enum class FasteBrevblokker(val brevBlokkId: String) {
        RETT_TIL_INNSYN("brev.blokk.rett-til-innsyn"),
        RETT_TIL_Å_KLAGE("brev.blokk.rett-til-aa-klage"),
        PERSONOPPLYSNINGER("brev.blokk.personopplysninger"),
        HJELP_FRA_ANDRE("brev.blokk.hjelp-fra-andre"),
        VEILEDNING_FRA_NAV("brev.blokk.veiledning-fra-nav"),
        SPØRSMÅL("brev.blokk.sporsmaal"),
    }

    class UkjentVedtakException(override val message: String, override val cause: Throwable? = null) :
        RuntimeException(message, cause)

    class ManglerBrevstøtte(override val message: String) : RuntimeException(message)
}
