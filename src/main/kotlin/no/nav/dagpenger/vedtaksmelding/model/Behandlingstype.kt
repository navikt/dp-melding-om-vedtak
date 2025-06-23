package no.nav.dagpenger.vedtaksmelding.model

import no.nav.dagpenger.saksbehandling.api.models.BehandlingstypeDTO

enum class Behandlingstype {
    RETT_TIL_DAGPENGER,
    KLAGE,
    MELDEKORT,
    ;

    companion object {
        fun BehandlingstypeDTO?.tilBehandlingstype(): Behandlingstype =
            this?.let {
                Behandlingstype.valueOf(it.value)
            } ?: RETT_TIL_DAGPENGER
    }
}
