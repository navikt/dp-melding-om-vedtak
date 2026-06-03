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
                "ARBEIDSSØKERPERIODE" -> ARBEIDSSØKERPERIODE
                "MELDEKORT" -> MELDEKORT
                "KLAGE" -> KLAGE
                else -> FRITEKST
            }
    }
}
