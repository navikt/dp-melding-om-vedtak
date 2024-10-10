CREATE OR REPLACE FUNCTION oppdater_endret_tidspunkt()
RETURNS TRIGGER AS $$
BEGIN
    NEW.endret_tidspunkt = current_timestamp;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TABLE IF NOT EXISTS fritekst_v1
(
    id                    UUID PRIMARY KEY,
    behandling_id         UUID                        NOT NULL UNIQUE,
    fritekst              TEXT,
    registrert_tidspunkt  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    endret_tidspunkt      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
    );
CREATE INDEX IF NOT EXISTS fritekst_behandling_index ON fritekst_v1 (behandling_id);
CREATE TRIGGER oppdater_endret_tidspunkt
    BEFORE UPDATE ON fritekst_v1
    FOR EACH ROW EXECUTE FUNCTION oppdater_endret_tidspunkt();
