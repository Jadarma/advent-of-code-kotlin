plugins {
    id("conventions.kotlin-lib")
}

dependencies {
    api(project(":aockt-core"))
    implementation(libs.kotlin.reflect)
    implementation(libs.kotest.engine)
    implementation(libs.kotest.assertions)
}
