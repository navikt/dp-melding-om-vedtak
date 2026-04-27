package no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans

enum class StansBrevblokker(
    val brevblokkId: String,
) {
    STANS_INNLEDNING("brev.blokk.stans-innledning"),
    STANS_ARBEID_OVER_TERSKEL("brev.blokk.stans-arbeid-over-terskel"),
    STANS_SVART_NEI_TIL_Å_STÅ_TILMELDT("brev.blokk.stans-svart-nei-til-aa-staa-tilmeldt"),
    STANS_IKKE_MELDT_SEG_I_TIDE("brev.blokk.stans-ikke-meldt-seg-i-tide"),
}
