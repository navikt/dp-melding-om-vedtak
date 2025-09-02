package no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_UTDANNING
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class AvslagMeldingUtdanningTest {
    @Test
    fun `Riktige brevblokker for avslag på grunn av utdanning`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    opplysninger = setOf(DagpengerOpplysning.KravTilUtdanning(false)),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_UTDANNING.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }
}
