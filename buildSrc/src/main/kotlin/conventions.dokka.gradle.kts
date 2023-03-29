import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("org.jetbrains.dokka")
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets.configureEach {
        jdkVersion.set(CompileOptions.Java.languageVersion.asInt())
        noStdlibLink.set(true)
    }
}
