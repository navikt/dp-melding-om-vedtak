package no.nav.dagpenger.vedtaksmelding.model.vedtak

import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.TEKST
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.ENHETSLØS
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.KRONER
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Opplysning(
    val opplysningTekstId: String,
    private val råVerdi: String,
    val datatype: Datatype,
    val enhet: Enhet = ENHETSLØS,
) {
    companion object {
        val NULL_OPPLYSNING =
            Opplysning(
                opplysningTekstId = "ukjent.opplysning",
                råVerdi = "ukjent",
                datatype = TEKST,
                enhet = ENHETSLØS,
            )
    }

    fun råVerdi(): String = råVerdi

    val formatertVerdi: String
        get() =
            when (datatype) {
                FLYTTALL ->
                    when (enhet) {
                        KRONER -> formaterDesimaltall(antallDesimaler = 2, desimaltall = råVerdi.toDouble())
                        else -> formaterDesimaltall(desimaltall = råVerdi.toDouble())
                    }
                else -> råVerdi
            }

    private fun formaterDesimaltall(
        desimaltall: Double,
        antallDesimaler: Int = 1,
    ): String {
        val norskFormat = Locale.of("nb", "NO")
        return when {
            erHeltall(desimaltall) -> String.format(norskFormat, format = "%,.0f", desimaltall)
            else -> String.format(norskFormat, format = "%,.${antallDesimaler}f", desimaltall)
        }
    }

    private fun erHeltall(desimaltall: Double) = desimaltall % 1 == 0.0

    fun formatering(): String {
        return when (this.datatype) {
            Datatype.DATO -> formatDate(this.råVerdi)
            else -> this.råVerdi
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
        val outputFormatter = DateTimeFormatter.ofPattern("dd. MMMM yyyy", Locale.of("nb", "NO"))
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
