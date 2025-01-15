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
    implementation(libs.bundles.ktor.server)
    implementation(libs.ktor.server.cio)
    implementation("io.ktor:ktor-server-swagger:${libs.versions.ktor.get()}")
    implementation(libs.jackson.datatype.jsr310)
    implementation("no.nav.dagpenger:oauth2-klient:2024.10.24-13.47.d2fb89767416")
    implementation(libs.bundles.postgres)

    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging.jvm)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.9.0")

    val kotlinxHtmlVersion = "0.11.0"
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinxHtmlVersion")
    implementation("io.github.allangomes:kotlinwind-css:0.0.4")

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.mockk)
    testImplementation(libs.mock.oauth2.server)
    testImplementation(libs.bundles.kotest.assertions)
    testImplementation(libs.bundles.postgres.test)
    testImplementation("io.ktor:ktor-client-mock:${libs.versions.ktor.get()}")

    testImplementation("io.kubernetes:client-java:21.0.1")
    testImplementation("org.jsoup:jsoup:1.14.3")
}

application {
    mainClass.set("no.nav.dagpenger.vedtaksmelding.AppKt")
}

tasks.withType<ShadowJar> {
    mergeServiceFiles()
}
