plugins {
    id("conventions.kotlin-lib")
}

dependencies {
    api(project(":aockt-core"))
    implementation(libs.kotlin.reflect)
    implementation(libs.kotest.api)
    implementation(libs.kotest.assertions)
}

tasks.withType<Test>().configureEach {
    systemProperties(
        "kotest.framework.config.fqn" to "io.github.jadarma.aockt.test.TestConfig",
        "kotest.framework.classpath.scanning.config.disable" to true,
        "kotest.framework.classpath.scanning.autoscan.disable" to true,
    )
}
