plugins {
    id("conventions.kotlin-lib")
}

dependencies {
    api(project(":aock-core"))
    implementation(project(":aock-sdk"))
}
