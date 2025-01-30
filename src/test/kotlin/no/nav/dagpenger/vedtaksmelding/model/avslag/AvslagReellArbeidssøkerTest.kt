package no.nav.dagpenger.vedtaksmelding.model.avslag

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.Avslag
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER_ARBEIDSFØR
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER_ETHVERT_ARBEID
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER_HELTID_DELTID
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER_MOBILITET
import no.nav.dagpenger.vedtaksmelding.model.AvslagVilkårMedBrevstøtte.REELL_ARBEIDSSØKER_REGISTRERT_SOM_ARBEIDSSØKER
import no.nav.dagpenger.vedtaksmelding.model.Utfall
import no.nav.dagpenger.vedtaksmelding.model.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.model.Vilkår
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class AvslagReellArbeidssøkerTest {
    private val behandlingId = UUIDv7.ny()

    private val reellArbeidssøkerIkkeOppfylt =
        Vilkår(
            navn = REELL_ARBEIDSSØKER.navn,
            status = Vilkår.Status.IKKE_OPPFYLT,
        )
    private val heltidDeltidIkkeOppfylt =
        Vilkår(
            navn = REELL_ARBEIDSSØKER_HELTID_DELTID.navn,
            status = Vilkår.Status.IKKE_OPPFYLT,
        )

    private val mobilitetIkkeOppfylt =
        Vilkår(
            navn = REELL_ARBEIDSSØKER_MOBILITET.navn,
            status = Vilkår.Status.IKKE_OPPFYLT,
        )

    private val arbeidsførIkkeOppfylt =
        Vilkår(
            navn = REELL_ARBEIDSSØKER_ARBEIDSFØR.navn,
            status = Vilkår.Status.IKKE_OPPFYLT,
        )

    private val ethvertArbeidIkkeOppfylt =
        Vilkår(
            navn = REELL_ARBEIDSSØKER_ETHVERT_ARBEID.navn,
            status = Vilkår.Status.IKKE_OPPFYLT,
        )

    private val registrertArbeidssøkerIkkeOppfylt =
        Vilkår(
            navn = REELL_ARBEIDSSØKER_REGISTRERT_SOM_ARBEIDSSØKER.navn,
            status = Vilkår.Status.IKKE_OPPFYLT,
        )

    @Test
    fun `Sjekker kriterier for brevstøtte`() {
        shouldNotThrow<Vedtaksmelding.ManglerBrevstøtte> {
            Avslag(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        vilkår = setOf(reellArbeidssøkerIkkeOppfylt),
                        utfall = Utfall.AVSLÅTT,
                        opplysninger = emptySet(),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldThrow<Vedtaksmelding.ManglerBrevstøtte> {
            Avslag(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        vilkår = emptySet(),
                        utfall = Utfall.AVSLÅTT,
                        opplysninger = emptySet(),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
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
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                "brev.blokk.vedtak-avslag",
                "brev.blokk.avslag-reell-arbeidssoker-overskrift",
                "brev.blokk.avslag-reell-arbeidssoker-heltid-deltid",
                "brev.blokk.avslag-reell-arbeidssoker-unntak-heltid-deltid-hele-norge",
                "brev.blokk.avslag-reell-arbeidssoker-hjemmel",
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
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                "brev.blokk.vedtak-avslag",
                "brev.blokk.avslag-reell-arbeidssoker-overskrift",
                "brev.blokk.avslag-reell-arbeidssoker-arbeid-i-hele-norge",
                "brev.blokk.avslag-reell-arbeidssoker-unntak-heltid-deltid-hele-norge",
                "brev.blokk.avslag-reell-arbeidssoker-hjemmel",
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
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                "brev.blokk.vedtak-avslag",
                "brev.blokk.avslag-reell-arbeidssoker-overskrift",
                "brev.blokk.avslag-reell-arbeidssoker-arbeidsfor",
                "brev.blokk.avslag-reell-arbeidssoker-hjemmel",
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
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                "brev.blokk.vedtak-avslag",
                "brev.blokk.avslag-reell-arbeidssoker-overskrift",
                "brev.blokk.avslag-reell-arbeidssoker-ethvert-arbeid",
                "brev.blokk.avslag-reell-arbeidssoker-hjemmel",
            ) + Vedtaksmelding.fasteBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - registrert arbeidssøker`() {
        Avslag(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(reellArbeidssøkerIkkeOppfylt, registrertArbeidssøkerIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                "brev.blokk.vedtak-avslag",
                "brev.blokk.avslag-reell-arbeidssoker-overskrift",
                "brev.blokk.avslag-reell-arbeidssoker-registrert-arbeidssoker",
                "brev.blokk.avslag-reell-arbeidssoker-hjemmel",
            ) + Vedtaksmelding.fasteBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - alle delvilkår ikke oppfylt`() {
        Avslag(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår =
                        setOf(
                            reellArbeidssøkerIkkeOppfylt, heltidDeltidIkkeOppfylt, mobilitetIkkeOppfylt,
                            arbeidsførIkkeOppfylt, ethvertArbeidIkkeOppfylt, registrertArbeidssøkerIkkeOppfylt,
                        ),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                "brev.blokk.vedtak-avslag",
                "brev.blokk.avslag-reell-arbeidssoker-overskrift",
                "brev.blokk.avslag-reell-arbeidssoker-heltid-deltid",
                "brev.blokk.avslag-reell-arbeidssoker-arbeid-i-hele-norge",
                "brev.blokk.avslag-reell-arbeidssoker-unntak-heltid-deltid-hele-norge",
                "brev.blokk.avslag-reell-arbeidssoker-arbeidsfor",
                "brev.blokk.avslag-reell-arbeidssoker-ethvert-arbeid",
                "brev.blokk.avslag-reell-arbeidssoker-registrert-arbeidssoker",
                "brev.blokk.avslag-reell-arbeidssoker-hjemmel",
            ) + Vedtaksmelding.fasteBlokker
    }
}
