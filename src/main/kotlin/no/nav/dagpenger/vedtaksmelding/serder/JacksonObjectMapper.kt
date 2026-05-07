package no.nav.dagpenger.vedtaksmelding.serder

import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.introspect.DefaultAccessorNamingStrategy
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.jacksonMapperBuilder

/**
 * Konfigurerer en JsonMapper.Builder med standardinnstillinger for dp-melding-om-vedtak.
 * Brukes av Ktor jackson3-plugin (som gir en Builder i lambdaen).
 */
fun JsonMapper.Builder.applyDefault(): JsonMapper.Builder =
    this
        .accessorNaming(
            DefaultAccessorNamingStrategy
                .Provider()
                .withFirstCharAcceptance(true, true),
        ).disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)

fun defaultObjectMapper(): ObjectMapper =
    jacksonMapperBuilder()
        .applyDefault()
        .build()
