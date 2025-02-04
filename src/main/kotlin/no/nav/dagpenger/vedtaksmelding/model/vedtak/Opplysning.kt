package no.nav.dagpenger.vedtaksmelding.model.vedtak

import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.DATO
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
                        KRONER -> formaterDesimaltall(antallDesimaler = 2, desimaltall = råVerdi.toDouble()) + " kroner"
                        else -> formaterDesimaltall(desimaltall = råVerdi.toDouble())
                    }

                DATO -> formaterDato(råVerdi)
                else -> råVerdi
            }

    private fun formaterDato(dateString: String): String {
        val date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
        val norskFormat = Locale.of("nb", "NO")
        return "${date.dayOfMonth}. " + date.format(DateTimeFormatter.ofPattern("MMMM yyyy", norskFormat))
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
