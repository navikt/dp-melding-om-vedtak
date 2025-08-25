package no.nav.dagpenger.vedtaksmelding.model

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

sealed class Enhet {
    val norskFormat = Locale.of("nb", "NO")

    fun formatertVerdi(verdi: Any): String {
        return when (verdi) {
            is String -> formatertVerdi(verdi)
            is Double -> formatertVerdi(verdi)
            is Int -> formatertVerdi(verdi)
            is Boolean -> formatertVerdi(verdi)
            is Number -> formatertVerdi(verdi)
            is LocalDate -> formatertVerdi(verdi)
            is YearMonth -> formatertVerdi(verdi)
            else -> throw IllegalArgumentException(
                "Kan ikke formatere verdi av type ${verdi::class.simpleName} med enhet ${this::class.simpleName}",
            )
        }
    }

    protected open fun formatertVerdi(verdi: String): String = verdi

    protected open fun formatertVerdi(verdi: Double): String = formatertVerdi(verdi.toString())

    protected open fun formatertVerdi(verdi: Int): String = formatertVerdi(verdi.toString())

    protected open fun formatertVerdi(verdi: Boolean): String = formatertVerdi(verdi.toString())

    protected open fun formatertVerdi(verdi: Number): String = formatertVerdi(verdi.toString())

    protected open fun formatertVerdi(verdi: LocalDate): String = formatertVerdi(verdi.toString())

    protected open fun formatertVerdi(verdi: YearMonth): String = formatertVerdi(verdi.toString())

    protected fun formaterTall(
        verdi: Number,
        antallDesimaler: Int = 0,
    ): String {
        val desimaltall = verdi.toDouble()
        return when {
            erHeltall(desimaltall) -> String.format(norskFormat, format = "%,.0f", desimaltall)
            else -> String.format(norskFormat, format = "%,.${antallDesimaler}f", desimaltall)
        }
    }

    protected fun formaterDato(verdi: LocalDate): String {
        return "${verdi.dayOfMonth}. " + verdi.format(DateTimeFormatter.ofPattern("MMMM yyyy", norskFormat))
    }

    protected fun erHeltall(desimaltall: Double) = desimaltall % 1 == 0.0

    data object ENHETSLØS : Enhet() {
        override fun formatertVerdi(verdi: Double): String = formaterTall(verdi)

        override fun formatertVerdi(verdi: LocalDate): String = formaterDato(verdi)

        override fun formatertVerdi(verdi: YearMonth): String {
            return verdi.format(DateTimeFormatter.ofPattern("MMMM yyyy", norskFormat))
        }
    }

    data object KRONER : Enhet() {
        private fun formaterKroner(verdi: Number): String = "${formaterTall(verdi)} kroner"

        override fun formatertVerdi(verdi: Int): String = formaterKroner(verdi)

        override fun formatertVerdi(verdi: Number): String = formaterKroner(verdi)
    }

    data object HELTALL : Enhet() {
        override fun formatertVerdi(verdi: Int): String = formaterTall(verdi)
    }

    data object UKER : Enhet()

    data object TIMER : Enhet() {
        private fun har1Desimal(desimaltall: Double): Boolean {
            val scaled = desimaltall * 10
            return scaled % 1 == 0.0 && desimaltall % 1 != 0.0
        }

        private fun formaterTimer(desimaltall: Double): String {
            return when {
                erHeltall(desimaltall) -> String.format(norskFormat, format = "%,.0f", desimaltall)
                har1Desimal(desimaltall) -> String.format(norskFormat, format = "%,.1f", desimaltall)
                else -> String.format(norskFormat, format = "%,.2f", desimaltall)
            }
        }

        override fun formatertVerdi(verdi: Double): String = formaterTimer(verdi)
    }

    data object BARN : Enhet()
}
