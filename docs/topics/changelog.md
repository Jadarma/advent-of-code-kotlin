# Change Log

## 0.3.0-SNAPSHOT

- Updated minimum JDK to 21.
- Updated Kotlin to `2.2.10` and Kotest to `6.0.3`.
- There are no breaking changes to AocKt itself, but there are for configuring 
  [Kotest 6.0](https://kotest.io/docs/release6).
  If you use the `AocKtExtension`, see [](project-config.md). 

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
