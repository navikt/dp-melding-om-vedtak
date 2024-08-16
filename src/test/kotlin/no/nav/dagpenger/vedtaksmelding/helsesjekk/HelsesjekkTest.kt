package no.nav.dagpenger.vedtaksmelding.helsesjekk

import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Test

class HelsesjekkTest {
    @Test
    fun `skal svare på helsesjekker`() =
        testApplication {
            application {
                helsesjekker()
            }

            client.get("/isalive").status shouldBe HttpStatusCode.OK
            client.get("/isready").status shouldBe HttpStatusCode.OK
        }
}
