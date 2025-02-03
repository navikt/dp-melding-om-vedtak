package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.saksbehandling.api.models.BehandlerDTO
import no.nav.dagpenger.saksbehandling.api.models.BehandlerEnhetDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.vedtaksmelding.Configuration
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagMelding
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseMelding
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.portabletext.HtmlConverter
import no.nav.dagpenger.vedtaksmelding.sanity.SanityKlient
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime

class VedtakHtmlTest {
    fun hentVedtak(navn: String): Vedtak {
        val resourseRetriever = object {}.javaClass
        return resourseRetriever.getResource(navn)?.let { VedtakMapper(it.readText()).vedtak() }
            ?: throw RuntimeException("Fant ikke ressurs")
    }

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
    val sanityKlient = SanityKlient(Configuration.sanityApiUrl)

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
                                brevblokkId = "brev.blokk.begrunnelse-avslag-minsteinntekt",
                                tekst = "noe saksbehandler har skrevet",
                                sistEndretTidspunkt = LocalDateTime.now(),
                                tittel = "Dette er en tittel",
                            ),
                        ),
                )

            htmlInnhold brevblokkRekkefølgeShouldBe
                listOf(
                    "brev.blokk.vedtak-avslag",
                    "brev.blokk.begrunnelse-avslag-minsteinntekt",
                    "brev.blokk.sporsmaal",
                    "brev.blokk.rett-til-innsyn",
                    "brev.blokk.rett-til-aa-klage",
                )

            htmlInnhold finnUtvidetBeskrivelseTekst "brev.blokk.begrunnelse-avslag-minsteinntekt" shouldBe "noe saksbehandler har skrevet"

            writeStringToFile(
                filePath = "build/temp/avslag-minsteinntekt.html",
                content = htmlInnhold,
            )
        }
    }

    private infix fun String.finnUtvidetBeskrivelseTekst(utvidetBeskrivelseId: String): String? {
        return Jsoup.parse(this).select("[data-utvidet-beskrivelse-id]").singleOrNull {
            it.attr("data-utvidet-beskrivelse-id") == utvidetBeskrivelseId
        }?.text()
    }

    private infix fun String.brevblokkRekkefølgeShouldBe(expectedOrder: List<String>) {
        val document: Document = Jsoup.parse(this)
        val elements: List<Element> = document.select("[data-brevblokk-id]")

        val actualOrder = elements.map { it.attr("data-brevblokk-id") }
        actualOrder shouldBe expectedOrder
    }

    @Test
    fun `Html av innvilgelse `() {
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
                    "brev.blokk.vedtak-innvilgelse",
                    "brev.blokk.begrunnelse-innvilgelsesdato",
                    "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                    "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                    "brev.blokk.barnetillegg",
                    "brev.blokk.grunnlag",
                    "brev.blokk.arbeidstiden-din",
                    "brev.blokk.egenandel",
                    "brev.blokk.du-maa-sende-meldekort",
                    "brev.blokk.utbetaling",
                    "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                    "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                    "brev.blokk.du-maa-melde-fra-om-endringer",
                    "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
                    "brev.blokk.sporsmaal",
                    "brev.blokk.rett-til-innsyn",
                    "brev.blokk.rett-til-aa-klage",
                )
            writeStringToFile(
                filePath = "build/temp/innvilgelse.html",
                content =
                htmlInnhold,
            )
        }
    }

    fun writeStringToFile(
        filePath: String,
        content: String,
    ) {
        val path = Paths.get(filePath)
        Files.createDirectories(path.parent)
        Files.writeString(path, content)
    }
}
