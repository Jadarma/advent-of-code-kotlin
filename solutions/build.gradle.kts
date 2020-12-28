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
    val kotlinSerializationJsonVersion: String by project
    implementation(project(":core"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationJsonVersion")

    val junitEngineVersion: String by rootProject
    testImplementation(project(":core", "testArchive"))
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitEngineVersion")
}
