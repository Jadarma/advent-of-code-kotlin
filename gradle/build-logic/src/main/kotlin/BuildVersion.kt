import CompileOptions.AocKt.CURRENT
import CompileOptions.AocKt.NEXT
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.kotlin.dsl.of

/**
 * A typesafe and validated build version to be shared across the project.
 *
 * @property version The semver value without suffixes. For the actual version, use the [toString] function.
 * @property type    The build type based on environment.
 */
data class BuildVersion(val version: String, val type: Type) {

    enum class Type {
        /** A pre-release build meant for local consumption only. */
        LOCAL,

        /** A pre-release build meant to be publicly shared. */
        SNAPSHOT,

        /** A productive build. */
        RELEASE;

        fun format(version: String): String = when (this) {
            LOCAL -> "$version-LOCAL"
            SNAPSHOT -> "$version-SNAPSHOT"
            RELEASE -> version
        }
    }

    init {
        require(version.matches(semVer)) {
            "Invalid version: $version."
        }
    }

    override fun toString(): String = type.format(version)

    // Convenience helpers.
    @Suppress("unused")
    val isRelease: Boolean get() = type == Type.RELEASE

    @Suppress("unused")
    val isSnapshot: Boolean get() = type == Type.SNAPSHOT

    @Suppress("unused")
    val isLocal: Boolean get() = type == Type.LOCAL

    internal companion object {
        private val semVer = Regex("""^\d+\.\d+\.\d+$""")

        private val versions: Map<Type, BuildVersion> = Type.values().associateWith {
            BuildVersion(
                version = if (it == Type.RELEASE) CURRENT else NEXT,
                type = it,
            )
        }

        fun get(isRelease: Boolean, isPublished: Boolean): BuildVersion = versions.getValue(
            when {
                isRelease -> Type.RELEASE
                isPublished -> Type.SNAPSHOT
                else -> Type.LOCAL
            }
        )
    }
}

/** Provides the build version of the project depending on environment variables. */
internal abstract class BuildVersionValueSource : ValueSource<BuildVersion, ValueSourceParameters.None> {
    override fun obtain(): BuildVersion = BuildVersion.get(
        isRelease = System.getenv("RELEASE_BUILD") == "true",
        isPublished = System.getenv("PUBLISHING_ENABLED") == "true",
    )
}

/** Get the active build version of the project. */
val Project.buildVersion: Provider<BuildVersion>
    get() = providers.of(BuildVersionValueSource::class) {}
