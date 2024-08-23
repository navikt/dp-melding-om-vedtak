package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class OpplysningTest {
    @Test
    fun `skal mappe opplysning navn til id`() {
        Opplysning(
            navn = "Krav på dagpenger",
            verdi = "true",
            datatype = "boolean",
            opplysningId = "test",
        ).id shouldBe "opplysning.krav-paa-dagpenger"

        Opplysning(
            navn = "Krav til minsteinntekt",
            verdi = "true",
            datatype = "boolean",
            opplysningId = "test",
        ).id shouldBe "opplysning.krav-til-minsteinntekt"

        Opplysning(
            navn = "ukjent",
            verdi = "true",
            datatype = "boolean",
            opplysningId = "test",
        ).id shouldBe "ukjent.opplysning.ukjent"
    }
}
