package no.nav.dagpenger.vedtaksmelding.vedtaksmelding

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.jackson.jackson
import mu.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.Saksbehandler
import java.util.*

class Mediator {
    suspend fun sendVedtak(
        behandling: UUID,
        saksbehandler: Saksbehandler,
    ) {

        // hente ut opplysning basert på behandlingId
        // må veksle obo token som ligger i saksbehandler
        // kalle dp-behandling med nytt token og behandlingId
        // ......
        // .....
    }
}

private val logger = KotlinLogging.logger { }
