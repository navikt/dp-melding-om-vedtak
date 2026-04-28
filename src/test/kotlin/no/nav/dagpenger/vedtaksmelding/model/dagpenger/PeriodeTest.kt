package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDate

class PeriodeTest {
    private val dato = LocalDate.of(2024, 6, 15)

    @Test
    fun `periode uten datoer inkluderer alle datoer`() {
        val periode = Periode(verdi = 1, opprinnelse = Opprinnelse.NY)
        periode.inkludererDato(dato) shouldBe true
        periode.inkludererDato(LocalDate.MIN) shouldBe true
        periode.inkludererDato(LocalDate.MAX) shouldBe true
    }

    @Test
    fun `periode med åpen start og lukket slutt inkluderer kun datoer til og med slutt`() {
        val periode =
            Periode(
                verdi = 1,
                opprinnelse = Opprinnelse.NY,
                gyldigTilOgMed = LocalDate.of(2024, 6, 15),
            )
        periode.inkludererDato(LocalDate.of(2020, 1, 1)) shouldBe true
        periode.inkludererDato(LocalDate.of(2024, 6, 15)) shouldBe true
        periode.inkludererDato(LocalDate.of(2024, 6, 16)) shouldBe false
    }

    @Test
    fun `periode med lukket start og åpen slutt inkluderer datoer fra og med start`() {
        val periode =
            Periode(
                verdi = 1,
                opprinnelse = Opprinnelse.NY,
                gyldigFraOgMed = LocalDate.of(2024, 6, 15),
            )
        periode.inkludererDato(LocalDate.of(2024, 6, 14)) shouldBe false
        periode.inkludererDato(LocalDate.of(2024, 6, 15)) shouldBe true
        periode.inkludererDato(LocalDate.of(2030, 1, 1)) shouldBe true
    }

    @Test
    fun `periode med både start og slutt inkluderer kun datoer i intervallet`() {
        val periode =
            Periode(
                verdi = 1,
                opprinnelse = Opprinnelse.NY,
                gyldigFraOgMed = LocalDate.of(2024, 1, 1),
                gyldigTilOgMed = LocalDate.of(2024, 12, 31),
            )
        periode.inkludererDato(LocalDate.of(2023, 12, 31)) shouldBe false
        periode.inkludererDato(LocalDate.of(2024, 1, 1)) shouldBe true
        periode.inkludererDato(LocalDate.of(2024, 6, 15)) shouldBe true
        periode.inkludererDato(LocalDate.of(2024, 12, 31)) shouldBe true
        periode.inkludererDato(LocalDate.of(2025, 1, 1)) shouldBe false
    }
}
