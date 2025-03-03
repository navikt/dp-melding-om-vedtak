package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.saksbehandling.api.models.BehandlerDTO
import no.nav.dagpenger.saksbehandling.api.models.BehandlerEnhetDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.vedtaksmelding.lagHttpKlient
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding.FasteBrevblokker.HJELP_FRA_ANDRE
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding.FasteBrevblokker.PERSONOPPLYSNINGER
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding.FasteBrevblokker.RETT_TIL_INNSYN
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding.FasteBrevblokker.RETT_TIL_Å_KLAGE
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding.FasteBrevblokker.SPØRSMÅL
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding.FasteBrevblokker.VEILEDNING_FRA_NAV
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_MINSTEINNTEKT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_MINSTEINNTEKT_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagMelding
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ARBEIDSTIDEN_DIN
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_BARNETILLEGG
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_EGENANDEL
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_UTBETALING
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseMelding
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.portabletext.HtmlConverter
import no.nav.dagpenger.vedtaksmelding.sanity.SanityKlient
import no.nav.dagpenger.vedtaksmelding.util.finnUtvidetBeskrivelseTekst
import no.nav.dagpenger.vedtaksmelding.util.readFile
import no.nav.dagpenger.vedtaksmelding.util.writeStringToFile
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class VedtakHtmlTest {
    private val sanityKlient =
        SanityKlient(
            "http://localhost:3000",
            lagHttpKlient(
                engine =
                    MockEngine {
                        respond(
                            content = "/json/sanity.json".readFile(),
                            headers = headersOf("Content-Type" to listOf("application/json")),
                        )
                    },
            ),
        )

    private fun hentVedtak(navn: String): Vedtak = navn.readFile().let { VedtakMapper(it).vedtak() }

    private val meldingOmVedtakData =
        MeldingOmVedtakDataDTO(
            fornavn = "Test ForNavn",
            etternavn = "Test EtterNavn",
            fodselsnummer = "12345678901",
            saksbehandler =
                BehandlerDTO(
                    fornavn = "Ola",
                    etternavn = "Nordmann",
                    enhet =
                        BehandlerEnhetDTO(
                            navn = "Enhet Navn",
                            postadresse = "Postadresse 123",
                        ),
                ),
            beslutter =
                BehandlerDTO(
                    fornavn = "Kari",
                    etternavn = "Nordmann",
                    enhet =
                        BehandlerEnhetDTO(
                            navn = "Enhet Navn",
                            postadresse = "Postadresse 123",
                        ),
                ),
        )

    @Test
    fun `Html av avslag minsteinntekt`() {
        runBlocking {
            val alleBrevblokker = sanityKlient.hentBrevBlokker()
            requireNotNull(alleBrevblokker) { "alleBrevblokker should not be null" }
            val avslagMelding =
                AvslagMelding(
                    vedtak = hentVedtak("/json/avslag.json"),
                    alleBrevblokker = alleBrevblokker,
                )

            avslagMelding.hentOpplysninger()
            val brevBlokker = avslagMelding.hentBrevBlokker()
            val htmlInnhold =
                HtmlConverter.toHtml(
                    brevBlokker = brevBlokker,
                    opplysninger = avslagMelding.hentOpplysninger(),
                    meldingOmVedtakData = meldingOmVedtakData,
                    fagsakId = "fagsakId test",
                    utvidetBeskrivelse =
                        setOf(
                            UtvidetBeskrivelse(
                                behandlingId = UUIDv7.ny(),
                                brevblokkId = AVSLAG_MINSTEINNTEKT_DEL_1.brevblokkId,
                                tekst = "noe saksbehandler har skrevet",
                                sistEndretTidspunkt = LocalDateTime.now(),
                                tittel = "Dette er en tittel",
                            ),
                        ),
                )

            htmlInnhold brevblokkRekkefølgeShouldBe
                listOf(
                    AVSLAG_INNLEDNING.brevblokkId,
                    AVSLAG_MINSTEINNTEKT_DEL_1.brevblokkId,
                    AVSLAG_MINSTEINNTEKT_DEL_2.brevblokkId,
                    RETT_TIL_INNSYN.brevBlokkId,
                    PERSONOPPLYSNINGER.brevBlokkId,
                    HJELP_FRA_ANDRE.brevBlokkId,
                    VEILEDNING_FRA_NAV.brevBlokkId,
                    RETT_TIL_Å_KLAGE.brevBlokkId,
                    SPØRSMÅL.brevBlokkId,
                )

            htmlInnhold finnUtvidetBeskrivelseTekst AVSLAG_MINSTEINNTEKT_DEL_1.brevblokkId shouldBe "noe saksbehandler har skrevet"

            writeStringToFile(
                filePath = "build/temp/avslag-minsteinntekt.html",
                content = htmlInnhold,
            )
        }
    }

    private infix fun String.brevblokkRekkefølgeShouldBe(expectedOrder: List<String>) {
        val document: Document = Jsoup.parse(this)
        val elements: List<Element> = document.select("[data-brevblokk-id]")

        val actualOrder = elements.map { it.attr("data-brevblokk-id") }
        actualOrder shouldBe expectedOrder
    }

    @Test
    fun `Html av innvilgelse`() {
        runBlocking {
            val innvilgelseMelding =
                InnvilgelseMelding(
                    vedtak = hentVedtak("/json/innvilgelsesVedtak.json"),
                    alleBrevblokker = sanityKlient.hentBrevBlokker(),
                )
            innvilgelseMelding.hentOpplysninger()
            val brevBlokker = innvilgelseMelding.hentBrevBlokker()
            val htmlInnhold =
                HtmlConverter.toHtml(
                    brevBlokker = brevBlokker,
                    opplysninger = innvilgelseMelding.hentOpplysninger(),
                    meldingOmVedtakData = meldingOmVedtakData,
                    fagsakId = "fagsakId test",
                )

            htmlInnhold brevblokkRekkefølgeShouldBe
                listOf(
                    INNVILGELSE_INNLEDNING.brevblokkId,
                    INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                    INNVILGELSE_DAGPENGEPERIODE.brevblokkId,
                    INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                    INNVILGELSE_BARNETILLEGG.brevblokkId,
                    INNVILGELSE_GRUNNLAG.brevblokkId,
                    INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId,
                    INNVILGELSE_EGENANDEL.brevblokkId,
                    INNVILGELSE_MELDEKORT.brevblokkId,
                    INNVILGELSE_UTBETALING.brevblokkId,
                    INNVILGELSE_SKATTEKORT.brevblokkId,
                    INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                    INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                    INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
                    RETT_TIL_INNSYN.brevBlokkId,
                    PERSONOPPLYSNINGER.brevBlokkId,
                    HJELP_FRA_ANDRE.brevBlokkId,
                    VEILEDNING_FRA_NAV.brevBlokkId,
                    RETT_TIL_Å_KLAGE.brevBlokkId,
                    SPØRSMÅL.brevBlokkId,
                )
            writeStringToFile(
                filePath = "build/temp/innvilgelse.html",
                content =
                htmlInnhold,
            )
        }
    }
}
