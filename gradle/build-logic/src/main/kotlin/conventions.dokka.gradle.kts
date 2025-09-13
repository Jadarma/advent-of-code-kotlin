import org.gradle.internal.extensions.stdlib.capitalized
import org.jetbrains.dokka.gradle.engine.parameters.DokkaSourceSetSpec
import org.jetbrains.dokka.gradle.engine.parameters.KotlinPlatform
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import java.time.LocalDate

plugins {
    id("org.jetbrains.dokka")
}

dokka {
    moduleName = "AocKt " + project.name
        .removePrefix("aockt-")
        .replace('-', ' ')
        .capitalized()

    dokkaSourceSets.named<DokkaSourceSetSpec>("main") {

        val docVersion = buildVersion.get()
        if (docVersion.isRelease) {
            sourceLink {
                val sourceTree = "v${docVersion.version}"
                val subProject = project.name
                remoteUrl("https://github.com/Jadarma/advent-of-code-kotlin/tree/$sourceTree/$subProject")
                remoteLineSuffix = "#L"
            }
        }

        analysisPlatform = KotlinPlatform.JVM
        reportUndocumented = true
        documentedVisibilities = setOf(VisibilityModifier.Public)
        enableJdkDocumentationLink = false
        enableKotlinStdLibDocumentationLink = false
        jdkVersion = CompileOptions.Java.languageVersion.asInt()
        languageVersion = CompileOptions.Kotlin.languageVersion.version
        apiVersion = CompileOptions.Kotlin.apiVersion.version
    }

    dokkaPublications.html {
        offlineMode = true
        suppressObviousFunctions = true
        suppressInheritedMembers = true
    }

    pluginsConfiguration.html {
        separateInheritedMembers = true
        homepageLink = "https://jadarma.github.io/advent-of-code-kotlin"
        footerMessage = "(c) 2020-${LocalDate.now().year} Dan Cîmpianu"
    }

    dokkaGeneratorIsolation = ClassLoaderIsolation()
}

tasks.register<Jar>("javadocJar") {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles the Javadoc Jar."
    from(tasks.dokkaGeneratePublicationHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}
