package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class VedtaksMeldingTest {
    @Test
    fun `skal lage en vedtaksmelding basert på en behandling`() {
        val boolskOpplysning: Opplysning =
            Opplysning(
                id = "opplysning.krav-paa-dagpenger",
                navn = "Opplysningens navn",
                verdi = "false",
                datatype = "boolean",
            )
        val stringOpplysning: Opplysning =
            Opplysning(
                id = "opplysning.krav-til-minsteinntekt",
                navn = "Opplysningens navn",
                verdi = "false",
                datatype = "boolean",
            )
        val opplysninger = setOf<Opplysning>(boolskOpplysning, stringOpplysning)

        Behandling(
            id = "019145eb-6fbb-769f-b1b1-d2450b383a98",
            tilstand = "Tilstand",
            opplysninger = opplysninger,
        ).let { behandling ->
            VedtaksMelding(behandling).let { vedtaksMelding ->
                vedtaksMelding.blokker() shouldBe listOf(
                    "brev.blokk.vedtak-avslag",
                    "brev.blokk.begrunnelse-avslag-minsteinntekt",
                ) + VedtaksMelding.FASTE_BLOKKER
            }
        }
    }
}
