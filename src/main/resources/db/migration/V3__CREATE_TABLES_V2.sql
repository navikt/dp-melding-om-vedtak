CREATE TABLE IF NOT EXISTS sanity_innhold_v1
(
    behandling_id        UUID PRIMARY KEY,
    sanity_innhold        JSONB,
    registrert_tidspunkt TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    endret_tidspunkt     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TRIGGER oppdater_endret_tidspunkt
    BEFORE UPDATE
    ON sanity_innhold_v1
    FOR EACH ROW
EXECUTE FUNCTION oppdater_endret_tidspunkt();

CREATE TABLE IF NOT EXISTS vedtaksmelding_html_v1
(
    behandling_id        UUID PRIMARY KEY,
    vedtaksmelding_html  TEXT,
    registrert_tidspunkt TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);