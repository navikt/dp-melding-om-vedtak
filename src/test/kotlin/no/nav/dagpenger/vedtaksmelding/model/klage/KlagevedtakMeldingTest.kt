package no.nav.dagpenger.vedtaksmelding.model.klage

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.util.readFile
import org.junit.jupiter.api.Test

class KlagevedtakMeldingTest {
    private val klageVedtak =
        KlagevedtakMapper(
            vedtakJson = "/json/klage/klagevedtak.json".readFile(),
        ).vedtak()

    @Test
    fun `Rikig brevblokker  for klagemelding `() {
        KlagevedtakMelding(
            klagevedtak = klageVedtak,
            alleBrevBlokker = emptyList(),
        ).let { klageMelding ->
            klageMelding.brevBlokkIder() shouldBe listOf(KlageBrevBlokker.KLAGE_OVERSENDT_KA.brevblokkId)
        }
    }
}
