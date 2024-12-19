package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class OpplysningOldTest {
    @Test
    fun `skal mappe opplysning navn til id`() {
        OpplysningOld(
            navn = "Krav til minsteinntekt",
            verdi = "true",
            datatype = "boolean",
            opplysningId = "test",
        ).opplysningTekstId shouldBe "opplysning.krav-til-minsteinntekt"

        OpplysningOld(
            navn = "ukjent",
            verdi = "true",
            datatype = "boolean",
            opplysningId = "test",
        ).opplysningTekstId shouldBe "ukjent.opplysning.ukjent"
    }
}
