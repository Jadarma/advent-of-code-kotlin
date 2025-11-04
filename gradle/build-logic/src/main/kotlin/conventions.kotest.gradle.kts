import CompileOptions.AocKt.GROUP_ID
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

plugins {
    kotlin("jvm")
    id("org.jetbrains.kotlinx.kover")
}

dependencies {
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.assertions)
}

kover {
    currentProject {
        sources {
            includedSourceSets = setOf("main")
        }
    }
    reports {
        total {
            html {
                onCheck = true
                charset = "UTF-8"
            }
            binary {
                onCheck = true
            }
        }
        filters {
            includes {
                classes("$GROUP_ID.*")
            }
            excludes {
                classes("*.*\$DefaultImpls")
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    // Don't cache tests, make them run again every time.
    outputs.upToDateWhen { false }

    // Pass along system properties for Kotest.
    systemProperties = System.getProperties()
        .asIterable()
        .filter { it.key.toString().startsWith("kotest.") }
        .associate { it.key.toString() to it.value }
}
