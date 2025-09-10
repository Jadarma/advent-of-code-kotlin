# Advent of Code - Kotlin (AocKt)

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-%237F52FF.svg?style=flat-square&logo=kotlin&logoColor=%237F52FF)](https://kotlinlang.org/)
[![Kotest](https://img.shields.io/badge/Kotest-6.0.3-%35ED35.svg?style=flat-square&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiI+PHBhdGggc3R5bGU9ImZpbGw6IzM1ZWQzNSIgZD0iTTEyIDJoNGwtOCA4IDQgNEg0di00TDAgNmg4WiIvPjwvc3ZnPg==)](https://kotest.io/)
[![Build Status](https://img.shields.io/github/actions/workflow/status/Jadarma/advent-of-code-kotlin/build.yml?style=flat-square&logo=github&label=Build&logoColor=%23171515)](https://github.com/Jadarma/advent-of-code-kotlin/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.jadarma.aockt/aockt-test?style=flat-square&color=blue&logo=apachemaven&logoColor=blue&label=Maven%20Central)](https://central.sonatype.com/namespace/io.github.jadarma.aockt)
[![Snapshot](https://img.shields.io/nexus/s/io.github.jadarma.aockt/aockt-test?server=https%3A%2F%2Fs01.oss.sonatype.org&style=flat-square&color=orange&logo=apachemaven&logoColor=orange&label=Snapshot)](https://s01.oss.sonatype.org/content/repositories/snapshots/io/github/jadarma/aockt/)

AocKt _(short for Advent of Code - Kotlin)_ is a simple library that makes running and testing your Kotlin solutions to
[Advent of Code](https://adventofcode.com) puzzles a breeze.

It is an opinionated testing framework built on [Kotest](https://kotest.io/) that defines a new `AdventSpec` specialized
for testing AoC puzzle solutions with minimal boilerplate.

## 📑 Documentation

Visit the [project website](https://jadarma.github.io/advent-of-code-kotlin) for installation instructions,
DSL documentation, workflow guides, advanced configuration options, and more!

## ✨ Features

- **Completely Offline** - Puzzle inputs and solutions are read from local files, no need for tokens.
- **Test-Driven** - Run your code from unit tests for faster feedback loops and fearless refactorings.
- **DSL-Driven** - Define your test cases with minimal code.
- **Configurable** - You decide what runs and when using optional parameters.
- **Minimal** - The test framework is the only non-Kotlin dependency.

## ⚡ Quick Start

<details open>
    <summary>Project Template</summary>

For your convenience, there is an
[advent-of-code-kotlin-template](https://github.com/Jadarma/advent-of-code-kotlin-template) repository which you can
use to generate your own solutions repo.
It comes with a pre-configured Gradle project with all bells and whistles you might need, as well as a
modified source structure for easier navigation.

_(If you need a working example, check out [my solutions repo](https://github.com/Jadarma/advent-of-code-kotlin-solutions).)_

</details>

<details>
    <summary>Standalone Gradle Project</summary>

To add AocKt to your existing project, simply add the dependencies and configure your unit tests to run with Kotest:

```kotlin
plugins {
    kotlin("jvm") version "$kotlinVersion"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.jadarma.aockt:aockt-core:$aocktVersion")
    testImplementation("io.github.jadarma.aockt:aockt-test:$aocktVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
}

tasks.test {
    useJUnitPlatform()
}
```
</details>

## 🧪 Test DSL Overview

AocKt provides the following DSL for testing puzzle solutions:

```kotlin
object Y9999D01 : Solution {                            // 1. 
    override fun partOne(input: String) = spoilers()
    override fun partTwo(input: String) = spoilers()
}

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
   required.
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

## 👥 Contributing

If you'd like to help out:

- Report bugs and submit feature requests via an [issue](https://github.com/Jadarma/advent-of-code-kotlin/issues).
- If this helped you unlock some stars, consider giving one to this repo! ⭐

## ⚖ License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) for details.\
_Advent of Code_ is a registered trademark of Eric K. Wastl in the United States.
