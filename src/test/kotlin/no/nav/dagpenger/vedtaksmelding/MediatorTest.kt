package no.nav.dagpenger.vedtaksmelding

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import no.nav.dagpenger.saksbehandling.api.models.BehandlerDTO
import no.nav.dagpenger.saksbehandling.api.models.BehandlerEnhetDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.vedtaksmelding.db.Postgres.withMigratedDb
import no.nav.dagpenger.vedtaksmelding.db.PostgresDataSourceBuilder.dataSource
import no.nav.dagpenger.vedtaksmelding.db.PostgresVedtaksmeldingRepository
import no.nav.dagpenger.vedtaksmelding.db.VedtaksmeldingRepository
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding.FasteBrevblokker.RETT_TIL_Å_KLAGE
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagMelding
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagVilkårMedBrevstøtte.MINSTEINNTEKT_ELLER_VERNEPLIKT
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak.Utfall
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vilkår
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk
import no.nav.dagpenger.vedtaksmelding.sanity.SanityKlient
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class MediatorTest {
    private val behandlingId = UUIDv7.ny()
    private val saksbehandler = Saksbehandler("tulleToken")
    private val resourseRetriever = object {}.javaClass

    @Test
    fun `Returnere rett vedtak ved bruk av hentVedtaksmelding `() {
        val vedtak =
            Vedtak(
                behandlingId = behandlingId,
                vilkår =
                    setOf(
                        Vilkår(
                            "Oppfyller kravet til minsteinntekt eller verneplikt",
                            Vilkår.Status.IKKE_OPPFYLT,
                        ),
                    ),
                utfall = Utfall.AVSLÅTT,
                opplysninger = emptySet(),
                fagsakId = "fagsakId test",
            )
        val behandlingKlient =
            mockk<BehandlingKlient>().also {
                coEvery { it.hentVedtak(behandlingId, saksbehandler) } returns Result.success(vedtak)
            }

        val resource = resourseRetriever.getResource("/json/sanity.json").readText()
        val sanityKlient =
            mockk<SanityKlient>().also {
                coEvery { it.hentBrevBlokkerJson() } returns resource
            }
        withMigratedDb { dataSource ->
            val repository = PostgresVedtaksmeldingRepository(dataSource)
            val mediator =
                Mediator(
                    behandlingKlient = behandlingKlient,
                    sanityKlient = sanityKlient,
                    vedtaksmeldingRepository = repository,
                )
            runBlocking {
                mediator.hentVedtaksmelding(behandlingId, saksbehandler).getOrThrow().shouldBeInstanceOf<AvslagMelding>()
            }
            repository.hentSanityInnhold(behandlingId) shouldEqualJson resource
        }
    }

    @Test
    fun `Returnere rett vedtak ved bruk av hentEndeligVedtaksmelding `() {
        val vedtak =
            resourseRetriever.getResource("/json/vedtak.json").readText().let {
                VedtakMapper(it).vedtak()
            }
        val meldingOmVedtakDataDTO =
            MeldingOmVedtakDataDTO(
                fornavn = "Ola",
                etternavn = "Nordmann",
                fodselsnummer = "12345678901",
                saksbehandler =
                    BehandlerDTO(
                        fornavn = "Ola",
                        etternavn = "Nordmann",
                        ident = "Z999999",
                        enhet =
                            BehandlerEnhetDTO(
                                postadresse = "NAV Test",
                                navn = "NAV Test",
                            ),
                    ),
            )

        val resource = resourseRetriever.getResource("/json/sanity.json").readText()
        val sanityKlient =
            mockk<SanityKlient>().also {
                coEvery { it.hentBrevBlokkerJson() } returns resource
            }
        val behandlingKlient =
            mockk<BehandlingKlient>().also {
                coEvery { it.hentVedtak(behandlingId, saksbehandler) } returns Result.success(vedtak)
            }

        withMigratedDb {
            val repository = PostgresVedtaksmeldingRepository(dataSource)
            repository.lagreSanityInnhold(behandlingId, resourseRetriever.getResource("/json/sanity.json").readText())
            val mediator =
                Mediator(
                    behandlingKlient = behandlingKlient,
                    sanityKlient = sanityKlient,
                    vedtaksmeldingRepository = repository,
                )
            runBlocking {
                val vedtakshtml = mediator.hentEndeligVedtak(behandlingId, saksbehandler, meldingOmVedtakDataDTO)
                vedtakshtml shouldBe repository.hentVedaksmeldingHtml(behandlingId)
            }
        }
    }

    @Test
    fun `skal sende ett eller annet vedtak`() {
        val vedtak =
            Vedtak(
                behandlingId = behandlingId,
                vilkår =
                    setOf(
                        Vilkår(
                            MINSTEINNTEKT_ELLER_VERNEPLIKT.navn,
                            Vilkår.Status.IKKE_OPPFYLT,
                        ),
                    ),
                utfall = Utfall.AVSLÅTT,
                opplysninger = emptySet(),
                fagsakId = "fagsakId test",
            )
        val behandlingKlient =
            mockk<BehandlingKlient>().also {
                coEvery { it.hentVedtak(behandlingId, saksbehandler) } returns Result.success(vedtak)
            }

        val mediator =
            Mediator(
                behandlingKlient = behandlingKlient,
                sanityKlient = SanityKlient(Configuration.sanityApiUrl),
                vedtaksmeldingRepository = mockk<VedtaksmeldingRepository>(relaxed = true),
            )

        runBlocking {
            mediator.hentVedtaksmelding(
                behandlingId = behandlingId,
                saksbehandler = saksbehandler,
            ).getOrThrow().shouldBeInstanceOf<AvslagMelding>()
        }

        coVerify(exactly = 1) {
            behandlingKlient.hentVedtak(behandlingId, saksbehandler)
        }
    }

    @Test
    fun `Kaster feil dersom vi ikke får hentet vedtak fra dp-behandling`() {
        val mediator =
            Mediator(
                mockk<BehandlingKlient>().also {
                    coEvery { it.hentVedtak(any(), any()) } throws RuntimeException("Noe gikk galt")
                },
                mockk(),
                vedtaksmeldingRepository = mockk<VedtaksmeldingRepository>(relaxed = true),
            )
        runBlocking {
            shouldThrow<RuntimeException> {
                mediator.hentVedtaksmelding(
                    behandlingId = behandlingId,
                    saksbehandler = saksbehandler,
                ).getOrThrow()
            }
        }
    }

    @Test
    fun `hent utvidet beskrivelse inkl tomme beskrivelser`() {
        val vedtaksmeldingRepository =
            mockk<VedtaksmeldingRepository>().also {
                coEvery { it.hentUtvidedeBeskrivelserFor(behandlingId) } returns
                    listOf(
                        UtvidetBeskrivelse(
                            behandlingId = behandlingId,
                            brevblokkId = RETT_TIL_Å_KLAGE.brevBlokkId,
                            tekst = "hallo",
                            sistEndretTidspunkt = LocalDateTime.MAX,
                        ),
                        UtvidetBeskrivelse(
                            behandlingId = behandlingId,
                            brevblokkId = "brev.blokk.rett-til-aa-random",
                            tekst = "random test",
                            sistEndretTidspunkt = LocalDateTime.MAX,
                        ),
                    )
            }
        val mediator =
            spyk(
                Mediator(
                    behandlingKlient = mockk(),
                    sanityKlient = mockk<SanityKlient>(),
                    vedtaksmeldingRepository = vedtaksmeldingRepository,
                ),
            ).also {
                coEvery { it.hentVedtaksmelding(behandlingId, saksbehandler) } returns
                    Result.success(
                        mockk<VedtakMelding>().also {
                            coEvery { it.hentBrevBlokker() } returns
                                listOf(
                                    BrevBlokk(
                                        textId = RETT_TIL_Å_KLAGE.brevBlokkId,
                                        title = "Rett til å klage",
                                        utvidetBeskrivelse = true,
                                        innhold = emptyList(),
                                        _type = "brevblokk",
                                    ),
                                    BrevBlokk(
                                        textId = "brev.blokk.rett-til-aa-random",
                                        title = "Fjas",
                                        utvidetBeskrivelse = true,
                                        innhold = emptyList(),
                                        _type = "brevblokk",
                                    ),
                                    BrevBlokk(
                                        textId = "brev.blokk.rett-til-ikke-innhold",
                                        title = "Tull",
                                        utvidetBeskrivelse = true,
                                        innhold = emptyList(),
                                        _type = "brevblokk",
                                    ),
                                    BrevBlokk(
                                        textId = "brev.blokk.rett-til-ikke-utvidet",
                                        title = "Tull",
                                        utvidetBeskrivelse = false,
                                        innhold = emptyList(),
                                        _type = "brevblokk",
                                    ),
                                )
                        },
                    )
            }

        runBlocking {
            val utvidedebeskrivelser = mediator.hentUtvidedeBeskrivelser(behandlingId, saksbehandler)
            utvidedebeskrivelser.size shouldBe 3
            utvidedebeskrivelser.single {
                it.brevblokkId == "brev.blokk.rett-til-ikke-innhold"
            }.tekst shouldBe null
            utvidedebeskrivelser.single {
                it.brevblokkId == RETT_TIL_Å_KLAGE.brevBlokkId
            }.tekst shouldBe "hallo"
            utvidedebeskrivelser.single {
                it.brevblokkId == "brev.blokk.rett-til-aa-random"
            }.tekst shouldBe "random test"
        }
    }
}
