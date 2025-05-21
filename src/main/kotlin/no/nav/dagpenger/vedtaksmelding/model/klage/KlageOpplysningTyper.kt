package no.nav.dagpenger.vedtaksmelding.model.klage

enum class KlageOpplysningTyper(
    val opplysningNavnId: String,
    val opplysningTekstId: String,
) {
    KlageMottatDato(
        opplysningNavnId = "KLAGE_MOTTATT",
        opplysningTekstId = "opplysning.klage-mottatt-dato",
    ),
    KlageUtfall(
        opplysningNavnId = "UTFALL",
        opplysningTekstId = "opplysning.klage-utfall",
    ),
    ErKlagenSkriftelig(
        opplysningNavnId = "ER_KLAGEN_SKRIFTLIG",
        opplysningTekstId = "opplysning.er-klagen-skriftlig",
    ),
    ErKlagenUnderskrevet(
        opplysningNavnId = "ER_KLAGEN_UNDERSKREVET",
        opplysningTekstId = "opplysning.er-klagen-underskrevet",
    ),
    KlagenNevnerEndring(
        opplysningNavnId = "KLAGEN_NEVNER_ENDRING",
        opplysningTekstId = "opplysning.klagen-nevner-endring",
    ),
    RettsligKlageinteresse(
        opplysningNavnId = "RETTSLIG_KLAGEINTERESSE",
        opplysningTekstId = "opplysning.rettsleg-klageinteresse",
    ),
}
