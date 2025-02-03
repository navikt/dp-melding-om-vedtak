package no.nav.dagpenger.vedtaksmelding.metrics

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Test

class MetricsTest {
    @Test
    fun `eksponerer metrikker`() {
        testApplication {
            application { metrics() }
            client.get("/metrics").apply {
                this.status shouldBe HttpStatusCode.OK
                this.bodyAsText() shouldContain "ktor_http_server_requests"
            }
        }
    }
}
