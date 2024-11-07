package no.nav.dagpenger.vedtaksmelding.db

import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import java.time.LocalDateTime
import java.util.UUID

interface VedtaksmeldingRepository {
    fun lagre(utvidetBeskrivelse: UtvidetBeskrivelse): LocalDateTime

    fun finn(
        behandlingId: UUID,
        brevblokkId: String,
    ): UtvidetBeskrivelse?

    fun hent(
        behandlingId: UUID,
        brevblokkId: String,
    ): UtvidetBeskrivelse

    fun hentUtvidedeBeskrivelserFor(behandlingId: UUID): List<UtvidetBeskrivelse>
}
