import CompileOptions.Java
import CompileOptions.Kotlin
import org.gradle.kotlin.dsl.dependencies

plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain {
        languageVersion = Java.languageVersion
        vendor = Java.jvmVendor
    }

    compilerOptions {
        languageVersion = Kotlin.languageVersion
        apiVersion = Kotlin.apiVersion
        jvmTarget = Kotlin.jvmTarget
        allWarningsAsErrors = true
    }

    explicitApi()
}

dependencies {
    implementation(libs.kotlin.stdlib)
}
