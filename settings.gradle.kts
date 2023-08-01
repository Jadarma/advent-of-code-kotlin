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
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.6.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}
