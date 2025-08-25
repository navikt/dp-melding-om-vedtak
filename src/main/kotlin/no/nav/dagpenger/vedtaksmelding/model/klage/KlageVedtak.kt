package no.nav.dagpenger.vedtaksmelding.model.klage

import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.OpplysningIkkeFunnet
import java.util.UUID

inline fun <reified T : KlageOpplysning<*, *>> KlageVedtak.any(predicate: (T) -> Boolean): Boolean {
    return this.opplysninger.filterIsInstance<T>().any { predicate(it) }
}

inline fun <reified T : KlageOpplysning<*, Boolean>> KlageVedtak.oppfylt(): Boolean {
    return this.opplysninger.filterIsInstance<T>().any { it.verdi }
}

inline fun <reified T : KlageOpplysning<*, Boolean>> KlageVedtak.ikkeOppfylt(): Boolean {
    return this.opplysninger.filterIsInstance<T>().any { !it.verdi }
}

inline fun <reified T : KlageOpplysning<*, *>> KlageVedtak.filterIsInstance(): List<T> {
    return this.opplysninger.filterIsInstance<T>()
}

data class KlageVedtak(
    val behandlingId: UUID,
    val fagsakId: String,
    val opplysninger: Set<KlageOpplysning<*, *>>,
) {
    fun finnOpplysning(opplysningTekstId: String) = this.opplysninger.singleOrNull { it.opplysningTekstId == opplysningTekstId }

    fun hentOpplysning(opplysningTekstId: String): Opplysning {
        val opplysning =
            finnOpplysning(opplysningTekstId)
                ?: throw OpplysningIkkeFunnet("Opplysning med tekstId $opplysningTekstId mangler for behandlingId $behandlingId")

        return opplysning
    }
}
