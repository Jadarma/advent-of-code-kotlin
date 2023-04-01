plugins {
    id("conventions.kotlin-base")
    id("conventions.kotest")
    id("conventions.detekt")
}

dependencies {
    implementation(project(":aockt-core"))
    testImplementation(project(":aockt-test"))
}
