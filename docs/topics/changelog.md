# Change Log

## 0.4.0-SNAPSHOT

- Update Kotlin to `2.3` and Kotest to `6.1`.
- Changed package structure. The `core` and `test` packages are gone and instead both should be replaced by just
  `io.github.jadarma.aockt`.
- The `Expensive` tag object now has internal visibility.
- Fix issue wherein solutions completing exceptionally would not fail the test.
- Reading test data now only trims new lines at end of file instead of all whitespace.
  This makes some problems, like [Y2025D06](https://adventofcode.com/2025/day/6) easier to parse.
- An `AdventSpec` missing its `AdventDay` annotation will now only fail the spec instead of the entire suite.

## 0.3.0

_[Released 2025-11-19](https://github.com/Jadarma/advent-of-code-kotlin/releases/tag/v0.3.0)_

- New minimum requirements: JDK 21, Kotlin `2.0`, and Kotest `6.0`.
  Check the [release notes](https://kotest.io/docs/release6), and how to [configure](project-extension.md) the extension.
- Removed `formatAdventSpecNames` configuration property.
  Formatting is automatically enabled when using the project extension.
- Advent specs are guaranteed to be executed chronologically when the project extension is registered.
- Spec definition now uses a dedicated DSL.
- Isolated debug runs can be configured from the DSL.

## 0.2.1

_[Released 2024-11-17](https://github.com/Jadarma/advent-of-code-kotlin/releases/tag/v0.2.1)_

- Remove `suspend` modifier from part DSL functions.
  It caused a false positive error in IntelliJ, but it was not needed anyway.

## 0.2.0

_[Released 2024-11-17](https://github.com/Jadarma/advent-of-code-kotlin/releases/tag/v0.2.0)_

- Updated Kotlin to `1.9.25` and Kotest to `5.9.1`.
- Fixed an issue with spec name formatting.
- Restricted the part DSLs to only call DSL functions.

## 0.1.0

_[Released 2023-08-05](https://github.com/Jadarma/advent-of-code-kotlin/releases/tag/v0.1.0)_

This is the first public release of the AocKt packages, offering all the base functionality.
The dependencies can now be fetched from `mavenCentral()`.
