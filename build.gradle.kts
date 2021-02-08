import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") apply false
}

subprojects {
    tasks {
        withType<Test> {
            useJUnitPlatform()
        }

        withType<KotlinCompile>().configureEach {
            kotlinOptions.apply {
                jvmTarget = "1.8"
                languageVersion = "1.4"
            }
        }
    }
}
