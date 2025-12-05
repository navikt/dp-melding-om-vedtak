package no.nav.dagpenger.vedtaksmelding.apiconfig

import com.auth0.jwk.JwkProviderBuilder
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.jwt.JWTAuthenticationProvider
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.parseAuthorizationHeader
import io.ktor.server.auth.principal
import io.ktor.server.request.ApplicationRequest
import no.nav.dagpenger.vedtaksmelding.Configuration
import java.net.URL

private val logger = KotlinLogging.logger {}

sealed class Klient

data object Maskin : Klient()

data class Saksbehandler(
    val token: String,
) : Klient()

internal fun ApplicationRequest.klient(): Klient =
    this.call.principal<JWTPrincipal>()?.let {
        when (it.payload.claims["idtyp"]?.asString() == "app") {
            true -> Maskin
            false -> Saksbehandler(this.jwt())
        }
    } ?: throw IllegalArgumentException("Ugyldig token eller manglende JWT i forespørselen")

internal fun ApplicationRequest.jwt(): String =
    this.parseAuthorizationHeader().let { authHeader ->
        (authHeader as? HttpAuthHeader.Single)?.blob ?: throw IllegalArgumentException("JWT not found")
    }

fun AuthenticationConfig.jwt(name: String) {
    jwt(name) {
        verifier(AzureAd)
        validate { jwtClaims ->
            jwtClaims.måInneholde(autorisertADGruppe = Configuration.saksbehandlerADGruppe)
            JWTPrincipal(jwtClaims.payload)
        }
    }
}

fun AuthenticationConfig.jwtM2M(name: String) {
    jwt(name) {
        verifier(AzureAd)
        validate { jwtClaims ->
            jwtClaims.måVæreApp()
            JWTPrincipal(jwtClaims.payload)
        }
    }
}

private fun JWTCredential.måVæreApp() {
    val erApp = this.payload.claims["idtyp"]?.asString() == "app"
    if (!erApp) {
        val errorMessage = "Credential inneholder ikke idtyp med verdi app"
        logger.warn { errorMessage }
        throw IllegalAccessException(errorMessage)
    }
}

private fun JWTCredential.måInneholde(autorisertADGruppe: String) {
    val groups = this.payload.claims["groups"]?.asList(String::class.java)
    if (groups == null) {
        val errorMessage = "Credential inneholder ikke groups claim"
        logger.warn { errorMessage }
        throw IllegalAccessException(errorMessage)
    }

    if (!groups.contains(autorisertADGruppe)) {
        val errorMessage = "Credential inneholder ikke riktig gruppe. Forventet $autorisertADGruppe men var $groups"
        logger.warn { errorMessage }
        throw IllegalAccessException(errorMessage)
    }
}

fun JWTAuthenticationProvider.Config.verifier(type: NaisJWTProviders) {
    type.createVerifier(this)
}

abstract class NaisJWTProviders private constructor(
    private val jwksUri: URL,
    private val issuer: String,
    private val clientId: String,
) {
    constructor(jwksUri: String, issuer: String, clientId: String) : this(
        jwksUri = URL(getEnvOrSystem(jwksUri)),
        issuer = getEnvOrSystem(issuer),
        clientId = getEnvOrSystem(clientId),
    )

    fun createVerifier(config: JWTAuthenticationProvider.Config) {
        config.verifier(
            JwkProviderBuilder(jwksUri).build(),
            issuer,
        ) { this.withAudience(clientId) }
    }
}

private fun getEnvOrSystem(name: String) = System.getenv(name) ?: System.getProperty(name)

object AzureAd : NaisJWTProviders("AZURE_OPENID_CONFIG_JWKS_URI", "AZURE_OPENID_CONFIG_ISSUER", "AZURE_APP_CLIENT_ID")
