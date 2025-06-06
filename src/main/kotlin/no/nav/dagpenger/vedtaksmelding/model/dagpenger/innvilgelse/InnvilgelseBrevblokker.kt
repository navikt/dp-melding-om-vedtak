package no.nav.dagpenger.vedtaksmelding.model.dagpenger.innvilgelse

enum class InnvilgelseBrevblokker(
    val brevblokkId: String,
) {
    INNVILGELSE_ORDINÆR(brevblokkId = "brev.blokk.innvilgelse-ordinaer"),
    INNVILGELSE_MED_EGENANDEL(brevblokkId = "brev.blokk.innvilgelse-med-egenandel"),
    INNVILGELSE_UTEN_EGENANDEL(brevblokkId = "brev.blokk.innvilgelse-uten-egenandel"),
    INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE(brevblokkId = "brev.blokk.begrunnelse-innvilgelsesdato"),
    INNVILGELSE_DAGPENGEPERIODE(brevblokkId = "brev.blokk.hvor-lenge-kan-du-faa-dagpenger"),
    INNVILGELSE_DAGPENGEPERIODE_VERNEPLIKT(brevblokkId = "brev.blokk.hvor-lenge-kan-du-faa-dagpenger-verneplikt"),
    INNVILGELSE_SLIK_HAR_VI_BEREGNET_DAGPENGENE_DINE(brevblokkId = "brev.blokk.slik-har-vi-beregnet-dagpengene-dine"),
    INNVILGELSE_ARBEIDSTIDEN_DIN(brevblokkId = "brev.blokk.arbeidstiden-din"),
    INNVILGELSE_ARBEIDSTIDEN_DIN_VERNEPLIKT(brevblokkId = "brev.blokk.arbeidstiden-din-verneplikt"),
    INNVILGELSE_EGENANDEL(brevblokkId = "brev.blokk.egenandel"),
    INNVILGELSE_GRUNNLAG(brevblokkId = "brev.blokk.grunnlag"),
    INNVILGELSE_GRUNNLAG_VERNEPLIKT(brevblokkId = "brev.blokk.grunnlag-for-verneplikt"),
    INNVILGELSE_BARNETILLEGG(brevblokkId = "brev.blokk.barnetillegg"),
    INNVILGELSE_SAMORDNET_GENERISK(brevblokkId = "brev.blokk.samordnet-generisk"),
    INNVILGELSE_SAMORDNET_SYKEPENGER(brevblokkId = "brev.blokk.samordnet-med-sykepenger"),
    INNVILGELSE_SAMORDNET_PLEIEPENGER(brevblokkId = "brev.blokk.samordnet-med-pleiepenger"),
    INNVILGELSE_SAMORDNET_OMSORGSPENGER("brev.blokk.samordnet-med-omsorgspenger"),
    INNVILGELSE_SAMORDNET_OPPLÆRINGSPENGER("brev.blokk.samordnet-med-opplaeringpenger"),
    INNVILGELSE_SAMORDNET_UFØRE("brev.blokk.samordnet-med-ufore"),
    INNVILGELSE_SAMORDNET_FORELDREPENGER(brevblokkId = "brev.blokk.samordnet-med-foreldrepenger"),
    INNVILGELSE_SAMORDNET_SVANGERSKAPSPENGER("brev.blokk.samordnet-med-svangerskapspenger"),
    INNVILGELSE_NITTI_PROSENT_REGEL(brevblokkId = "brev.blokk.nitti-prosent-regel"),
    INNVILGELSE_VERNEPLIKT_GUNSTIGEST("brev.blokk.verneplikt-gunstigere-enn-inntekt"),
    INNVILGELSE_MELDEKORT(brevblokkId = "brev.blokk.du-maa-sende-meldekort"),
    INNVILGELSE_UTBETALING(brevblokkId = "brev.blokk.utbetaling"),
    INNVILGELSE_SKATTEKORT(brevblokkId = "brev.blokk.husk-aa-sjekke-skattekortet-ditt"),
    INNVILGELSE_STANS_ÅRSAKER(brevblokkId = "brev.blokk.vi-stanser-dagpengene-dine-automatisk-naar-du"),
    INNVILGELSE_MELD_FRA_OM_ENDRINGER(brevblokkId = "brev.blokk.du-maa-melde-fra-om-endringer"),
    INNVILGELSE_KONSEKVENSER_FEILOPPLYSNING(brevblokkId = "brev.blokk.konsekvenser-av-aa-gi-uriktige-eller-mangelfulle-opplysninger"),

    INNVILGELSE_PERMITTERT(brevblokkId = "brev.blokk.innvilgelse-permittert"),
    INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT(brevblokkId = "brev.blokk.begrunnelse-innvilgelsesdato-permittert"),
    INNVILGELSE_DAGPENGEPERIODE_PERMITTERT(brevblokkId = "brev.blokk.hvor-lenge-kan-du-faa-dagpenger-permittert"),
    INNVILGELSE_ARBEIDSFORHOLD_AVSLUTT_PERMITTERT(brevblokkId = "brev.blokk.arbeidsforhold-avslutt-permittert"),
    INNVILGELSE_HVA_SKJER_ETTER_PERMITTERINGEN(brevblokkId = "brev.blokk.hva-skjer-etter-permitteringen"),

    INNVILGELSE_PERMITTERT_FISK(brevblokkId = "brev.blokk.innvilgelse-permittert-fisk"),
    INNVILGELSE_VIRKNINGSDATO_BEGRUNNELSE_PERMITTERT_FISK(brevblokkId = "brev.blokk.begrunnelse-innvilgelsesdato-permittert-fisk"),
    INNVILGELSE_DAGPENGEPERIODE_PERMITTERT_FISK_DEL_1(brevblokkId = "brev.blokk.hvor-lenge-kan-du-faa-dagpenger-permittert-fisk-del-1"),
    INNVILGELSE_DAGPENGEPERIODE_PERMITTERT_FISK_DEL_2(brevblokkId = "brev.blokk.hvor-lenge-kan-du-faa-dagpenger-permittert-fisk-del-2"),
}
