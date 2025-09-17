# Multiple Solutions

You sometimes might want to implement multiple solutions to the same puzzle.
For example to compare imperative vs. functional code styles, or test different data structures, or algorithms.

Since all these different implementations solve the same puzzle, they should have identical test cases.
The `AdventSpec` is designed to test a single `Solution` at a time.
However, that doesn't mean you need to duplicate the test code!
You can instead define an abstract specification for your test cases, and use it to derive test classes for however many
implementations!

<link-summary rel="summary"/>
<tldr id="summary">
    You can use abstract classes to define the same test cases for multiple implementations.
</tldr>

### Example

Let's learn by _(trivial)_ example.
Assume the fictitious puzzle, in which part one is supposed to add numbers, and part two to multiply them.
We have two implementations _(omitted for brevity)_, `SolutionA` and `SolutionB`.

The test classes are defined in the same way, each annotated by an `@AdventDay` annotation.
However, instead of extending an `AdventSpec` directly, they instead extend an intermediary abstract class, which
defines the test cases once.

This method ensures all solutions to a puzzle run against the same tests, and you can add as many
solutions as you want, at the low-low cost of two lines of boilerplate a piece.

Full code sample:

```kotlin
object SolutionA : Solution { /* ... */ }
object SolutionB : Solution { /* ... */ }

@AdventDay(9999, 1, "Magic Numbers", "Variant A")
class SolutionATest : Y9999D01Spec<SolutionA>()

@AdventDay(9999, 1, "Magic Numbers", "Variant B")
class SolutionBTest : Y9999D01Spec<SolutionB>()

abstract class Y9999D01Spec<T : Solution> : AdventSpec<T>({
    partOne { "1,2,3,4" shouldOutput 10 }
    partTwo { "1,2,3,4" shouldOutput 24 }
})
```

> You can provide a `variant` argument to the `@AdventDay` annotation.
> This is purely for aesthetics when formatting the display name.
> {style="note"}
