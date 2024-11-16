# Project-Wide Config

AocKt provides an extension you may register in your Kotest project.
Registering is optional, but recommended.

To register it, add it to your Kotest [project level config](https://kotest.io/docs/framework/project-config.html).
Simply using the code below should work, although it's also recommended to disable classpath scanning and register the
config class manually via `kotest.properties`.

```kotlin
import io.github.jadarma.aockt.test.AocKtExtension
import io.github.jadarma.aockt.test.ExecMode
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import kotlin.time.Duration.Companion.seconds

object TestConfig : AbstractProjectConfig() {
    override fun extensions() = listOf<Extension>(
        AocKtExtention(
            formatAdventSpecNames = true,
            efficiencyBenchmark = 15.seconds,
            executionMode = ExecMode.All,
        ),
    )
}
```

> The constructor parameters provided above are also the default values.

<link-summary rel="summary"/>
<tldr id="summary">
    You can register a Kotest extension to apply the same default settings to all the puzzle tests in your project.
</tldr>

## Configuration Properties

### `formatAdventSpecNames`

When the `AocKtExtension` is registered, it formats the names of all `AdventSpec` classes based on the data provided in
the `AdventDay` annotation to be more pleasing to view in the test runner.

Set this to `false` in order to opt out of this behavior.

For example, the following class will be formatted as `Y2015D01: Not Quite Lisp` instead of `Y2015D01Test`.

```kotlin
@AdventDay(2015, 1, "Not Quite Lisp")
class Y2015D01Test : AdventSpec<Y2015D01>({ /* ... */ })
```

When using [multiple solutions](multiple-solutions.md), the variant name will also be included in parentheses to help
distinguishing them.

### `efficiencyBenchmark`

Only applies to tests against user input, not examples.
If the solution completes under this time value, it will pass the efficiency test.

You can lower this value if you want to further challenge yourself, but careful when going too low, as JVM
execution times depend on warm-up and might lead to flaky tests.

The default value is 15 seconds, a reference to the [about page](https://adventofcode.com/about), which states that
_"every problem has a solution that completes in at most 15 seconds on ten-year-old hardware"_, if you go above that it
usually means you did not find the intended solution.

### `executionMode`

Determines which tests will run.

Possible values are:

- `All`: Runs all the tests it can find. The default behavior.
- `ExamplesOnly`: Will not run against user inputs even if they are present.
  Useful when running a project with encrypted inputs _(e.g.: running a clone of someone else's solution repo)_.
- `SkipExamples`: Will only run against user inputs even if examples are defined.
  Useful when all you care about is ensuring your solutions still give the correct answer.
