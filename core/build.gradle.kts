plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    val junitEngineVersion: String by rootProject
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitEngineVersion")
}
