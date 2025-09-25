CREATE TABLE IF NOT EXISTS brev_variant_v1
(
    behandling_id        UUID PRIMARY KEY,
    brev_variant         TEXT NOT NULL ,
    registrert_tidspunkt TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    endret_tidspunkt     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TRIGGER oppdater_endret_tidspunkt
    BEFORE UPDATE
    ON brev_variant_v1
    FOR EACH ROW
EXECUTE FUNCTION oppdater_endret_tidspunkt();
