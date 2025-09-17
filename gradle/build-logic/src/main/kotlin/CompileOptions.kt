import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

/**
 * Variables to use for various compilation configurations, to allow them to be referenced from precompiled scripts and
 * be changed globally.
 */
object CompileOptions {

    object AocKt {
        const val GROUP_ID: String = "io.github.jadarma.aockt"
        const val CURRENT: String = "0.2.1" // Last released version.
        const val NEXT: String = "0.3.0" // Current snapshot.
    }

    object Java {
        val languageVersion: JavaLanguageVersion = JavaLanguageVersion.of(21)
        val jvmVendor: JvmVendorSpec = JvmVendorSpec.ADOPTIUM
    }

    object Kotlin {
        val languageVersion: KotlinVersion = KotlinVersion.KOTLIN_2_2
        val apiVersion: KotlinVersion = KotlinVersion.KOTLIN_2_0
        val jvmTarget: JvmTarget = JvmTarget.fromTarget(Java.languageVersion.asInt().toString())
    }
}
