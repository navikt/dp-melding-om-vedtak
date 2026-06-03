package no.nav.dagpenger.vedtaksmelding.model

enum class Behandlingstype {
    SØKNAD,
    ARBEIDSSØKERPERIODE,
    MELDEKORT,
    KLAGE,
    FRITEKST,
    ;

    companion object {
        fun String.tilBehandlingstype(): Behandlingstype =
            when (this.uppercase()) {
                "SØKNAD" -> SØKNAD
                "RETT_TIL_DAGPENGER" -> SØKNAD
                "ARBEIDSSØKERPERIODE" -> ARBEIDSSØKERPERIODE
                "MELDEKORT" -> MELDEKORT
                "KLAGE" -> KLAGE
                else -> FRITEKST
            }
    }
}
