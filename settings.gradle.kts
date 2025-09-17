rootProject.name = "advent-of-code-kotlin"
includeBuild("gradle/build-logic")
include(":aockt-core", ":aockt-test")

pluginManagement {
    repositories {
        includeBuild("gradle/build-logic")
    }
}

plugins {
    id("conventions.project")
}
