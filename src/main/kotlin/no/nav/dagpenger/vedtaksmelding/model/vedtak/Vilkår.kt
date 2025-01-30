package no.nav.dagpenger.vedtaksmelding.model.vedtak

data class Vilkår(
    val navn: String,
    val status: Status,
) {
    enum class Status {
        OPPFYLT,
        IKKE_OPPFYLT,
    }
}
