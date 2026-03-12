package no.nav.dagpenger.vedtaksmelding.model.dagpenger.stans

enum class StansBrevblokker(
    val brevblokkId: String,
) {
    STANS_INNLEDNING("brev.blokk.stans-innledning"),
    STANS_OPPHOLD_UTLANDET_DEL_1("brev.blokk.stans-opphold-utlandet-del-1"),
    STANS_OPPHOLD_UTLANDET_DEL_2("brev.blokk.stans-opphold-utlandet-del-2"),
    STANS_IKKE_REGISTRERT_ARBEIDSSØKER_DEL_1("brev.blokk.stans-ikke-registrert-arbeidssoker-del-1"),
    STANS_IKKE_REGISTRERT_ARBEIDSSØKER_DEL_2("brev.blokk.stans-ikke-registrert-arbeidssoker-del-2"),
    STANS_ANDRE_FULLE_YTELSER_DEL_1("brev.blokk.stans-andre-fulle-ytelser-del-1"),
    STANS_ANDRE_FULLE_YTELSER_DEL_2("brev.blokk.stans-andre-fulle-ytelser-del-2"),
    STANS_INSTITUSJONSOPPHOLD_DEL_1("brev.blokk.stans-institusjonsopphold-del-1"),
    STANS_INSTITUSJONSOPPHOLD_DEL_2("brev.blokk.stans-institusjonsopphold-del-2"),
    STANS_DØDSFALL_DEL_1("brev.blokk.stans-doedsfall-del-1"),
    STANS_DØDSFALL_DEL_2("brev.blokk.stans-doedsfall-del-2"),
}
