package io.github.jadarma.aockt.test

import io.github.jadarma.aockt.test.internal.configureAocKtDisplayNameExtension
import io.kotest.core.extensions.ProjectExtension
import io.kotest.core.project.ProjectContext

/**
 * A Kotest Extension to configure the AdventSpecs.
 *
 * To register the extension:
 *
 * ```kotlin
 * object TestConfig : AbstractProjectConfig() {
 *     override fun extensions() = listOf<Extension>(AocktExtension())
 * }
 * ```
 *
 * @property formatAdventSpecNames Whether to pretty print the names of the AdventSpec in the test output.
 *   Enabled by default.
 */
public class AocKtExtension(
    public val formatAdventSpecNames: Boolean = true,
) : ProjectExtension {

    override suspend fun interceptProject(context: ProjectContext, callback: suspend (ProjectContext) -> Unit) {

        if(formatAdventSpecNames) {
            context.configureAocKtDisplayNameExtension()
        }

        callback(context)
    }
}
