package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.util.readFile
import org.junit.jupiter.api.Test

class KlagevedtakMeldingTest {
    private val klageVedtak =
        KlagevedtakMapper(
            vedtakJson = "/json/klage/klagevedtak.json".readFile(),
        ).vedtak()

    @Test
    fun `Rikig brevblokker rekkefølge for klagemelding `() {
        KlagevedtakMelding(
            klagevedtak = klageVedtak,
            alleBrevBlokker = emptyList(),
        ).brevBlokkIder() shouldBe KlagevedtakMelding.fasteAvsluttendeBlokker
    }
}
