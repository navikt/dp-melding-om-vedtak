package no.nav.dagpenger.vedtaksmelding.model

import no.nav.dagpenger.vedtaksmelding.Mediator
import no.nav.dagpenger.vedtaksmelding.model.Vilkår.Status.IKKE_OPPFYLT

sealed class VedtaksMelding2(
    protected open val vedtak: Vedtak,
    protected open val mediator: Mediator,
) {
    val fasteBlokker =
        listOf(
            "brev.blokk.sporsmaal",
            "brev.blokk.rett-til-innsyn",
            "brev.blokk.rett-til-aa-klage",
        )

    abstract val brevBlokkIder: Set<String>
    abstract val isApplicable: Boolean

    suspend fun doStuff() {
        val opplysningstekstIder = mediator.hentOpplysningTekstIder(brevBlokkIder.toList())
    }
}

data class AvslagMinsteInntekt(
    override val vedtak: Vedtak,
    override val mediator: Mediator,
) : VedtaksMelding2(vedtak, mediator) {
    fun Set<Vilkår>.avslagMinsteinntekt(): Boolean {
        return this.any { it.navn == "Krav til minsteinntekt" && it.status == IKKE_OPPFYLT }
    }

    override val brevBlokkIder: Set<String> =
        setOf("brev.blokk.vedtak-avslag", "brev.blokk.begrunnelse-avslag-minsteinntekt")
    override val isApplicable: Boolean = vedtak.utfall == Utfall.AVSLÅTT && vedtak.vilkår.avslagMinsteinntekt()
}

data class Innvilgelse(
    override val vedtak: Vedtak,
    override val mediator: Mediator,
) : VedtaksMelding2(vedtak, mediator) {
    override val brevBlokkIder: Set<String> =
        setOf("brev.blokk.vedtak-avslag", "brev.blokk.begrunnelse-avslag-minsteinntekt")
    override val isApplicable: Boolean = vedtak.utfall == Utfall.INNVILGET
}
