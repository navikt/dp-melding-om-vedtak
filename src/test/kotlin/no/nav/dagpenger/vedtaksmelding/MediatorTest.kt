package no.nav.dagpenger.vedtaksmelding

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
import no.nav.dagpenger.vedtaksmelding.model.Behandlingstype
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding.FasteBrevblokker.RETT_TIL_Å_KLAGE
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vilkår
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VilkårTyper.MINSTEINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagMelding
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk
import no.nav.dagpenger.vedtaksmelding.sanity.SanityKlient
import no.nav.dagpenger.vedtaksmelding.util.readFile
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class MediatorTest {
    private val behandlingId = UUIDv7.ny()
    private val saksbehandler = Saksbehandler("tulleToken")

    private val resource = "/json/sanity.json".readFile()
    private val sanityKlient =
        mockk<SanityKlient>().also {
            coEvery { it.hentBrevBlokkerJson() } returns resource
        }

    private val mockKlageBehandlingKlient = mockk<KlageBehandlingKlient>()

    @Test
    fun `Returnere rett vedtak ved bruk av hentVedtaksmelding `() {
        val vedtak =
            Vedtak(
                behandlingId = behandlingId,
                vilkår =
                    setOf(
                        Vilkår(
                            MINSTEINNTEKT.vilkårNavn,
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
        withMigratedDb { dataSource ->
            val repository = PostgresVedtaksmeldingRepository(dataSource)
            val mediator =
                Mediator(
                    behandlingKlient = behandlingKlient,
                    sanityKlient = sanityKlient,
                    klageBehandlingKlient = mockKlageBehandlingKlient,
                    vedtaksmeldingRepository = repository,
                )
            runBlocking {
                mediator
                    .hentVedtaksmelding(
                        behandlingId,
                        saksbehandler,
                        Behandlingstype.RETT_TIL_DAGPENGER,
                    ).shouldBeInstanceOf<AvslagMelding>()
            }
            repository.hentSanityInnhold(behandlingId) shouldEqualJson resource
        }
    }

    @Test
    fun `Returnere rett vedtak ved bruk av hentEndeligVedtaksmelding `() {
        val vedtak =
            ("/json/vedtak.json").readFile().let {
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

        val behandlingKlient =
            mockk<BehandlingKlient>().also {
                coEvery { it.hentVedtak(behandlingId, saksbehandler) } returns Result.success(vedtak)
            }

        withMigratedDb {
            val repository = PostgresVedtaksmeldingRepository(dataSource)
            repository.lagreSanityInnhold(behandlingId, resource)
            val mediator =
                Mediator(
                    klageBehandlingKlient = mockKlageBehandlingKlient,
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
                            MINSTEINNTEKT.vilkårNavn,
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
                sanityKlient = sanityKlient,
                klageBehandlingKlient = mockKlageBehandlingKlient,
                vedtaksmeldingRepository = mockk<VedtaksmeldingRepository>(relaxed = true),
            )

        runBlocking {
            mediator
                .hentVedtaksmelding(
                    behandlingId = behandlingId,
                    saksbehandler = saksbehandler,
                    Behandlingstype.RETT_TIL_DAGPENGER,
                ).shouldBeInstanceOf<AvslagMelding>()
        }

        coVerify(exactly = 1) {
            behandlingKlient.hentVedtak(behandlingId, saksbehandler)
        }
    }

    @Test
    fun `Kaster feil dersom vi ikke får hentet vedtak fra dp-behandling`() {
        val mediator =
            Mediator(
                behandlingKlient =
                    mockk<BehandlingKlient>().also {
                        coEvery { it.hentVedtak(any(), any()) } throws RuntimeException("Noe gikk galt")
                    },
                klageBehandlingKlient = mockKlageBehandlingKlient,
                sanityKlient = sanityKlient,
                vedtaksmeldingRepository = mockk<VedtaksmeldingRepository>(relaxed = true),
            )
        runBlocking {
            shouldThrow<RuntimeException> {
                mediator.hentVedtaksmelding(
                    behandlingId = behandlingId,
                    saksbehandler = saksbehandler,
                    Behandlingstype.RETT_TIL_DAGPENGER,
                )
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
                    sanityKlient = sanityKlient,
                    klageBehandlingKlient = mockKlageBehandlingKlient,
                    vedtaksmeldingRepository = vedtaksmeldingRepository,
                ),
            ).also {
                coEvery { it.hentVedtaksmelding(behandlingId, saksbehandler, behanldingstype = any()) } returns
                    mockk<VedtakMelding>(relaxed = true).also {
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
                    }
            }
        runBlocking {
            val utvidedebeskrivelser =
                mediator
                    .hentVedtak(
                        behandlingId = behandlingId,
                        behandler = saksbehandler,
                        meldingOmVedtakData =
                            mockk<MeldingOmVedtakDataDTO>(relaxed = true).also {
                                every { it.behandlingstype } returns null
                            },
                    ).utvidedeBeskrivelser
            require(true) {
                "utvidedeBeskrivelser should not be null"
            }

            utvidedebeskrivelser.size shouldBe 3
            utvidedebeskrivelser
                .single {
                    it.brevblokkId == "brev.blokk.rett-til-ikke-innhold"
                }.tekst shouldBe ""
            utvidedebeskrivelser
                .single {
                    it.brevblokkId == RETT_TIL_Å_KLAGE.brevBlokkId
                }.tekst shouldBe "hallo"
            utvidedebeskrivelser
                .single {
                    it.brevblokkId == "brev.blokk.rett-til-aa-random"
                }.tekst shouldBe "random test"
        }
    }
}
