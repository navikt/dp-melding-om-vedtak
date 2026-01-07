package no.nav.dagpenger.vedtaksmelding.model.klage

enum class KlageBrevBlokker(
    val brevblokkId: String,
) {
    KLAGE_OPPRETTHOLDELSE_DEL_1("brev.blokk.klage-opprettholdelse-del-1"),
    KLAGE_OPPRETTHOLDELSE_DEL_2("brev.blokk.klage-opprettholdelse-del-2"),
    KLAGE_OPPRETTHOLDELSE_DEL_3("brev.blokk.klage-opprettholdelse-del-3"),

    KLAGE_AVVIST_INTRO("brev.blokk.klage-avvist-intro"),
    KLAGE_AVVIST_SKRIFTLIG_OG_SIGNERT("brev.blokk.klage-avvist-skriftlig-og-signert"),
    KLAGE_AVVIST_NEVNER_ENDRING("brev.blokk.klage-avvist-nevner-endring"),
    KLAGE_AVVIST_SKRIFTLIG_OG_NEVNER_ENDRING_HJEMMEL("brev.blokk.klage-avvist-skriftlig-og-nevne-endring-hjemmel"),
    KLAGE_AVVIST_KLAGEFIRST("brev.blokk.klage-avvist-klagefrist"),
    KLAGE_AVVIST_KLAGEFIRST_HJEMMEL("brev.blokk.klage-avvist-klagefrist-hjemmel"),
    KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE("brev.blokk.klage-avvist-rettslig-klageinteresse"),
    KLAGE_AVVIST_RETTSLIG_KLAGEINTERESSE_HJEMMEL("brev.blokk.klage-avvist-rettslig-klageinteresse-hjemmel"),
}
