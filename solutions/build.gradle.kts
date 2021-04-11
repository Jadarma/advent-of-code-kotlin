plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application
}

application {
    applicationName = "aoc-solutions"
    mainClass.set("aoc.solutions.MainKt")
}

dependencies {
    implementation(project(":core"))
    implementation(libs.kotlin.serializationJson)

    testImplementation(project(":core", "testArchive"))
    testImplementation(libs.junit.kotlin)
    testRuntimeOnly(libs.junit.engine)
}
