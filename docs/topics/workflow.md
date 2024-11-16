# Workflow

After setting up your [project template](https://github.com/Jadarma/advent-of-code-kotlin-template), or configuring
your own, this is the place to start learning all about using AocKt!

This tutorial goes very in-depth to cover all the bases, and also be more accessible to beginners.
Don't be discouraged by its length, you only need it for the first run, after which it will become
reflex.

Good luck!

<link-summary rel="summary"/>
<tldr id="summary">
    This tutorial describes the intended workflow when solving Advent of Code with AocKt.
</tldr>

## Prerequisite: Project Structure

AocKt runs entirely locally, by reading files on disk.
For each puzzle, you will end up with five files, explained later.
For reference, they will be distributed as such:

<tabs>
<tab id="template" title="AocKt Template Project">

```text
projectDir
├── inputs
│  └── aockt
│     └── y2015
│        └── d01
│           └── input.txt
│           └── solution_part1.txt
│           └── solution_part2.txt
├── solutions
│  └── aockt
│     └── y2015
│        └── Y2015D01.kt
└── tests
   └── aockt
      └── y2015
         └── Y2015D01Test.kt
```
</tab>
<tab id="standalone" title="Standalone Project">

```text
projectDir
├── src/main/kotlin
│  └── aockt
│     └── y2015
│        └── Y2015D01.kt
├── src/test/kotlin
│  └── aockt
│     └── y2015
│        └── Y2015D01Test.kt
└── src/test/resources
   └── aockt
      └── y2015
         └── d01
            └── input.txt
            └── solution_part1.txt
            └── solution_part2.txt
```

> Therein after these source sets will be referred by an alias:
> - `solutions` is your `src/main/kotlin`
> - `tests` is your `src/test/kotlin`
> - `inputs` is your `src/test/resources`

</tab>
</tabs>

- Your implementations go into `solutions`.
  While it is not a requirement to split them into packages by year or have a specific naming convention, doing so
  helps with organising.
- Unit tests go into `tests` and should have the same structure as your `solutions`.
- Your puzzle inputs go into `inputs`. 
  For these, ***the format is enforced***:
    - Each day has a directory as a base path: `/aockt/y[year]/d[twoDigitDay]`
    - In that directory, the input is in the `input.txt` file.
    - The solutions use `solution_part1.txt` and `solution_part2.txt`.
      They are added in as discovered.

> ***DO NOT Commit Puzzle Inputs!***
> 
> _It is against the rules to redistribute your puzzle inputs!_
>
> Use something like [`git-crypt`](https://github.com/AGWA/git-crypt) to ensure inputs can only be read by you if you
> plan on sharing your repository publicly.
> 
> _(In the template project, inputs are git-ignored by default.)_
> {style="warning"}

## Step 0: Read And Understand The Puzzle

Have a thorough read of the puzzle.
The devil's in the details, and also have a look at your puzzle input.
Sometimes you'll get extra insight by observing patterns in inputs!

After you think you have a clear initial understanding, move on.

## Step 1: Get Your Puzzle Input

Copy your puzzle input and paste it in `inputs/aockt/y2015/d01/input.txt`.

## Step 2: Create Your Solution Stubs

Create a `class` or `object` for your solution that implements the `Solution` type.


In your `solutions`, create:

```kotlin
package aockt.y2015

import io.github.jadarma.aockt.core.Solution

object Y2015D01 : Solution
```

> Your class _must have_ a zero-arg constructor.

Following the same logic, create its `tests` analog:

```kotlin
package aockt.y2015

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2015, 1, "Not Quite Lisp")
class Y2015D01Test : AdventSpec<Y2015D01>({})
```

> The `@AdventDay` annotation is required.
> Pass your solution class as the type argument of the `AdventSpec<T>`.

## Step 3: Define your Test Cases

Inside the `AdventSpec`'s constructor, you pass in the configuration DSL.
Each puzzle part has its own context.

Let's start with part one:

```kotlin
package aockt.y2015

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2015, 1, "Not Quite Lisp")
class Y2015D01Test : AdventSpec<Y2015D01>({
    partOne()
})
```

Like many other puzzles, this day provides you with example inputs and outputs to test your solution.

If that is the case, you can define them in a lambda.
The syntax is `"string" shouldOutput "output"`, the output can be a string, or a number, it will be checked against its
string representation.
For multiple inputs, `listOf("string") shouldAllOutput "output"` is synonymous.

```kotlin
package aockt.y2015

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2015, 1, "Not Quite Lisp")
class Y2015D01Test : AdventSpec<Y2015D01>({
    partOne {
        listOf("(())", "()()") shouldAllOutput 0
        listOf("(((", "(()(()(", "))(((((") shouldAllOutput 3
        listOf("())", "))(") shouldAllOutput -1
        listOf(")))", ")())())") shouldAllOutput -3
    }
})
```

It is recommended you use the [Kotest IntelliJ Plugin](https://kotest.io/docs/intellij/intellij-plugin.html), so you
can run the test via the gutter icon.

Your first run should look like this:

<img src="workflow-run-1.png" alt="Results of the initial run."/>

Each example has its own test, and since you added your input, it also tests against that.

> If you're not interested in running on our input until validating the examples, you can
> [ignore your input](test-config.md#executionmode) by setting:
> 
> `partOne(executionMode = ExecMode.ExamplesOnly)`
> {style="note"}

## Step 4: Implement Your Solution

Go back to your solution and implement `partOne`.
The function can return anything representable by a string.
In practical terms, it will always be either a string or an integer.

Have a try at the problem:

```kotlin
package aockt.y2015

import io.github.jadarma.aockt.core.Solution

object Y2015D01 : Solution {
    override fun partOne(input: String): Int = spoilers()
}
```

> In some puzzles parsing the input into some abstract data structures is necessary.
> A good tip is to write parsing logic separately so that it may be used by both parts:
>  ```kotlin
>  private fun parse(input: String): Something = input.spoilers()
>  override fun partOne(input: String): Int = parse(input).spoilers()
>  ```
> {style="note"}

## Step 5: Test Your Solution

Run the test again as you go.

> You should be able to run the test again using the `Ctrl+F5` shortcut.
> {style="note"}

<img src="workflow-run-2.png" alt="Results of the second run."/>

Hmm, seems like we're missing something.
Clicking on each example will show us the actual versus expected result.
In this case, seems we have a sign flip issue.

<img src="workflow-run-3.png" alt="Results of the third run."/>

That was it!

## Step 5: Submit Your Answer

Now that you're confident your solution is correct, run it on your own input _(by reverting the `executionMode` if you
set it earlier)_.

<img src="workflow-run-4.png" alt="Results of user input."/>

Your solution gave an answer, but you don't know if it's correct.
Go back to the website and submit it.

> Unfortunately, currently you cannot copy-paste from the UI, you'll have to do it the old way.

**Congratulations on collecting the first star!** ⭐

If not, try to look at what's special in your input, or hunt down any bugs.

Once you have verified the right answer, paste it in `inputs/aockt/y2015/d01/solution_part1.txt`.
Now, when you run the test, your code will be tested against the known answer:

<img src="workflow-run-5.png" alt="Results of verified solution."/>

## Step 6: Tackle Part Two

There is one more star to be gained.
The website will give you a second task based on the same puzzle.

You can ignore part one tests for now, and [set](test-config.md#enabled) `partOne(enabled = false)`.

Then repeat the same steps:
- Implement `fun partTwo(input: String)` in your solution.
- Define your test with `partTwo()` and define any new examples.
- Run the tests until you get a solution candidate.
- After getting the right answer, save it to `solution_part2.txt`.

**Congratulations on collecting both stars!** ⭐⭐

## Step 7: Refactor Fearlessly _(and Optionally)_

After completing the puzzle, your test should look like this:

```kotlin
package aockt.y2015

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2015, 1, "Not Quite Lisp")
class Y2015D01Test : AdventSpec<Y2015D01>({

    partOne {
        listOf("(())", "()()") shouldAllOutput 0
        listOf("(((", "(()(()(") shouldAllOutput 3
        listOf("())", "))(") shouldAllOutput -1
        listOf(")))", ")())())") shouldAllOutput -3
    }

    partTwo {
        ")" shouldOutput 1
        "()())" shouldOutput 5
    }
})
```

And your test runs like this:

<img src="workflow-run-6.png" alt="Results of both verified solutions."/>

> Your runs might be collapsed since they're all passing.
> You can expand them all with <shortcut>Ctrl+Plus</shortcut> to get the above view.
> {style="note"}

Now you can save your changes, clean up your code, refactor some lines, if that's your thing.
Run tests often, they will help you know when you made a logic-altering change.

*That's it! Now relax and get ready for the next day!*

<seealso style="cards">
  <category ref="related">
    <a href="project-config.md"/>
    <a href="test-config.md"/>
    <a href="debugging.md"/>
  </category>
</seealso>