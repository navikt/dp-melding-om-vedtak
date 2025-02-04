package no.nav.dagpenger.vedtaksmelding.portabletext

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.saksbehandling.api.models.BehandlerDTO
import no.nav.dagpenger.saksbehandling.api.models.BehandlerEnhetDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.vedtaksmelding.Configuration.objectMapper
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_MINSTEINNTEKT_BEGRUNNELSE
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.sanity.ResultDTO
import no.nav.dagpenger.vedtaksmelding.util.finnUtvidetBeskrivelseNode
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

@Suppress("ktlint:standard:max-line-length")
class HtmlConverterTest {
    private fun String.lesFil(): String {
        val resourseRetriever = object {}.javaClass
        return resourseRetriever.getResource(this)?.readText() ?: throw RuntimeException("Fant ikke ressurs $this")
    }

    private fun hentVedtak(navn: String): Vedtak {
        return navn.lesFil().let { VedtakMapper(it).vedtak() }
    }

    @Test
    fun `Skal bygge HTML med utvidede beskrivelse`() {
        val sanityTekster =
            "/json/sanity.json".lesFil().let {
                objectMapper.readValue(it, ResultDTO::class.java)
            }.result

        hentVedtak("/json/avslag.json").let { vedtak -> VedtakMelding.byggVedtaksmelding(vedtak, sanityTekster) }
            .let { vedtakMelding ->
                HtmlConverter.toHtml(
                    brevBlokker = vedtakMelding.hentBrevBlokker(),
                    opplysninger = vedtakMelding.hentOpplysninger(),
                    meldingOmVedtakData = meldingOmVedtakDTO,
                    fagsakId = "123456789",
                    utvidetBeskrivelse =
                        setOf(
                            UtvidetBeskrivelse(
                                behandlingId = UUIDv7.ny(),
                                brevblokkId = AVSLAG_MINSTEINNTEKT_BEGRUNNELSE.brevblokkId,
                                tekst = """ Dette er en test linje 1
                                    linje 1
                                    
                                    
                                    linje 2
                                    
                                    
                                    linje 3
                                    
                                    linje 4
                                    """,
                            ),
                        ),
                )
            } finnUtvidetBeskrivelseNode AVSLAG_MINSTEINNTEKT_BEGRUNNELSE.brevblokkId shouldBe
            """<p data-utvidet-beskrivelse-id="brev.blokk.begrunnelse-avslag-minsteinntekt"> Dette er en test linje 1<br> linje 1<br> <br> <br> linje 2<br> <br> <br> linje 3<br> <br> linje 4<br> </p>"""
    }

    private val meldingOmVedtakDTO =
        MeldingOmVedtakDataDTO(
            fornavn = "Test ForNavn",
            etternavn = "Test EtterNavn",
            fodselsnummer = "12345678901",
            saksbehandler =
                BehandlerDTO(
                    fornavn = "Ola",
                    etternavn = "Nordmann",
                    enhet =
                        BehandlerEnhetDTO(
                            navn = "Enhet Navn",
                            postadresse = "Postadresse 123",
                        ),
                ),
            beslutter =
                BehandlerDTO(
                    fornavn = "Kari",
                    etternavn = "Nordmann",
                    enhet =
                        BehandlerEnhetDTO(
                            navn = "Enhet Navn",
                            postadresse = "Postadresse 123",
                        ),
                ),
        )
}
