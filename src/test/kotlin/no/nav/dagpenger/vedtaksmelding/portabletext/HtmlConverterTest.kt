package no.nav.dagpenger.vedtaksmelding.portabletext

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.saksbehandling.api.models.BehandlerDTO
import no.nav.dagpenger.saksbehandling.api.models.BehandlerEnhetDTO
import no.nav.dagpenger.saksbehandling.api.models.BehandlingstypeDTO.RETT_TIL_DAGPENGER
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.vedtaksmelding.Configuration.objectMapper
import no.nav.dagpenger.vedtaksmelding.model.UtvidetBeskrivelse
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMapper
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.VedtakMelding
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.avslag.AvslagBrevblokker.AVSLAG_MINSTEINNTEKT_DEL_1
import no.nav.dagpenger.vedtaksmelding.sanity.ResultDTO
import no.nav.dagpenger.vedtaksmelding.util.finnUtvidetBeskrivelseNode
import no.nav.dagpenger.vedtaksmelding.util.readFile
import no.nav.dagpenger.vedtaksmelding.util.writeStringToFile
import no.nav.dagpenger.vedtaksmelding.uuid.UUIDv7
import org.junit.jupiter.api.Test

@Suppress("ktlint:standard:max-line-length")
class HtmlConverterTest {
    private fun hentVedtak(navn: String): Vedtak = navn.readFile().let { VedtakMapper(it).vedtak() }

    @Test
    fun `Hubba`() {
        HtmlConverter.toHtml(
            meldingOmVedtakDTO,
            utvidetBeskrivelse =
                setOf(
                    UtvidetBeskrivelse(
                        behandlingId = UUIDv7.ny(),
                        brevblokkId = "sfa",
                        tekst = "<h1>Hubba</h1>",
                        sistEndretTidspunkt = java.time.LocalDateTime.now(),
                        tittel = "Hubba",
                    ),
                ),
        ).also {
            writeStringToFile("build/temp/hubba.html", it)
        }
    }

    @Test
    fun `Skal bygge HTML med ulike tekst formattering`() {
        val sanityTekster =
            "/json/sanity.json"
                .readFile()
                .let {
                    objectMapper.readValue(it, ResultDTO::class.java)
                }.result
                .filter { it.textId.contains("hubba.bubba") }

        sanityTekster.size shouldBe 1

        shouldNotThrowAny {
            HtmlConverter
                .toHtml(
                    brevBlokker = sanityTekster,
                    opplysninger = emptyList(),
                    meldingOmVedtakData = meldingOmVedtakDTO,
                ).also {
                    writeStringToFile("build/temp/test.html", it)
                }
        }
    }

    @Test
    fun `Skal sanitisere og legge inn utvidede beskrivelser i HTML som blir laget`() {
        val sanityTekster =
            "/json/sanity.json"
                .readFile()
                .let {
                    objectMapper.readValue(it, ResultDTO::class.java)
                }.result

        hentVedtak("/json/avslag_resultat.json")
            .let { vedtak -> VedtakMelding.byggVedtaksmelding(vedtak, sanityTekster) }
            .let { vedtakMelding ->
                HtmlConverter.toHtml(
                    brevBlokker = vedtakMelding.hentBrevBlokker(),
                    opplysninger = vedtakMelding.hentOpplysninger(),
                    meldingOmVedtakData = meldingOmVedtakDTO,
                    utvidetBeskrivelse =
                        setOf(
                            UtvidetBeskrivelse(
                                behandlingId = UUIDv7.ny(),
                                brevblokkId = AVSLAG_MINSTEINNTEKT_DEL_1.brevblokkId,
                                tekst =
                                    """Dette er en test linje med <, > og &
Dette er linje 2
 
Dette er linje 3


Dette er linje 4
                                    """.trimMargin(),
                            ),
                        ),
                )
            } finnUtvidetBeskrivelseNode AVSLAG_MINSTEINNTEKT_DEL_1.brevblokkId shouldBe
            """<p data-utvidet-beskrivelse-id="brev.blokk.avslag-minsteinntekt-del-1">Dette er en test linje med &lt;, &gt; og &amp;<br>Dette er linje 2<br> <br>Dette er linje 3<br><br><br>Dette er linje 4</p>"""
    }

    private val meldingOmVedtakDTO =
        MeldingOmVedtakDataDTO(
            behandlingstype = RETT_TIL_DAGPENGER,
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
