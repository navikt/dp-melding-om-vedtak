package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.OpplysningIkkeFunnet
import java.util.UUID

inline fun <reified T : DagpengerOpplysning<*, *>> Vedtak.finnOpplysning(): T? {
    return this.opplysninger.filterIsInstance<T>().singleOrNull()
}

inline fun <reified T : DagpengerOpplysning<*, Number>> Vedtak.finnOpplysning(predicate: (Number) -> Boolean): T? {
    return this.opplysninger.filterIsInstance<T>().singleOrNull { predicate(it.verdi) }
}

inline fun <reified T : DagpengerOpplysning<*, *>> Vedtak.hentOpplysning(): T {
    return this.finnOpplysning<T>() ?: throw OpplysningIkkeFunnet("Opplysning av type ${T::class} mangler")
}

inline fun <reified T : DagpengerOpplysning<*, Boolean>> Vedtak.ikkeOppfylt(): Boolean {
    return this.finnOpplysning<T>()?.let { !it.verdi } ?: false
}

inline fun <reified T : DagpengerOpplysning<*, Boolean>> Vedtak.oppfylt(): Boolean {
    return this.finnOpplysning<T>()?.verdi ?: false
}

private val sikkerlogg = KotlinLogging.logger("tjenestekall")

data class Vedtak(
    val behandlingId: UUID,
    val utfall: Utfall,
    val opplysninger: Set<DagpengerOpplysning<*, *>>,
) {
    fun finnOpplysning(opplysningTekstId: String) = this.opplysninger.singleOrNull { it.opplysningTekstId == opplysningTekstId }

    fun hentOpplysning(opplysningTekstId: String): Opplysning {
        sikkerlogg.debug { "Finn opplysning for behandlingId $behandlingId, opplysning $opplysningTekstId" }
        val opplysning =
            finnOpplysning(opplysningTekstId)
                ?: throw OpplysningIkkeFunnet("Opplysning med tekstId $opplysningTekstId mangler for behandlingId $behandlingId")
        sikkerlogg.debug { "Opplysning for behandlingId $behandlingId, $opplysningTekstId: $opplysning" }
        return opplysning
    }

    enum class Utfall {
        AVSLÅTT,
        INNVILGET,
    }
}
