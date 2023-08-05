plugins {
    `java-library`
    `maven-publish`
    signing
}

java {
    withSourcesJar()
}

val releaseVersion: String? = System.getenv("RELEASE_VERSION").takeUnless { it.isNullOrBlank() }
val isSnapshot = releaseVersion?.endsWith("-SNAPSHOT") ?: false
val isRelease = !isSnapshot

val ossrhUsername: String? = System.getenv("OSSRH_USERNAME")
val ossrhPassword: String? = System.getenv("OSSRH_PASSWORD")
val credentialsAvailable = ossrhUsername != null && ossrhPassword != null

signing {
    useGpgCmd()
    if(isRelease) sign(publishing.publications)
}

publishing {
    if(credentialsAvailable) {
        repositories.maven {
            url = when(isRelease) {
                true -> uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                false -> uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            }
            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }

    publications.create<MavenPublication>(project.name) {
        from(components["java"])
        artifact(tasks.named("dokkaJavadocJar"))

        group = "io.github.jadarma.aockt"
        artifactId = project.name
        version = releaseVersion

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
