package no.nav.dagpenger.vedtaksmelding.model

enum class KlageOpplysningTyper(val opplysningNavnId: String, val opplysningTekstId: String) {
    KlageMottattDato(
        opplysningNavnId = "KLAGE_MOTTATT",
        opplysningTekstId = "opplysning.klage-mottatt-dato",
    ),
    PåklagetVedtakDato(
        opplysningNavnId = "KLAGEN_GJELDER_VEDTAKSDATO",
        opplysningTekstId = "opplysning.klage-paaklaget-vedtak-dato",
    ),
    KlageUtfall(
        opplysningNavnId = "UTFALL",
        opplysningTekstId = "opplysning.klage-utfall",
    ),
    ErKlagenSkriftelig(
        opplysningNavnId = "ER_KLAGEN_SKRIFTLIG",
        opplysningTekstId = "opplysning.klage-er-skriftlig",
    ),
    ErKlagenUnderskrevet(
        opplysningNavnId = "ER_KLAGEN_UNDERSKREVET",
        opplysningTekstId = "opplysning.klage-er-underskrevet",
    ),
    KlagenNevnerEndring(
        opplysningNavnId = "KLAGEN_NEVNER_ENDRING",
        opplysningTekstId = "opplysning.klage-nevner-endring",
    ),
    RettsligKlageinteresse(
        opplysningNavnId = "RETTSLIG_KLAGEINTERESSE",
        opplysningTekstId = "opplysning.klage-klageinteresse-er-rettslig",
    ),
    KlagefristOppfylt(
        opplysningNavnId = "KLAGEFRIST_OPPFYLT",
        opplysningTekstId = "opplysning.klage-klagefrist-oppfylt",
    ),
}
