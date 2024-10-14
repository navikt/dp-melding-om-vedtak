package no.nav.dagpenger.vedtaksmelding.db

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.db.Postgres.withMigratedDb
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

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
        }
    }
}
