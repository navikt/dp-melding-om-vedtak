package no.nav.dagpenger.vedtaksmelding.model.klage
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_NEVNER_ENDRING_DEL_3
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_DEL_5
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_HJEMMEL_DEL_6
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_SKRIFTLIG_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_SKRIFTLIG_OG_NEVNER_ENDRING_HJEMMEL_DEL_4
import no.nav.dagpenger.vedtaksmelding.util.readFile
import org.junit.jupiter.api.Test

class KlageMeldingAvvistAlleTest {
    private val klageVedtak =
        KlagevedtakMapper(
            vedtakJson = "/json/klage/klagevedtakAvvistAlle.json".readFile(),
        ).vedtak()

    @Test
    fun `Rikige brevblokker for klagemelding med avvisning pga alle vilkår er ikke oppfylt`() {
        KlageMelding(
            klagevedtak = klageVedtak,
            alleBrevBlokker = emptyList(),
        ).let { klageMelding ->
            klageMelding.brevBlokkIder() shouldBe
                listOf(
                    KLAGE_AVVIST_DEL_1.brevblokkId,
                    KLAGE_AVVIST_SKRIFTLIG_DEL_2.brevblokkId,
                    KLAGE_AVVIST_NEVNER_ENDRING_DEL_3.brevblokkId,
                    KLAGE_AVVIST_SKRIFTLIG_OG_NEVNER_ENDRING_HJEMMEL_DEL_4.brevblokkId,
                    KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_DEL_5.brevblokkId,
                    KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_HJEMMEL_DEL_6.brevblokkId,
                ) + KlageMelding.fasteAvsluttendeBlokkerForVedtak
        }
    }
}
