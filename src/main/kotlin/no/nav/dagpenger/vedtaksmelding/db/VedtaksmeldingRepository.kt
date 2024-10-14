package no.nav.dagpenger.vedtaksmelding.db

import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import java.util.UUID

interface VedtaksmeldingRepository {
    fun lagre(utvidetBeskrivelse: UtvidetBeskrivelse)

    fun finn(
        behandlingId: UUID,
        brevblokkId: String,
    ): UtvidetBeskrivelse?

    fun hent(
        behandlingId: UUID,
        brevblokkId: String,
    ): UtvidetBeskrivelse
}
