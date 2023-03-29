import CompileOptions.Java
import CompileOptions.Kotlin
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain {
        languageVersion.set(Java.languageVersion)
        vendor.set(Java.jvmVendor)
    }
}

tasks.withType<KotlinCompilationTask<KotlinJvmCompilerOptions>>().configureEach {
    compilerOptions {
        languageVersion.set(Kotlin.languageVersion)
        apiVersion.set(Kotlin.apiVersion)
        jvmTarget.set(Kotlin.jvmTarget)
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
}
