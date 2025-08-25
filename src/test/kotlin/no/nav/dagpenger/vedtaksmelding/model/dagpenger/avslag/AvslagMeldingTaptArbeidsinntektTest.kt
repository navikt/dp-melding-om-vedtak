package no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_2
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class AvslagMeldingTaptArbeidsinntektTest {
    @Test
    fun `Riktige brevblokker for avslag tapt arbeidsinntekt`() {
        val behandlingId = UUIDv7.ny()
        val taptArbeidsinntektIkkeOppfylt = DagpengerOpplysning.KravTilTapAvArbeidsinntekt(false)
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
//                    vilkår = setOf(taptArbeidsinntektIkkeOppfylt),
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    opplysninger = setOf(taptArbeidsinntektIkkeOppfylt),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_2.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }
}
