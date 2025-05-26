package no.nav.dagpenger.vedtaksmelding.model.klage

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_INTRO
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_SKRIFTLIG_OG_NEVNER_ENDRING_HJEMMEL
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_SKRIFTLIG_OG_SIGNERT
import no.nav.dagpenger.vedtaksmelding.util.readFile
import org.junit.jupiter.api.Test

class KlageMeldingAvvistSkriftligTest {
    private val klageVedtak =
        KlagevedtakMapper(
            vedtakJson = "/json/klage/klagevedtakAvvistSkriftlig.json".readFile(),
        ).vedtak()

    @Test
    fun `Rikige brevblokker for klagemelding med avvisning som gjelder at klagen ikke er skriftlig`() {
        KlageMelding(
            klagevedtak = klageVedtak,
            alleBrevBlokker = emptyList(),
        ).let { klageMelding ->
            klageMelding.brevBlokkIder() shouldBe
                listOf(
                    KLAGE_AVVIST_INTRO.brevblokkId,
                    KLAGE_AVVIST_SKRIFTLIG_OG_SIGNERT.brevblokkId,
                    KLAGE_AVVIST_SKRIFTLIG_OG_NEVNER_ENDRING_HJEMMEL.brevblokkId,
                ) + KlageMelding.fasteAvsluttendeBlokker
        }
    }
}
