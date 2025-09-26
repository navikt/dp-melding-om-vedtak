package no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding.ManglerBrevstøtte
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ARBEIDSTIDEN_DIN
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_BARNETILLEGG
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_EGENANDEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_GRUNNLAG
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MED_EGENANDEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELDEKORT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_MELD_FRA_OM_ENDRINGER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_NITTI_PROSENT_REGEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ORDINÆR
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SKATTEKORT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_STANS_ÅRSAKER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_UTBETALING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class OrdinæreDagpengerTest {
    private val behandlingId = UUIDv7.ny()

    @Test
    fun `kriterier for å lage innvigelse`() {
        shouldThrow<ManglerBrevstøtte> {
            InnvilgelseMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.AVSLÅTT,
                        opplysninger = emptySet(),
                    ),
                alleBrevblokker = emptyList(),
            )
        }

        shouldNotThrowAny {
            InnvilgelseMelding(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        utfall = Vedtak.Utfall.INNVILGET,
                        opplysninger = emptySet(),
                    ),
                alleBrevblokker = emptyList(),
            )
        }
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger uten noen spesialregler`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_ORDINÆR.brevblokkId,
                INNVILGELSE_MED_EGENANDEL.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                INNVILGELSE_DAGPENGEPERIODE.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_GRUNNLAG.brevblokkId,
                INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId,
                INNVILGELSE_EGENANDEL.brevblokkId,
                INNVILGELSE_MELDEKORT.brevblokkId,
                INNVILGELSE_UTBETALING.brevblokkId,
                INNVILGELSE_SKATTEKORT.brevblokkId,
                INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker

        InnvilgelseMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.Egenandel(3000),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikig brevblokker for innvilgelse av ordinære dagpenger med barnetillegg-opplysning`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_ORDINÆR.brevblokkId,
                INNVILGELSE_MED_EGENANDEL.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                INNVILGELSE_DAGPENGEPERIODE.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_BARNETILLEGG.brevblokkId,
                INNVILGELSE_GRUNNLAG.brevblokkId,
                INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId,
                INNVILGELSE_EGENANDEL.brevblokkId,
                INNVILGELSE_MELDEKORT.brevblokkId,
                INNVILGELSE_UTBETALING.brevblokkId,
                INNVILGELSE_SKATTEKORT.brevblokkId,
                INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker

        InnvilgelseMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.INNVILGET,
                    setOf(
                        DagpengerOpplysning.AntallBarnSomGirRettTilBarnetillegg(1),
                        DagpengerOpplysning.Egenandel(3000),
                    ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder

        InnvilgelseMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.AntallBarnSomGirRettTilBarnetillegg(0),
                            DagpengerOpplysning.Egenandel(3000),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder - INNVILGELSE_BARNETILLEGG.brevblokkId
    }

    @Test
    fun `Rikig brevblokker for innvilgelse av ordinære dagpenger med barnetillegg og nittiProsentRegel`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_ORDINÆR.brevblokkId,
                INNVILGELSE_MED_EGENANDEL.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                INNVILGELSE_DAGPENGEPERIODE.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_BARNETILLEGG.brevblokkId,
                INNVILGELSE_NITTI_PROSENT_REGEL.brevblokkId,
                INNVILGELSE_GRUNNLAG.brevblokkId,
                INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId,
                INNVILGELSE_EGENANDEL.brevblokkId,
                INNVILGELSE_MELDEKORT.brevblokkId,
                INNVILGELSE_UTBETALING.brevblokkId,
                INNVILGELSE_SKATTEKORT.brevblokkId,
                INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker

        InnvilgelseMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.AntallBarnSomGirRettTilBarnetillegg(1),
                            DagpengerOpplysning.Egenandel(3000),
                            DagpengerOpplysning.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget(
                                10,
                            ),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder

        InnvilgelseMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.AntallBarnSomGirRettTilBarnetillegg(1),
                            DagpengerOpplysning.Egenandel(3000),
                            DagpengerOpplysning.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget(
                                0,
                            ),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder - INNVILGELSE_NITTI_PROSENT_REGEL.brevblokkId
    }

    @Test
    fun `Rikig brevblokker rekkefølge for innvilgelse med barnetillegg, nittiProsentRegel`() {
        val forventedeBrevblokkIder =
            listOf(
                INNVILGELSE_ORDINÆR.brevblokkId,
                INNVILGELSE_MED_EGENANDEL.brevblokkId,
                INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE.brevblokkId,
                INNVILGELSE_DAGPENGEPERIODE.brevblokkId,
                INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE.brevblokkId,
                INNVILGELSE_BARNETILLEGG.brevblokkId,
                INNVILGELSE_NITTI_PROSENT_REGEL.brevblokkId,
                INNVILGELSE_GRUNNLAG.brevblokkId,
                INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId,
                INNVILGELSE_EGENANDEL.brevblokkId,
                INNVILGELSE_MELDEKORT.brevblokkId,
                INNVILGELSE_UTBETALING.brevblokkId,
                INNVILGELSE_SKATTEKORT.brevblokkId,
                INNVILGELSE_STANS_ÅRSAKER.brevblokkId,
                INNVILGELSE_MELD_FRA_OM_ENDRINGER.brevblokkId,
                INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker

        InnvilgelseMelding(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    utfall = Vedtak.Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.AntallBarnSomGirRettTilBarnetillegg(1),
                            DagpengerOpplysning.Egenandel(3000),
                            DagpengerOpplysning.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget(
                                10,
                            ),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }
}
