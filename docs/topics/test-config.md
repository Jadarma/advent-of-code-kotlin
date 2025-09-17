# Per-Test Config

<link-summary rel="summary"/>
<tldr id="summary">
    You can use optional parameters in the DSL to tweak individual puzzle runs.
</tldr>

The puzzle part DSL accepts optional properties to allow fine-grained control over test execution and behavior.
They are meant to be used as temporary knobs and dials during puzzle solving and refactoring.

## Configuration Properties

### `enabled`

Set to `false` to disable all tests related to this puzzle.

Useful when you want to focus on the second part once the first one is solved, or ignoring the second part when
refactoring the first, without resorting to commenting out the DSL.

### `expensive`

Marks a solution as known to be inefficient.

There are hard days when you just want to get it over with for now, and you result to brute force, or simply not enough
optimisations.
Parts that are marked as expensive will skip the efficiency benchmark test.

> Expensive tests are also tagged.
> If you automate your test runs, you may [tell Kotest to skip some tests](https://kotest.io/docs/framework/tags.html#running-with-tags):
> ```shell
> gradle test -Dkotest.tags="!Expensive"
> ```
> {style="note"}

### `executionMode`

Override the globally set [`executionMode`](project-extension.md#executionmode).

This may come in handy during refactoring.
If your solution is working, but expensive, you might want to execute example inputs only first, to save time during
incremental sanity check test runs.

### `efficiencyBenchmark`

Override the globally set [`efficiencyBenchmark`](project-extension.md#efficiencybenchmark).

If you decided to set a lower global value to challenge yourself, but you did not manage to optimize this puzzle enough,
you can provide a different setting for this puzzle only.

> Only use this when the solution runs relatively quickly _(say a few seconds)_.
> For longer running solutions, you might want to use `expensive = true` instead.
