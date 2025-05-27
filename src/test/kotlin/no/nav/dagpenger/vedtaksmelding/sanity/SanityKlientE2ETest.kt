package no.nav.dagpenger.vedtaksmelding.sanity

import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.vedtaksmelding.Configuration
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths

class SanityKlientE2ETest {
    @Disabled
    @Test
    fun hentNyeBrevblokker() {
        runBlocking {
            // Use a relative path
            val filePath = Paths.get("src/test/resources/json/sanity.json").toAbsolutePath().toString()
            val jsonContent = SanityKlient(Configuration.sanityApiUrl).hentBrevBlokkerJson()
            File(filePath).writeText(jsonContent)
        }
    }
}
