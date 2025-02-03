package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.KRONER
import org.junit.jupiter.api.Test

class OpplysningFormateringTest {
    // NBSP - Non breaking space blir lagt på som default ved norsk formatering av tusenskille i tall
    // F.eks: 10 000 vil se slik ut: 10NBSP000
    // https://en.wikipedia.org/wiki/Non-breaking_space
    private val nonBreakingSpace = "\u00A0"

    @Test
    fun `Flyttall med enhet kroner skal formateres riktig`() {
        Opplysning(
            opplysningTekstId = "bubba",
            verdi = "10.5",
            datatype = FLYTTALL,
            enhet = KRONER,
        ).formaterVerdi() shouldBe "10,50"

        Opplysning(
            opplysningTekstId = "bubba",
            verdi = "10.599",
            datatype = FLYTTALL,
            enhet = KRONER,
        ).formaterVerdi() shouldBe "10,60"

        Opplysning(
            opplysningTekstId = "bubba",
            verdi = "1000.0",
            datatype = FLYTTALL,
            enhet = KRONER,
        ).formaterVerdi() shouldBe "1${nonBreakingSpace}000"

        Opplysning(
            opplysningTekstId = "bubba",
            verdi = "10",
            datatype = FLYTTALL,
            enhet = KRONER,
        ).formaterVerdi() shouldBe "10"
    }

    @Test
    fun `Flyttall formateres riktig`() {
        Opplysning(
            opplysningTekstId = "bubba",
            verdi = "10",
            datatype = FLYTTALL,
        ).formaterVerdi() shouldBe "10"

        Opplysning(
            opplysningTekstId = "bubba",
            verdi = "10.0",
            datatype = FLYTTALL,
        ).formaterVerdi() shouldBe "10"

        Opplysning(
            opplysningTekstId = "bubba",
            verdi = "10.5",
            datatype = FLYTTALL,
        ).formaterVerdi() shouldBe "10,5"

        Opplysning(
            opplysningTekstId = "bubba",
            verdi = "1000000.59",
            datatype = FLYTTALL,
        ).formaterVerdi() shouldBe "1${nonBreakingSpace}000${nonBreakingSpace}000,6"

        Opplysning(
            opplysningTekstId = "bubba",
            verdi = "10000.593",
            datatype = FLYTTALL,
        ).formaterVerdi() shouldBe "10${nonBreakingSpace}000,6"
    }
}
