package no.nav.dagpenger.vedtaksmelding.model

import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.TEKST
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.ENHETSLØS

data class Vedtak(
    val vilkår: Set<Vilkår> = emptySet(),
    val utfall: Utfall,
    val opplysninger: Set<Opplysning> = emptySet(),
) {
    fun finnOpplysning(opplysningTekstId: String): Opplysning? =
        this.opplysninger.singleOrNull { it.opplysningTekstId == opplysningTekstId }

    fun hentOpplysning(opplysningTekstId: String): Opplysning =
        finnOpplysning(opplysningTekstId)
            ?: throw OpplysningIkkeFunnet("Opplysning med tekstId $opplysningTekstId mangler")
}

class OpplysningIkkeFunnet(message: String) : RuntimeException(message)

data class Vilkår(
    val navn: String,
    val status: Status,
) {
    enum class Status {
        OPPFYLT,
        IKKE_OPPFYLT,
    }
}

enum class Utfall {
    INNVILGET,
    AVSLÅTT,
}

data class Opplysning(
    val opplysningTekstId: String,
    val verdi: String,
    val datatype: Datatype,
    val enhet: Enhet = ENHETSLØS,
) {
    companion object {
        val NULL_OPPLYSNING =
            Opplysning(
                opplysningTekstId = "ukjent.opplysning",
                verdi = "ukjent",
                datatype = TEKST,
                enhet = ENHETSLØS,
            )
    }

    enum class Datatype {
        TEKST,
        HELTALL,
        FLYTTALL,
        DATO,
        BOOLSK,
    }

    enum class Enhet {
        KRONER,
        DAGER,
        ENHETSLØS,
        UKER,
        BARN,
        TIMER,
    }
}
