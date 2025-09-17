# Project Extension

<link-summary rel="summary"/>
<tldr id="summary">
    You can register the AocKt extension to configure test execution and extend functionality.
</tldr>

Registering the extension is optional, but recommended.
It offers the following features:

- **Global Configuration**

    You can configure your own defaults for [test execution](test-config.md) parameters.
    Otherwise, the same defaults will be used.
    See [below](#configuration-properties) for a detailed description of each parameter.

- **Display Name Formatting**

    All `AdventSpec`s will have a nicely formatted display name derived from their `@AdventDay` annotation.
    For example, `@AdventDay(2015, 1, "Not Quite Lisp", "FP")` will become
    `Y2015D01: Not Quite Lisp (FP)`.
    All other specs and tests follow the normal Kotest formatting rules.

## Registering The Extension

To register it, add it to your Kotest [project level config](https://kotest.io/docs/framework/project-config.html):

```kotlin
package my.aoc

import io.github.jadarma.aockt.test.AocKtExtension
import io.github.jadarma.aockt.test.ExecMode
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension

object TestConfig : AbstractProjectConfig() {
    override val extensions = listOf<Extension>(
        AocKtExtension()
    )
}
```

To make Kotest use this configuration, you must register it in the 
[`kotest.properties`](https://kotest.io/docs/intellij/intellij-properties.html#specifying-the-properties-filename)
file in your `src/test/resources`:

```properties
kotest.framework.config.fqn=my.aoc.TestConfig
kotest.framework.classpath.scanning.autoscan.disable=true
kotest.framework.classpath.scanning.config.disable=true
```

You should also register the fqn as a system property for Gradle:

```kotlin
tasks.test {
    systemProperty("kotest.framework.config.fqn", "my.aoc.TestConfig")
}
```



## Configuration Properties

Preferences that can be set as constructor arguments.

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
