package no.nav.dagpenger.vedtaksmelding.sanity

import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.vedtaksmelding.Configuration
import org.junit.jupiter.api.Test
import java.io.File

class SanityKlientE2ETest {
//    @Disabled
    @Test
    fun hentNyeBrevblokker() {
        runBlocking {
            // Tilpass til eget miljø
            val filePath = "C:\\navit\\dp-melding-om-vedtak\\src\\test\\resources\\json\\sanity.json"
            val jsonContent = SanityKlient(Configuration.sanityApiUrl).hentBrevBlokkerJson()
            File(filePath).writeText(jsonContent)
        }
    }
}
