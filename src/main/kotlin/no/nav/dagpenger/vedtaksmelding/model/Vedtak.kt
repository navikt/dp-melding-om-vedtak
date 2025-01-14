package no.nav.dagpenger.vedtaksmelding.model
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.DATO
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.TEKST
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.ENHETSLØS
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.KRONER
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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

    fun formatering(): String {
        return when (this.datatype) {
            DATO -> formatDate(this.verdi)
            else -> this.verdi
        }
    }

    fun enhet(): String {
        return when (this.enhet) {
            KRONER -> " kroner"
            else -> ""
        }
    }

    fun verdiMedEnhet(): String {
        return "${this.formatering()}${this.enhet()}"
    }

    fun formatDate(dateString: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("dd. MMMM yyyy", Locale("no"))
        val date = LocalDate.parse(dateString, inputFormatter)
        return date.format(outputFormatter)
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
