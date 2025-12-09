package no.nav.dagpenger.vedtaksmelding.model.klage

import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.Enhet
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import java.time.LocalDate

sealed class KlageOpplysning<E : Enhet, V : Any>(
    open val verdi: V,
) : Opplysning {
    companion object {
        val logger = KotlinLogging.logger {}
    }

    protected abstract val enhet: E

    fun formatertVerdi(): String = enhet.formatertVerdi(verdi)

    data class KlageMottattDato(
        override val verdi: LocalDate,
    ) : KlageOpplysning<Enhet.ENHETSLØS, LocalDate>(verdi) {
        companion object {
            val opplysningNavnId: String = "KLAGE_MOTTATT"
        }

        override val opplysningTekstId = "opplysning.klage-mottatt-dato"
        override val enhet = Enhet.ENHETSLØS
    }

    data class PåklagetVedtakDato(
        override val verdi: LocalDate,
    ) : KlageOpplysning<Enhet.ENHETSLØS, LocalDate>(verdi) {
        companion object {
            val opplysningNavnId: String = "KLAGEN_GJELDER_VEDTAKSDATO"
        }

        override val opplysningTekstId = "opplysning.klage-paaklaget-vedtak-dato"
        override val enhet = Enhet.ENHETSLØS
    }

    data class KlageUtfall(
        override val verdi: String,
    ) : KlageOpplysning<Enhet.ENHETSLØS, String>(verdi) {
        companion object {
            val opplysningNavnId: String = "UTFALL"
        }

        override val opplysningTekstId = "opplysning.klage-utfall"
        override val enhet = Enhet.ENHETSLØS
    }

    data class ErKlagenSkriftelig(
        override val verdi: Boolean,
    ) : KlageOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningNavnId: String = "ER_KLAGEN_SKRIFTLIG"
        }

        override val opplysningTekstId = "opplysning.klage-er-skriftlig"
        override val enhet = Enhet.ENHETSLØS
    }

    data class ErKlagenUnderskrevet(
        override val verdi: Boolean,
    ) : KlageOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningNavnId: String = "ER_KLAGEN_UNDERSKREVET"
        }

        override val opplysningTekstId = "opplysning.klage-er-underskrevet"
        override val enhet = Enhet.ENHETSLØS
    }

    data class KlagenNevnerEndring(
        override val verdi: Boolean,
    ) : KlageOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningNavnId: String = "KLAGEN_NEVNER_ENDRING"
        }

        override val opplysningTekstId = "opplysning.klage-nevner-endring"
        override val enhet = Enhet.ENHETSLØS
    }

    data class RettsligKlageinteresse(
        override val verdi: Boolean,
    ) : KlageOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningNavnId: String = "RETTSLIG_KLAGEINTERESSE"
        }

        override val opplysningTekstId: String = "opplysning.klage-klageinteresse-er-rettslig"
        override val enhet: Enhet.ENHETSLØS = Enhet.ENHETSLØS
    }

    data class KlagefristOppfylt(
        override val verdi: Boolean,
    ) : KlageOpplysning<Enhet.ENHETSLØS, Boolean>(verdi) {
        companion object {
            val opplysningNavnId: String = "KLAGEFRIST_OPPFYLT"
        }

        override val opplysningTekstId = "opplysning.klage-klagefrist-oppfylt"
        override val enhet = Enhet.ENHETSLØS
    }
}
