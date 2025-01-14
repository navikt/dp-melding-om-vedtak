package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.jupiter.api.Test

class AvslagTest {
    private val minsteInntektIkkeOppfylt =
        Vilkår(
            navn = "Oppfyller kravet til minsteinntekt eller verneplikt",
            status = Vilkår.Status.IKKE_OPPFYLT,
        )

    private val reellArbeidssøkerIkkeOppfylt =
        Vilkår(
            navn = "Krav til arbeidssøker",
            status = Vilkår.Status.IKKE_OPPFYLT,
        )
    private val heltidDeltidIkkeOppfylt =
        Vilkår(
            navn = "Oppfyller kravet til heltid- og deltidsarbeid",
            status = Vilkår.Status.IKKE_OPPFYLT,
        )

    private val mobilitetIkkeOppfylt =
        Vilkår(
            navn = "Oppfyller kravet til mobilitet",
            status = Vilkår.Status.IKKE_OPPFYLT,
        )

    @Test
    fun `Rikige brevblokker for avslag på minsteinntekt`() {
        Avslag(
            vedtak =
                Vedtak(
                    vilkår = setOf(minsteInntektIkkeOppfylt),
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

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - vilje til både heltid og deltid`() {
        Avslag(
            vedtak =
                Vedtak(
                    vilkår = setOf(reellArbeidssøkerIkkeOppfylt, heltidDeltidIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe
            listOf(
                "brev.blokk.vedtak-avslag",
                "brev.blokk.avslag-reell-arbeidssoker-heltid-deltid",
            ) + Vedtaksmelding.fasteBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - vilje til arbeid i hele Norge`() {
        Avslag(
            vedtak =
                Vedtak(
                    vilkår = setOf(reellArbeidssøkerIkkeOppfylt, mobilitetIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe
            listOf(
                "brev.blokk.vedtak-avslag",
                "brev.blokk.avslag-reell-arbeidssoker-arbeid-i-hele-norge",
            ) + Vedtaksmelding.fasteBlokker
    }

    @Test
    fun `Manglende kriterier for brevstøtte`() {
        shouldThrow<IllegalArgumentException> {
            Avslag(
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
            Avslag(
                vedtak =
                    Vedtak(
                        vilkår = setOf(minsteInntektIkkeOppfylt),
                        utfall = Utfall.INNVILGET,
                        opplysninger = emptySet(),
                    ),
                mediator = mockk(),
            )
        }
    }
}

/*
*
    {
      "navn": "Oppfyller kravet til mobilitet",
      "status": "IkkeOppfylt",
      "vurderingstidspunkt": "2025-01-14T08:47:45.360187",
      "hjemmel": "folketrygdloven § 4-5"
    },
    {
      "navn": "Oppfyller kravet til å være arbeidsfør",
      "status": "Oppfylt",
      "vurderingstidspunkt": "2025-01-14T08:47:45.3602",
      "hjemmel": "folketrygdloven § 4-5"
    },
    {
      "navn": "Oppfyller kravet til å ta ethvert arbeid",
      "status": "IkkeOppfylt",
      "vurderingstidspunkt": "2025-01-14T08:47:45.360213",
      "hjemmel": "folketrygdloven § 4-5"
    },
    {
      "navn": "Krav til arbeidssøker",
      "status": "IkkeOppfylt",
      "vurderingstidspunkt": "2025-01-14T08:47:45.367496",
      "hjemmel": "folketrygdloven § 4-5"
    },
* */
