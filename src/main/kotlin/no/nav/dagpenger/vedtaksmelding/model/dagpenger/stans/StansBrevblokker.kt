package no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans

enum class StansBrevblokker(
    val brevblokkId: String,
) {
    STANS_OVERSKRIFT(brevblokkId = "brev.blokk.stans.overskrift"),
    STANS_VIRKNINGSDATO(brevblokkId = "brev.blokk.stans.virkningsdato"),
    STANS_INSTITUSJONSOPPJOLD_DEL_1(brevblokkId = "brev.blokk.stans-institusjonsopphold-del-1"),
    STANS_INSTITUSJONSOPPJOLD_DEL_2(brevblokkId = "brev.blokk.stans-institusjonsopphold-del-2"),
}
