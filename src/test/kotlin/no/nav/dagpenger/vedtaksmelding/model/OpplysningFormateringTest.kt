package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.DATO
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.KRONER
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.TIMER
import org.junit.jupiter.api.Test

class OpplysningFormateringTest {
    // NBSP - Non breaking space blir lagt på som default ved norsk formatering av tusenskille i tall
    // F.eks: 10 000 vil se slik ut: 10NBSP000
    // https://en.wikipedia.org/wiki/Non-breaking_space
    private val nonBreakingSpace = "\u00A0"

    @Test
    fun `Dato formateres riktig`() {
        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "2025-01-01",
            datatype = DATO,
        ).formatertVerdi shouldBe "1. januar 2025"

        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "2025-01-10",
            datatype = DATO,
        ).formatertVerdi shouldBe "10. januar 2025"
    }

    @Test
    fun `Formatering av timer`() {
        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "18.75",
            datatype = FLYTTALL,
            enhet = TIMER,
        ).formatertVerdi shouldBe "18,75"
        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "18.008",
            datatype = FLYTTALL,
            enhet = TIMER,
        ).formatertVerdi shouldBe "18,01"
        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "18.7",
            datatype = FLYTTALL,
            enhet = TIMER,
        ).formatertVerdi shouldBe "18,7"
        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "18.0",
            datatype = FLYTTALL,
            enhet = TIMER,
        ).formatertVerdi shouldBe "18"
        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "18",
            datatype = FLYTTALL,
            enhet = TIMER,
        ).formatertVerdi shouldBe "18"
        // Edge-case som egentlig burde resultere i 18, men vi har valgt å ikke bruke tid på å håndtere det
        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "18.004",
            datatype = FLYTTALL,
            enhet = TIMER,
        ).formatertVerdi shouldBe "18,00"
    }

    @Test
    fun `Heltall med enhet kroner skal formateres riktig`() {
        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "10",
            datatype = HELTALL,
            enhet = KRONER,
        ).formatertVerdi shouldBe "10 kroner"

        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "1000",
            datatype = HELTALL,
            enhet = KRONER,
        ).formatertVerdi shouldBe "1${nonBreakingSpace}000 kroner"
    }

    @Test
    fun `Flyttall med enhet kroner skal formateres riktig`() {
        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "10.5",
            datatype = FLYTTALL,
            enhet = KRONER,
        ).formatertVerdi shouldBe "10,50 kroner"

        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "10.599",
            datatype = FLYTTALL,
            enhet = KRONER,
        ).formatertVerdi shouldBe "10,60 kroner"

        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "1000.0",
            datatype = FLYTTALL,
            enhet = KRONER,
        ).formatertVerdi shouldBe "1${nonBreakingSpace}000 kroner"

        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "10",
            datatype = FLYTTALL,
            enhet = KRONER,
        ).formatertVerdi shouldBe "10 kroner"
    }

    @Test
    fun `Flyttall formateres riktig`() {
        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "10",
            datatype = FLYTTALL,
        ).formatertVerdi shouldBe "10"

        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "10.0",
            datatype = FLYTTALL,
        ).formatertVerdi shouldBe "10"

        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "10.5",
            datatype = FLYTTALL,
        ).formatertVerdi shouldBe "10,5"

        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "1000000.59",
            datatype = FLYTTALL,
        ).formatertVerdi shouldBe "1${nonBreakingSpace}000${nonBreakingSpace}000,6"

        Opplysning(
            opplysningTekstId = "bubba",
            råVerdi = "10000.593",
            datatype = FLYTTALL,
        ).formatertVerdi shouldBe "10${nonBreakingSpace}000,6"
    }
}
