@file:Suppress("UnstableApiUsage")

rootProject.name = "advent-of-code-kotlin"
include(":aockt-cli", ":aockt-core", ":aockt-sdk", ":aockt-test")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}
