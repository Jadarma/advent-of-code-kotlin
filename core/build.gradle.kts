import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

dependencies {
    val clicktVersion: String by project
    val reflectionsVersion: String by project
    val mordantVersion: String by project
    implementation(kotlin("reflect"))
    implementation("org.reflections:reflections:$reflectionsVersion")
    implementation("com.github.ajalt.clikt:clikt:$clicktVersion")
    implementation("com.github.ajalt.mordant:mordant:$mordantVersion")

    val junitEngineVersion: String by rootProject
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitEngineVersion")
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
            "-Xopt-in=org.mylibrary.OptInAnnotation",
            "-Xexplicit-api=strict"
        )
    }
}

artifacts {
    add("testArchive", tasks.getByName("testJar"))
}
