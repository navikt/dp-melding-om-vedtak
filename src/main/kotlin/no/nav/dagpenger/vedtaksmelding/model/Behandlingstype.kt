package no.nav.dagpenger.vedtaksmelding.model

enum class Behandlingstype {
    SØKNAD,
    ARBEIDSSØKERPERIODE,
    MELDEKORT,
    MANUELL,
    SAMORDNING,
    KLAGE,
    FRITEKST,
    ;

    companion object {
        fun String.tilBehandlingstype(): Behandlingstype =
            when (this.uppercase()) {
                "SØKNAD" -> SØKNAD
                "ARBEIDSSØKERPERIODE" -> ARBEIDSSØKERPERIODE
                "MELDEKORT" -> MELDEKORT
                "MANUELL" -> MANUELL
                "SAMORDNING" -> SAMORDNING
                "KLAGE" -> KLAGE
                else -> FRITEKST
            }
    }
}
