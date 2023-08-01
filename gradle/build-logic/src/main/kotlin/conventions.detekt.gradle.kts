import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType

plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    buildUponDefaultConfig = true
    parallel = true
    config.from(files("$rootDir/gradle/detekt/detekt.yml"))
    baseline = file("$rootDir/gradle/detekt/baseline_${project.name}.xml")
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = CompileOptions.Kotlin.jvmTarget.target
}
