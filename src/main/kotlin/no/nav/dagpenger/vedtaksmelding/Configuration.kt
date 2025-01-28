package no.nav.dagpenger.vedtaksmelding

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.natpryce.konfig.ConfigurationMap
import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.Key
import com.natpryce.konfig.overriding
import com.natpryce.konfig.stringType
import no.nav.dagpenger.oauth2.CachedOauth2Client
import no.nav.dagpenger.oauth2.OAuth2Config

object Configuration {
    private val defaultProperties =
        ConfigurationMap(
            mapOf(
                "GRUPPE_SAKSBEHANDLER" to "SaksbehandlerADGruppe",
                "DP_BEHANDLING_API_SCOPE" to "api://dev-gcp.teamdagpenger.dp-behandling/.default",
                "DP_BEHANDLING_API_URL" to "http://dp-behandling/behandling",
                "SANITY_API_URL" to "https://rt6o382n.api.sanity.io/v2022-03-07/data/query/production",
            ),
        )

    private val properties by lazy {
        ConfigurationProperties.systemProperties() overriding EnvironmentVariables() overriding defaultProperties
    }

    private val azureAdClient: CachedOauth2Client by lazy {
        val azureAdConfig = OAuth2Config.AzureAd(properties)
        CachedOauth2Client(
            tokenEndpointUrl = azureAdConfig.tokenEndpointUrl,
            authType = azureAdConfig.clientSecret(),
        )
    }

    val dbBehandlingApiUrl by lazy { properties[Key("DP_BEHANDLING_API_URL", stringType)] }

    val dpBehandlingOboExchanger: (String) -> String by lazy {
        val scope = properties[Key("DP_BEHANDLING_API_SCOPE", stringType)]
        { token: String ->
            val accessToken = azureAdClient.onBehalfOf(token, scope).access_token
            requireNotNull(accessToken) { "Failed to get access token" }
            accessToken
        }
    }

    val saksbehandlerADGruppe by lazy { properties[Key("GRUPPE_SAKSBEHANDLER", stringType)] }
    val sanityApiUrl by lazy { properties[Key("SANITY_API_URL", stringType)] }

    val objectMapper: ObjectMapper =
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .enable(SerializationFeature.INDENT_OUTPUT)
}
