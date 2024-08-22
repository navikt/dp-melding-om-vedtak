package no.nav.dagpenger.vedtaksmelding.model

class Behandling(
    val id: String,
    val tilstand: String,
    val opplysninger: Set<Opplysning>,
)
