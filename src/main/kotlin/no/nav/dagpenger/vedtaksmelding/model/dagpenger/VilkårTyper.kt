package no.nav.dagpenger.vedtaksmelding.model.dagpenger

enum class VilkårTyper(
    val vilkårNavn: String,
) {
    REELL_ARBEIDSSØKER(vilkårNavn = "Krav til arbeidssøker"), // Toppnivå-vilkår § 4-5
    REELL_ARBEIDSSØKER_MOBILITET(vilkårNavn = "Oppfyller kravet til mobilitet"), // Delvilkår
    REELL_ARBEIDSSØKER_ARBEIDSFØR(vilkårNavn = "Oppfyller kravet til å være arbeidsfør"), // Delvilkår
    REELL_ARBEIDSSØKER_HELTID_DELTID(vilkårNavn = "Oppfyller kravet til heltid- og deltidsarbeid"), // Delvilkår
    REELL_ARBEIDSSØKER_ETHVERT_ARBEID(vilkårNavn = "Oppfyller kravet til å ta ethvert arbeid"), // Delvilkår

    // Skal behandles som et eget selvstendig vilkår, selv om dette også gjelder § 4-5
    REELL_ARBEIDSSØKER_REGISTRERT_SOM_ARBEIDSSØKER(vilkårNavn = "Registrert som arbeidssøker på søknadstidspunktet"),

    IKKE_UTESTENGT(vilkårNavn = "Oppfyller krav til ikke utestengt"),

    OPPHOLD_I_NORGE(vilkårNavn = "Oppfyller kravet til opphold i Norge eller unntak"),

    IKKE_ANDRE_FULLE_YTELSER(vilkårNavn = "Mottar ikke andre fulle ytelser"),

    MINSTEINNTEKT(vilkårNavn = "Oppfyller kravet til minsteinntekt"),

    TAPT_ARBEIDSTID_ELLER_ARBEIDSINNTEKT(vilkårNavn = "Krav til tap av arbeidsinntekt og arbeidstid"), // Toppnivå-vilkår
    TAPT_ARBEIDSTID(vilkårNavn = "Tap av arbeidstid er minst terskel"), // Delvilkår
    TAPT_ARBEIDSINNTEKT(vilkårNavn = "Krav til tap av arbeidsinntekt"), // Delvilkår

    IKKE_PÅVIRKET_AV_STREIK_ELLER_LOCKOUT(vilkårNavn = "Er medlemmet ikke påvirket av streik eller lock-out?"),

    IKKE_PASSERT_ALDERSGRENSE(vilkårNavn = "Oppfyller kravet til alder"),

    IKKE_UTDANNING(vilkårNavn = "Krav til utdanning eller opplæring"),

    MEDLEMSKAP(vilkårNavn = "Oppfyller kravet til medlemskap"),

    LOVVALG(vilkårNavn = "Er omfattet av trygdelovgivningen i Norge"),

    PERMITTERING(vilkårNavn = "Oppfyller kravet til permittering"),

    PERMITTERING_FISK(vilkårNavn = "Oppfyller kravet til permittering i fiskeindustrien"),
}
