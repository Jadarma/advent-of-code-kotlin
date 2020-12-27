# Advent Of Code - Kotlin

My solutions to [Advent of Code](https://adventofcode.com/), using idiomatic Kotlin and my very own helper framework
to make everything clean and easy to use.

_"Anything that's worth doing, it's worth overdoing."_

---

## Usage Guide

### Use as Template
This project already comes with my own solutions.
Fortunately, you can very easily remove them and implement them yourself.

Either clone the `clean-template` branch, or just delete my implementations with:

```shell
rm -rf ./solutions/src/main/kotlin/y* ./solutions/src/main/resources/aoc/input/y*
```

More about what these paths mean will be explained shortly.

### Project Structure
To add your solutions you only need to add the input and write the implementation in the `:solutions` subproject.
The `:core` subproject only contains the _"framework"_ and automation helpers.
You don't need to touch anything here unless you want to add new features.

Here is a sample tree of the `:solution` subproject source set.
```
src
├── main
│  ├── kotlin
│  │  ├── y2015
│  │  │  └── Y2015D01.kt
│  │  └── Main.kt
│  └── resources
│     └── aoc
│        └── input
│           └── y2015
│              └── input_Y2015D01.txt
└── test
   └── kotlin
      └── y2015
         └── Y2015D01Test.kt
```

There are a few simple key points here:
- Solutions go into `src/main/kotlin`.
  While it is not a requirement to split them into yearly packages, it is recommended.
  There is also no requirement for the file/class name, but keeping it simple and consistent helps with organising.
- Unit tests go into `src/test/kotlin` and should have the same structure as the `main` source set.
- Input data goes into `src/main/resources`.
  The format for these ***is enforced*** and it needs to follow the `/aoc/input/y[year]/input_Y[year]D[paddedDay].txt`
  format, otherwise it will not be recognised!
- Only classes from `aoc.solution.*` package will be taken into consideration!
  This is already the default package of the source set (defined in `Main.kt`).
  If the runner doesn't find it, this is most likely the cause.

### Implementing Solutions

All solutions are automatically registered if they implement the base `aoc.core.AdventDay` class, which only requires
the `year`, `day`, and optionally `title`(be a good sport and add it, it will look nice).

After which you only need to override the `partOne` and `partTwo` functions, which already supply you with the input
(provided it exists in the resources).

A minimal example:
```kotlin
class  Y2015D01 : AdventDay(2015, 1, "Not Quite List") {
    override fun partOne(input: String) = "Won't spoil it here"
    override fun partTwo(input: String) = "But you get the idea."
}
```

**PRO-TIP**: The given `input` is the entire contents of the file, but you sometimes need to read it line by line.
For this, don't use `input.split('\n')`, you have the more idiomatic `input.lines()`.
Even better,  use `input.lineSequence()` for more efficiency when many stream operators are needed.

### Unit Testing Solutions

Before jumping straight to running the code and seeing what sticks, you could try writing some unit tests to validate
the short examples provided in the problem descriptions.

There is already a base test class provided for you: `aoc.core.test.AdventTest`.
Simply extend your test class with it and provide your implementation:

```kotlin
class Y2015D01Test : AdventTest(Y2015D01())
```

From here you can easily write your unit tests (using `kotlin.test` or `junit5`).
You can call `partOne` and `partTwo` directly from the test class, no need to use it on an instance, they are delegated
under the hood.

```kotlin
class Y2015D01Test : AdventTest(Y2015D01()) {
    
    @Test
    fun `Generic foo assertion`() = assertEquals("bar", partOne("foo"))
}
```

If you run it you will see you already have two more tests that are ignored, responsible for asserting the validity of
the actual input.
After you solve the problem and obtain the correct answer(s), you may pass it to the test class, and it will
automatically check that the implementation continues to produce the same output, using the same input file from the
resources directory.
This is helpful if you want to later refactor your code and not worry about breaking something.

```kotlin
class Y2015D01Test : AdventTest(Y2015D01(), "foo", "bar") // <- The real answers.
```

### Running Solutions

You can run any solution using the Gradle run task.
It is up to you whether you want to use the terminal or create a run configuration in IntelliJ.

By default, `./gradlew run` will execute the last chronological day's solution, however you can customize this and run
a specific one, an entire year, or all of them using the command line options, for example, the next command will run
the second day of 2015's advent one hundred times (repeating averages the execution times, so you can conduct useless
benchmarks for fun!)

```shell
./gradlew run --args="--day 2015 2 --repeat 100"
```

You can see all available options with the `--help` flag.

The printed report for each advent day looks like this:

```
 Y2015D01 │                          Not Quite Lisp                           
──────────┼────────┬───────────────────────────────────────────┬───────────────
 Part     │ Result │ ResultData                                │ Average Time  
══════════╪════════╪═══════════════════════════════════════════╪═══════════════
 Part 1   │ Pass   │ 1337                                      │ 12.7ms        
──────────┼────────┼───────────────────────────────────────────┼───────────────
 Part 2   │ Pass   │ 1234                                      │ 483us         
                                      @see: https://adventofcode.com/2015/day/1
```

The `Result` column can be one of the following:
- `n/a` - The function was not implemented.
- `Pass` - The function returned without errors.
  *Note:* This is not a unit test!
  The answer given might still be wrong.
- `Warn` - The function returned without errors, but not always with the same answer.
  This can only happen when using the `--repeat` option, and shows that you probably have accidental randomness issues.
- `Fail` - The test errored out. The exception type and short message is given.
  There is no stacktrace! Use unit tests with debugging for that.

**PRO-TIP:** Since Gradle pipes the output of our "console app", ANSI color codes are disabled by default.
You can get a much prettier output by adding "--color=always" to your argument list!
