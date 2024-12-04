package no.nav.dagpenger.vedtaksmelding

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.Behandling
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import java.time.LocalDate
import java.time.Month
import java.util.UUID

private val logger = KotlinLogging.logger {}

interface BehandlingKlient {
    suspend fun hentBehandling(
        behandling: UUID,
        saksbehandler: Saksbehandler,
    ): Result<Behandling>
}

internal class BehandlngHttpKlient(
    private val dpBehandlingApiUrl: String,
    private val tokenProvider: (String) -> String,
    private val httpClient: HttpClient = lagHttpKlient(engine = CIO.create { }),
) : BehandlingKlient {
    override suspend fun hentBehandling(
        behandling: UUID,
        saksbehandler: Saksbehandler,
    ): Result<Behandling> {
        return kotlin.runCatching {
            httpClient.get(urlString = "$dpBehandlingApiUrl/$behandling") {
                header(HttpHeaders.Authorization, "Bearer ${tokenProvider.invoke(saksbehandler.token)}")
                accept(ContentType.Application.Json)
            }.body<BehandlingDTO>().let { behandlingDTO ->
                val mutableOpplysninger: MutableSet<Opplysning> =
                    behandlingDTO.opplysning.map { opplysningDTO ->
                        Opplysning(
                            navn = opplysningDTO.navn,
                            verdi = opplysningDTO.verdi,
                            datatype = opplysningDTO.datatype,
                            opplysningId = opplysningDTO.id,
                        )
                    }.toMutableSet()

                val førsteMånedAvOpptjeningsperiode: LocalDate? =
                    mutableOpplysninger.singleOrNull {
                        it.opplysningTekstId == "opplysning.forste-maaned-av-opptjeningsperiode"
                    }?.let { opplysning ->
                        LocalDate.parse(opplysning.verdi)
                    }
                val sisteAvsluttendeKalendermåned: LocalDate? =
                    mutableOpplysninger.singleOrNull {
                        it.opplysningTekstId == "opplysning.siste-avsluttende-kalendermaaned"
                    }?.let { opplysning ->
                        LocalDate.parse(opplysning.verdi)
                    }

                if (førsteMånedAvOpptjeningsperiode != null) {
                    mutableOpplysninger.add(
                        Opplysning(
                            opplysningTekstId = "opplysning.forste-maaned-aar-for-inntektsperiode-1",
                            navn = "opplysning.inntektsperiode-1-forste-maaned-aar",
                            verdi = førsteMånedAvOpptjeningsperiode.norskMånedOgÅr(),
                            datatype = "tekst",
                            opplysningId = "utledet",
                        ),
                    )
                    mutableOpplysninger.add(
                        Opplysning(
                            opplysningTekstId = "opplysning.forste-maaned-aar-for-inntektsperiode-2",
                            navn = "opplysning.inntektsperiode-2-forste-maaned-aar",
                            verdi = førsteMånedAvOpptjeningsperiode.plusYears(1).norskMånedOgÅr(),
                            datatype = "tekst",
                            opplysningId = "utledet",
                        ),
                    )
                    mutableOpplysninger.add(
                        Opplysning(
                            opplysningTekstId = "opplysning.forste-maaned-aar-for-inntektsperiode-3",
                            navn = "opplysning.inntektsperiode-3-forste-maaned-aar",
                            verdi = førsteMånedAvOpptjeningsperiode.plusYears(2).norskMånedOgÅr(),
                            datatype = "tekst",
                            opplysningId = "utledet",
                        ),
                    )
                }

                if (sisteAvsluttendeKalendermåned != null) {
                    mutableOpplysninger.add(
                        Opplysning(
                            opplysningTekstId = "opplysning.siste-maaned-aar-for-inntektsperiode-1",
                            navn = "opplysning.inntektsperiode-1-siste-maaned-aar",
                            verdi = sisteAvsluttendeKalendermåned.minusYears(2).norskMånedOgÅr(),
                            datatype = "tekst",
                            opplysningId = "utledet",
                        ),
                    )
                    mutableOpplysninger.add(
                        Opplysning(
                            opplysningTekstId = "opplysning.siste-maaned-aar-for-inntektsperiode-2",
                            navn = "opplysning.inntektsperiode-2-siste-maaned-aar",
                            verdi = sisteAvsluttendeKalendermåned.minusYears(1).norskMånedOgÅr(),
                            datatype = "tekst",
                            opplysningId = "utledet",
                        ),
                    )
                    mutableOpplysninger.add(
                        Opplysning(
                            opplysningTekstId = "opplysning.siste-maaned-aar-for-inntektsperiode-3",
                            navn = "opplysning.inntektsperiode-3-siste-maaned-aar",
                            verdi = sisteAvsluttendeKalendermåned.norskMånedOgÅr(),
                            datatype = "tekst",
                            opplysningId = "utledet",
                        ),
                    )
                }

                Behandling(
                    id = UUID.fromString(behandlingDTO.behandlingId),
                    tilstand = behandlingDTO.tilstand,
                    opplysninger =
                        behandlingDTO.opplysning.map { opplysningDTO ->
                            Opplysning(
                                navn = opplysningDTO.navn,
                                verdi = opplysningDTO.verdi,
                                datatype = opplysningDTO.datatype,
                                opplysningId = opplysningDTO.id,
                            )
                        }.toSet() + mutableOpplysninger.toSet(),
                )
            }
        }.onFailure { logger.error(it) { "Kall til dp-behandling feilet ${it.message}" } }
    }
}

private data class BehandlingDTO(
    val behandlingId: String,
    val tilstand: String,
    val opplysning: List<OpplysningDTO>,
) {
    data class OpplysningDTO(
        val id: String,
        val navn: String,
        val verdi: String,
        val datatype: String,
    )
}

private fun LocalDate.norskMånedOgÅr() =
    when (this.month) {
        Month.JANUARY -> "januar ${this.year}"
        Month.FEBRUARY -> "februar ${this.year}"
        Month.MARCH -> "mars ${this.year}"
        Month.APRIL -> "april ${this.year}"
        Month.MAY -> "mai ${this.year}"
        Month.JUNE -> "juni ${this.year}"
        Month.JULY -> "juli ${this.year}"
        Month.AUGUST -> "august ${this.year}"
        Month.SEPTEMBER -> "september ${this.year}"
        Month.OCTOBER -> "oktober ${this.year}"
        Month.NOVEMBER -> "november ${this.year}"
        Month.DECEMBER -> "desember ${this.year}"
    }
