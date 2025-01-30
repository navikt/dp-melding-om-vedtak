package no.nav.dagpenger.vedtaksmelding.model.vedtak

import mu.KotlinLogging
import java.util.UUID

private val sikkerlogg = KotlinLogging.logger("tjenestekall")

data class Vedtak(
    val behandlingId: UUID,
    val vilkår: Set<Vilkår> = emptySet(),
    val utfall: Utfall,
    val opplysninger: Set<Opplysning> = emptySet(),
) {
    fun finnOpplysning(opplysningTekstId: String): Opplysning? {
        val opplysning = this.opplysninger.singleOrNull { it.opplysningTekstId == opplysningTekstId }
        return opplysning
    }

    fun hentOpplysning(opplysningTekstId: String): Opplysning {
        sikkerlogg.info { "Finn opplysning for behandlingId $behandlingId, opplysning $opplysningTekstId" }
        val opplysning =
            finnOpplysning(opplysningTekstId)
                ?: throw OpplysningIkkeFunnet("Opplysning med tekstId $opplysningTekstId mangler for behandlingId $behandlingId")
        sikkerlogg.info { "Opplysning for behandlingId $behandlingId, $opplysningTekstId: $opplysning" }
        return opplysning
    }

    enum class Utfall {
        INNVILGET,
        AVSLÅTT,
    }

    class OpplysningIkkeFunnet(message: String) : RuntimeException(message)
}
