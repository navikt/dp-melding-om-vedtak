package no.nav.dagpenger.vedtaksmelding

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.saksbehandling.api.models.BehandlerDTO
import no.nav.dagpenger.saksbehandling.api.models.BehandlerEnhetDTO
import no.nav.dagpenger.saksbehandling.api.models.MeldingOmVedtakDataDTO
import no.nav.dagpenger.vedtaksmelding.portabletext.HtmlConverter.hentNavn
import org.junit.jupiter.api.Test

class MeldingOmVedtakDataDTOTest {
    @Test
    fun `hent navn `() {
        lagMeldingOmVedtakDto(
            fornavn = "Test fornavn",
            mellomnavn = null,
            etternavn = "Test etternavn",
        ).hentNavn() shouldBe "Test fornavn Test etternavn"

        lagMeldingOmVedtakDto(
            fornavn = "Test fornavn",
            mellomnavn = "Test mellomnavn",
            etternavn = "Test etternavn",
        ).hentNavn() shouldBe "Test fornavn Test mellomnavn Test etternavn"
    }

    private fun lagMeldingOmVedtakDto(
        fornavn: String,
        mellomnavn: String?,
        etternavn: String,
    ): MeldingOmVedtakDataDTO {
        return MeldingOmVedtakDataDTO(
            fornavn = fornavn,
            etternavn = etternavn,
            fodselsnummer = "12345678901",
            sakId = "sak123",
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
            mellomnavn = mellomnavn,
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
}
