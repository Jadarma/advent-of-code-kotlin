plugins {
    id("conventions.kotlin-base")
    id("conventions.kotest")
    id("conventions.detekt")
    id("conventions.dokka")
    id("conventions.publish")
}

kotlin {
    explicitApi()

    compilerOptions {
        allWarningsAsErrors = true
    }
}
