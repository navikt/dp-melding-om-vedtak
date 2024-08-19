import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("common")
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}
dependencies {
    val ktorVersion = libs.versions.ktor.get()
    implementation(project(":openapi"))
    implementation(libs.bundles.ktor.server)
    implementation("io.ktor:ktor-server-swagger:$ktorVersion")
    implementation(libs.ktor.server.cio)
    implementation(libs.jackson.datatype.jsr310)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk)
    testImplementation(libs.bundles.kotest.assertions)

    testImplementation(libs.ktor.server.test.host)
}

application {
    mainClass.set("no.nav.dagpenger.vedtaksmelding.AppKt")
}

tasks.withType<ShadowJar> {
    mergeServiceFiles()
}
