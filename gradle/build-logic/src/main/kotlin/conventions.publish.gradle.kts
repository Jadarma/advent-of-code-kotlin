plugins {
    `java-library`
    `maven-publish`
    signing
}

java {
    withSourcesJar()
}

// Configure remote Maven repositories for publishing, if any.
if (System.getenv("PUBLISHING_ENABLED") == "true") {
    val isRelease = project.buildVersion.get().isRelease
    val signingKey: String? = System.getenv("SIGNING_KEY")
    val signingPassword: String? = System.getenv("SIGNING_PASSWORD")
    val ossrhUsername: String? = System.getenv("OSSRH_USERNAME")
    val ossrhPassword: String? = System.getenv("OSSRH_PASSWORD")
    val ossrhRepoUrl: String =
        if (isRelease) "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
        else "https://s01.oss.sonatype.org/content/repositories/snapshots/"

    check(ossrhUsername != null && ossrhPassword != null) {
        "Publishing is enabled but credentials for remote repository were not given."
    }

    publishing.repositories.maven {
        url = uri(ossrhRepoUrl)
        credentials { username = ossrhUsername; password = ossrhPassword }
    }

    if (isRelease) {
        check(signingKey != null && signingPassword != null) {
            "A release build with publishing enabled requires signing key credentials."
        }
        signing {
            useInMemoryPgpKeys(signingKey, signingPassword)
            sign(publishing.publications)
        }
    }
} else {
    publishing.repositories.mavenLocal()
}

// Declare the maven publication for the current  build version.
publishing.publications.create<MavenPublication>(project.name) {
    from(components["java"])
    artifact(tasks.named("javadocJar"))

    group = "io.github.jadarma.aockt"
    artifactId = project.name
    version = project.buildVersion.toString()

    pom {
        name.set("Advent of Code Kotlin")
        description.set("Helper test libraries that make implementing Advent Of Code solutions a breeze.")
        url.set("https://jadarma.github.io/advent-of-code-kotlin")

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
