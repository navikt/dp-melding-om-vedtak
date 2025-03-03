package no.nav.dagpenger.vedtaksmelding.model.avslag

enum class AvslagBrevblokker(val brevblokkId: String) {
    AVSLAG_INNLEDNING(brevblokkId = "brev.blokk.vedtak-avslag"),
    AVSLAG_INNLEDNING_PERMITTERT(brevblokkId = "brev.blokk.vedtak-avslag-permittert"),

    AVSLAG_MINSTEINNTEKT(brevblokkId = "brev.blokk.begrunnelse-avslag-minsteinntekt"),

    AVSLAG_OPPHOLD_UTLAND_DEL_1(brevblokkId = "brev.blokk.avslag-opphold-utlandet-del-1"),
    AVSLAG_OPPHOLD_UTLAND_DEL_2(brevblokkId = "brev.blokk.avslag-opphold-utlandet-del-2"),

    AVSLAG_ANDRE_FULLE_YTELSER(brevblokkId = "brev.blokk.avslag-andre-fulle-ytelser"),

    AVSLAG_STREIK_LOCKOUT_DEL_1(brevblokkId = "brev.blokk.avslag-streik-lockout-del-1"),
    AVSLAG_STREIK_LOCKOUT_DEL_2(brevblokkId = "brev.blokk.avslag-streik-lockout-del-2"),

    AVSLAG_TAPT_ARBEIDSTID_DEL_1(brevblokkId = "brev.blokk.avslag-tapt-arbeidstid-del-1"),
    AVSLAG_TAPT_ARBEIDSTID_DEL_2(brevblokkId = "brev.blokk.avslag-tapt-arbeidstid-del-2"),
    AVSLAG_TAPT_ARBEIDSTID_FASTSATT_VANLIG_ARBEDSTID_0(brevblokkId = "brev.blokk.avslag-tapt-arbeidstid-fastsatt-vanlig-arbeidstid-0"),
    AVSLAG_TAPT_ARBEIDSTID_PERMITTERT_FISK(brevblokkId = "brev.blokk.avslag-tapt-arbeidstid-permittert-fisk"),

    AVSLAG_TAPT_ARBEIDSINNTEKT(brevblokkId = "brev.blokk.avslag-tapt-arbeidsinntekt"),

    AVSLAG_UTESTENGT(brevblokkId = "brev.blokk.avslag-utestengt"),
    AVSLAG_UTESTENGT_HJEMMEL(brevblokkId = "brev.blokk.avslag-utestengt-hjemmel"),

    AVSLAG_REELL_ARBEIDSSØKER_OVERSKRIFT(brevblokkId = "brev.blokk.avslag-reell-arbeidssoker-overskrift"),
    AVSLAG_REELL_ARBEIDSSØKER_HELTID_DELTID(brevblokkId = "brev.blokk.avslag-reell-arbeidssoker-heltid-deltid"),
    AVSLAG_REELL_ARBEIDSSØKER_HELE_NORGE(brevblokkId = "brev.blokk.avslag-reell-arbeidssoker-arbeid-i-hele-norge"),
    AVSLAG_REELL_ARBEIDSSØKER_UNNTAK_HELTID_DELTID_HELE_NORGE(
        brevblokkId = "brev.blokk.avslag-reell-arbeidssoker-unntak-heltid-deltid-hele-norge",
    ),
    AVSLAG_REELL_ARBEIDSSØKER_ARBEIDSFØR(brevblokkId = "brev.blokk.avslag-reell-arbeidssoker-arbeidsfor"),
    AVSLAG_REELL_ARBEIDSSØKER_ETHVERT_ARBEID(brevblokkId = "brev.blokk.avslag-reell-arbeidssoker-ethvert-arbeid"),
    AVSLAG_REELL_ARBEIDSSØKER_REGISTRERT_ARBEIDSSOKER(brevblokkId = "brev.blokk.avslag-reell-arbeidssoker-registrert-arbeidssoker"),
    AVSLAG_REELL_ARBEIDSSØKER_HJEMMEL(brevblokkId = "brev.blokk.avslag-reell-arbeidssoker-hjemmel"),

    AVSLAG_ALDER(brevblokkId = "brev.blokk.avslag-alder"),

    AVSLAG_UTDANNING(brevblokkId = "brev.blokk.avslag-utdanning"),

    AVSLAG_MEDLEMSKAP_DEL_1(brevblokkId = "brev.blokk.avslag-medlemskap-del-1"),
    AVSLAG_MEDLEMSKAP_DEL_2(brevblokkId = "brev.blokk.avslag-medlemskap-del-2"),

    AVSLAG_PERMITTERT_DEL_1(brevblokkId = "brev.blokk.avslag-permittert-del-1"),
    AVSLAG_PERMITTERT_DEL_2(brevblokkId = "brev.blokk.avslag-permittert-del-2"),
}
