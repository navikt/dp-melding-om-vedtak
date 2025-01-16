package no.nav.dagpenger.vedtaksmelding.model

import java.time.LocalDateTime
import java.util.UUID

data class UtvidetBeskrivelse(
    val behandlingId: UUID,
    val brevblokkId: String,
    val tekst: String?,
    val sistEndretTidspunkt: LocalDateTime? = null,
    val tittel: String = mapping[brevblokkId] ?: "Ukjent tittel",
) {
    companion object {
        val mapping =
            mapOf(
                "brev.blokk.avslag-reell-arbeidssoker-arbeidsfor" to "Arbeidsfør",
                "brev.blokk.vedtak-innvilgelse" to "Nav har innvilget søknaden din om dagpenger",
                "brev.blokk.avslag-reell-arbeidssoker-oppholdstillatelse-heltid-deltid"
                    to "Oppholdstillatelsen din må gi deg rett til å ta arbeid på heltid eller deltid",
                "brev.blokk.begrunnelse-avslag-minsteinntekt" to "Begrunnelse krav til inntekt er for lav",
                "brev.blokk.avslag-reell-arbeidssoker-heltid-deltid" to "Villig til å ta arbeid på heltid og deltid",
                "brev.blokk.arbeidstiden-din" to "Arbeidstiden din",
                "brev.blokk.avslag-reell-arbeidssoker-oppholdstillatelse-alle-jobber"
                    to "Oppholdstillatelsen din må gi deg rett til å ta alle typer arbeid",
                "brev.blokk.avslag-reell-arbeidssoker-arbeid-i-hele-norge"
                    to "Villig til å ta arbeid hvor som helst i Norge",
                "brev.blokk.hvor-lenge-kan-du-faa-dagpenger" to "Hvor lenge kan du få dagpenger?",
            )
    }
}
