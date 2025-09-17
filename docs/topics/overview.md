# Features and Overview

AocKt _(short for Advent of Code - Kotlin)_ is a simple library that makes running and testing your Kotlin solutions to
[Advent of Code](https://adventofcode.com) puzzles a breeze.

It is an opinionated testing framework built on [Kotest](https://kotest.io/) that defines a new `AdventSpec` specialized
for testing AoC puzzle solutions with minimal boilerplate.

## ✨ Features

- **Completely Offline** - Puzzle inputs and solutions are read from local files, no need for tokens.
- **Test-Driven** - Run your code from unit tests for faster feedback loops and fearless refactorings.
- **DSL-Driven** - Define your test cases with minimal code.
- **Configurable** - You decide what runs and when using optional parameters.
- **Minimal** - The test framework is the only non-Kotlin dependency.

## ⚡ Quick Start

<tabs>
<tab id="template" title="AocKt Template Project">

For your convenience, there is an
<a href="https://github.com/Jadarma/advent-of-code-kotlin-template"><code>advent-of-code-kotlin-template</code></a>
repository which you can use to generate your own solutions repo.
It comes with a pre-configured Gradle project with all bells and whistles you might need, as well as a modified source
structure for easier navigation.

_(If you need a working example, check out [my solutions repo](https://github.com/Jadarma/advent-of-code-kotlin-solutions).)_

</tab>
<tab id="standalone" title="Standalone Gradle Project">

To add AocKt to your existing project, simply add the dependencies and configure your unit tests to run with Kotest:

```kotlin
plugins {
    kotlin("jvm") version "%kotlin-version%"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.jadarma.aockt:aockt-core:%aockt-version%")
    testImplementation("io.github.jadarma.aockt:aockt-test:%aockt-version%")
    testImplementation("io.kotest:kotest-runner-junit5:%kotest-version%")
}

tasks.test {
    useJUnitPlatform()
}
```
</tab>
<tab id="snapshot" title="SNAPSHOT Builds">

To consume pre-release builds, you must register the snapshot repository.\
Replace `x.x.x-SNAPSHOT` with the version from the badge below _(if it exists)_:

![Maven SNAPSHOT](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fio%2Fgithub%2Fjadarma%2Faockt%2Faockt-test%2Fmaven-metadata.xml&strategy=latestProperty&style=flat-square&logo=apachemaven&logoColor=orange&label=Snapshot&color=orange)

```kotlin
plugins {
    kotlin("jvm") version "%kotlin-version%"
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://central.sonatype.com/repository/maven-snapshots/")
        content { includeGroup("io.github.jadarma.aockt") }
    }
}

dependencies {
    implementation("io.github.jadarma.aockt:aockt-core:x.x.x-SNAPSHOT")
    testImplementation("io.github.jadarma.aockt:aockt-test:x.x.x-SNAPSHOT")
    testImplementation("io.kotest:kotest-runner-junit5:%kotest-version%")
}

tasks.test {
    useJUnitPlatform()
}
```

**PS:**
You can also [submit an issue](https://github.com/Jadarma/advent-of-code-kotlin/issues) if you find bugs or have suggestions.
Thank you for trying out the latest development build! 💚

</tab>
</tabs>

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

<seealso style="cards">
  <category ref="related">
    <a href="workflow.md"/>
    <a href="project-extension.md"/>
  </category>
</seealso>
