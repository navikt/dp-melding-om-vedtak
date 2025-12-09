package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import no.nav.dagpenger.vedtaksmelding.model.Enhet
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import java.time.LocalDate
import java.util.UUID

sealed class PeriodisertDagpengerOpplysning<E : Enhet, V : Any>(
    perioder: List<Periode<E, V>>,
) : Opplysning {
    init {
        perioder.sorted().also { this.perioder = it }
    }

    val perioder: List<Periode<E, V>>

    data class Periode<E : Enhet, V : Any>(
        val fom: LocalDate,
        val tom: LocalDate?,
        val verdi: V,
        private val enhet: E,
    ) : Comparable<Periode<E, V>> {
        fun formatertVerdi(): String = enhet.formatertVerdi(verdi)

        override fun compareTo(other: Periode<E, V>): Int =
            when {
                this.tom == null && other.tom == null -> 0
                this.tom == null -> -1
                other.tom == null -> 1
                else -> other.tom.compareTo(this.tom)
            }
    }

    class DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel(
        perioder: List<Periode<Enhet.KRONER, Number>>,
    ) : PeriodisertDagpengerOpplysning<Enhet.KRONER, Number>(perioder) {
        companion object {
            val opplysningTypeId: UUID = UUID.fromString("0194881f-9428-74d5-b160-f63a4c61a24f")
        }

        override val opplysningTekstId = "opplysning.dagsats-med-barnetillegg-etter-samordning-og-90-prosent-regel"
        constructor(behandlingResultatData: BehandlingResultatData) : this(
            behandlingResultatData.pengePerioder(
                opplysningTypeId,
            ),
        )
    }
}
