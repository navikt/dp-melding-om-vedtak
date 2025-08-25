package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.YearMonth

class EnhetTest {
    // NBSP - Non breaking space blir lagt på som default ved norsk formatering av tusenskille i tall
    // F.eks: 10 000 vil se slik ut: 10NBSP000
    // https://en.wikipedia.org/wiki/Non-breaking_space
    private val nonBreakingSpace = "\u00A0"

    @Test
    fun `Heltall med enhet kroner skal formateres riktig`() {
        Enhet.KRONER.formatertVerdi(verdi = 10) shouldBe "10 kroner"

        Enhet.KRONER.formatertVerdi(verdi = 1000) shouldBe "1${nonBreakingSpace}000 kroner"
    }

    @Test
    fun `Heltall uten enhet formateres riktig`() {
        Enhet.HELTALL.formatertVerdi(verdi = 10) shouldBe "10"
        Enhet.HELTALL.formatertVerdi(verdi = 1000) shouldBe "1${nonBreakingSpace}000"
    }

    @Test
    fun `Enhetsløse verdier formateres riktig`() {
        Enhet.ENHETSLØS.formatertVerdi(10.0) shouldBe "10"
        Enhet.ENHETSLØS.formatertVerdi(10) shouldBe "10"

        Enhet.ENHETSLØS.formatertVerdi(LocalDate.of(2025, 1, 1)) shouldBe "1. januar 2025"
        Enhet.ENHETSLØS.formatertVerdi(LocalDate.of(2025, 1, 10)) shouldBe "10. januar 2025"
        Enhet.ENHETSLØS.formatertVerdi(YearMonth.of(2025, 1)) shouldBe "januar 2025"
    }

    @Test
    fun `TIMER verdier formateres riktig`() {
        @Test
        Enhet.TIMER.formatertVerdi(18.75) shouldBe "18,75"
        Enhet.TIMER.formatertVerdi(18.008) shouldBe "18,01"
        Enhet.TIMER.formatertVerdi(18.7) shouldBe "18,7"
        Enhet.TIMER.formatertVerdi(18.0) shouldBe "18"
        // Edge-case som egentlig burde resultere i 18, men vi har valgt å ikke bruke tid på å håndtere det
        Enhet.TIMER.formatertVerdi(18.004) shouldBe "18,00"
    }

    @Test
    fun `UKER verdier formateres riktig`() {
        Enhet.UKER.formatertVerdi(10) shouldBe "10"
    }

    @Test
    fun `BARN verdier formateres riktig`() {
        Enhet.BARN.formatertVerdi(10) shouldBe "10"
    }
}