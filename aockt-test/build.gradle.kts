plugins {
    id("conventions.kotlin-lib")
}

dependencies {
    api(project(":aockt-core"))
    implementation(project(":aockt-sdk"))
}
