@file:Suppress("UnstableApiUsage")

rootProject.name = "advent-of-code-kotlin"
include(":aock-cli", ":aock-core", ":aock-sdk", ":aock-test", ":aock-util")

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
