plugins {
    id("conventions.kotlin-base")
    id("conventions.kotest")
    id("conventions.detekt")
    id("conventions.dokka")
    id("conventions.publish")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

kotlin {
    explicitApi()

    compilerOptions {
        allWarningsAsErrors = true
    }
}
