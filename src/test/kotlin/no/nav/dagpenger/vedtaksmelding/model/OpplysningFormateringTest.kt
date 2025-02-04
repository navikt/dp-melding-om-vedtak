package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.DATO
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.KRONER
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
