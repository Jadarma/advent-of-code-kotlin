plugins {
    `java-library`
    `maven-publish`
    signing
}

java {
    withJavadocJar()
    withSourcesJar()
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

publishing {
    repositories {
        // TODO: Configure Maven Central.
        mavenLocal()
    }

    publications.create<MavenPublication>(project.name) {
        from(components["java"])

        group = "io.github.jadarma.aockt"
        artifactId = project.name
        version = libs.versions.aockt.get()

        pom {
            name.set("Advent of Code Kotlin")
            description.set("Helper test libraries that make implementing Advent Of Code solutions a breeze.")
            url.set("https://github.com/Jadarma/advent-of-code-kotlin")

            scm {
                connection.set("scm:git:git://github.com/Jadarma/advent-of-code-kotlin.git")
                developerConnection.set("scm:git:ssh://github.com/Jadarma/advent-of-code-kotlin.git")
                url.set("https://github.com/Jadarma/advent-of-code-kotlin")
            }

            licenses {
                license {
                    name.set("MIT License")
                    url.set("https://opensource.org/license/mit")
                    inceptionYear.set("2020")
                }
            }

            developers {
                developer {
                    id.set("Jadarma")
                    name.set("Dan Cîmpianu")
                    email.set("dancristiancimpianu@gmail.com")
                }
            }
        }
    }
}
