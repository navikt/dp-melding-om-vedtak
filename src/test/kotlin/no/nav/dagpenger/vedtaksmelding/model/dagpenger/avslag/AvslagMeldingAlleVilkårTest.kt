package no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.DagpengerOpplysning
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_ALDER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_ANDRE_FULLE_YTELSER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING_PERMITTERT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_INNLEDNING_PERMITTERT_FISK
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_MEDLEMSKAP_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_MEDLEMSKAP_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_MINSTEINNTEKT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_MINSTEINNTEKT_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_OPPHOLD_UTLAND_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_OPPHOLD_UTLAND_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_PERMITTERT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_PERMITTERT_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_REGISTRERT_ARBEIDSSOKER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_STREIK_LOCKOUT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_STREIK_LOCKOUT_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_12_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_6_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_36_MND
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_UTDANNING
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_UTESTENGT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_UTESTENGT_HJEMMEL
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class AvslagMeldingAlleVilkårTest {
    @Test
    fun `Riktige brevblokker i korrekt rekkefølge ved avslag på alle vilkår ved ordinære dagpenger`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    vedtakType = Vedtak.VedtakType.AVSLAG_DAGPENGER,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste12Måneder(true),
                            DagpengerOpplysning.IkkeFulleYtelser(false),
                            DagpengerOpplysning.IkkeStreikEllerLockout(false),
                            DagpengerOpplysning.KravTilAlder(false),
                            DagpengerOpplysning.KravTilArbeidssøker(false),
                            DagpengerOpplysning.KravTilTapAvArbeidsinntekt(false),
                            DagpengerOpplysning.KravTilTapAvArbeidsinntektOgArbeidstid(false),
                            DagpengerOpplysning.KravTilTaptArbeidstid(false),
                            DagpengerOpplysning.KravTilUtdanning(false),
                            DagpengerOpplysning.OppfyllerKravTilArbeidsfør(false),
                            DagpengerOpplysning.OppfyllerKravTilArbeidssøker(false),
                            DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(false),
                            DagpengerOpplysning.OppfyllerKravTilMobilitet(false),
                            DagpengerOpplysning.OppfyllerKravetTilEthvertArbeid(false),
                            DagpengerOpplysning.OppfyllerKravetTilIkkeUtestengt(false),
                            DagpengerOpplysning.OppfyllerKravetTilOpphold(false),
                            DagpengerOpplysning.OppfyllerMedlemskap(false),
                            DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(false),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING.brevblokkId,
                AVSLAG_MINSTEINNTEKT_DEL_1.brevblokkId,
                AVSLAG_MINSTEINNTEKT_DEL_2.brevblokkId,
                AVSLAG_ALDER.brevblokkId,
                AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_12_MND.brevblokkId,
                AVSLAG_MEDLEMSKAP_DEL_1.brevblokkId,
                AVSLAG_MEDLEMSKAP_DEL_2.brevblokkId,
                AVSLAG_UTESTENGT.brevblokkId,
                AVSLAG_UTESTENGT_HJEMMEL.brevblokkId,
                AVSLAG_UTDANNING.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_REGISTRERT_ARBEIDSSOKER.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId,
                AVSLAG_OPPHOLD_UTLAND_DEL_1.brevblokkId,
                AVSLAG_OPPHOLD_UTLAND_DEL_2.brevblokkId,
                AVSLAG_ANDRE_FULLE_YTELSER.brevblokkId,
                AVSLAG_STREIK_LOCKOUT_DEL_1.brevblokkId,
                AVSLAG_STREIK_LOCKOUT_DEL_2.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker i korrekt rekkefølge ved avslag på alle vilkår ved permittering`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    vedtakType = Vedtak.VedtakType.AVSLAG_DAGPENGER,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder(true),
                            DagpengerOpplysning.IkkeFulleYtelser(false),
                            DagpengerOpplysning.IkkeStreikEllerLockout(false),
                            DagpengerOpplysning.KravTilAlder(false),
                            DagpengerOpplysning.KravTilArbeidssøker(false),
                            DagpengerOpplysning.KravTilTapAvArbeidsinntekt(false),
                            DagpengerOpplysning.KravTilTapAvArbeidsinntektOgArbeidstid(false),
                            DagpengerOpplysning.KravTilTaptArbeidstid(false),
                            DagpengerOpplysning.KravTilUtdanning(false),
                            DagpengerOpplysning.OppfyllerKravTilArbeidsfør(false),
                            DagpengerOpplysning.OppfyllerKravTilArbeidssøker(false),
                            DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(false),
                            DagpengerOpplysning.OppfyllerKravTilMobilitet(false),
                            DagpengerOpplysning.OppfyllerKravetTilEthvertArbeid(false),
                            DagpengerOpplysning.OppfyllerKravetTilIkkeUtestengt(false),
                            DagpengerOpplysning.OppfyllerKravetTilOpphold(false),
                            DagpengerOpplysning.OppfyllerMedlemskap(false),
                            DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(false),
                            DagpengerOpplysning.OppfyllerKravetTilPermittering(false),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING_PERMITTERT.brevblokkId,
                AVSLAG_PERMITTERT_DEL_1.brevblokkId,
                AVSLAG_PERMITTERT_DEL_2.brevblokkId,
                AVSLAG_MINSTEINNTEKT_DEL_1.brevblokkId,
                AVSLAG_MINSTEINNTEKT_DEL_2.brevblokkId,
                AVSLAG_ALDER.brevblokkId,
                AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_DEL_3_SISTE_6_MND.brevblokkId,
                AVSLAG_MEDLEMSKAP_DEL_1.brevblokkId,
                AVSLAG_MEDLEMSKAP_DEL_2.brevblokkId,
                AVSLAG_UTESTENGT.brevblokkId,
                AVSLAG_UTESTENGT_HJEMMEL.brevblokkId,
                AVSLAG_UTDANNING.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_REGISTRERT_ARBEIDSSOKER.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId,
                AVSLAG_OPPHOLD_UTLAND_DEL_1.brevblokkId,
                AVSLAG_OPPHOLD_UTLAND_DEL_2.brevblokkId,
                AVSLAG_ANDRE_FULLE_YTELSER.brevblokkId,
                AVSLAG_STREIK_LOCKOUT_DEL_1.brevblokkId,
                AVSLAG_STREIK_LOCKOUT_DEL_2.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }

    @Test
    fun `Riktige brevblokker i korrekt rekkefølge ved avslag på alle vilkår ved permittering fra fiskeindustri`() {
        AvslagMelding(
            vedtak =
                Vedtak(
                    behandlingId = UUIDv7.ny(),
                    vedtakType = Vedtak.VedtakType.AVSLAG_DAGPENGER,
                    opplysninger =
                        setOf(
                            DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste36Måneder(true),
                            DagpengerOpplysning.IkkeFulleYtelser(false),
                            DagpengerOpplysning.IkkeStreikEllerLockout(false),
                            DagpengerOpplysning.KravTilAlder(false),
                            DagpengerOpplysning.KravTilArbeidssøker(false),
                            DagpengerOpplysning.KravTilTapAvArbeidsinntekt(false),
                            DagpengerOpplysning.KravTilTapAvArbeidsinntektOgArbeidstid(false),
                            DagpengerOpplysning.KravTilTaptArbeidstid(false),
                            DagpengerOpplysning.KravTilUtdanning(false),
                            DagpengerOpplysning.OppfyllerKravTilArbeidsfør(false),
                            DagpengerOpplysning.OppfyllerKravTilArbeidssøker(false),
                            DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(false),
                            DagpengerOpplysning.OppfyllerKravTilMobilitet(false),
                            DagpengerOpplysning.OppfyllerKravetTilEthvertArbeid(false),
                            DagpengerOpplysning.OppfyllerKravetTilIkkeUtestengt(false),
                            DagpengerOpplysning.OppfyllerKravetTilOpphold(false),
                            DagpengerOpplysning.OppfyllerMedlemskap(false),
                            DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(false),
                            DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri(false),
                        ),
                ),
            alleBrevblokker = emptyList(),
        ).brevBlokkIder() shouldBe
            listOf(
                AVSLAG_INNLEDNING_PERMITTERT_FISK.brevblokkId,
                AVSLAG_PERMITTERT_DEL_1.brevblokkId,
                AVSLAG_PERMITTERT_DEL_2.brevblokkId,
                AVSLAG_MINSTEINNTEKT_DEL_1.brevblokkId,
                AVSLAG_MINSTEINNTEKT_DEL_2.brevblokkId,
                AVSLAG_ALDER.brevblokkId,
                AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSINNTEKT_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_1.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_2.brevblokkId,
                AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK_DEL_3_SISTE_36_MND.brevblokkId,
                AVSLAG_MEDLEMSKAP_DEL_1.brevblokkId,
                AVSLAG_MEDLEMSKAP_DEL_2.brevblokkId,
                AVSLAG_UTESTENGT.brevblokkId,
                AVSLAG_UTESTENGT_HJEMMEL.brevblokkId,
                AVSLAG_UTDANNING.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_REGISTRERT_ARBEIDSSOKER.brevblokkId,
                AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL.brevblokkId,
                AVSLAG_OPPHOLD_UTLAND_DEL_1.brevblokkId,
                AVSLAG_OPPHOLD_UTLAND_DEL_2.brevblokkId,
                AVSLAG_ANDRE_FULLE_YTELSER.brevblokkId,
                AVSLAG_STREIK_LOCKOUT_DEL_1.brevblokkId,
                AVSLAG_STREIK_LOCKOUT_DEL_2.brevblokkId,
            ) + Vedtaksmelding.fasteAvsluttendeBlokker
    }
}
