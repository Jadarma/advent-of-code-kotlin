plugins {
    kotlin("jvm")
    application
}

application {
    applicationName = "aoc-solutions"
    mainClass.set("aoc.solutions.MainKt")
}

dependencies {
    implementation(project(":core"))

    val junitEngineVersion: String by rootProject
    testImplementation(project(":core", "testArchive"))
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitEngineVersion")
}
