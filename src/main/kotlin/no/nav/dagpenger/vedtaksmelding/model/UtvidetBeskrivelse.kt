package no.nav.dagpenger.vedtaksmelding.model

import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ARBEID_I_HELE_NORGE
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.BEGRUNNELSE_AVSLAG_MINSTEINNTEKT
import java.time.LocalDateTime
import java.util.UUID

data class UtvidetBeskrivelse(
    val behandlingId: UUID,
    val brevblokkId: String,
    val tekst: String?,
    val sistEndretTidspunkt: LocalDateTime? = null,
    val tittel: String = titler[brevblokkId] ?: "Ukjent tittel",
) {
    companion object {
        val titler =
            mapOf(
                AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR.brevblokkId to "Arbeidsfør",
                "brev.blokk.vedtak-innvilgelse" to "Nav har innvilget søknaden din om dagpenger",
                "brev.blokk.avslag-reell-arbeidssoker-oppholdstillatelse-heltid-deltid"
                    to "Oppholdstillatelsen din må gi deg rett til å ta arbeid på heltid eller deltid",
                BEGRUNNELSE_AVSLAG_MINSTEINNTEKT.brevblokkId to "Begrunnelse krav til inntekt er for lav",
                AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId to "Villig til å ta arbeid på heltid og deltid",
                "brev.blokk.arbeidstiden-din" to "Arbeidstiden din",
                "brev.blokk.avslag-reell-arbeidssoker-oppholdstillatelse-alle-jobber"
                    to "Oppholdstillatelsen din må gi deg rett til å ta alle typer arbeid",
                AVSLAG_REELL_ARBEIDSSØKER_ARBEID_I_HELE_NORGE.brevblokkId
                    to "Villig til å ta arbeid hvor som helst i Norge",
                "brev.blokk.hvor-lenge-kan-du-faa-dagpenger" to "Hvor lenge kan du få dagpenger?",
            )
    }
}
