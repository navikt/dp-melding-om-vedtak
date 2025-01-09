package no.nav.dagpenger.vedtaksmelding.model

import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.vedtaksmelding.Configuration
import no.nav.dagpenger.vedtaksmelding.Mediator
import no.nav.dagpenger.vedtaksmelding.portabletext.HtmlConverter
import no.nav.dagpenger.vedtaksmelding.sanity.SanityKlient
import org.junit.jupiter.api.Test

class AvslagHtmlTest {
    private val resourseRetriever = object {}.javaClass
    private val vedtak =
        resourseRetriever.getResource("/json/avslag.json")?.let { VedtakMapper(it.readText()).vedtak() }
            ?: throw RuntimeException("Fant ikke ressurs")

    @Test
    fun hubba() {
        val avslag =
            AvslagMinsteInntekt(
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
            println(HtmlConverter.toHtml(brevBlokker, avslag.hentOpplysninger()))
        }
    }
}
