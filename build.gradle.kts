plugins {
    id("common")
    `java-library`
}
dependencies {
    testImplementation(libs.mockk)
    testImplementation(libs.bundles.kotest.assertions)
}
