package no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_ALDER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class AvslagMeldingAlderTest {
    @Test
    fun `Riktige brevblokker for avslag alder`() {
        val behandlingId = UUIDv7.ny()
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vedtakType = Vedtak.VedtakType.AVSLAG_DAGPENGER,
                    opplysninger = setOf(DagpengerOpplysning.KravTilAlder(false)),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_ALDER.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }
}
