package no.nav.dagpenger.vedtaksmelding.model

import java.time.LocalDateTime
import java.util.UUID

data class UtvidetBeskrivelse(
    val behandlingId: UUID,
    val brevblokkId: String,
    val tekst: String?,
    val sistEndretTidspunkt: LocalDateTime? = null,
    val tittel: String = "Ukjent tittel",
)
