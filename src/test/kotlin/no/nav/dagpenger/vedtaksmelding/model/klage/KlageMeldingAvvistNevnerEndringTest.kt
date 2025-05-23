package no.nav.dagpenger.vedtaksmelding.model.klage

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_NEVNER_ENDRING_DEL_3
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_SKRIFTLIG_OG_NEVNER_ENDRING_HJEMMEL_DEL_4
import no.nav.dagpenger.vedtaksmelding.util.readFile
import org.junit.jupiter.api.Test

class KlageMeldingAvvistNevnerEndringTest {
    private val klageVedtak =
        KlagevedtakMapper(
            vedtakJson = "/json/klage/klagevedtakAvvistNevnerEndring.json".readFile(),
        ).vedtak()

    @Test
    fun `Rikige brevblokker for klagemelding med avvisning som gjelder at bruker ikke nevner endring`() {
        KlageMelding(
            klagevedtak = klageVedtak,
            alleBrevBlokker = emptyList(),
        ).let { klageMelding ->
            klageMelding.brevBlokkIder() shouldBe
                listOf(
                    KLAGE_AVVIST_DEL_1.brevblokkId,
                    KLAGE_AVVIST_NEVNER_ENDRING_DEL_3.brevblokkId,
                    KLAGE_AVVIST_SKRIFTLIG_OG_NEVNER_ENDRING_HJEMMEL_DEL_4.brevblokkId,
                ) + KlageMelding.fasteAvsluttendeBlokkerForVedtak
        }
    }
}
