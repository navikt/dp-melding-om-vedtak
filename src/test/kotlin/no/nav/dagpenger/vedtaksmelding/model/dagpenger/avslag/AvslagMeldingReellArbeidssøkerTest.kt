package no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Opprinnelse
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Periode
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding
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

    private val reellArbeidssøkerIkkeOppfylt = DagpengerOpplysning.KravTilArbeidssøker(false, listOf(Periode(false, Opprinnelse.NY)))
    private val heltidDeltidIkkeOppfylt = DagpengerOpplysning.OppfyllerKravTilArbeidssøker(false, listOf(Periode(false, Opprinnelse.NY)))
    private val mobilitetIkkeOppfylt = DagpengerOpplysning.OppfyllerKravTilMobilitet(false, listOf(Periode(false, Opprinnelse.NY)))
    private val arbeidsførIkkeOppfylt = DagpengerOpplysning.OppfyllerKravTilArbeidsfør(false, listOf(Periode(false, Opprinnelse.NY)))
    private val ethvertArbeidIkkeOppfylt =
        DagpengerOpplysning.OppfyllerKravetTilEthvertArbeid(
            false,
            listOf(Periode(false, Opprinnelse.NY)),
        )
    private val registrertArbeidssøkerIkkeOppfylt =
        DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(
            false,
            listOf(Periode(false, Opprinnelse.NY)),
        )

    @Test
    fun `Sjekker kriterier for brevstøtte`() {
        shouldNotThrow<Vedtaksmelding.ManglerBrevstøtte> {
            AvslagMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.AVSLÅTT,
                        automatiskBehandling = false,
                        opplysninger = setOf(reellArbeidssøkerIkkeOppfylt),
                        behandletHendelseType = "SØKNAD",
                    ),
                alleBrevblokker = emptyList(),
            )
        }
        shouldThrow<Vedtaksmelding.ManglerBrevstøtte> {
            AvslagMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.AVSLÅTT,
                        automatiskBehandling = false,
                        opplysninger = emptySet(),
                        behandletHendelseType = "SØKNAD",
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
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    automatiskBehandling = false,
                    opplysninger = setOf(reellArbeidssøkerIkkeOppfylt, heltidDeltidIkkeOppfylt),
                    behandletHendelseType = "SØKNAD",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Bugfix - Skal ikke legge til unntaksblokk når ikke blokk om heltid deltid eller hele norge inngår i brevblokklista`() {
        val minsteinntektIkkeOppfylt = DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(false, listOf(Periode(false, Opprinnelse.NY)))
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    automatiskBehandling = false,
                    opplysninger = setOf(minsteinntektIkkeOppfylt),
                    behandletHendelseType = "SØKNAD",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_MINSTEINNTEKT_DEL_1.brevblokkId,
                AVSLAG_MINSTEINNTEKT_DEL_2.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - vilje til arbeid i hele Norge`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    automatiskBehandling = false,
                    opplysninger = setOf(reellArbeidssøkerIkkeOppfylt, mobilitetIkkeOppfylt),
                    behandletHendelseType = "SØKNAD",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - arbeidsfør`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    automatiskBehandling = false,
                    opplysninger = setOf(reellArbeidssøkerIkkeOppfylt, arbeidsførIkkeOppfylt),
                    behandletHendelseType = "SØKNAD",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - vilje til å ta ethvert arbeid`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    automatiskBehandling = false,
                    opplysninger = setOf(reellArbeidssøkerIkkeOppfylt, ethvertArbeidIkkeOppfylt),
                    behandletHendelseType = "SØKNAD",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - registrert arbeidssøker`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    automatiskBehandling = false,
                    opplysninger = setOf(registrertArbeidssøkerIkkeOppfylt),
                    behandletHendelseType = "SØKNAD",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_REGISTRERT_ARBEIDSSOKER.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag reell arbeidssøker - alle delvilkår ikke oppfylt`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    automatiskBehandling = false,
                    opplysninger =
                        setOf(
                            reellArbeidssøkerIkkeOppfylt,
                            heltidDeltidIkkeOppfylt,
                            mobilitetIkkeOppfylt,
                            arbeidsførIkkeOppfylt,
                            ethvertArbeidIkkeOppfylt,
                            registrertArbeidssøkerIkkeOppfylt,
                        ),
                    behandletHendelseType = "SØKNAD",
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
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }
}
