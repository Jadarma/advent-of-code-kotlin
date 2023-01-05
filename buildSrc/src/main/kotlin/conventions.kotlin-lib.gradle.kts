import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
    id("org.jetbrains.dokka")
}

kotlin {
    explicitApi()
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

detekt {
    buildUponDefaultConfig = true
    parallel = true
    config = files("$rootDir/gradle/detekt/detekt.yml")
    baseline = file("$rootDir/gradle/detekt/baseline_${project.name}.xml")
}

tasks {
    withType<KotlinCompilationTask<KotlinJvmCompilerOptions>>().configureEach {
        compilerOptions {
            languageVersion.set(KotlinVersion.KOTLIN_1_8)
            apiVersion.set(KotlinVersion.KOTLIN_1_8)
            jvmTarget.set(JvmTarget.JVM_17)
            allWarningsAsErrors.set(true)
        }
    }

    withType<Detekt>().configureEach {
        jvmTarget = JvmTarget.JVM_17.target
    }

    withType<Test>().configureEach {
        useJUnitPlatform()
    }

    withType<DokkaTask>().configureEach {
        dokkaSourceSets.configureEach {
            jdkVersion.set(17)
            noStdlibLink.set(true)
        }
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)

    testImplementation(libs.bundles.kotest)
}
