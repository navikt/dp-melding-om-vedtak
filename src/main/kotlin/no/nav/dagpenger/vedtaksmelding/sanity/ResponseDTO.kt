package no.nav.dagpenger.vedtaksmelding.sanity

data class ResponseDTO(val result: List<BrevBlokkDTO>)

data class BrevBlokkDTO(
    val textId: String,
    val innhold: InnholdDTO,
) {
    data class InnholdDTO(
        val behandlingOpplysninger: List<BehandlingOpplysningDTO>,
    )
}

data class BehandlingOpplysningDTO(
    val textId: String,
    val type: String?,
)
