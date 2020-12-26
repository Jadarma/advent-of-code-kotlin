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
