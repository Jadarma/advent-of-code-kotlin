@file:Suppress("UnstableApiUsage")

rootProject.name = "advent-of-code-kotlin"
include(":aockt-core", ":aockt-test", ":example")

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
