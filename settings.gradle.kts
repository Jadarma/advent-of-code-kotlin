rootProject.name = "advent-of-code-kotlin"
include("core")

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
    }
}
