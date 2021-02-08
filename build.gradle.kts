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
                useIR = true
                jvmTarget = "11"
                languageVersion = "1.4"
            }
        }
    }
}
