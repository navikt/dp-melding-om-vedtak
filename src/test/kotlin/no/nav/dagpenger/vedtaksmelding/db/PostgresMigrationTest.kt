package no.nav.dagpenger.vedtaksmelding.db

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.vedtaksmelding.db.Postgres.withCleanDb
import no.nav.dagpenger.vedtaksmelding.db.PostgresDataSourceBuilder.runMigration
import org.junit.jupiter.api.Test

class PostgresMigrationTest {
    @Test
    fun `Migration scripts are applied successfully`() {
        withCleanDb {
            val migrations = runMigration()
            migrations shouldBe 2
        }
    }
}
