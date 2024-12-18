package no.nav.dagpenger.vedtaksmelding.model

import no.nav.dagpenger.vedtaksmelding.model.Opplysning2.Datatype
import no.nav.dagpenger.vedtaksmelding.model.Opplysning2.Datatype.TEKST
import no.nav.dagpenger.vedtaksmelding.model.Opplysning2.Enhet
import no.nav.dagpenger.vedtaksmelding.model.Opplysning2.Enhet.ENHETSLØS

data class Vedtak(
    val vilkår: Set<Vilkår> = emptySet(),
    val utfall: Utfall,
    val opplysninger: Set<Opplysning2> = emptySet(),
) {
    fun finnOpplysning(opplysningTekstId: String): Opplysning2? =
        this.opplysninger.singleOrNull { it.opplysningTekstId == opplysningTekstId }
}

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

data class Opplysning2(
    val opplysningTekstId: String,
    val verdi: String,
    val datatype: Datatype,
    val enhet: Enhet = ENHETSLØS,
) {
    companion object {
        val NULL_OPPLYSNING =
            Opplysning2(
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
