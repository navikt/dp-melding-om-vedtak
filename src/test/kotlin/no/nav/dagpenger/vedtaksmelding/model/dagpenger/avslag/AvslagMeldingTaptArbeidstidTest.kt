package no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Opprinnelse
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Periode
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding
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
                    automatiskBehandling = false,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.OppfyllerVilkåretOmTapAvArbeidstid(false, listOf(Periode(false, Opprinnelse.NY))),
                            DagpengerOpplysning.FastsattVanligArbeidstidPerUke(37.5, listOf(Periode(37.5, Opprinnelse.NY))),
                            DagpengerOpplysning.FastsattNyArbeidstidPerUke(20.5, listOf(Periode(20.5, Opprinnelse.NY))),
                            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder(true, listOf(Periode(true, Opprinnelse.NY))),
                        ),
                    behandletHendelseType = "SØKNAD",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_6_MND.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid ved ordinær rettighet og 12 måneders gjennomsnittsberegning`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    automatiskBehandling = false,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.OppfyllerVilkåretOmTapAvArbeidstid(false, listOf(Periode(false, Opprinnelse.NY))),
                            DagpengerOpplysning.FastsattVanligArbeidstidPerUke(37.5, listOf(Periode(37.5, Opprinnelse.NY))),
                            DagpengerOpplysning.FastsattNyArbeidstidPerUke(20.5, listOf(Periode(20.5, Opprinnelse.NY))),
                            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste12Måneder(
                                true,
                                listOf(Periode(true, Opprinnelse.NY)),
                            ),
                        ),
                    behandletHendelseType = "SØKNAD",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_12_MND.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid ved ordinær rettighet og 36 måneders gjennomsnittsberegning`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    automatiskBehandling = false,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.OppfyllerVilkåretOmTapAvArbeidstid(false, listOf(Periode(false, Opprinnelse.NY))),
                            DagpengerOpplysning.FastsattVanligArbeidstidPerUke(37.5, listOf(Periode(37.5, Opprinnelse.NY))),
                            DagpengerOpplysning.FastsattNyArbeidstidPerUke(20.5, listOf(Periode(20.5, Opprinnelse.NY))),
                            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste36Måneder(
                                true,
                                listOf(Periode(true, Opprinnelse.NY)),
                            ),
                        ),
                    behandletHendelseType = "SØKNAD",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_36_MND.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid ved rettighet permittering fisk og 6 måneders beregning`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    automatiskBehandling = false,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.OppfyllerVilkåretOmTapAvArbeidstid(false, listOf(Periode(false, Opprinnelse.NY))),
                            DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri(true, listOf(Periode(true, Opprinnelse.NY))),
                            DagpengerOpplysning.FastsattVanligArbeidstidPerUke(37.5, listOf(Periode(37.5, Opprinnelse.NY))),
                            DagpengerOpplysning.FastsattNyArbeidstidPerUke(20.5, listOf(Periode(20.5, Opprinnelse.NY))),
                            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder(true, listOf(Periode(true, Opprinnelse.NY))),
                        ),
                    behandletHendelseType = "SØKNAD",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_6_MND.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid ved rettighet permittering fisk og 12 måneders gjennomsnittsberegning`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    automatiskBehandling = false,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.OppfyllerVilkåretOmTapAvArbeidstid(false, listOf(Periode(false, Opprinnelse.NY))),
                            DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri(true, listOf(Periode(true, Opprinnelse.NY))),
                            DagpengerOpplysning.FastsattVanligArbeidstidPerUke(37.5, listOf(Periode(37.5, Opprinnelse.NY))),
                            DagpengerOpplysning.FastsattNyArbeidstidPerUke(20.5, listOf(Periode(20.5, Opprinnelse.NY))),
                            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste12Måneder(
                                true,
                                listOf(Periode(true, Opprinnelse.NY)),
                            ),
                        ),
                    behandletHendelseType = "SØKNAD",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_12_MND.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker for avslag arbeidstid ved rettighet permittering fisk og 36 måneders gjennomsnittsberegning`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    utfall = Vedtak.Utfall.AVSLÅTT,
                    automatiskBehandling = false,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.OppfyllerVilkåretOmTapAvArbeidstid(false, listOf(Periode(false, Opprinnelse.NY))),
                            DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri(true, listOf(Periode(true, Opprinnelse.NY))),
                            DagpengerOpplysning.FastsattVanligArbeidstidPerUke(37.5, listOf(Periode(37.5, Opprinnelse.NY))),
                            DagpengerOpplysning.FastsattNyArbeidstidPerUke(20.5, listOf(Periode(20.5, Opprinnelse.NY))),
                            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste36Måneder(
                                true,
                                listOf(Periode(true, Opprinnelse.NY)),
                            ),
                        ),
                    behandletHendelseType = "SØKNAD",
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_36_MND.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }
}
