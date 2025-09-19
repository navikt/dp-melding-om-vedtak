package no.nav.dagpenger.vedtaksmelding.model

import no.nav.dagpenger.saksbehandling.api.models.BehandlingstypeDTO

enum class Behandlingstype {
    RETT_TIL_DAGPENGER,
    KLAGE,
    MELDEKORT,
    MANUELL,
    ;

    companion object {
        fun BehandlingstypeDTO.tilBehandlingstype(): Behandlingstype =
            when (this) {
                BehandlingstypeDTO.RETT_TIL_DAGPENGER -> RETT_TIL_DAGPENGER
                BehandlingstypeDTO.SØKNAD -> RETT_TIL_DAGPENGER
                BehandlingstypeDTO.KLAGE -> KLAGE
                BehandlingstypeDTO.MELDEKORT -> MELDEKORT
                BehandlingstypeDTO.MANUELL -> MANUELL
            }
    }
}
