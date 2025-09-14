plugins {
    id("conventions.library")
}

dependencies {
    api(project(":aockt-core"))
    implementation(libs.kotlin.reflect)
    implementation(libs.kotest.engine)
    implementation(libs.kotest.assertions)
}

tasks.test {
    systemProperty("kotest.framework.config.fqn", "io.github.jadarma.aockt.test.TestConfig")
}
