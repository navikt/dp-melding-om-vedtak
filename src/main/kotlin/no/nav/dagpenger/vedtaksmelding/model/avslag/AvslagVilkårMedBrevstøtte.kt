package no.nav.dagpenger.vedtaksmelding.model.avslag

enum class AvslagVilkårMedBrevstøtte(val navn: String) {
    REELL_ARBEIDSSØKER(navn = "Krav til arbeidssøker"),
    REELL_ARBEIDSSØKER_MOBILITET(navn = "Oppfyller kravet til mobilitet"),
    REELL_ARBEIDSSØKER_ARBEIDSFØR(navn = "Oppfyller kravet til å være arbeidsfør"),
    REELL_ARBEIDSSØKER_REGISTRERT_SOM_ARBEIDSSØKER(navn = "Registrert som arbeidssøker på søknadstidspunktet"),
    REELL_ARBEIDSSØKER_HELTID_DELTID(navn = "Oppfyller kravet til heltid- og deltidsarbeid"),
    REELL_ARBEIDSSØKER_ETHVERT_ARBEID(navn = "Oppfyller kravet til å ta ethvert arbeid"),

    IKKE_UTESTENGT(navn = "Oppfyller krav til ikke utestengt"),

    OPPHOLD_I_NORGE(navn = "Oppfyller kravet til opphold i Norge"),

    IKKE_ANDRE_FULLE_YTELSER(navn = "Mottar ikke andre fulle ytelser"),

    MINSTEINNTEKT_ELLER_VERNEPLIKT(navn = "Oppfyller kravet til minsteinntekt eller verneplikt"),

    TAPT_ARBEIDSTID(navn = "Tap av arbeidstid er minst terskel"),

    TAPT_ARBEIDSINNTEKT(navn = "Krav til tap av arbeidsinntekt"),

    MEDLEM_PÅVIRKET_AV_STREIK_ELLER_LOCKOUT(navn = "Er medlemmet ikke påvirket av streik eller lock-out?"),
}
