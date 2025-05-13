package no.nav.dagpenger.vedtaksmelding.model

import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding.FasteBrevblokker
import no.nav.dagpenger.vedtaksmelding.model.vedtak.KlageVedtak
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk

class KlagevedtakMelding(
    private val klagevedtak: KlageVedtak,
    val brevBlokker: List<BrevBlokk>,
) : Brev {
    override fun brevBlokkIder(): List<String> {
        TODO("Not yet implemented")
    }

    override fun hentBrevBlokker(): List<BrevBlokk> {
        return brevBlokker
    }

    override fun hentOpplysninger(): List<Opplysning> {
        TODO("Not yet implemented")
    }

    fun hentFagsakId(): String {
        return klagevedtak.fagsakId
    }

    companion object {
        val fasteAvsluttendeBlokker =
            listOf(
                FasteBrevblokker.RETT_TIL_INNSYN.brevBlokkId,
                FasteBrevblokker.PERSONOPPLYSNINGER.brevBlokkId,
                FasteBrevblokker.HJELP_FRA_ANDRE.brevBlokkId,
                FasteBrevblokker.VEILEDNING_FRA_NAV.brevBlokkId,
                FasteBrevblokker.RETT_TIL_Å_KLAGE.brevBlokkId,
                FasteBrevblokker.SPØRSMÅL.brevBlokkId,
            )
    }

    class UkjentVedtakException(override val message: String, override val cause: Throwable? = null) :
        RuntimeException(message, cause)

    class ManglerBrevstøtte(override val message: String) : RuntimeException(message)
}
