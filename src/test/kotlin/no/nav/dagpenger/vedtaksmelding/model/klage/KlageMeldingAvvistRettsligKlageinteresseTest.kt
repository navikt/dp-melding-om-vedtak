package no.nav.dagpenger.vedtaksmelding.model.klage

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_DEL_5
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_HJEMMEL_DEL_6
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
                    KLAGE_AVVIST_DEL_1.brevblokkId,
                    KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_DEL_5.brevblokkId,
                    KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_HJEMMEL_DEL_6.brevblokkId,
                ) + KlageMelding.fasteAvsluttendeBlokkerForVedtak
        }
    }
}
