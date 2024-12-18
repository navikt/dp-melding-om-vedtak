package no.nav.dagpenger.vedtaksmelding.model

import no.nav.dagpenger.vedtaksmelding.Mediator

sealed class VedtaksMelding2(
    protected open val vedtakMapper: VedtakMapper,
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
    override val vedtakMapper: VedtakMapper,
    override val mediator: Mediator,
) : VedtaksMelding2(vedtakMapper, mediator) {
    fun Set<Vilkår>.avslagMinsteinntekt(): Boolean {
        return this.any { it.navn == "Krav til minsteinntekt" && it.status == Vilkår.Status.IKKE_OPPFYLT }
    }

    override val brevBlokkIder: Set<String> =
        setOf("brev.blokk.vedtak-avslag", "brev.blokk.begrunnelse-avslag-minsteinntekt")
    override val isApplicable: Boolean = vedtakMapper.utfall == VedtakMapper.Utfall.AVSLÅTT && vedtakMapper.vilkår.avslagMinsteinntekt()
}

data class Innvilgelse(
    override val vedtakMapper: VedtakMapper,
    override val mediator: Mediator,
) : VedtaksMelding2(vedtakMapper, mediator) {
    override val brevBlokkIder: Set<String> =
        setOf("brev.blokk.vedtak-avslag", "brev.blokk.begrunnelse-avslag-minsteinntekt")
    override val isApplicable: Boolean = vedtakMapper.utfall == VedtakMapper.Utfall.INNVILGET
}
