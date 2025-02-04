package no.nav.dagpenger.vedtaksmelding.model.vedtak

import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.DATO
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.HELTALL
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
                        KRONER -> "${formaterTall(antallDesimaler = 2, desimaltall = råVerdi.toDouble())} kroner"
                        else -> formaterTall(antallDesimaler = 1, desimaltall = råVerdi.toDouble())
                    }
                HELTALL ->
                    when (enhet) {
                        KRONER -> "${formaterTall(desimaltall = råVerdi.toDouble())} kroner"
                        else -> formaterTall(desimaltall = råVerdi.toDouble())
                    }

                DATO -> formaterDato(råVerdi)
                TEKST ->
                    when (opplysningTekstId) {
                        "opplysning.brukt-beregningsregel-grunnlag" -> råVerdi.lowercase()
                        else -> råVerdi
                    }
                else -> råVerdi
            }

    private fun formaterTall(
        desimaltall: Double,
        antallDesimaler: Int = 0,
    ): String {
        val norskFormat = Locale.of("nb", "NO")
        return when {
            erHeltall(desimaltall) -> String.format(norskFormat, format = "%,.0f", desimaltall)
            else -> String.format(norskFormat, format = "%,.${antallDesimaler}f", desimaltall)
        }
    }

    private fun erHeltall(desimaltall: Double) = desimaltall % 1 == 0.0

    private fun formaterDato(dateString: String): String {
        val date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
        val norskFormat = Locale.of("nb", "NO")
        return "${date.dayOfMonth}. " + date.format(DateTimeFormatter.ofPattern("MMMM yyyy", norskFormat))
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
