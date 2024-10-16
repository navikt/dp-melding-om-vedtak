package no.nav.dagpenger.vedtaksmelding.db

import kotliquery.TransactionalSession
import kotliquery.queryOf
import kotliquery.sessionOf
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import java.time.LocalDateTime
import java.util.UUID
import javax.sql.DataSource

class PostgresVedtaksmeldingRepository(private val dataSource: DataSource) : VedtaksmeldingRepository {
    override fun lagre(utvidetBeskrivelse: UtvidetBeskrivelse): LocalDateTime {
        return sessionOf(dataSource).use { session ->
            session.transaction { tx ->
                tx.lagre(utvidetBeskrivelse)
            }
        }
    }

    override fun finn(
        behandlingId: UUID,
        brevblokkId: String,
    ): UtvidetBeskrivelse? {
        return sessionOf(dataSource).use { session ->
            session.run(
                queryOf(
                    //language=PostgreSQL
                    statement =
                        """
                        SELECT utbe.tekst
                        FROM   utvidet_beskrivelse_v1 utbe
                        WHERE  utbe.behandling_id = :behandling_id
                        AND    utbe.brevblokk_id  = :brevblokk_id
                        """.trimIndent(),
                    paramMap =
                        mapOf(
                            "behandling_id" to behandlingId,
                            "brevblokk_id" to brevblokkId,
                        ),
                ).map { row ->
                    UtvidetBeskrivelse(
                        behandlingId = behandlingId,
                        brevblokkId = brevblokkId,
                        tekst = row.string("tekst"),
                    )
                }.asSingle,
            )
        }
    }

    override fun hent(
        behandlingId: UUID,
        brevblokkId: String,
    ): UtvidetBeskrivelse {
        return finn(
            behandlingId = behandlingId,
            brevblokkId = brevblokkId,
        ) ?: throw DataNotFoundException(
            "Kunne ikke finne utvidet beskrivelse for behandlingId: $behandlingId og brevblokkId: $brevblokkId",
        )
    }

    override fun hentUtvidedeBeskrivelserFor(behandlingId: UUID): List<UtvidetBeskrivelse> {
        return sessionOf(dataSource).use { session ->
            session.run(
                queryOf(
                    //language=PostgreSQL
                    statement =
                        """
                        SELECT  tekst, brevblokk_id, endret_tidspunkt 
                        FROM    utvidet_beskrivelse_v1
                        WHERE   behandling_id = :behandling_id
                        AND     tekst IS NOT NULL 
                        AND     tekst != ''
                        """.trimIndent(),
                    paramMap =
                        mapOf(
                            "behandling_id" to behandlingId,
                        ),
                ).map { row ->
                    UtvidetBeskrivelse(
                        behandlingId = behandlingId,
                        brevblokkId = row.string("brevblokk_id"),
                        tekst = row.string("tekst"),
                        sistEndretTidspunkt = row.localDateTime("endret_tidspunkt"),
                    )
                }.asList,
            )
        }
    }
}

private fun TransactionalSession.lagre(utvidetBeskrivelse: UtvidetBeskrivelse): LocalDateTime {
    return run(
        queryOf(
            //language=PostgreSQL
            statement =
                """
                INSERT INTO utvidet_beskrivelse_v1
                    (behandling_id, brevblokk_id, tekst)
                VALUES
                    (:behandling_id, :brevblokk_id, :tekst)
                ON CONFLICT (behandling_id, brevblokk_id) DO UPDATE SET tekst = :tekst
                RETURNING endret_tidspunkt
                """.trimIndent(),
            paramMap =
                mapOf(
                    "behandling_id" to utvidetBeskrivelse.behandlingId,
                    "brevblokk_id" to utvidetBeskrivelse.brevblokkId,
                    "tekst" to utvidetBeskrivelse.tekst,
                ),
        ).map { row -> row.localDateTime("endret_tidspunkt") }.asSingle,
    ) ?: throw KanIkkeLagreUtvidetBeskrivelseException(
        "Kunne ikke lagre utvidet beskrivelse for behandlingId: " +
            "${utvidetBeskrivelse.behandlingId} og brevblokkId: ${utvidetBeskrivelse.brevblokkId}",
    )
}

class DataNotFoundException(message: String) : RuntimeException(message)

class KanIkkeLagreUtvidetBeskrivelseException(message: String) : RuntimeException(message)
