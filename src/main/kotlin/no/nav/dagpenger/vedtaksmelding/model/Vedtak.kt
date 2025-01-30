package no.nav.dagpenger.vedtaksmelding.model

import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.DATO
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Datatype.TEKST
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.ENHETSLØS
import no.nav.dagpenger.vedtaksmelding.model.Opplysning.Enhet.KRONER
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

private val sikkerlogg = KotlinLogging.logger("tjenestekall")

data class Vedtak(
    val behandlingId: UUID,
    val vilkår: Set<Vilkår> = emptySet(),
    val utfall: Utfall,
    val opplysninger: Set<Opplysning> = emptySet(),
) {
    fun finnOpplysning(opplysningTekstId: String): Opplysning? {
        val opplysning = this.opplysninger.singleOrNull { it.opplysningTekstId == opplysningTekstId }
        return opplysning
    }

    fun hentOpplysning(opplysningTekstId: String): Opplysning {
        sikkerlogg.info { "Finn opplysning for behandlingId $behandlingId, opplysning $opplysningTekstId" }
        val opplysning =
            finnOpplysning(opplysningTekstId)
                ?: throw OpplysningIkkeFunnet("Opplysning med tekstId $opplysningTekstId mangler for behandlingId $behandlingId")
        sikkerlogg.info { "Opplysning for behandlingId $behandlingId, $opplysningTekstId: $opplysning" }
        return opplysning
    }
}

class OpplysningIkkeFunnet(message: String) : RuntimeException(message)

data class Vilkår(
    val navn: String,
    val status: Status,
) {
    enum class Status {
        OPPFYLT,
        IKKE_OPPFYLT,
    }
}

enum class AvslagVilkårMedBrevstøtte(val navn: String) {
    REELL_ARBEIDSSØKER(navn = "Krav til arbeidssøker"),
    REELL_ARBEIDSSØKER_MOBILITET(navn = "Oppfyller kravet til mobilitet"),
    REELL_ARBEIDSSØKER_ARBEIDSFØR(navn = "Oppfyller kravet til å være arbeidsfør"),
    REELL_ARBEIDSSØKER_REGISTRERT_SOM_ARBEIDSSØKER(navn = "Registrert som arbeidssøker på søknadstidspunktet"),
    REELL_ARBEIDSSØKER_HELTID_DELTID(navn = "Oppfyller kravet til heltid- og deltidsarbeid"),
    REELL_ARBEIDSSØKER_ETHVERT_ARBEID(navn = "Oppfyller kravet til å ta ethvert arbeid"),

    IKKE_UTESTENGT(navn = "Oppfyller krav til ikke utestengt"),

    OPPHOLD_I_NORGE(navn = "Oppfyller kravet til opphold i Norge"),

    IKKE_ANDRE_FULLE_YTELSER(navn = "Mottar ikke andre fulle ytelser"),

    MINSTEINNTEKT_ELLER_VERNEPLIKT(navn = "Oppfyller kravet til minsteinntekt eller verneplikt"),

    TAPT_ARBEIDSTID(navn = "Tap av arbeidstid er minst terskel"),
}

enum class Utfall {
    INNVILGET,
    AVSLÅTT,
}

data class Opplysning(
    val opplysningTekstId: String,
    val verdi: String,
    val datatype: Datatype,
    val enhet: Enhet = ENHETSLØS,
) {
    companion object {
        val NULL_OPPLYSNING =
            Opplysning(
                opplysningTekstId = "ukjent.opplysning",
                verdi = "ukjent",
                datatype = TEKST,
                enhet = ENHETSLØS,
            )
    }

    fun formatering(): String {
        return when (this.datatype) {
            DATO -> formatDate(this.verdi)
            else -> this.verdi
        }
    }

    fun enhet(): String {
        return when (this.enhet) {
            KRONER -> " kroner"
            else -> ""
        }
    }

    fun verdiMedEnhet(): String {
        return "${this.formatering()}${this.enhet()}"
    }

    fun formatDate(dateString: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("dd. MMMM yyyy", Locale("no"))
        val date = LocalDate.parse(dateString, inputFormatter)
        return date.format(outputFormatter)
    }

    enum class Datatype {
        TEKST,
        HELTALL,
        FLYTTALL,
        DATO,
        BOOLSK,
    }

    enum class Enhet {
        KRONER,
        DAGER,
        ENHETSLØS,
        UKER,
        BARN,
        TIMER,
    }
}
