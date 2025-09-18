# Change Log

## 0.3.0-SNAPSHOT

- New minimum requirements: JDK 21, Kotlin `2.0`, and Kotest `6.0`.
  Check the [release notes](https://kotest.io/docs/release6), and how to [configure](project-extension.md) the extension.
- Removed `formatAdventSpecNames` configuration property.
  Formatting is automatically enabled when using the project extension.
- Advent specs are guaranteed to be executed chronologically when the project extension is registered.

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
