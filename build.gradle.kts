import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("common")
    application
    alias(libs.plugins.shadow.jar)
}
dependencies {
    implementation(project(":openapi"))
    implementation("ch.qos.logback:logback-classic:1.5.13")
    implementation("net.logstash.logback:logstash-logback-encoder:8.0")
    implementation("com.fasterxml.uuid:java-uuid-generator:5.1.0")
    implementation(libs.kotlin.logging)
    implementation(libs.konfig)
    implementation(libs.bundles.ktor.server)
    implementation(libs.ktor.server.cio)
    implementation("io.ktor:ktor-server-swagger:${libs.versions.ktor.get()}")
    implementation(libs.ktor.server.metrics.micrometer)
    implementation("io.micrometer:micrometer-registry-prometheus:1.14.3")
    implementation(libs.jackson.datatype.jsr310)
    implementation("no.nav.dagpenger:oauth2-klient:2025.08.20-08.53.9250ac7fbd99")
    implementation(libs.bundles.postgres)

    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging.jvm)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.9.0")

    val kotlinxHtmlVersion = "0.12.0"
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinxHtmlVersion")

    testImplementation(libs.kotest.assertions.json)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.mockk)
    testImplementation(libs.mock.oauth2.server)
    testImplementation(libs.bundles.kotest.assertions)
    testImplementation(libs.bundles.postgres.test)
    testImplementation(libs.ktor.client.mock)

    testImplementation("org.jsoup:jsoup:1.15.3")
}

application {
    mainClass.set("no.nav.dagpenger.vedtaksmelding.AppKt")
}

tasks.withType<ShadowJar> {
    mergeServiceFiles()
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
