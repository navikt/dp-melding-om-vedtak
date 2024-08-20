package no.nav.dagpenger.vedtaksmelding

import com.natpryce.konfig.ConfigurationMap
import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.Key
import com.natpryce.konfig.overriding
import com.natpryce.konfig.stringType

object Configuration {
    private val defaultProperties =
        ConfigurationMap(
            mapOf(
                "GRUPPE_SAKSBEHANDLER" to "SaksbehandlerADGruppe",
            ),
        )
    val properties = ConfigurationProperties.systemProperties() overriding EnvironmentVariables() overriding defaultProperties

    val saksbehandlerADGruppe by lazy { properties[Key("GRUPPE_SAKSBEHANDLER", stringType)] }
}
