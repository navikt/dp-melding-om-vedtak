package no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans

enum class StansBrevblokker(
    val brevblokkId: String,
) {
    STANS_INNLEDNING("brev.blokk.stans-innledning"),
    STANS_ARBEID_OVER_TERSKEL("brev.blokk.stans-arbeid-over-terskel"),
    STANS_REELL_ARBEIDSSØKER_SVART_NEI_TIL_Å_STÅ_TILMELDT("brev.blokk.stans-reell-arbeidssoker-svart-nei-til-aa-staa-tilmeldt"),
    STANS_REELL_ARBEIDSSØKER_GENERELL("brev.blokk.stans-reell-arbeidssoker-generell"),
    STANS_IKKE_MELDT_SEG_I_TIDE("brev.blokk.stans-ikke-meldt-seg-i-tide"),
    STANS_ALDER("brev.blokk.stans-alder"),
    STANS_TRENGER_DU_FORTSATT_DAGPENGER("brev.blokk.trenger-du-fortsatt-dagpenger"),
}
