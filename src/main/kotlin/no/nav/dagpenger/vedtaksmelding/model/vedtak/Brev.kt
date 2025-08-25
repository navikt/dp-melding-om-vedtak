package no.nav.dagpenger.vedtaksmelding.model.vedtak

import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.portabletext.BrevBlokk

interface Brev {
    fun brevBlokkIder(): List<String>

    fun hentBrevBlokker(): List<BrevBlokk>

    fun hentOpplysninger(): List<Opplysning>
}
