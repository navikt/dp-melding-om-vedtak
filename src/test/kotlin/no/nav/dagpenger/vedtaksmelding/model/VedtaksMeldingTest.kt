package no.nav.dagpenger.vedtaksmelding.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.vedtaksmelding.Mediator
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test
import java.util.UUID

class VedtaksMeldingTest {
    private fun lagOpplysning(
        id: String,
        verdi: String,
    ): Opplysning {
        return Opplysning(
            opplysningTekstId = id,
            navn = "test",
            verdi = verdi,
            datatype = "boolean",
            opplysningId = "test",
        )
    }

    val minsteinntekt: Opplysning =
        lagOpplysning(
            id = "opplysning.krav-til-minsteinntekt",
            verdi = "true",
        )

    val kravPåDagpengerTrue: Opplysning =
        lagOpplysning(
            id = "opplysning.krav-paa-dagpenger",
            verdi = "true",
        )

    val utførtSammordningFalse: Opplysning =
        lagOpplysning(id = "opplysning.har-samordnet", verdi = "false")

    val nittiProsentRegelIkkeBrukt: Opplysning =
        lagOpplysning(
            id = "opplysning.andel-av-dagsats-med-barnetillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget",
            verdi = 0.toString(),
        )

    val ingenBarn: Opplysning = lagOpplysning(id = "opplysning.antall-barn-som-gir-rett-til-barnetillegg", verdi = "0")

    @Test
    fun `Returner kun faste blokker dersom ingen opplysninger trigger spesifikke brevblokker `() {
        val minsteinntekt: Opplysning =
            lagOpplysning(
                id = "opplysning.krav-til-minsteinntekt",
                verdi = "true",
            )
        val opplysninger = setOf(minsteinntekt)

        val mediator =
            mockk<Mediator>().also {
                coEvery { it.hentOpplysningTekstIder(VedtaksMelding.FASTE_BLOKKER.toList()) } returns emptyList()
            }
        runBlocking {
            Behandling(
                id = UUID.fromString("019145eb-6fbb-769f-b1b1-d2450b383a98"),
                tilstand = "Tilstand",
                opplysninger = opplysninger,
            ).let { behandling ->
                VedtaksMelding(behandling, mediator).let { vedtaksMelding ->
                    vedtaksMelding.hentBrevBlokkIder() shouldBe VedtaksMelding.FASTE_BLOKKER
                    vedtaksMelding.hentOpplysninger() shouldBe emptyList()
                }
            }
        }
    }

    @Test
    fun `Skal kke kaste exception når vi ikke finner opplysninger vi forventer at alltid følger med behandlingen`() {
        shouldNotThrowAny {
            {
                VedtaksMelding(
                    Behandling(
                        id = UUIDv7.ny(),
                        tilstand = "y",
                        opplysninger = emptySet(),
                    ),
                    mockk(relaxed = true),
                ).hentBrevBlokkIder()
            }
        }
    }

    @Test
    fun `Rikig brevblokker for avslag på minsteinntekt`() {
        val minsteinntekt: Opplysning =
            lagOpplysning(
                id = "opplysning.krav-til-minsteinntekt",
                verdi = "false",
            )
        val opplysninger = setOf(minsteinntekt)
        val forventedeBrevblokkIder =
            listOf(
                "brev.blokk.vedtak-avslag",
                "brev.blokk.begrunnelse-avslag-minsteinntekt",
            ) + VedtaksMelding.FASTE_BLOKKER

        val mediator =
            mockk<Mediator>().also {
                coEvery { it.hentOpplysningTekstIder(forventedeBrevblokkIder) } returns
                    listOf(
                        minsteinntekt.opplysningTekstId,
                    )
            }
        runBlocking {
            Behandling(
                id = UUID.fromString("019145eb-6fbb-769f-b1b1-d2450b383a98"),
                tilstand = "Tilstand",
                opplysninger = opplysninger,
            ).let { behandling ->
                VedtaksMelding(behandling, mediator).let { vedtaksMelding ->
                    vedtaksMelding.hentBrevBlokkIder() shouldBe forventedeBrevblokkIder
                    vedtaksMelding.hentOpplysninger() shouldBe listOf(minsteinntekt)
                }
            }
        }
    }

    @Test
    fun `Rikig brevblokker for innvilgelse av ordinære dagpenger uten barn, samordning eller 90 % regel`() {
        val opplysninger =
            setOf(minsteinntekt, kravPåDagpengerTrue, ingenBarn, utførtSammordningFalse, nittiProsentRegelIkkeBrukt)
        val forventedeBrevblokkIder =
            listOf(
                "brev.blokk.vedtak-innvilgelse",
                "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                "brev.blokk.grunnlag",
                "brev.blokk.arbeidstiden-din",
                "brev.blokk.egenandel",
                "brev.blokk.du-maa-sende-meldekort",
                "brev.blokk.utbetaling",
                "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                "brev.blokk.du-maa-melde-fra-om-endringer",
                "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
            ) + VedtaksMelding.FASTE_BLOKKER

        val mediator =
            mockk<Mediator>()
        runBlocking {
            Behandling(
                id = UUID.fromString("019145eb-6fbb-769f-b1b1-d2450b383a98"),
                tilstand = "Tilstand",
                opplysninger = opplysninger,
            ).let { behandling ->
                VedtaksMelding(behandling, mediator).let { vedtaksMelding ->
                    vedtaksMelding.hentBrevBlokkIder() shouldBe forventedeBrevblokkIder
                }
            }
        }
    }

    @Test
    fun `Rikig brevblokker for innvilgelse av ordinære dagpenger for person med barn`() {
        val antallBarn: Opplysning =
            lagOpplysning(id = "opplysning.antall-barn-som-gir-rett-til-barnetillegg", verdi = "1")

        val opplysninger =
            setOf(
                minsteinntekt,
                kravPåDagpengerTrue,
                antallBarn,
                utførtSammordningFalse,
                nittiProsentRegelIkkeBrukt,
            )

        val forventedeBrevblokkIder =
            listOf(
                "brev.blokk.vedtak-innvilgelse",
                "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                "brev.blokk.barnetillegg",
                "brev.blokk.grunnlag",
                "brev.blokk.arbeidstiden-din",
                "brev.blokk.egenandel",
                "brev.blokk.du-maa-sende-meldekort",
                "brev.blokk.utbetaling",
                "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                "brev.blokk.du-maa-melde-fra-om-endringer",
                "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
            ) + VedtaksMelding.FASTE_BLOKKER

        val mediator =
            mockk<Mediator>()
        runBlocking {
            Behandling(
                id = UUID.fromString("019145eb-6fbb-769f-b1b1-d2450b383a98"),
                tilstand = "Tilstand",
                opplysninger = opplysninger,
            ).let { behandling ->
                VedtaksMelding(behandling, mediator).let { vedtaksMelding ->
                    vedtaksMelding.hentBrevBlokkIder() shouldBe forventedeBrevblokkIder
                }
            }
        }
    }

    @Test
    fun `Rikig brevblokker for innvilgelse av ordinære dagpenger for person med samordning`() {
        val utførtSammordningTrue: Opplysning = lagOpplysning(id = "opplysning.har-samordnet", verdi = "true")

        val opplysninger =
            setOf(minsteinntekt, kravPåDagpengerTrue, ingenBarn, utførtSammordningTrue, nittiProsentRegelIkkeBrukt)

        val forventedeBrevblokkIder =
            listOf(
                "brev.blokk.vedtak-innvilgelse",
                "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                "brev.blokk.samordning",
                "brev.blokk.grunnlag",
                "brev.blokk.arbeidstiden-din",
                "brev.blokk.egenandel",
                "brev.blokk.du-maa-sende-meldekort",
                "brev.blokk.utbetaling",
                "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                "brev.blokk.du-maa-melde-fra-om-endringer",
                "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
            ) + VedtaksMelding.FASTE_BLOKKER

        val mediator =
            mockk<Mediator>()
        runBlocking {
            Behandling(
                id = UUID.fromString("019145eb-6fbb-769f-b1b1-d2450b383a98"),
                tilstand = "Tilstand",
                opplysninger = opplysninger,
            ).let { behandling ->
                VedtaksMelding(behandling, mediator).let { vedtaksMelding ->
                    vedtaksMelding.hentBrevBlokkIder() shouldBe forventedeBrevblokkIder
                }
            }
        }
    }

    @Test
    fun `Rikig brevblokker for innvilgelse av ordinære dagpenger for person med nittiProsentRegel`() {
        val nittiProsentRegelBrukt: Opplysning =
            lagOpplysning(
                id = "opplysning.andel-av-dagsats-med-barnetillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget",
                verdi = 10.toString(),
            )

        val opplysninger =
            setOf(minsteinntekt, kravPåDagpengerTrue, ingenBarn, utførtSammordningFalse, nittiProsentRegelBrukt)

        val forventedeBrevblokkIder =
            listOf(
                "brev.blokk.vedtak-innvilgelse",
                "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                "brev.blokk.nittiprosentregel",
                "brev.blokk.grunnlag",
                "brev.blokk.arbeidstiden-din",
                "brev.blokk.egenandel",
                "brev.blokk.du-maa-sende-meldekort",
                "brev.blokk.utbetaling",
                "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                "brev.blokk.du-maa-melde-fra-om-endringer",
                "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
            ) + VedtaksMelding.FASTE_BLOKKER

        val mediator =
            mockk<Mediator>()
        runBlocking {
            Behandling(
                id = UUID.fromString("019145eb-6fbb-769f-b1b1-d2450b383a98"),
                tilstand = "Tilstand",
                opplysninger = opplysninger,
            ).let { behandling ->
                VedtaksMelding(behandling, mediator).let { vedtaksMelding ->
                    vedtaksMelding.hentBrevBlokkIder() shouldBe forventedeBrevblokkIder
                }
            }
        }
    }
}
