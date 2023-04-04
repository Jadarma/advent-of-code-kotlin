import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    id("conventions.kotlin-base")
    id("conventions.kotest")
    id("conventions.detekt")
    id("conventions.dokka")
    id("conventions.publish")
}

kotlin {
    explicitApi()
}

tasks.withType<KotlinCompilationTask<KotlinJvmCompilerOptions>>().configureEach {
    compilerOptions {
        allWarningsAsErrors.set(true)
    }
}
