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
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
