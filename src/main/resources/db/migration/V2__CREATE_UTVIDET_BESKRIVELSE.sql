DROP TABLE fritekst_v1;

CREATE TABLE IF NOT EXISTS utvidet_beskrivelse_v1
(
    id                    SERIAL PRIMARY KEY,
    behandling_id         UUID                        NOT NULL,
    brevblokk_id          TEXT                        NOT NULL,
    tekst                 TEXT,
    registrert_tidspunkt  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    endret_tidspunkt      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT behandling_brevblokk_uk UNIQUE (behandling_id, brevblokk_id)
);

CREATE INDEX IF NOT EXISTS behandling_brevblokk_index ON utvidet_beskrivelse_v1 (behandling_id, brevblokk_id);

CREATE TRIGGER oppdater_endret_tidspunkt
BEFORE UPDATE ON utvidet_beskrivelse_v1
FOR EACH ROW EXECUTE FUNCTION oppdater_endret_tidspunkt();
