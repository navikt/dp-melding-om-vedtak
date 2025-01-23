package no.nav.dagpenger.vedtaksmelding.model.innvilgelse

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import no.nav.dagpenger.vedtaksmelding.model.Innvilgelse
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.BARN
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.KRONER
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.UKER
import no.nav.dagpenger.vedtaksmelding.model.Utfall
import no.nav.dagpenger.vedtaksmelding.model.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.Vedtaksmelding
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

class InnvilgelseTest {
    private val behandlingId = UUIDv7.ny()

    private fun nittiprosentRegelOpplysning(verdi: String = "10") =
        Opplysning(
            opplysningTekstId =
                "opplysning.andel-av-dagsats-med-barne" +
                    "tillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget",
            verdi = verdi,
            datatype = FLYTTALL,
            enhet = KRONER,
        )

    private fun samordnetOpplysning(verdi: String = "true") =
        Opplysning(
            opplysningTekstId = "opplysning.har-samordnet",
            verdi = verdi,
            datatype = BOOLSK,
        )

    private fun samordnetYtelseDagsats(
        opplysningTekstId: String,
        dagsats: Int = 100,
    ) = Opplysning(
        opplysningTekstId = opplysningTekstId,
        verdi = dagsats.toString(),
        datatype = BOOLSK,
    )

    private fun barnetilleggOpplysning(verdi: String = "1") =
        Opplysning(
            opplysningTekstId = "opplysning.antall-barn-som-gir-rett-til-barnetillegg",
            verdi = verdi,
            datatype = HELTALL,
            enhet = BARN,
        )

    @Test
    fun `kriterier for å lage innvigelse`() {
        shouldThrow<IllegalArgumentException> {
            Innvilgelse(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        vilkår = emptySet(),
                        utfall = Utfall.AVSLÅTT,
                        opplysninger = emptySet(),
                    ),
                mediator = mockk(),
            )
        }

        shouldNotThrowAny {
            Innvilgelse(
                vedtak =
                    Vedtak(
                        behandlingId = behandlingId,
                        vilkår = emptySet(),
                        utfall = Utfall.INNVILGET,
                        opplysninger = emptySet(),
                    ),
                mediator = mockk(),
            )
        }
    }

    @Test
    fun `Rikig brevblokker for innvilgelse av ordinære dagpenger uten barn, samordning eller 90 % regel`() {
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
            ) + Vedtaksmelding.fasteBlokker

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger = emptySet(),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikig brevblokker for innvilgelse av dagpenger etter verneplikt`() {
        val forventedeBrevblokkIder =
            listOf(
                "brev.blokk.vedtak-innvilgelse",
                "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                "brev.blokk.grunnlag-for-verneplikt",
                "brev.blokk.arbeidstiden-din",
                "brev.blokk.egenandel",
                "brev.blokk.du-maa-sende-meldekort",
                "brev.blokk.utbetaling",
                "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                "brev.blokk.du-maa-melde-fra-om-endringer",
                "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
            ) + Vedtaksmelding.fasteBlokker

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger = setOf(Opplysning("opplysning.er-innvilget-med-verneplikt", "true", BOOLSK)),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikig brevblokker for innvilgelse av dagpenger etter verneplikt når bruker også oppfyller inntektskravet`() {
        val forventedeBrevblokkIder =
            listOf(
                "brev.blokk.vedtak-innvilgelse",
                "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                "brev.blokk.grunnlag-for-verneplikt",
                "brev.blokk.verneplikt-gunstigere-enn-inntekt",
                "brev.blokk.arbeidstiden-din",
                "brev.blokk.egenandel",
                "brev.blokk.du-maa-sende-meldekort",
                "brev.blokk.utbetaling",
                "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                "brev.blokk.du-maa-melde-fra-om-endringer",
                "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
            ) + Vedtaksmelding.fasteBlokker

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            Opplysning("opplysning.er-innvilget-med-verneplikt", "true", BOOLSK),
                            Opplysning("opplysning.krav-til-minsteinntekt", "true", BOOLSK),
                            Opplysning(
                                "opplysning.antall-stonadsuker-som-gis-ved-ordinare-dagpenger",
                                "52",
                                HELTALL,
                                UKER,
                            ),
                        ),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikig brevblokker for innvilgelse av ordinære dagpenger for person med barn`() {
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
            ) + Vedtaksmelding.fasteBlokker

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            barnetilleggOpplysning(),
                        ),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            barnetilleggOpplysning("0"),
                        ),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder - "brev.blokk.barnetillegg"
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak med samordning av sykepenger`() {
        val forventedeBrevblokkIder =
            listOf(
                "brev.blokk.vedtak-innvilgelse",
                "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                "brev.blokk.samordning",
                "brev.blokk.samordning-sykepenger",
                "brev.blokk.grunnlag",
                "brev.blokk.arbeidstiden-din",
                "brev.blokk.egenandel",
                "brev.blokk.du-maa-sende-meldekort",
                "brev.blokk.utbetaling",
                "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                "brev.blokk.du-maa-melde-fra-om-endringer",
                "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
            ) + Vedtaksmelding.fasteBlokker

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            samordnetOpplysning(), samordnetYtelseDagsats("opplysning.sykepenger-dagsats"),
                        ),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak med samordning av pleiepenger`() {
        val forventedeBrevblokkIder =
            listOf(
                "brev.blokk.vedtak-innvilgelse",
                "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                "brev.blokk.samordning",
                "brev.blokk.samordning-pleiepenger",
                "brev.blokk.grunnlag",
                "brev.blokk.arbeidstiden-din",
                "brev.blokk.egenandel",
                "brev.blokk.du-maa-sende-meldekort",
                "brev.blokk.utbetaling",
                "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                "brev.blokk.du-maa-melde-fra-om-endringer",
                "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
            ) + Vedtaksmelding.fasteBlokker

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            samordnetOpplysning(), samordnetYtelseDagsats("opplysning.pleiepenger-dagsats"),
                        ),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak med samordning av omsorgspenger`() {
        val forventedeBrevblokkIder =
            listOf(
                "brev.blokk.vedtak-innvilgelse",
                "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                "brev.blokk.samordning",
                "brev.blokk.samordning-omsorgspenger",
                "brev.blokk.grunnlag",
                "brev.blokk.arbeidstiden-din",
                "brev.blokk.egenandel",
                "brev.blokk.du-maa-sende-meldekort",
                "brev.blokk.utbetaling",
                "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                "brev.blokk.du-maa-melde-fra-om-endringer",
                "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
            ) + Vedtaksmelding.fasteBlokker

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            samordnetOpplysning(), samordnetYtelseDagsats("opplysning.omsorgspenger-dagsats"),
                        ),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak med samordning av opplæringspenger`() {
        val forventedeBrevblokkIder =
            listOf(
                "brev.blokk.vedtak-innvilgelse",
                "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                "brev.blokk.samordning",
                "brev.blokk.samordning-opplaeringspenger",
                "brev.blokk.grunnlag",
                "brev.blokk.arbeidstiden-din",
                "brev.blokk.egenandel",
                "brev.blokk.du-maa-sende-meldekort",
                "brev.blokk.utbetaling",
                "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                "brev.blokk.du-maa-melde-fra-om-endringer",
                "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
            ) + Vedtaksmelding.fasteBlokker

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            samordnetOpplysning(), samordnetYtelseDagsats("opplysning.opplaeringspenger-dagsats"),
                        ),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak med samordning av uføre`() {
        val forventedeBrevblokkIder =
            listOf(
                "brev.blokk.vedtak-innvilgelse",
                "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                "brev.blokk.samordning",
                "brev.blokk.samordning-ufore",
                "brev.blokk.grunnlag",
                "brev.blokk.arbeidstiden-din",
                "brev.blokk.egenandel",
                "brev.blokk.du-maa-sende-meldekort",
                "brev.blokk.utbetaling",
                "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                "brev.blokk.du-maa-melde-fra-om-endringer",
                "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
            ) + Vedtaksmelding.fasteBlokker

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            samordnetOpplysning(), samordnetYtelseDagsats("opplysning.ufore-dagsats"),
                        ),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak med samordning av foreldrepenger`() {
        val forventedeBrevblokkIder =
            listOf(
                "brev.blokk.vedtak-innvilgelse",
                "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                "brev.blokk.samordning",
                "brev.blokk.samordning-foreldrepenger",
                "brev.blokk.grunnlag",
                "brev.blokk.arbeidstiden-din",
                "brev.blokk.egenandel",
                "brev.blokk.du-maa-sende-meldekort",
                "brev.blokk.utbetaling",
                "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                "brev.blokk.du-maa-melde-fra-om-endringer",
                "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
            ) + Vedtaksmelding.fasteBlokker

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            samordnetOpplysning(), samordnetYtelseDagsats("opplysning.foreldrepenger-dagsats"),
                        ),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak med samordning av svangerskapspenger`() {
        val forventedeBrevblokkIder =
            listOf(
                "brev.blokk.vedtak-innvilgelse",
                "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                "brev.blokk.samordning",
                "brev.blokk.samordning-svangerskapspenger",
                "brev.blokk.grunnlag",
                "brev.blokk.arbeidstiden-din",
                "brev.blokk.egenandel",
                "brev.blokk.du-maa-sende-meldekort",
                "brev.blokk.utbetaling",
                "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                "brev.blokk.du-maa-melde-fra-om-endringer",
                "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
            ) + Vedtaksmelding.fasteBlokker

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            samordnetOpplysning(), samordnetYtelseDagsats("opplysning.svangerskapspenger-dagsats"),
                        ),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak med samordning med alle ytelser`() {
        val forventedeBrevblokkIder =
            listOf(
                "brev.blokk.vedtak-innvilgelse",
                "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                "brev.blokk.samordning",
                "brev.blokk.samordning-sykepenger",
                "brev.blokk.samordning-pleiepenger",
                "brev.blokk.samordning-omsorgspenger",
                "brev.blokk.samordning-opplaeringspenger",
                "brev.blokk.samordning-ufore",
                "brev.blokk.samordning-foreldrepenger",
                "brev.blokk.samordning-svangerskapspenger",
                "brev.blokk.grunnlag",
                "brev.blokk.arbeidstiden-din",
                "brev.blokk.egenandel",
                "brev.blokk.du-maa-sende-meldekort",
                "brev.blokk.utbetaling",
                "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                "brev.blokk.du-maa-melde-fra-om-endringer",
                "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
            ) + Vedtaksmelding.fasteBlokker

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            samordnetOpplysning(),
                            samordnetYtelseDagsats("opplysning.sykepenger-dagsats"),
                            samordnetYtelseDagsats("opplysning.pleiepenger-dagsats"),
                            samordnetYtelseDagsats("opplysning.omsorgspenger-dagsats"),
                            samordnetYtelseDagsats("opplysning.opplaeringspenger-dagsats"),
                            samordnetYtelseDagsats("opplysning.ufore-dagsats"),
                            samordnetYtelseDagsats("opplysning.foreldrepenger-dagsats"),
                            samordnetYtelseDagsats("opplysning.svangerskapspenger-dagsats"),
                        ),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikige brevblokker for innvilgelse av ordinære dagpenger for vedtak uten samordning`() {
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
            ) + Vedtaksmelding.fasteBlokker

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            samordnetOpplysning("false"),
                        ),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }

    @Test
    fun `Rikig brevblokker for innvilgelse av ordinære dagpenger for person med nittiProsentRegel`() {
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
            ) + Vedtaksmelding.fasteBlokker

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            nittiprosentRegelOpplysning(),
                        ),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            nittiprosentRegelOpplysning(),
                        ),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger =
                        setOf(
                            nittiprosentRegelOpplysning("0"),
                        ),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder - "brev.blokk.nittiprosentregel"
    }

    @Test
    fun `Rikig brevblokker rekkefølge for innvilgelse med barnetillegg,nittiprosent regel og samordning`() {
        val forventedeBrevblokkIder =
            listOf(
                "brev.blokk.vedtak-innvilgelse",
                "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                "brev.blokk.barnetillegg",
                "brev.blokk.nittiprosentregel",
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
            ) + Vedtaksmelding.fasteBlokker

        Innvilgelse(
            vedtak =
                Vedtak(
                    behandlingId = behandlingId,
                    vilkår = emptySet(),
                    utfall = Utfall.INNVILGET,
                    opplysninger =
                        setOf(nittiprosentRegelOpplysning(), samordnetOpplysning(), barnetilleggOpplysning()),
                ),
            mediator = mockk(),
        ).brevBlokkIder() shouldBe forventedeBrevblokkIder
    }
}
