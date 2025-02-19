package no.nav.dagpenger.vedtaksmelding.model.avslag

enum class AvslagVilkårMedBrevstøtte(val vilkårNavn: String) {
    REELL_ARBEIDSSØKER(vilkårNavn = "Krav til arbeidssøker"),
    REELL_ARBEIDSSØKER_MOBILITET(vilkårNavn = "Oppfyller kravet til mobilitet"),
    REELL_ARBEIDSSØKER_ARBEIDSFØR(vilkårNavn = "Oppfyller kravet til å være arbeidsfør"),
    REELL_ARBEIDSSØKER_REGISTRERT_SOM_ARBEIDSSØKER(vilkårNavn = "Registrert som arbeidssøker på søknadstidspunktet"),
    REELL_ARBEIDSSØKER_HELTID_DELTID(vilkårNavn = "Oppfyller kravet til heltid- og deltidsarbeid"),
    REELL_ARBEIDSSØKER_ETHVERT_ARBEID(vilkårNavn = "Oppfyller kravet til å ta ethvert arbeid"),

    IKKE_UTESTENGT(vilkårNavn = "Oppfyller krav til ikke utestengt"),

    OPPHOLD_I_NORGE(vilkårNavn = "Oppfyller kravet til opphold i Norge"),

    IKKE_ANDRE_FULLE_YTELSER(vilkårNavn = "Mottar ikke andre fulle ytelser"),

    MINSTEINNTEKT(vilkårNavn = "Oppfyller kravet til minsteinntekt"),

    TAPT_ARBEIDSTID_ELLER_ARBEIDSINNTEKT(vilkårNavn = "Krav til tap av arbeidsinntekt og arbeidstid"),
    TAPT_ARBEIDSTID(vilkårNavn = "Tap av arbeidstid er minst terskel"),
    TAPT_ARBEIDSINNTEKT(vilkårNavn = "Krav til tap av arbeidsinntekt"),

    IKKE_PÅVIRKET_AV_STREIK_ELLER_LOCKOUT(vilkårNavn = "Er medlemmet ikke påvirket av streik eller lock-out?"),

    IKKE_PASSERT_ALDERSGRENSE(vilkårNavn = "Oppfyller kravet til alder"),

    IKKE_UTDANNING(vilkårNavn = "Krav til utdanning eller opplæring"),
}
