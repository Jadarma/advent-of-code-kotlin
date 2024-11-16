# Debugging Solutions

Some puzzles are tricky, you are _so close_ to the right solution but there's a pesky bug hiding in plain sight.

To hunt for it, it's better to make use of the debugger than to spam your code with `.also(::println)`!

However, there's a problem.
If you run the whole `AdventSpec` in debug mode, you might not trigger the breakpoint when you expect, because the
solution is run against multiple inputs and examples.

AocKt _does not address this issue directly_, but you can easily work around it!

<link-summary rel="summary"/>
<tldr id="summary">
    AocKt only automates unit tests, but you can easily define isolated debug runs yourself.
</tldr>

## Main Function

You can define a main function near your solution and call it manually.
IntelliJ will offer a gutter icon, right click it and run it with the debugger:

```kotlin
fun main() {
    Y9999D01.partOne("<handcrafted input or edge case>")  
} 

object Y9999D01 : Solution { /* ... */ }
```

## Test Function

The same can be achieved from the test source-set if you like.

Since an `AdventSpec` is just a specialization of a `FunSpec`, you can register other tests too!
If you use the [Kotest IntelliJ Plugin](https://kotest.io/docs/intellij/intellij-plugin.html), you can run individual
tests from the gutter icons.
Therefore, you can simply define a separate test within your spec, and use the gutter icon to run that with the debugger.

The `AdventSpec` exposes your implementation in the `solution` property, so you can call it manually with the input
you wish to test.

```kotlin
@AdventDay(9999, 1, "Magic Numbers")
class Y9999D01 : AdventSpec<Y9999D01>({
    
    test("debug") {
        solution.partOne("<handcrafted input or edge case>")
    }
    
    // ...
})
```