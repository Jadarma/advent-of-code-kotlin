# Debugging Solutions

<link-summary rel="summary"/>
<tldr id="summary">
    You can define isolated runs to help with debugging.
</tldr>

Some puzzles are tricky, you are _so close_ to the right solution but there's a pesky bug hiding in plain sight.

To hunt for it, it's better to make use of the debugger than to spam your code with `.also(::println)`!

However, there's a problem.
If you run the whole `AdventSpec` in debug mode, you might not trigger the breakpoint when you expect, because the
solution is run against multiple inputs and examples.

To fix this, you can define isolated runs.

## Debug Scope

In your `AdventSpec`, you can use the `debug` scope, which will provide the instance of the solution, as well as
your puzzle input, if it is available.
When this scope is used, it defines a focused test, meaning any other parts and examples will be ignored and won't run.

You can then use the `AdventSpec` gutter icon to run the test in debug mode.

```kotlin
@AdventDay(9999, 1, "Magic Numbers")
class Y9999D01 : AdventSpec<Y9999D01>({
    
    debug {
        solution.partOne("<handcrafted input or edge case>")
        // or, to run on your actual input
        solution.partOne(input)
    }
    
    // Any other declarations are effectively ignored ...
})
```

## Main Function

Alternatively, you can define a main function near your solution and call it manually.
However, this method requires you to provide the input yourself.

IntelliJ will offer a gutter icon, right click it and run it with the debugger:

```kotlin
fun main() {
    Y9999D01.partOne("<handcrafted input or edge case>")  
} 

object Y9999D01 : Solution { /* ... */ }
```
