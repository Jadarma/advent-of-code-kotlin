import dev.detekt.gradle.Detekt
import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("jvm")
    id("dev.detekt")
}

detekt {
    buildUponDefaultConfig = false
    parallel = true
    baseline = file("$rootDir/gradle/detekt/baseline/${project.name}.xml")
}

tasks {
    val detektMain: Detekt by named<Detekt>("detektMain") {
        config.setFrom(
            "$rootDir/gradle/detekt/config/detekt.yml",
            "$rootDir/gradle/detekt/config/${project.name}.yml",
        )
    }
    val detektTest: Detekt by named<Detekt>("detektTest") {
        config.setFrom(
            "$rootDir/gradle/detekt/config/detekt.yml",
            "$rootDir/gradle/detekt/config/${project.name}.yml",
            "$rootDir/gradle/detekt/config/detekt-test.yml",
        )
    }

    check.configure {
        // Replace the default detekt dependency and instead force the use of type resolution.
        dependsOn(detektMain, detektTest)
        setDependsOn(dependsOn.filterNot { it is TaskProvider<*> && it.name == "detekt" })
    }

    withType<Detekt>().configureEach {
        reports {
            html.required = true
            sarif.required = true
            markdown.required = false
            checkstyle.required = false
        }
    }
}
