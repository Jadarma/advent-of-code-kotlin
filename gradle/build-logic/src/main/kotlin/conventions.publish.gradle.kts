import CompileOptions.AocKt.GROUP_ID
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm

plugins {
    id("com.vanniktech.maven.publish")
}

mavenPublishing {

    if(System.getenv("PUBLISHING_ENABLED") == "true") {
        publishToMavenCentral(automaticRelease = false)
        signAllPublications()
    }

    configure(
        KotlinJvm(
            sourcesJar = true,
            javadocJar = JavadocJar.Dokka("dokkaGeneratePublicationHtml"),
        )
    )

    coordinates(
        groupId = GROUP_ID,
        artifactId = project.name,
        version = buildVersion.get().toString(),
    )

    pom {
        name = "Advent of Code Kotlin"
        description = "A simple library that makes running and testing your Kotlin solutions to Advent of Code puzzles a breeze."
        url = "https://jadarma.github.io/advent-of-code-kotlin"
        inceptionYear = "2020"

        scm {
            url = "https://github.com/Jadarma/advent-of-code-kotlin"
            connection = "scm:git:git://github.com/Jadarma/advent-of-code-kotlin.git"
            developerConnection = "scm:git:ssh://github.com/Jadarma/advent-of-code-kotlin.git"
        }

        developers {
            developer {
                id = "Jadarma"
                name = "Dan Cîmpianu"
                url = "https://github.com/Jadarma"
                email = "dancristiancimpianu@gmail.com"
            }
        }

        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/license/mit"
            }
        }
    }
}
