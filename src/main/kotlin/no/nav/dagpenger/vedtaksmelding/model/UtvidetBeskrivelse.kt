package no.nav.dagpenger.vedtaksmelding.model

import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_MINSTEINNTEKT_BEGRUNNELSE
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_ARBEID_NORGE
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_ARBEIDSTIDEN_DIN
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_DAGPENGEPERIODE
import no.nav.dagpenger.vedtaksmelding.model.innvilgelse.InnvilgelseBrevblokker.INNVILGELSE_INNLEDNING
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
                INNVILGELSE_INNLEDNING.brevblokkId to "Nav har innvilget søknaden din om dagpenger",
                AVSLAG_MINSTEINNTEKT_BEGRUNNELSE.brevblokkId to "Begrunnelse krav til inntekt er for lav",
                AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID.brevblokkId to "Villig til å ta arbeid på heltid og deltid",
                INNVILGELSE_ARBEIDSTIDEN_DIN.brevblokkId to "Arbeidstiden din",
                AVSLAG_REELL_ARBEIDSSØKER_ARBEID_NORGE.brevblokkId to "Villig til å ta arbeid hvor som helst i Norge",
                INNVILGELSE_DAGPENGEPERIODE.brevblokkId to "Hvor lenge kan du få dagpenger?",
            )
    }

    fun htmlTekst(): String? {
        return tekst?.replace("\n", "<br/>")
    }
}
