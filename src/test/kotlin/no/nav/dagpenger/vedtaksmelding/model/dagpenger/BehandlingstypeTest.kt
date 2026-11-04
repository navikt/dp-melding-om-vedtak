package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.Behandlingstype
import no.nav.dagpenger.vedtaksmelding.model.Behandlingstype.Companion.tilBehandlingstype
import org.junit.jupiter.api.Test

class BehandlingstypeTest {
    @Test
    fun `Behandlingstype tilBehandlingstype`() {
        "SØKNAD".tilBehandlingstype() shouldBe Behandlingstype.SØKNAD
        "MANUELL".tilBehandlingstype() shouldBe Behandlingstype.MANUELL
        "MELDEKORT".tilBehandlingstype() shouldBe Behandlingstype.MELDEKORT
        "ARBEIDSSØKERPERIODE".tilBehandlingstype() shouldBe Behandlingstype.ARBEIDSSØKERPERIODE
        "KLAGE".tilBehandlingstype() shouldBe Behandlingstype.KLAGE
        "SAMORDNING".tilBehandlingstype() shouldBe Behandlingstype.SAMORDNING
        "OMGJØRING".tilBehandlingstype() shouldBe Behandlingstype.FRITEKST
        "FERIETILLEGG".tilBehandlingstype() shouldBe Behandlingstype.FRITEKST
        "MIGRERING".tilBehandlingstype() shouldBe Behandlingstype.FRITEKST
        "KLAGEFØRSTEINSTANS".tilBehandlingstype() shouldBe Behandlingstype.FRITEKST
        "KLAGEKLAGEINSTANS".tilBehandlingstype() shouldBe Behandlingstype.FRITEKST
    }
}
