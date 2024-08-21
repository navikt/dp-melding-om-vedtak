package no.nav.dagpenger.vedtaksmelding

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
            ),
        )

    val properties by lazy {
        ConfigurationProperties.systemProperties() overriding EnvironmentVariables() overriding defaultProperties
    }

    val azureAdClient: CachedOauth2Client by lazy {
        val azureAdConfig = OAuth2Config.AzureAd(properties)
        CachedOauth2Client(
            tokenEndpointUrl = azureAdConfig.tokenEndpointUrl,
            authType = azureAdConfig.clientSecret(),
        )
    }

    val dpBehandlingOboExchanger: (String) -> String by lazy {
        val scope = properties[Key("DP_BEHANDLING_API_SCOPE", stringType)]
        { token: String ->
            val accessToken = azureAdClient.onBehalfOf(token, scope).accessToken
            requireNotNull(accessToken) { "Failed to get access token" }
            accessToken
        }
    }

    val saksbehandlerADGruppe by lazy { properties[Key("GRUPPE_SAKSBEHANDLER", stringType)] }
}
