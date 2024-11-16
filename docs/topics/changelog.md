# Change Log

## 0.2.0-SNAPSHOT 🚧

_(Coming Soon)_

- Updated Kotlin to `1.9.25` and Kotest to `5.9.1`.
- Fixed an issue with spec name formatting.
- Restricted the part DSLs to only call DSL functions.

Consider trying the snapshot build and providing feedback!

```kotlin
repositories {
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
}
dependencies {
    implementation("io.github.jadarma.aockt:aockt-core:0.2.0-SNAPSHOT")
    testImplementation("io.github.jadarma.aockt:aockt-test:0.2.0-SNAPSHOT")
}
```

## 0.1.0

_[Released 2023-08-05](https://github.com/Jadarma/advent-of-code-kotlin/releases/tag/v0.1.0)_

This is the first public release of the AocKt packages, offering all the base functionality.
The dependencies can now be fetched from `mavenCentral()`.
