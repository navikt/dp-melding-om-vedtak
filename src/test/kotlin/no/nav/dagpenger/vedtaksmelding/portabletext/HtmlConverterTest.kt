package no.nav.dagpenger.vedtaksmelding.portabletext

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.saksbehandling.api.models.BehandlerDTO
import no.nav.dagpenger.saksbehandling.api.models.BehandlerEnhetDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.vedtaksmelding.Configuration.objectMapper
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import no.nav.dagpenger.vedtaksmelding.model.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.avslag.AvslagBrevblokker.AVSLAG_MINSTEINNTEKT
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Vedtak
import no.nav.dagpenger.vedtaksmelding.sanity.ResultDTO
import no.nav.dagpenger.vedtaksmelding.util.finnUtvidetBeskrivelseNode
import no.nav.dagpenger.vedtaksmelding.util.readFile
import no.nav.dagpenger.vedtaksmelding.util.writeStringToFile
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

@Suppress("ktlint:standard:max-line-length")
class HtmlConverterTest {
    private fun hentVedtak(navn: String): Vedtak {
        return navn.readFile().let { VedtakMapper(it).vedtak() }
    }

    @Test
    fun `Skal bygge HTML med ulike tekst formattering`() {
        val sanityTekster =
            "/json/sanity.json".readFile().let {
                objectMapper.readValue(it, ResultDTO::class.java)
            }.result.filter { it.textId.contains("hubba.bubba") }

        sanityTekster.size shouldBe 1

        shouldNotThrowAny {
            HtmlConverter.toHtml(
                brevBlokker = sanityTekster,
                opplysninger = emptyList(),
                meldingOmVedtakData = meldingOmVedtakDTO,
                fagsakId = "123456789",
            ).also {
                writeStringToFile("build/temp/test.html", it)
            }
        }
    }

    @Test
    fun `Skal sanitisere og legge inn utvidede beskrivelse i HTML som blir laget`() {
        val sanityTekster =
            "/json/sanity.json".readFile().let {
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
                                brevblokkId = AVSLAG_MINSTEINNTEKT.brevblokkId,
                                tekst =
                                    """Dette er en test linje med <, > og &
Dette er linje 2
 
Dette er linje 3


Dette er linje 4
                                    """.trimMargin(),
                            ),
                        ),
                )
            } finnUtvidetBeskrivelseNode AVSLAG_MINSTEINNTEKT.brevblokkId shouldBe
            """<p data-utvidet-beskrivelse-id="brev.blokk.begrunnelse-avslag-minsteinntekt">Dette er en test linje med &lt;, &gt; og &amp;<br>Dette er linje 2<br> <br>Dette er linje 3<br><br><br>Dette er linje 4</p>"""
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
