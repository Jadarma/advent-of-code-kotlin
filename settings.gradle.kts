enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "advent-of-code-kotlin"
include("core", "solutions")

pluginManagement {
    // Wish this could be imported from the version catalog.
    val kotlinVersion = "1.4.32"

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}
