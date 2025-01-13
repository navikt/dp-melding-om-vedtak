package no.nav.dagpenger.vedtaksmelding.model

import io.mockk.mockk
import kotlinx.coroutines.runBlocking
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
            writeStringToFile(content = HtmlConverter.toHtml(brevBlokker, avslag.hentOpplysninger()))
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
