package no.nav.dagpenger.vedtaksmelding.model.klage

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_INTRO
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_HJEMMEL
import no.nav.dagpenger.vedtaksmelding.util.readFile
import org.junit.jupiter.api.Test

class KlageMeldingAvvistRettsligKlageinteresseTest {
    private val klageVedtak =
        KlagevedtakMapper(
            vedtakJson = "/json/klage/klagevedtakAvvistRettsligKlageinteresse.json".readFile(),
        ).vedtak()

    @Test
    fun `Rikige brevblokker for klagemelding med avvisning som gjelder at bruker ikke har rettslig klageinteresse`() {
        KlageMelding(
            klagevedtak = klageVedtak,
            alleBrevBlokker = emptyList(),
        ).let { klageMelding ->
            klageMelding.brevBlokkIder() shouldBe
                listOf(
                    KLAGE_AVVIST_INTRO.brevblokkId,
                    KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE.brevblokkId,
                    KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_HJEMMEL.brevblokkId,
                ) + KlageMelding.fasteAvsluttendeBlokker
        }
    }
}
