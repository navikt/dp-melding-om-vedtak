package no.nav.dagpenger.vedtaksmelding.model.innvilgelse

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.KRONER
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.UKER
import org.junit.jupiter.api.Test

class VernepliktTest {
    private val resourceRetriever = object {}.javaClass

    private val vedtakVerneplikt =
        resourceRetriever.getResource("/json/vedtak-verneplikt.json")?.let { VedtakMapper(it.readText()).vedtak() }
            ?: throw RuntimeException("Fant ikke ressurs")

    @Test
    fun `skal hente antall G som gis som grunnlag ved bruk av vernepliktregel`() {
        vedtakVerneplikt.finnOpplysning("opplysning.antall-g-som-gis-som-grunnlag-ved-verneplikt") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.antall-g-som-gis-som-grunnlag-ved-verneplikt",
                råVerdi = "3.0",
                datatype = FLYTTALL,
                enhet = KRONER,
            ).also {
                it.formatertVerdi shouldBe "3"
            }
    }

    @Test
    fun `Hent opplysning Antall stønadsuker som gis ved ordinære dagpenger`() {
        vedtakVerneplikt.finnOpplysning("opplysning.antall-stonadsuker-som-gis-ved-ordinare-dagpenger") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.antall-stonadsuker-som-gis-ved-ordinare-dagpenger",
                råVerdi = "0",
                datatype = HELTALL,
                enhet = UKER,
            ).also {
                it.formatertVerdi shouldBe "0"
            }
    }

    @Test
    fun `skal hente opplysning om innvilgelse av verneplikt og riktig antall stonadsuker`() {
        vedtakVerneplikt.finnOpplysning("opplysning.antall-stonadsuker") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.antall-stonadsuker",
                råVerdi = "26",
                datatype = HELTALL,
                enhet = UKER,
            )
        vedtakVerneplikt.finnOpplysning("opplysning.er-innvilget-med-verneplikt") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.er-innvilget-med-verneplikt",
                råVerdi = true.toString(),
                datatype = BOOLSK,
            )
    }
}
