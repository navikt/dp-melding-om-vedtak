package no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_STREIK_LOCKOUT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_STREIK_LOCKOUT_DEL_2
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class AvslagMeldingStreikLockoutTest {
    @Test
    fun `Riktige brevblokker for avslag streik eller lockout`() {
        val behandlingId = UUIDv7.ny()
        val avslagStreikEllerLockoutIkkeOppfylt = DagpengerOpplysning.IkkeStreikEllerLockout(false)
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    opplysninger = setOf(avslagStreikEllerLockoutIkkeOppfylt),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_STREIK_LOCKOUT_DEL_1.brevblokkId,
                AVSLAG_STREIK_LOCKOUT_DEL_2.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }
}
