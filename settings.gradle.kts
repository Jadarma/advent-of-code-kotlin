@file:Suppress("UnstableApiUsage")

rootProject.name = "advent-of-code-kotlin"
includeBuild("gradle/build-logic")
include(":aockt-core", ":aockt-test")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    // https://plugins.gradle.org/plugin/org.gradle.toolchains.foojay-resolver-convention
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}
