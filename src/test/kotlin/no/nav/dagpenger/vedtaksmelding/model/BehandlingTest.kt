package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class BehandlingTest {
    @Test
    fun `hent ut navn, dataType, verdi og behandlingsId fra behandling`() {
        val opplysninger =
            setOf(
                Opplysning(
                    opplysningTekstId = "opplysning.krav-til-minsteinntekt",
                    navn = "Krav til minsteinntekt",
                    verdi = "true",
                    datatype = "boolean",
                    opplysningId = "test",
                ),
            )

        val behandling =
            Behandling(
                id = "019145eb-6fbb-769f-b1b1-d2450b383a98",
                tilstand = "Tilstand",
                opplysninger = opplysninger,
            )

        behandling.hentOpplysning("opplysning.krav-til-minsteinntekt") shouldBe
            Opplysning(
                opplysningTekstId = "opplysning.krav-til-minsteinntekt",
                navn = "Krav til minsteinntekt",
                verdi = "true",
                datatype = "boolean",
                opplysningId = "test",
            )
        shouldThrow<NoSuchElementException> {
            behandling.hentOpplysning("tull")
        }
    }
}
