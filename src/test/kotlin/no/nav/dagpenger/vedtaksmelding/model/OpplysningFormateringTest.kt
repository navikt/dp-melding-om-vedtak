package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.KRONER
import org.junit.jupiter.api.Test

class OpplysningFormateringTest {
    @Test
    fun `Flyttall med enhet kroner skal formateres riktig`() {
        Opplysning(
            opplysningTekstId = "bubba",
            verdi = "10.5",
            datatype = FLYTTALL,
            enhet = KRONER,
        ).formaterVerdi() shouldBe "10.50"

        Opplysning(
            opplysningTekstId = "bubba",
            verdi = "10.599",
            datatype = FLYTTALL,
            enhet = KRONER,
        ).formaterVerdi() shouldBe "10.60"

        Opplysning(
            opplysningTekstId = "bubba",
            verdi = "10.0",
            datatype = FLYTTALL,
            enhet = KRONER,
        ).formaterVerdi() shouldBe "10"

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
        ).formaterVerdi() shouldBe "10.5"

        Opplysning(
            opplysningTekstId = "bubba",
            verdi = "10.59",
            datatype = FLYTTALL,
        ).formaterVerdi() shouldBe "10.6"

        Opplysning(
            opplysningTekstId = "bubba",
            verdi = "10.593",
            datatype = FLYTTALL,
        ).formaterVerdi() shouldBe "10.6"
    }
}
