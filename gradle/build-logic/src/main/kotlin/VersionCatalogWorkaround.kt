import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName

/**
 * VooDoo magic workaround for accessing Gradle Version Catalogs from within precompiled convention scripts.
 * Also see: https://github.com/gradle/gradle/issues/15383
 */
val Project.libs: LibrariesForLibs get() =
    rootProject.project.extensions.getByName<LibrariesForLibs>("libs")
