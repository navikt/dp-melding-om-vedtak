package no.nav.dagpenger.vedtaksmelding.sanity

import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.vedtaksmelding.lagHttpKlient
import org.junit.jupiter.api.Test

class SanityKlientMappingTest {
    private val resourseRetriever = object {}.javaClass

    @Test
    fun `test av enkel brevblokk mapping med kun en opplysning`() {
        runBlocking {
            SanityKlient(
                sanityUrl = "http://locahost/sanity",
                httpKlient = lagHttpKlient(engine = lageMockEngine(), SanityKlient.httpClientConfig),
            ).hentOpplysningTekstIder(listOf("brev.blokk.vedtak-avslag")) shouldBe listOf("opplysning.provingsdato")
        }
    }

    @Test
    fun `test av brevblokk med flere opplysnigner`() {
        runBlocking {
            SanityKlient(
                sanityUrl = "http://locahost/sanity", httpKlient = lagHttpKlient(engine = lageMockEngine()),
            ).hentOpplysningTekstIder(listOf("brev.blokk.barnetillegg")) shouldBe
                listOf(
                    "opplysning.antall-barn-som-gir-rett-til-barnetillegg",
                    "opplysning.barnetillegg-i-kroner",
                )
        }
    }

    @Test
    fun `test av brevblokk uten opplysninger`() {
        runBlocking {
            SanityKlient(
                sanityUrl = "http://locahost/sanity", httpKlient = lagHttpKlient(engine = lageMockEngine()),
            ).hentOpplysningTekstIder(listOf("brev.blokk.vedtak-innvilget")) shouldBe emptyList()
        }
    }

    private fun lageMockEngine(jsonResponse: String = resourseRetriever.getResource("/json/sanity.json")!!.readText()): MockEngine {
        return MockEngine { request ->
            respond(jsonResponse, headers = headersOf("Content-Type" to listOf("application/json")))
        }
    }
}
