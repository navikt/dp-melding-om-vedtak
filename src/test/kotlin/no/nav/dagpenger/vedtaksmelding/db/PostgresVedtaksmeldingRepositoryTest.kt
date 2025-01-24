package no.nav.dagpenger.vedtaksmelding.db

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import no.nav.dagpenger.vedtaksmelding.db.Postgres.withMigratedDb
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class PostgresVedtaksmeldingRepositoryTest {
    @Test
    fun `Skal kunne lagre utvidet beskrivelse for brevblokk og behandling, samt hente den igjen`() {
        val behandlingId = UUIDv7.ny()
        val brevblokkId = "brevblokk1"
        val tekst = "Dette er en fin tekst"
        val utvidetBeskrivelse =
            UtvidetBeskrivelse(
                behandlingId = behandlingId,
                brevblokkId = brevblokkId,
                tekst = tekst,
            )
        withMigratedDb { datasource ->
            val repository = PostgresVedtaksmeldingRepository(datasource)
            repository.lagre(utvidetBeskrivelse)

            val utvidetBeskrivelseFraDB =
                repository.hent(
                    behandlingId = behandlingId,
                    brevblokkId = brevblokkId,
                )
            utvidetBeskrivelseFraDB shouldBe utvidetBeskrivelse
            repository.lagre(utvidetBeskrivelse.copy(tekst = "Oppdatert tekst"))
            val oppdatertUtvidetBeskrivelse =
                repository.hent(
                    behandlingId = behandlingId,
                    brevblokkId = brevblokkId,
                )
            oppdatertUtvidetBeskrivelse.tekst shouldBe "Oppdatert tekst"
        }
    }

    @Test
    fun `Lagring av utvidet beskrivelse skal returnere sistEndretTidspunkt`() {
        val behandlingId = UUIDv7.ny()
        val brevblokkId = "brevblokk1"
        val tekst = "Dette er en fin tekst"
        val utvidetBeskrivelse =
            UtvidetBeskrivelse(
                behandlingId = behandlingId,
                brevblokkId = brevblokkId,
                tekst = tekst,
            )
        withMigratedDb { datasource ->
            val repository = PostgresVedtaksmeldingRepository(datasource)
            val sistEndretTidspunkt = repository.lagre(utvidetBeskrivelse)
            sistEndretTidspunkt shouldNotBe null
            sistEndretTidspunkt shouldBeAfter LocalDateTime.now().minusSeconds(1)
        }
    }

    @Test
    fun `Skal kunne hente utvidede beskrivelser for behandling`() {
        val behandlingId = UUIDv7.ny()
        val brevblokkId = "brevblokk1"
        val tekst = "Dette er en fin tekst"
        val utvidetBeskrivelse =
            UtvidetBeskrivelse(
                behandlingId = behandlingId,
                brevblokkId = brevblokkId,
                tekst = tekst,
            )
        withMigratedDb { datasource ->
            val repository = PostgresVedtaksmeldingRepository(datasource)
            repository.lagre(utvidetBeskrivelse)

            val utvidedeBeskrivelserFraDB = repository.hentUtvidedeBeskrivelserFor(behandlingId)
            utvidedeBeskrivelserFraDB[0].behandlingId shouldBe utvidetBeskrivelse.behandlingId
            utvidedeBeskrivelserFraDB[0].tekst shouldBe utvidetBeskrivelse.tekst
            utvidedeBeskrivelserFraDB[0].brevblokkId shouldBe utvidetBeskrivelse.brevblokkId
            utvidedeBeskrivelserFraDB[0].sistEndretTidspunkt shouldNotBe null
        }
    }

    @Test
    fun `Skal kun hente utvidede beskrivelser som ikke er en tom streng`() {
        val behandlingId = UUIDv7.ny()
        val brevblokkId = "brevblokk1"
        val brevblokkId2 = "brevblokk2"
        val tekst = "Dette er en fin tekst"
        val utvidetBeskrivelse =
            UtvidetBeskrivelse(
                behandlingId = behandlingId,
                brevblokkId = brevblokkId,
                tekst = tekst,
            )

        val utvidetBeskrivelseMedTomTekst =
            UtvidetBeskrivelse(
                behandlingId = behandlingId,
                brevblokkId = brevblokkId2,
                tekst = "",
            )

        val utvidetBeskrivelseMedNullTekst =
            UtvidetBeskrivelse(
                behandlingId = behandlingId,
                brevblokkId = brevblokkId2,
                tekst = null,
            )

        withMigratedDb { datasource ->
            val repository = PostgresVedtaksmeldingRepository(datasource)
            repository.lagre(utvidetBeskrivelse)
            repository.lagre(utvidetBeskrivelseMedTomTekst)
            repository.lagre(utvidetBeskrivelseMedNullTekst)

            val utvidetBeskrivelserFraDB = repository.hentUtvidedeBeskrivelserFor(behandlingId)

            utvidetBeskrivelserFraDB.size shouldBe 1

            utvidetBeskrivelserFraDB[0].behandlingId shouldBe utvidetBeskrivelse.behandlingId
            utvidetBeskrivelserFraDB[0].tekst shouldBe utvidetBeskrivelse.tekst
            utvidetBeskrivelserFraDB[0].brevblokkId shouldBe utvidetBeskrivelse.brevblokkId
            utvidetBeskrivelserFraDB[0].sistEndretTidspunkt shouldNotBe null
        }
    }

    @Test
    fun `lagre, hente og oppdatere sanity innhold`() {
        val behandlingId = UUIDv7.ny()
        val sanityInnhold = """{"key": "value"}"""
        val sanityInnholdUpdate = """{"key": "value2"}"""

        withMigratedDb { dataSource ->
            val repository = PostgresVedtaksmeldingRepository(dataSource)

            repository.lagreSanityInnhold(behandlingId, sanityInnhold)
            repository.hentSanityInnhold(behandlingId) shouldBe sanityInnhold

            repository.lagreSanityInnhold(behandlingId, sanityInnholdUpdate)
            repository.hentSanityInnhold(behandlingId) shouldBe sanityInnholdUpdate
        }
    }

    @Test
    fun `lagre og hente vedtakshtml`() {
        val behandlingId = UUIDv7.ny()
        val vedtaksHtml = "<html><body><h1>Hei</h1></body></html>"
        val vedtaksHtmlUpdate = "<html><body><h1>Hei2</h1></body></html>"

        withMigratedDb { dataSource ->
            val repository = PostgresVedtaksmeldingRepository(dataSource)

            repository.lagreVedaksmeldingHtml(behandlingId, vedtaksHtml)
            repository.hentVedaksmeldingHtml(behandlingId) shouldBe vedtaksHtml
            shouldThrow<Exception> { repository.lagreVedaksmeldingHtml(behandlingId, vedtaksHtmlUpdate) }
        }
    }
}
