plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin.plugin.jvm)
    implementation(libs.detekt.plugin)
    implementation(libs.dokka.plugin)
    // Also see `src/main/kotlin/VersionCatalogWorkaround.kt
    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
