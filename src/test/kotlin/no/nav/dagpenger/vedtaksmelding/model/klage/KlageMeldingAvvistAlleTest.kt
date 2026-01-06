package no.nav.dagpenger.vedtaksmelding.model.klage
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_INTRO
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_KLAGEFIRST
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_KLAGEFIRST_HJEMMEL
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_NEVNER_ENDRING
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_HJEMMEL
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_SKRIFTLIG_OG_NEVNER_ENDRING_HJEMMEL
import no.nav.dagpenger.vedtaksmelding.model.klage.KlageBrevBlokker.KLAGE_AVVIST_SKRIFTLIG_OG_SIGNERT
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
                    KLAGE_AVVIST_INTRO.brevblokkId,
                    KLAGE_AVVIST_SKRIFTLIG_OG_SIGNERT.brevblokkId,
                    KLAGE_AVVIST_NEVNER_ENDRING.brevblokkId,
                    KLAGE_AVVIST_SKRIFTLIG_OG_NEVNER_ENDRING_HJEMMEL.brevblokkId,
                    KLAGE_AVVIST_KLAGEFIRST.brevblokkId,
                    KLAGE_AVVIST_KLAGEFIRST_HJEMMEL.brevblokkId,
                    KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE.brevblokkId,
                    KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_HJEMMEL.brevblokkId,
                ) + KlageMelding.fasteAvsluttendeBlokkerVedtak
        }
    }
}
