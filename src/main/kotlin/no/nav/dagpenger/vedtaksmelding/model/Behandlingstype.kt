package no.nav.dagpenger.vedtaksmelding.model

enum class Behandlingstype {
    RETT_TIL_DAGPENGER,
    ARBEIDSSØKERPERIODE,
    MELDEKORT,
    KLAGE,
    FRITEKST,
    ;

    companion object {
        fun String.tilBehandlingstype(): Behandlingstype =
            when (this.uppercase()) {
                "RETT_TIL_DAGPENGER" -> RETT_TIL_DAGPENGER
                "ARBEIDSSØKERPERIODE" -> ARBEIDSSØKERPERIODE
                "MELDEKORT" -> MELDEKORT
                "KLAGE" -> KLAGE
                else -> FRITEKST
            }
    }
}
