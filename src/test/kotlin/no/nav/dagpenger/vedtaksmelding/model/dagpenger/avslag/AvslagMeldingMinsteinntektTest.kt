package no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Opprinnelse
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Periode
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_MINSTEINNTEKT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_MINSTEINNTEKT_DEL_2
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class AvslagMeldingMinsteinntektTest {
    @Test
    fun `Rikige brevblokker for avslag på minsteinntekt`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    automatiskBehandling = false,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(false, listOf(Periode(false, Opprinnelse.NY))),
                        ),
                    behandletHendelseType = "SØKNAD",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_MINSTEINNTEKT_DEL_1.brevblokkId,
                AVSLAG_MINSTEINNTEKT_DEL_2.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }
}
