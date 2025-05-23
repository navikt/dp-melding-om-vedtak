package no.nav.dagpenger.vedtaksmelding.model.klage

enum class KlageBrevBlokker(
    val brevblokkId: String,
) {
    KLAGE_OPPRETTHOLDELSE_DEL_1("brev.blokk.klage-opprettholdelse-del-1"),
    KLAGE_OPPRETTHOLDELSE_DEL_2("brev.blokk.klage-opprettholdelse-del-2"),

    KLAGE_AVVIST_DEL_1("brev.blokk.klage-avvist-del-1"),
    KLAGE_AVVIST_SKRIFTLIG_DEL_2("brev.blokk.klage-avvist-skriftlig-del-2"),
    KLAGE_AVVIST_NEVNER_ENDRING_DEL_3("brev.blokk.klage-avvist-nevner-endring-del-3"),
    KLAGE_AVVIST_SKRIFTLIG_OG_NEVNER_ENDRING_HJEMMEL_DEL_4("brev.blokk.klage-avvist-skriftlig-og-nevne-endring-hjemmel-del-4"),
    KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_DEL_5("brev.blokk.klage-avvist-rettslig-klageinteresse-del-5"),
    KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_HJEMMEL_DEL_6("brev.blokk.klage-avvist-rettslig-klageinteresse-hjemmel-del-6"),
}
