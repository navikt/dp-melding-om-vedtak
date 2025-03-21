package no.nav.dagpenger.vedtaksmelding.model.avslag

import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk

class TomtVedtak(
    override val vedtak: Vedtak,
    alleBrevblokker: List<BrevBlokk>,
) : VedtakMelding(vedtak) {
    override val harBrevstøtte: Boolean = true
    override val brevBlokkIder: List<String> = listOf("test.tomt.brev")
    override val brevBlokker: List<BrevBlokk> = alleBrevblokker.filter { it.textId == "test.tomt.brev" }
}
