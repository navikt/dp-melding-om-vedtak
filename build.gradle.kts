plugins {
    id("common")
    `java-library`
}
dependencies {
    implementation(libs.bundles.ktor.server)
    implementation(libs.ktor.server.cio)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk)
    testImplementation(libs.bundles.kotest.assertions)

    testImplementation(libs.ktor.server.test.host)
}
