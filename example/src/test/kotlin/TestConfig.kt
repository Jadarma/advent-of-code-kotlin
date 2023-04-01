import io.github.jadarma.aockt.test.AocktDisplayNameExtension
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.engine.test.names.DefaultDisplayNameFormatter

object TestConfig : AbstractProjectConfig() {
    override fun extensions() = listOf(
        AocktDisplayNameExtension(DefaultDisplayNameFormatter())
    )
}
