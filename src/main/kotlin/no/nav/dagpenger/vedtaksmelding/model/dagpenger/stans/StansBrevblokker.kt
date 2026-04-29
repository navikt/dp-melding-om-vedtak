package no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans

enum class StansBrevblokker(
    val brevblokkId: String,
) {
    STANS_INNLEDNING("brev.blokk.stans-innledning"),
    STANS_ARBEID_OVER_TERSKEL("brev.blokk.stans-arbeid-over-terskel"),
    STANS_REELL_ARBEIDSSØKER_SVART_NEI_TIL_Å_STÅ_TILMELDT("brev.blokk.stans-reell-arbeidssoker-svart-nei-til-aa-staa-tilmeldt"),
    STANS_REELL_ARBEIDSSØKER_GENERELL_DEL_1("brev.blokk.stans-reell-arbeidssoker-generell-del-1"),
    STANS_REELL_ARBEIDSSØKER_GENERELL_DEL_2("brev.blokk.stans-reell-arbeidssoker-generell-del-2"),
    STANS_IKKE_MELDT_SEG_I_TIDE("brev.blokk.stans-ikke-meldt-seg-i-tide"),
}
