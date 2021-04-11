import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.reflections)
    implementation(libs.clikt)
    implementation(libs.mordant)

    testImplementation(libs.junit.kotlin)
    testRuntimeOnly(libs.junit.engine)
}

configurations.register("testArchive") {
    extendsFrom(configurations.testCompile.get())
}

tasks {
    register<Jar>("testJar") {
        from(project.sourceSets.test.get().output)
        archiveClassifier.set("test")
        description = "Create a JAR from the test source set."
    }

    withType<KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.time.ExperimentalTime",
            "-Xexplicit-api=strict"
        )
    }
}

artifacts {
    add("testArchive", tasks.getByName("testJar"))
}
