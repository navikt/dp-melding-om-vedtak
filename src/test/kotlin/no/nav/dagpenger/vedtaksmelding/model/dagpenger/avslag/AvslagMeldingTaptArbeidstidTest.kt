package no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_12_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_36_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_6_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_12_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_36_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_6_MND
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class AvslagMeldingTaptArbeidstidTest {
    @Test
    fun `Riktige brevblokker for avslag arbeidstid ved ordinær rettighet og 6 måneders beregning`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.KravTilTaptArbeidstid(false),
                            DagpengerOpplysning.FastsattVanligArbeidstidPerUke(37.5),
                            DagpengerOpplysning.FastsattNyArbeidstidPerUke(20.5),
                            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder(true),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_6_MND.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid ved ordinær rettighet og 12 måneders gjennomsnittsberegning`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.KravTilTaptArbeidstid(false),
                            DagpengerOpplysning.FastsattVanligArbeidstidPerUke(37.5),
                            DagpengerOpplysning.FastsattNyArbeidstidPerUke(20.5),
                            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste12Måneder(true),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_12_MND.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid ved ordinær rettighet og 36 måneders gjennomsnittsberegning`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.KravTilTaptArbeidstid(false),
                            DagpengerOpplysning.FastsattVanligArbeidstidPerUke(37.5),
                            DagpengerOpplysning.FastsattNyArbeidstidPerUke(20.5),
                            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste36Måneder(true),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_36_MND.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid ved rettighet permittering fisk og 6 måneders beregning`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.KravTilTaptArbeidstid(false),
                            DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri(true),
                            DagpengerOpplysning.FastsattVanligArbeidstidPerUke(37.5),
                            DagpengerOpplysning.FastsattNyArbeidstidPerUke(20.5),
                            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder(true),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_6_MND.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid ved rettighet permittering fisk og 12 måneders gjennomsnittsberegning`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.KravTilTaptArbeidstid(false),
                            DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri(true),
                            DagpengerOpplysning.FastsattVanligArbeidstidPerUke(37.5),
                            DagpengerOpplysning.FastsattNyArbeidstidPerUke(20.5),
                            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste12Måneder(true),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_12_MND.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid ved rettighet permittering fisk og 36 måneders gjennomsnittsberegning`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
//                    vilkår = setOf(arbeidstidIkkeOppfylt, permitteringFiskVilkår),
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.KravTilTaptArbeidstid(false),
                            DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri(true),
                            DagpengerOpplysning.FastsattVanligArbeidstidPerUke(37.5),
                            DagpengerOpplysning.FastsattNyArbeidstidPerUke(20.5),
                            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste36Måneder(true),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_36_MND.brevblokkId,
            ) + VedtakMelding.fasteAvsluttendeBlokker
    }
}
