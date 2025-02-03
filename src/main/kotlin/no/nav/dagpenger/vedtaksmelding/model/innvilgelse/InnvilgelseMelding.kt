package no.nav.dagpenger.vedtaksmelding.model.innvilgelse

import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak.Utfall.INNVILGET
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk

class InnvilgelseMelding(
    override val vedtak: Vedtak,
    alleBrevblokker: List<BrevBlokk>,
) : VedtakMelding(vedtak) {
    override val harBrevstøtte: Boolean = vedtak.utfall == INNVILGET

    init {
        require(this.harBrevstøtte) {
            throw ManglerBrevstøtte("Innvilgelse for behandling ${this.vedtak.behandlingId} mangler brevstøtte")
        }
    }

    override val brevBlokkIder: List<String>
        get() {
            val innledendeBrevblokker =
                listOf(
                    "brev.blokk.vedtak-innvilgelse",
                    "brev.blokk.begrunnelse-innvilgelsesdato",
                    "brev.blokk.hvor-lenge-kan-du-faa-dagpenger",
                    "brev.blokk.slik-har-vi-beregnet-dagpengene-dine",
                )

            val avsluttendeBrevblokker =
                listOf(
                    "brev.blokk.arbeidstiden-din",
                    "brev.blokk.egenandel",
                    "brev.blokk.du-maa-sende-meldekort",
                    "brev.blokk.utbetaling",
                    "brev.blokk.husk-aa-sjekke-skattekortet-ditt",
                    "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du",
                    "brev.blokk.du-maa-melde-fra-om-endringer",
                    "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger",
                )

            return innledendeBrevblokker + barnetillegg() + nittiProsentRegel() + samordnet() + grunnlag() + avsluttendeBrevblokker
        }
    override val brevBlokker: List<BrevBlokk> =
        run {
            val brevBlokkMap = alleBrevblokker.associateBy { it.textId }
            brevBlokkIder().mapNotNull { id -> brevBlokkMap[id] }
        }

    private fun nittiProsentRegel(): List<String> {
        val id = "opplysning.andel-av-dagsats-med-barnetillegg-som-overstiger-maks-andel-av-dagpengegrunnlaget"
        return vedtak.opplysninger.find {
            it.opplysningTekstId == id &&
                it.verdi.toDouble() > 0
        }
            ?.let {
                listOf("brev.blokk.nittiprosentregel")
            } ?: emptyList()
    }

    private fun samordnet(): List<String> {
        if (!vedtak.opplysninger.any { it.opplysningTekstId == "opplysning.har-samordnet" && it.verdi == "true" }) {
            return emptyList()
        }

        val samordnedeYtelser = mutableSetOf<Pair<String, Double>>()
        vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.sykepenger-dagsats" && it.verdi > "0" }?.let {
            samordnedeYtelser.add("Sykepenger" to it.verdi.toDouble())
        }
        vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.pleiepenger-dagsats" && it.verdi > "0" }?.let {
            samordnedeYtelser.add("Pleiepenger" to it.verdi.toDouble())
        }
        vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.omsorgspenger-dagsats" && it.verdi > "0" }?.let {
            samordnedeYtelser.add("Omsorgspenger" to it.verdi.toDouble())
        }
        vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.opplaeringspenger-dagsats" && it.verdi > "0" }?.let {
            samordnedeYtelser.add("Opplæringspenger" to it.verdi.toDouble())
        }
        vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.ufore-dagsats" && it.verdi > "0" }?.let {
            samordnedeYtelser.add("Uføre" to it.verdi.toDouble())
        }
        vedtak.opplysninger.find { it.opplysningTekstId == "opplysning.foreldrepenger-dagsats" && it.verdi > "0" }?.let {
            samordnedeYtelser.add("Foreldrepenger" to it.verdi.toDouble())
        }
        vedtak.opplysninger.find {
            it.opplysningTekstId == "opplysning.svangerskapspenger-dagsats" && it.verdi > "0"
        }?.let {
            samordnedeYtelser.add("Svangerskapspenger" to it.verdi.toDouble())
        }

        val samordningBlokker = mutableListOf<String>()
        if (samordnedeYtelser.size == 1) {
            when (samordnedeYtelser.first().first) {
                "Sykepenger" -> samordningBlokker.add("brev.blokk.samordnet-med-sykepenger")
                "Pleiepenger" -> samordningBlokker.add("brev.blokk.samordnet-med-pleiepenger")
                "Omsorgspenger" -> samordningBlokker.add("brev.blokk.samordnet-med-omsorgspenger")
                "Opplæringspenger" -> samordningBlokker.add("brev.blokk.samordnet-med-opplaeringspenger")
                "Uføre" -> samordningBlokker.add("brev.blokk.samordnet-med-ufore")
                "Foreldrepenger" -> samordningBlokker.add("brev.blokk.samordnet-med-foreldrepenger")
                "Svangerskapspenger" -> samordningBlokker.add("brev.blokk.samordnet-med-svangerskapspenger")
            }
        } else {
            samordningBlokker.add("brev.blokk.samordnet-generisk")
        }
        return samordningBlokker
    }

    private fun barnetillegg(): List<String> {
        return vedtak.opplysninger.find {
            it.opplysningTekstId == "opplysning.antall-barn-som-gir-rett-til-barnetillegg" && it.verdi.toInt() > 0
        }
            ?.let {
                listOf("brev.blokk.barnetillegg")
            } ?: emptyList()
    }

    private fun grunnlag(): List<String> {
        val grunnlagBlokker = mutableListOf<String>()
        val erInnvilgetMedVerneplikt =
            vedtak.opplysninger.any { it.opplysningTekstId == "opplysning.er-innvilget-med-verneplikt" && it.verdi == "true" }
        val kravTilMinsteinntektErOppfylt =
            vedtak.opplysninger.any { it.opplysningTekstId == "opplysning.krav-til-minsteinntekt" && it.verdi == "true" }

        if (erInnvilgetMedVerneplikt) {
            grunnlagBlokker.add("brev.blokk.grunnlag-for-verneplikt")
            if (kravTilMinsteinntektErOppfylt) {
                grunnlagBlokker.add("brev.blokk.verneplikt-gunstigere-enn-inntekt")
            }
        } else {
            grunnlagBlokker.add("brev.blokk.grunnlag")
        }
        return grunnlagBlokker.toList()
    }
}
