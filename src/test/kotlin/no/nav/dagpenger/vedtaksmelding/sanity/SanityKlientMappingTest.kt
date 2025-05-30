package no.nav.dagpenger.vedtaksmelding.sanity

import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.vedtaksmelding.lagHttpKlient
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AntallBarnSomGirRettTilBarnetillegg
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.BarnetilleggIKroner
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.Prøvingsdato
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_UTESTENGT_HJEMMEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_BARNETILLEGG
import org.junit.jupiter.api.Test

class SanityKlientMappingTest {
    private val resourseRetriever = object {}.javaClass

    @Test
    fun `test av enkel brevblokk mapping med kun en opplysning`() {
        runBlocking {
            SanityKlient(
                sanityUrl = "http://locahost/sanity",
                httpKlient = lagHttpKlient(engine = lageMockEngine(), block = SanityKlient.httpClientConfig),
            ).hentOpplysningTekstIder(listOf(AVSLAG_INNLEDNING.brevblokkId)) shouldBe listOf(Prøvingsdato.opplysningTekstId)
        }
    }

    @Test
    fun `test av brevblokk med flere opplysnigner`() {
        runBlocking {
            SanityKlient(
                sanityUrl = "http://locahost/sanity",
                httpKlient = lagHttpKlient(engine = lageMockEngine()),
            ).hentOpplysningTekstIder(listOf(INNVILGELSE_BARNETILLEGG.brevblokkId)) shouldBe
                listOf(
                    AntallBarnSomGirRettTilBarnetillegg.opplysningTekstId,
                    BarnetilleggIKroner.opplysningTekstId,
                )
        }
    }

    @Test
    fun `test av brevblokk uten opplysninger`() {
        runBlocking {
            SanityKlient(
                sanityUrl = "http://locahost/sanity",
                httpKlient = lagHttpKlient(engine = lageMockEngine()),
            ).hentOpplysningTekstIder(listOf(AVSLAG_UTESTENGT_HJEMMEL.brevblokkId)) shouldBe emptyList()
        }
    }

    private fun lageMockEngine(jsonResponse: String = resourseRetriever.getResource("/json/sanity.json")!!.readText()): MockEngine =
        MockEngine { _ ->
            respond(jsonResponse, headers = headersOf("Content-Type" to listOf("application/json")))
        }
}
