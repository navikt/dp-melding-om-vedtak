package no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_ANDRE_FULLE_YTELSER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class AvslagMeldingAndreFulleYtelserTest {
    @Test
    fun `Brevstøtte for avslag grunnet for lite tapt arbeidstid`() {
        shouldNotThrow<VedtakMelding.ManglerBrevstøtte> {
            AvslagMelding(
                vedtak =
                    Vedtak(
                        behandlingId = UUIDv7.ny(),
                        utfall = Vedtak.Utfall.AVSLÅTT,
                        opplysninger =
                            setOf(
                                DagpengerOpplysning.KravTilTaptArbeidstid(false),
                            ),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
    }

    @Test
    fun `Riktige brevblokker for avslag andre fulle ytelser`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    opplysninger = setOf(DagpengerOpplysning.IkkeFulleYtelser(false)),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_ANDRE_FULLE_YTELSER.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }
}
