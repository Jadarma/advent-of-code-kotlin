# Advent of Code - Kotlin (AocKt)

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20-%237F52FF.svg?style=flat-square&logo=kotlin&logoColor=%237F52FF)](https://kotlinlang.org/)
[![Kotest](https://img.shields.io/badge/Kotest-5.7.2-%35ED35.svg?style=flat-square&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiI+PHBhdGggc3R5bGU9ImZpbGw6IzM1ZWQzNSIgZD0iTTEyIDJoNGwtOCA4IDQgNEg0di00TDAgNmg4WiIvPjwvc3ZnPg==)](https://kotest.io/)
[![Build Status](https://img.shields.io/github/actions/workflow/status/Jadarma/advent-of-code-kotlin/build.yml?style=flat-square&logo=github&label=Build&logoColor=%23171515)](https://github.com/Jadarma/advent-of-code-kotlin/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.jadarma.aockt/aockt-test?style=flat-square&color=blue&logo=apachemaven&logoColor=blue&label=Maven%20Central)](https://central.sonatype.com/namespace/io.github.jadarma.aockt)
[![Snapshot](https://img.shields.io/nexus/s/io.github.jadarma.aockt/aockt-test?server=https%3A%2F%2Fs01.oss.sonatype.org&style=flat-square&color=orange&logo=apachemaven&logoColor=orange&label=Snapshot)](https://s01.oss.sonatype.org/content/repositories/snapshots/io/github/jadarma/aockt/)

AocKt _(short for Advent of Code - Kotlin)_ is a simple library that makes running and testing your Kotlin solutions to
[Advent of Code](https://adventofcode.com) puzzles a breeze.

It is an opinionated testing framework built on [Kotest](https://kotest.io/) that defines a new `AdventSpec` specialized
for testing AoC puzzle solutions with minimal boilerplate.

## ✨ Features

- *Completely Offline* - Puzzle inputs and solutions are read from local files, no need for tokens.
- *Test Driven* - Run your code from unit tests for faster feedback loops and fearless refactorings.
- *DSL Driven* - Define your test cases with minimal code.
- *Configurable* - You decide what runs and when using optional parameters.
- *Minimal* - The test framework is the only non-Kotlin dependency.

## ⚡ Quick Start

### Gradle

To use AocKt, simply add the dependencies and configure your project to run unit tests with Kotest:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.jadarma.aockt:aockt-core:$aocktVersion")
    testImplementation("io.github.jadarma.aockt:aockt-test:$aocktVersion")
    testImplementation("io.kotest:kotest-runner-junit5:5.7.1")
}

tasks.test {
    useJUnitPlatform()
}
```

### Project Template

For your convenience, there is an
[advent-of-code-kotlin-template](https://github.com/Jadarma/advent-of-code-kotlin-template) repository which you can
use to generate your own solutions repo, featuring a pre-configured Gradle project, modified source directory locations
for easier navigation, and a detailed README with workflow examples.
If you need a working example, you can check out
[my solutions repo](https://github.com/Jadarma/advent-of-code-kotlin-solutions) as well.

## 📄 Usage Guide

<details open>
  <summary>Basic Test Definition</summary>

```kotlin
object Y9999D01 : Solution { /* ... */ }                // 1.

@AdventDay(9999, 1, "Magic Numbers")                    // 2.
class Y9999D01Test : AdventSpec<Y9999D01>({             // 3.
    partOne {                                           // 4.
        "1,2,3,4" shouldOutput 4                        // 5.
        listOf("2", "2,2", "2,4,6,8") shouldAllOutput 0 // 6.
    }
    partTwo()                                           // 7.
})
```

In the above example:

1. Your solution should implement the `Solution` interface.
2. Each test class should be annotated with the `@AdventDay` annotation. Title is optional, but the year and day are
   required, so the spec knows what user input to test with.
3. Rather than passing it as an instance, the `AdventSpec` takes in your solution as a type parameter.
4. Use the `partOne` and `partTwo` functions as needed.
   Inside the lambda you can define test cases.
   The `Solution` functions will only be invoked if the relevant part DSL is used.
   If you have not yet implemented the second part, or it doesn't exist
   _(e.g.: Every year, part two of the last day just requires collecting all other 49 stars)_,
   then you may simply omit it.
5. To define a test case, use the `shouldOutput` function.
   Each usage will define another test case.
   The value tested against is checked against its string value, so `shouldOutput 4` and `shouldOutput "4"` are
   equivalent.
6. As a shorthand for defining multiple examples that should output the same thing, use the `shouldAllOutput` function.
7. If you don't have any examples, but do want to run the part against your input the lambda can be omitted.

</details>

<details>
    <summary>Project Configuration</summary>

AocKt provides an extension you may register in your Kotest project to tweak the behaviour of the `AdventSpec`.
Registering it is optional but recommended, and can be done like any other extension:

```kotlin
object TestConfig : AbstractProjectConfig() {

    override fun extensions() = listOf<Extension>(
        AocKtExtension(),
    )
}
```

The following optional parameters exist:

- *formatAdventSpecNames* - When the extension is registered, `AdventSpec` classes have a pretty formatted name in the
  test runner. Set this to `false` to opt out of this behavior.
- *efficiencyBenchmark* - If a solution completes under this time value, it will pass its efficiency test.
  Lower this value to increase the challenge or increase it to adjust for your hardware *(the latter shouldn't be
  necessary)*.
  Can be overridden for individual parts, see *Execution Configuration for Parts* for more details.
  Default is fifteen seconds.
- *executionMode* - Choose the default execution mode for the entire project (run only examples, only user input, or
  all of them).
  If set to `ExamplesOnly`, does not run against the true puzzle input even if present.
  Useful when running the project with encrypted inputs (e.g. running a clone of someone else's solution repo).
  If set to `SkipExamples`, will only test against user input.
  Can be overridden for individual parts, see *Execution Configuration for Parts* for more details.
  Default is all.

</details>

<details>
    <summary>Testing Against User Input</summary>

By default, only the example defined in the DSL will run.
However, the solution can be tested against real user input if it is detected.
AocKt looks inside the test resources directory for them.
The structure is fixed and must match the following:

```text
testResourcesDir
  └──  aockt
     └── y{year}
        └── d{two-digit-day}
           └── input.txt
           └── solution_part1.txt
           └── solution_part2.txt
```

If the `input.txt` file exists, the `Solution` will be ran against it after the example tests.
It is normal that at first the solutions are unknown, and therefore missing.
The unverified solution will be added in parens at the end of the test name, which you can then submit to the website.

If the `solution_part1` or `solution_part2.txt` exist, then the value contained within them is assumed to be the correct
output when running against `input.txt`, and will be validated.

**IMPORTANT!:** The reason for keeping the user inputs separate from the tests (apart from readability) is that
[puzzle inputs should not be redistributed](https://old.reddit.com/r/adventofcode/wiki/faqs/copyright/inputs).
If you plan on sharing your solutions repository publicly, either `.gitignore` the `src/test/resources/aockt` directory
or commit them as encrypted blobs only you can read!

</details>

<details>
    <summary>Execution configuration for Parts</summary>

The `partOne` and `partTwo` scopes can be configured with optional parameters.
These allow you to modify the way the generated tests behave.
They are mostly meant to help during development, and reverted once your solutions are complete.

The following optional parameters exist:

- *enabled* - True by default. When set to false, all tests for this part are ignored.
  Useful for when working on the second part of a puzzle and want to skip re-checking the first when
  running the spec.

- *expensive* - False by default. Lowers the time restrictions for execution.
  While most puzzles should be solvable in under 15 seconds, sometimes it's hard to come up with an optimised solution
  on the first try.
  This option tags the tests as slow if you want to exclude them from bulk execution.

- *executionMode* - Defaults to project configuration.
  If set to `ExamplesOnly`, does not run against the true puzzle input even if present.
  Useful when refactoring an expensive day and no not wish to waste time on the big test while the small ones do fail.
  If set to `SkipExamples`, does not run against the example test cases even if present.
  Useful for isolating a single execution of the solution, useful when debugging.

- *efficiencyBenchmark* - Defaults to project configuration.
  The maximum runtime a solution can have while being considered efficient by the time tests.
  Only the user input tests are measured.

</details>

<details>
    <summary>Multiple Solutions for the Same Puzzle</summary>

The `AdventSpec` is designed to test a single `Solution` at a time.
However, that doesn't mean you need to duplicate the code if you want to show off multiple approaches to the solution!
You can instead define an abstract specification for your test cases, and use it to derive an arbitrary number of
specific implementation test classes, and supply the `variant` parameter to the `AdventDay` annotation to disambiguate
between the two.

```kotlin
object SolutionA : Solution { /* ... */ }
object SolutionB : Solution { /* ... */ }

@AdventDay(9999, 1, "Magic Numbers", "Variant A")
class SolutionATest : Y9999D01Spec<SolutionA>()

@AdventDay(9999, 1, "Magic Numbers", "Variant B")
class SolutionBTest : Y9999D01Spec<SolutionB>()

abstract class Y9999D01Spec<T : Solution> : AdventSpec<T>({
    val exampleInput = "1,2,3,4"
    partOne { exampleInput shouldOutput 4 }
    partTwo { exampleInput shouldOutput 24 }
})
```

</details>

<details>
    <summary>Workflow Example</summary>

For a complete workflow example, check out the
[project template](https://github.com/Jadarma/advent-of-code-kotlin-template#-workflow-example).
</details>

## 👥 Contributing

If you'd like to help out:

- Report bugs and submit feature requests via an [issue](https://github.com/Jadarma/advent-of-code-kotlin/issues).
- If this helped you unlock some stars, consider giving one to this repo! ⭐

## ⚖ License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) for details.\
_Advent of Code_ is a registered trademark of Eric K Wastl in the United States.
