package no.nav.dagpenger.vedtaksmelding.model.dagpenger

data class Vilkår(
    val navn: String,
    val status: Status,
) {
    enum class Status {
        OPPFYLT,
        IKKE_OPPFYLT,
    }
}
