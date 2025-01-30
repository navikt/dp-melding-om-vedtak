package no.nav.dagpenger.vedtaksmelding.model.vedtak

import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.DATO
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.KRONER
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Opplysning(
    val opplysningTekstId: String,
    val verdi: String,
    val datatype: Datatype,
    val enhet: Enhet = Enhet.ENHETSLØS,
) {
    companion object {
        val NULL_OPPLYSNING =
            Opplysning(
                opplysningTekstId = "ukjent.opplysning",
                verdi = "ukjent",
                datatype = Datatype.TEKST,
                enhet = Enhet.ENHETSLØS,
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
        val outputFormatter = DateTimeFormatter.ofPattern("dd. MMMM yyyy", Locale.of("no"))
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
