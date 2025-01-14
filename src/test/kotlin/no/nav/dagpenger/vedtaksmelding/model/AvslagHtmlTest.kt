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

class AvslagHtmlTest {
    private val resourseRetriever = object {}.javaClass
    private val vedtak =
        resourseRetriever.getResource("/json/avslag.json")?.let { VedtakMapper(it.readText()).vedtak() }
            ?: throw RuntimeException("Fant ikke ressurs")

    @Test
    fun hubba() {
        val meldingOmVedtakData =
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
        val avslag =
            Avslag(
                vedtak = vedtak,
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
            writeStringToFile(content = HtmlConverter.toHtml(brevBlokker, avslag.hentOpplysninger(), meldingOmVedtakData))
        }
    }

    fun writeStringToFile(
        filePath: String = "build/temp/hubba.html",
        content: String,
    ) {
        val path = Paths.get(filePath)
        Files.createDirectories(path.parent)
        Files.writeString(path, content)
    }
}
