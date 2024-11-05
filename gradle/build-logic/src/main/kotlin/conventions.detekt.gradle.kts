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
    baseline = file("$rootDir/gradle/detekt/baseline/${project.name}.xml")
    config.from(
        files(
            "$rootDir/gradle/detekt/config/detekt.yml",
            "$rootDir/gradle/detekt/config/${project.name}.yml",
        )
    )
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = CompileOptions.Kotlin.jvmTarget.target
}
