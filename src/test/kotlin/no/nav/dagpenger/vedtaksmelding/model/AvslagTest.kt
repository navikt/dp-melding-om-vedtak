package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class AvslagTest {
    private val behandlingId = UUIDv7.ny()
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

    private val arbeidsførIkkeOppfylt =
        Vilkår(
            navn = "Oppfyller kravet til å være arbeidsfør",
            status = Vilkår.Status.IKKE_OPPFYLT,
        )

    private val ethvertArbeidIkkeOppfylt =
        Vilkår(
            navn = "Oppfyller kravet til å ta ethvert arbeid",
            status = Vilkår.Status.IKKE_OPPFYLT,
        )

    @Test
    fun `Rikige brevblokker for avslag på minsteinntekt`() {
        Avslag(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
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
    fun `Riktige brevblokker for avslag reell arbeidssøker - vilje til å jobbe både heltid og deltid`() {
        Avslag(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(reellArbeidssøkerIkkeOppfylt, heltidDeltidIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe
            listOf(
                "brev.blokk.vedtak-avslag",
                "brev.blokk.avslag-reell-arbeidssoker-heltid-deltid",
                "brev.blokk.avslag-reell-arbeidssoker-4-5-c",
            ) + Vedtaksmelding.fasteBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - vilje til arbeid i hele Norge`() {
        Avslag(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(reellArbeidssøkerIkkeOppfylt, mobilitetIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe
            listOf(
                "brev.blokk.vedtak-avslag",
                "brev.blokk.avslag-reell-arbeidssoker-arbeid-i-hele-norge",
                "brev.blokk.avslag-reell-arbeidssoker-4-5-b",
            ) + Vedtaksmelding.fasteBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - arbeidsfør`() {
        Avslag(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(reellArbeidssøkerIkkeOppfylt, arbeidsførIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe
            listOf(
                "brev.blokk.vedtak-avslag",
//                "brev.blokk.avslag-reell-arbeidssoker-arbeidsfor",
            ) + Vedtaksmelding.fasteBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - vilje til å ta ethvert arbeid`() {
        Avslag(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(reellArbeidssøkerIkkeOppfylt, ethvertArbeidIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe
            listOf(
                "brev.blokk.vedtak-avslag",
//                "brev.blokk.avslag-reell-arbeidssoker-ethvert-arbeid",
            ) + Vedtaksmelding.fasteBlokker
    }

    @Test
    fun `Manglende kriterier for brevstøtte`() {
        shouldThrow<IllegalArgumentException> {
            Avslag(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
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
                        behandlingId = behandlingId,
                        vilkår = setOf(minsteInntektIkkeOppfylt),
                        utfall = Utfall.INNVILGET,
                        opplysninger = emptySet(),
                    ),
                mediator = mockk(),
            )
        }
    }
}
