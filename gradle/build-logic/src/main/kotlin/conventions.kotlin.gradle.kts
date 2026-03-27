import CompileOptions.Java
import CompileOptions.Kotlin

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
