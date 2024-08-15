package no.nav.dagpenger.vedtaksmelding

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class VedtaksMeldingTest {
    @Test
    fun `skal lage en vedtaksmelding basert på en behandling`() {
        val boolskOpplysning: Opplysning =
            Opplysning(
                id = "opplysning.krav-til-minsteinntekt",
                navn = "Opplysningens navn",
                verdi = "true",
                datatype = "boolean",
            )
        val stringOpplysning: Opplysning =
            Opplysning(
                id = "opplysning.krav-til-minsteinntekt",
                navn = "Opplysningens navn",
                verdi = "true",
                datatype = "boolean",
            )
        val opplysninger = setOf<Opplysning>(boolskOpplysning, stringOpplysning)

        Behandling(opplysninger).let { behandling ->
            VedtaksMelding(behandling).let { vedtaksMelding ->
                vedtaksMelding.blokker() shouldBe setOf("hubba", "bibba")
            }
        }
    }
}
