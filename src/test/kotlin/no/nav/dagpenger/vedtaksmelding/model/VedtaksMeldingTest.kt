package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class VedtaksMeldingTest {
    private fun lagOpplysning(
        id: String,
        verdi: String,
    ): Opplysning {
        return Opplysning(
            id = id,
            navn = "test",
            verdi = verdi,
            datatype = "boolean",
            opplysningId = "test",
        )
    }

    @Test
    fun `Rikig vedtaksmelding for innvilgelse `() {
        val kravPåDagpenger: Opplysning =
            lagOpplysning(
                id = "opplysning.krav-paa-dagpenger",
                verdi = "true",
            )

        val minsteinntekt: Opplysning =
            lagOpplysning(
                id = "opplysning.krav-til-minsteinntekt",
                verdi = "true",
            )
        val opplysninger = setOf(kravPåDagpenger, minsteinntekt)

        Behandling(
            id = "019145eb-6fbb-769f-b1b1-d2450b383a98",
            tilstand = "Tilstand",
            opplysninger = opplysninger,
        ).let { behandling ->
            VedtaksMelding(behandling).let { vedtaksMelding ->
                vedtaksMelding.blokker() shouldBe VedtaksMelding.FASTE_BLOKKER
            }
        }
    }

    @Test
    fun `Skal kaste exception når vi ikke finner krav på dagpenger`() {
        shouldThrow<UgyldigVedtakException> {
            VedtaksMelding(
                Behandling(
                    id = "x",
                    tilstand = "y",
                    opplysninger = setOf(),
                ),
            ).blokker()
        }
    }

    @Test
    fun `Rikig vedtaksmelding for avslag på minsteinntekt`() {
        val kravPåDagpenger: Opplysning =
            lagOpplysning(
                id = "opplysning.krav-paa-dagpenger",
                verdi = "false",
            )

        val minsteinntekt: Opplysning =
            lagOpplysning(
                id = "opplysning.krav-til-minsteinntekt",
                verdi = "false",
            )
        val opplysninger = setOf(kravPåDagpenger, minsteinntekt)

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
