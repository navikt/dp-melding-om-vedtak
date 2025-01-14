package no.nav.dagpenger.vedtaksmelding.model

import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.saksbehandling.api.models.BehandlerDTO
import no.nav.dagpenger.saksbehandling.api.models.BehandlerEnhetDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.vedtaksmelding.Configuration
import no.nav.dagpenger.vedtaksmelding.Mediator
import no.nav.dagpenger.vedtaksmelding.portabletext.HtmlConverter
import no.nav.dagpenger.vedtaksmelding.sanity.SanityKlient
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

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
            sakId = "sak123",
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
        val avslag =
            Avslag(
                vedtak = hentVedtak("/json/avslag.json"),
                mediator =
                    Mediator(
                        behandlingKlient = mockk(),
                        sanityKlient = SanityKlient(Configuration.sanityApiUrl),
                        vedtaksmeldingRepository = mockk(),
                    ),
            )
        runBlocking {
            avslag.hentOpplysninger()
            val brevBlokker = avslag.hentBrevBlokker()
            writeStringToFile(
                filePath = "build/temp/avslag-minsteinntekt.html",
                content = HtmlConverter.toHtml(brevBlokker, avslag.hentOpplysninger(), meldingOmVedtakData),
            )
        }
    }

    @Test
    fun `Html av innvilgelse `() {
        val innvilgelse =
            Innvilgelse(
                vedtak = hentVedtak("/json/innvilgelsesVedtak.json"),
                mediator =
                    Mediator(
                        behandlingKlient = mockk(),
                        sanityKlient = SanityKlient(Configuration.sanityApiUrl),
                        vedtaksmeldingRepository = mockk(),
                    ),
            )
        runBlocking {
            innvilgelse.hentOpplysninger()
            val brevBlokker = innvilgelse.hentBrevBlokker()
            writeStringToFile(
                filePath = "build/temp/innvilgelse.html",
                content =
                    HtmlConverter.toHtml(
                        brevBlokker = brevBlokker,
                        opplysninger = innvilgelse.hentOpplysninger(),
                        meldingOmVedtakData = meldingOmVedtakData,
                    ),
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
