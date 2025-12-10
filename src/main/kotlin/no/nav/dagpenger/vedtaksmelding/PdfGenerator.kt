package no.nav.dagpenger.vedtaksmelding

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsBytes

object PdfGenerator {
    private val url = "http://dp-behov-pdf-generator/convert-html-to-pdf"

    suspend fun convertHtmlToPdf(html: String): ByteArray = httpClient.post(url) { setBody(html) }.bodyAsBytes()

    private val httpClient = HttpClient { }
}
