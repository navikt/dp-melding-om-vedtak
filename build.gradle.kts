import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("common")
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}
dependencies {
    implementation(project(":openapi"))
    implementation("ch.qos.logback:logback-classic:1.5.7")
    implementation("net.logstash.logback:logstash-logback-encoder:8.0")
    implementation("com.fasterxml.uuid:java-uuid-generator:5.1.0")
    implementation(libs.kotlin.logging)
    implementation(libs.konfig)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.dp.biblioteker.oauth2.klient)
    implementation(libs.bundles.postgres)

    implementation("io.ktor:ktor-server-core:3.0.0")
    implementation("io.ktor:ktor-server-cio:3.0.0")
    implementation("io.ktor:ktor-serialization-jackson:3.0.0")
    implementation("io.ktor:ktor-server-auth:3.0.0")
    implementation("io.ktor:ktor-server-call-logging:3.0.0")
    implementation("io.ktor:ktor-server-content-negotiation:3.0.0")
    implementation("io.ktor:ktor-server-status-pages:3.0.0")
    implementation("io.ktor:ktor-server-auth-jwt:3.0.0")

    implementation("io.ktor:ktor-client-core:3.0.0")
    implementation("io.ktor:ktor-client-cio:3.0.0")
    implementation("io.ktor:ktor-client-content-negotiation:3.0.0")
    implementation("io.ktor:ktor-client-logging:3.0.0")

    testImplementation(libs.mockk)
    testImplementation(libs.mock.oauth2.server)
    testImplementation(libs.bundles.kotest.assertions)
    testImplementation(libs.bundles.postgres.test)

    testImplementation("io.ktor:ktor-server-test-host:3.0.0")
    testImplementation("io.ktor:ktor-client-mock:3.0.0")
    testImplementation("io.kubernetes:client-java:21.0.1")
}

application {
    mainClass.set("no.nav.dagpenger.vedtaksmelding.AppKt")
}

tasks.withType<ShadowJar> {
    mergeServiceFiles()
}
