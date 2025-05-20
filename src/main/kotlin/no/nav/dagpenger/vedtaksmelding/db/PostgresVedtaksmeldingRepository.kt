package no.nav.dagpenger.vedtaksmelding.db

import kotliquery.TransactionalSession
import kotliquery.queryOf
import kotliquery.sessionOf
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import org.postgresql.util.PGobject
import java.time.LocalDateTime
import java.util.UUID
import javax.sql.DataSource

class PostgresVedtaksmeldingRepository(
    private val dataSource: DataSource,
) : VedtaksmeldingRepository {
    override fun lagre(utvidetBeskrivelse: UtvidetBeskrivelse): LocalDateTime =
        sessionOf(dataSource).use { session ->
            session.transaction { tx ->
                tx.lagre(utvidetBeskrivelse)
            }
        }

    override fun finn(
        behandlingId: UUID,
        brevblokkId: String,
    ): UtvidetBeskrivelse? =
        sessionOf(dataSource).use { session ->
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

    override fun hent(
        behandlingId: UUID,
        brevblokkId: String,
    ): UtvidetBeskrivelse =
        finn(
            behandlingId = behandlingId,
            brevblokkId = brevblokkId,
        ) ?: throw DataNotFoundException(
            "Kunne ikke finne utvidet beskrivelse for behandlingId: $behandlingId og brevblokkId: $brevblokkId",
        )

    override fun hentUtvidedeBeskrivelserFor(behandlingId: UUID): List<UtvidetBeskrivelse> =
        sessionOf(dataSource).use { session ->
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

    override fun lagreSanityInnhold(
        behandlingId: UUID,
        sanityInnhold: String,
    ): Unit =
        sessionOf(dataSource).use { session ->
            session.run(
                queryOf(
                    //language=PostgreSQL
                    statement =
                        """
                        INSERT INTO sanity_innhold_v1
                            (behandling_id, sanity_innhold)
                        VALUES
                            (:behandling_id, :sanity_innhold)
                        ON CONFLICT (behandling_id) DO UPDATE SET sanity_innhold = :sanity_innhold
                        """.trimIndent(),
                    paramMap =
                        mapOf(
                            "behandling_id" to behandlingId,
                            "sanity_innhold" to
                                PGobject().also {
                                    it.type = "jsonb"
                                    it.value = sanityInnhold
                                },
                        ),
                ).asUpdate,
            )
        }

    override fun hentSanityInnhold(behandlingId: UUID): String =
        sessionOf(dataSource).use { session ->
            session.run(
                queryOf(
                    //language=PostgreSQL
                    statement =
                        """
                        SELECT sanity_innhold
                        FROM   sanity_innhold_v1
                        WHERE  behandling_id = :behandling_id
                        """.trimIndent(),
                    paramMap =
                        mapOf(
                            "behandling_id" to behandlingId,
                        ),
                ).map { row -> row.string("sanity_innhold") }.asSingle,
            ) ?: throw DataNotFoundException("Fant ikke sanity innhold for behandlingId: $behandlingId")
        }

    override fun lagreVedaksmeldingHtml(
        behandlingId: UUID,
        vedtaksmeldingHtml: String,
    ): Unit =
        sessionOf(dataSource).use { session ->
            session.run(
                queryOf(
                    //language=PostgreSQL
                    statement =
                        """
                        INSERT INTO vedtaksmelding_html_v1
                            (behandling_id, vedtaksmelding_html)
                        VALUES
                            (:behandling_id, :vedtaksmelding_html)
                        ON CONFLICT (behandling_id) DO UPDATE SET vedtaksmelding_html = :vedtaksmelding_html
                        """.trimIndent(),
                    paramMap =
                        mapOf(
                            "behandling_id" to behandlingId,
                            "vedtaksmelding_html" to vedtaksmeldingHtml,
                        ),
                ).asUpdate,
            )
        }

    override fun hentVedaksmeldingHtml(behandlingId: UUID): String =
        sessionOf(dataSource).use { session ->
            session.run(
                queryOf(
                    //language=PostgreSQL
                    statement =
                        """
                        SELECT vedtaksmelding_html
                        FROM   vedtaksmelding_html_v1
                        WHERE  behandling_id = :behandling_id
                        """.trimIndent(),
                    paramMap =
                        mapOf(
                            "behandling_id" to behandlingId,
                        ),
                ).map { row -> row.string("vedtaksmelding_html") }.asSingle,
            ) ?: throw DataNotFoundException("Fant ikke vedtaksmelding html for behandlingId: $behandlingId")
        }
}

private fun TransactionalSession.lagre(utvidetBeskrivelse: UtvidetBeskrivelse): LocalDateTime =
    run(
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

class DataNotFoundException(
    message: String,
) : RuntimeException(message)

class KanIkkeLagreUtvidetBeskrivelseException(
    message: String,
) : RuntimeException(message)
