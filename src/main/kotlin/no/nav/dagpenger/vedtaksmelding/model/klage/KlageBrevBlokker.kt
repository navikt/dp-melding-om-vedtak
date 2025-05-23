package no.nav.dagpenger.vedtaksmelding.model.klage

enum class KlageBrevBlokker(
    val brevblokkId: String,
) {
    KLAGE_OPPRETTHOLDELSE_DEL_1("brev.blokk.klage-opprettholdelse-del-1"),
    KLAGE_OPPRETTHOLDELSE_DEL_2("brev.blokk.klage-opprettholdelse-del-2"),
}
