package no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans

enum class StansBrevblokker(
    val brevblokkId: String,
) {
    STANS_INNLEDNING(brevblokkId = "brev.blokk.stans"),
    STANS_INSTITUSJONSOPPJOLD_DEL_1(brevblokkId = "brev.blokk.stans-institusjonsopphold-del-1"),
    STANS_INSTITUSJONSOPPJOLD_DEL_2(brevblokkId = "brev.blokk.stans-institusjonsopphold-del-2"),
}
