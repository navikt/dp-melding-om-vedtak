package no.nav.dagpenger.vedtaksmelding.model

import no.nav.dagpenger.vedtaksmelding.Mediator
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk

sealed class VedtaksMelding2(
    protected open val vedtak: VedtakMapper,
    protected open val mediator: Mediator,
) {
    val FASTE_BLOKKER =
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
    override val vedtak: VedtakMapper,
    override val mediator: Mediator,
) : VedtaksMelding2(vedtak, mediator) {
    fun Set<Vilkår>.avslagMinsteinntekt(): Boolean {
        return this.any { it.navn == "Krav til minsteinntekt" && it.status == Vilkår.Status.IKKE_OPPFYLT }
    }
    override val brevBlokkIder: Set<String> =
        setOf("brev.blokk.vedtak-avslag", "brev.blokk.begrunnelse-avslag-minsteinntekt")
    override val isApplicable: Boolean = vedtak.utfall == VedtakMapper.Utfall.AVSLÅTT && vedtak.vilkår.avslagMinsteinntekt()
}


data class Innvilgelse(
    override val vedtak: VedtakMapper,
    override val mediator: Mediator,
) : VedtaksMelding2(vedtak, mediator) {
    override val brevBlokkIder: Set<String> =
        setOf("brev.blokk.vedtak-avslag", "brev.blokk.begrunnelse-avslag-minsteinntekt")
    override val isApplicable: Boolean = vedtak.utfall == VedtakMapper.Utfall.INNVILGET
}


fun main() {
    val vedtak = VedtakMapper("")
    val mediator = Mediator()
    val vedtaksMelding = listOf(AvslagMinsteInntekt(vedtak, mediator),
        Innvilgelse(vedtak, mediator)
    ).single{
        it.isApplicable
    }
    vedtaksMelding.lagHtml()


}






