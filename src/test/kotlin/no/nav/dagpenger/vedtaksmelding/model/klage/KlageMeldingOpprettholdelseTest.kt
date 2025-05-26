package no.nav.dagpenger.vedtaksmelding.model.klage

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_OPPRETTHOLDELSE_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_OPPRETTHOLDELSE_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageMelding.Companion.fasteAvsluttendeBlokker
import no.nav.dagpenger.vedtaksmelding.util.readFile
import org.junit.jupiter.api.Test

class KlageMeldingOpprettholdelseTest {
    private val klageVedtak =
        KlagevedtakMapper(
            vedtakJson = "/json/klage/klagevedtakOpprettholdelse.json".readFile(),
        ).vedtak()

    @Test
    fun `Rikige brevblokker for klagemelding med opprettholdelse av vedtak`() {
        KlageMelding(
            klagevedtak = klageVedtak,
            alleBrevBlokker = emptyList(),
        ).let { klageMelding ->
            klageMelding.brevBlokkIder() shouldBe
                listOf(
                    KLAGE_OPPRETTHOLDELSE_DEL_1.brevblokkId,
                    KLAGE_OPPRETTHOLDELSE_DEL_2.brevblokkId,
                ) + fasteAvsluttendeBlokker
        }
    }
}
