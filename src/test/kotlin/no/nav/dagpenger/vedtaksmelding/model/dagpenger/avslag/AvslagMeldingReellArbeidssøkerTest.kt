package no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vilkår
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.MINSTEINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.REELL_ARBEIDSSØKER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.REELL_ARBEIDSSØKER_ARBEIDSFØR
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.REELL_ARBEIDSSØKER_ETHVERT_ARBEID
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.REELL_ARBEIDSSØKER_HELTID_DELTID
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.REELL_ARBEIDSSØKER_MOBILITET
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.REELL_ARBEIDSSØKER_REGISTRERT_SOM_ARBEIDSSØKER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_MINSTEINNTEKT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_MINSTEINNTEKT_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_REGISTRERT_ARBEIDSSOKER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class AvslagMeldingReellArbeidssøkerTest {
    private val behandlingId = UUIDv7.ny()

    private val reellArbeidssøkerIkkeOppfylt =
        Vilkår(
            navn = REELL_ARBEIDSSØKER.vilkårNavn,
            status = Vilkår.Status.IKKE_OPPFYLT,
        )
    private val heltidDeltidIkkeOppfylt =
        Vilkår(
            navn = REELL_ARBEIDSSØKER_HELTID_DELTID.vilkårNavn,
            status = Vilkår.Status.IKKE_OPPFYLT,
        )

    private val mobilitetIkkeOppfylt =
        Vilkår(
            navn = REELL_ARBEIDSSØKER_MOBILITET.vilkårNavn,
            status = Vilkår.Status.IKKE_OPPFYLT,
        )

    private val arbeidsførIkkeOppfylt =
        Vilkår(
            navn = REELL_ARBEIDSSØKER_ARBEIDSFØR.vilkårNavn,
            status = Vilkår.Status.IKKE_OPPFYLT,
        )

    private val ethvertArbeidIkkeOppfylt =
        Vilkår(
            navn = REELL_ARBEIDSSØKER_ETHVERT_ARBEID.vilkårNavn,
            status = Vilkår.Status.IKKE_OPPFYLT,
        )

    private val registrertArbeidssøkerIkkeOppfylt =
        Vilkår(
            navn = REELL_ARBEIDSSØKER_REGISTRERT_SOM_ARBEIDSSØKER.vilkårNavn,
            status = Vilkår.Status.IKKE_OPPFYLT,
        )

    @Test
    fun `Sjekker kriterier for brevstøtte`() {
        shouldNotThrow<VedtakMelding.ManglerBrevstøtte> {
            AvslagMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        vilkår = setOf(reellArbeidssøkerIkkeOppfylt),
                        utfall = Utfall.AVSLÅTT,
                        opplysninger = emptySet(),
                        fagsakId = "fagsakId test",
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldThrow<VedtakMelding.ManglerBrevstøtte> {
            AvslagMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        vilkår = emptySet(),
                        utfall = Utfall.AVSLÅTT,
                        opplysninger = emptySet(),
                        fagsakId = "fagsakId test",
                    ),
                alleBrevblokker = emptyList(),
            )
        }
    }

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - vilje til å jobbe både heltid og deltid`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(reellArbeidssøkerIkkeOppfylt, heltidDeltidIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Bugfix - Skal ikke legge til unntaksblokk når ikke blokk om heltid deltid eller hele norge inngår i brevblokklista`() {
        val minsteinntektIkkeOppfylt =
            Vilkår(
                navn = MINSTEINNTEKT.vilkårNavn,
                status = Vilkår.Status.IKKE_OPPFYLT,
            )
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(minsteinntektIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_MINSTEINNTEKT_DEL_1.brevblokkId,
                AVSLAG_MINSTEINNTEKT_DEL_2.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - vilje til arbeid i hele Norge`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(reellArbeidssøkerIkkeOppfylt, mobilitetIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - arbeidsfør`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(reellArbeidssøkerIkkeOppfylt, arbeidsførIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - vilje til å ta ethvert arbeid`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(reellArbeidssøkerIkkeOppfylt, ethvertArbeidIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - registrert arbeidssøker`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = setOf(registrertArbeidssøkerIkkeOppfylt),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_REGISTRERT_ARBEIDSSOKER.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - alle delvilkår ikke oppfylt`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår =
                        setOf(
                            reellArbeidssøkerIkkeOppfylt,
                            heltidDeltidIkkeOppfylt,
                            mobilitetIkkeOppfylt,
                            arbeidsførIkkeOppfylt,
                            ethvertArbeidIkkeOppfylt,
                            registrertArbeidssøkerIkkeOppfylt,
                        ),
                    utfall = Utfall.AVSLÅTT,
                    opplysninger = emptySet(),
                    fagsakId = "fagsakId test",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_REGISTRERT_ARBEIDSSOKER.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Manglende kriterier for brevstøtte`() {
        shouldThrow<VedtakMelding.ManglerBrevstøtte> {
            AvslagMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        vilkår = emptySet(),
                        utfall = Utfall.AVSLÅTT,
                        opplysninger = emptySet(),
                        fagsakId = "fagsakId test",
                    ),
                alleBrevblokker = emptyList(),
            )
        }
    }
}
