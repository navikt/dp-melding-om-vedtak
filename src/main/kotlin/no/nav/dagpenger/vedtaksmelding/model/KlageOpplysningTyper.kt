package no.nav.dagpenger.vedtaksmelding.model

enum class KlageOpplysningTyper(val opplysningNavnId: String, val opplysningTekstId: String) {
    KlageMottatDato(
        opplysningNavnId = "KLAGE_MOTTATT",
        opplysningTekstId = "opplysning.klage-mottatt-dato",
    ),
}
