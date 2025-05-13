package no.nav.dagpenger.vedtaksmelding.model.vedtak

import java.util.UUID

data class KlageVedtak(
    val behandlingId: UUID,
    val opplysninger: Set<Opplysning> = emptySet(),
    val fagsakId: String,
) {
    fun finnOpplysning(opplysningTekstId: String) = this.opplysninger.singleOrNull { it.opplysningTekstId == opplysningTekstId }

    fun hentOpplysning(opplysningTekstId: String): Opplysning {
        val opplysning =
            finnOpplysning(opplysningTekstId)
                ?: throw OpplysningIkkeFunnet("Opplysning med tekstId $opplysningTekstId mangler for behandlingId $behandlingId")

        return opplysning
    }

    class OpplysningIkkeFunnet(message: String) : RuntimeException(message)
}
