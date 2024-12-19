package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.jupiter.api.Test

class AvslagMinsteInntektTest {
    private val avslagMinsteInntektKriter =
        Vilkår(
            navn = "Krav til minsteinntekt",
            status = Vilkår.Status.IKKE_OPPFYLT,
        )

    @Test
    fun `Riktige avslags kriterier`() {
        shouldThrow<IllegalArgumentException> {
            AvslagMinsteInntekt(
                vedtak =
                    Vedtak(
                        vilkår = emptySet(),
                        utfall = Utfall.AVSLÅTT,
                        opplysninger = emptySet(),
                    ),
                mediator = mockk(),
            )
        }

        shouldThrow<IllegalArgumentException> {
            AvslagMinsteInntekt(
                vedtak =
                    Vedtak(
                        vilkår = setOf(avslagMinsteInntektKriter),
                        utfall = Utfall.INNVILGET,
                        opplysninger = emptySet(),
                    ),
                mediator = mockk(),
            )
        }
    }

    @Test
    fun `Rikig brevblokker for avslag på minsteinntekt`() {
        AvslagMinsteInntekt(
            vedtak =
                Vedtak(
                    vilkår = setOf(avslagMinsteInntektKriter),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe
            listOf(
                "brev.blokk.vedtak-avslag",
                "brev.blokk.begrunnelse-avslag-minsteinntekt",
            ) + Vedtaksmelding.fasteBlokker
    }
}
