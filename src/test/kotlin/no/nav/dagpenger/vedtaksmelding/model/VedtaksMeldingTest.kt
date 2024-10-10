package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.vedtaksmelding.Mediator
import org.junit.jupiter.api.Test

class VedtaksMeldingTest {
    private fun lagOpplysning(
        id: String,
        verdi: String,
    ): Opplysning {
        return Opplysning(
            opplysningTekstId = id,
            navn = "test",
            verdi = verdi,
            datatype = "boolean",
            opplysningId = "test",
        )
    }

    @Test
    fun `Rikig vedtaksmelding for innvilgelse `() {
        val minsteinntekt: Opplysning =
            lagOpplysning(
                id = "opplysning.krav-til-minsteinntekt",
                verdi = "true",
            )
        val opplysninger = setOf(minsteinntekt)

        val mediator =
            mockk<Mediator>().also {
                coEvery { it.hentOpplysningTekstIder(VedtaksMelding.FASTE_BLOKKER.toList()) } returns emptyList()
            }
        runBlocking {
            Behandling(
                id = "019145eb-6fbb-769f-b1b1-d2450b383a98",
                tilstand = "Tilstand",
                opplysninger = opplysninger,
            ).let { behandling ->
                VedtaksMelding(behandling, mediator).let { vedtaksMelding ->
                    vedtaksMelding.hentBrevBlokkIder() shouldBe VedtaksMelding.FASTE_BLOKKER
                    vedtaksMelding.hentOpplysninger() shouldBe emptyList()
                }
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
                mockk(relaxed = true),
            ).hentBrevBlokkIder()
        }
    }

    @Test
    fun `Rikig vedtaksmelding for avslag på minsteinntekt`() {
        val minsteinntekt: Opplysning =
            lagOpplysning(
                id = "opplysning.krav-til-minsteinntekt",
                verdi = "false",
            )
        val opplysninger = setOf(minsteinntekt)
        val forventedeBrevblokkIder =
            listOf(
                "brev.blokk.vedtak-avslag",
                "brev.blokk.begrunnelse-avslag-minsteinntekt",
            ) + VedtaksMelding.FASTE_BLOKKER

        val mediator =
            mockk<Mediator>().also {
                coEvery { it.hentOpplysningTekstIder(forventedeBrevblokkIder) } returns
                    listOf(
                        minsteinntekt.opplysningTekstId,
                    )
            }
        runBlocking {
            Behandling(
                id = "019145eb-6fbb-769f-b1b1-d2450b383a98",
                tilstand = "Tilstand",
                opplysninger = opplysninger,
            ).let { behandling ->
                VedtaksMelding(behandling, mediator).let { vedtaksMelding ->
                    vedtaksMelding.hentBrevBlokkIder() shouldBe forventedeBrevblokkIder
                    vedtaksMelding.hentOpplysninger() shouldBe listOf(minsteinntekt)
                }
            }
        }
    }
}
