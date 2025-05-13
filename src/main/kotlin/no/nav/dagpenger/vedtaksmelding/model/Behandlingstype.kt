package no.nav.dagpenger.vedtaksmelding.model

enum class Behandlingstype {
    RETT_TIL_DAGPENGER,
    KLAGE,
    ;

    companion object {
        fun String?.tilBehandlingstype(): Behandlingstype {
            return try {
                Behandlingstype.valueOf(this?.uppercase() ?: "")
            } catch (e: Throwable) {
                RETT_TIL_DAGPENGER
            }
        }
    }
}
