package aoc.solutions.y2015

import aoc.core.AdventDay

class Y2015D15 : AdventDay(2015, 15, "Science for Hungry People") {

    private val inputRegex =
        Regex("""(\w+): capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (-?\d+)""")

    /** Parses a single line of input and returns an [Ingredient] and its properties. */
    private fun parseInput(input: String): Ingredient {
        val (name, capacity, durability, flavor, texture, calories) = inputRegex.matchEntire(input)!!.destructured
        return Ingredient(name, capacity.toInt(), durability.toInt(), flavor.toInt(), texture.toInt(), calories.toInt())
    }

    /** Cookie making properties and nutritional values of an ingredient. */
    private data class Ingredient(
        val name: String,
        val capacity: Int,
        val durability: Int,
        val flavor: Int,
        val texture: Int,
        val calories: Int,
    )

    /** The required ingredients */
    private data class CookieRecipe(val ingredients: Map<Ingredient, Int>) {

        val calories by lazy {
            ingredients.entries.sumBy { (it, count) -> it.calories * count }
        }

        val score by lazy {
            with(ingredients.entries) {
                listOf(
                    sumBy { (it, count) -> it.capacity * count },
                    sumBy { (it, count) -> it.durability * count },
                    sumBy { (it, count) -> it.flavor * count },
                    sumBy { (it, count) -> it.texture * count },
                ).map { maxOf(0, it) }.reduce(Int::times)
            }
        }
    }

    /** Finds all different way you can use [ingredientCount] to add up to [capacity] units. */
    private fun bruteForceMeasurements(ingredientCount: Int, capacity: Int): List<List<Int>> = when (ingredientCount) {
        in Int.MIN_VALUE..0 -> throw IllegalArgumentException("What would you want to cook with no ingredients?")
        1 -> listOf(listOf(capacity))
        else -> (0..capacity).flatMap { measure ->
            bruteForceMeasurements(ingredientCount - 1, capacity - measure).map { listOf(measure) + it }
        }
    }

    /**
     *  With the available [ingredients] and using exactly [capacity] units of measurement in total, finds the right
     *  combination that maximises the cookie score. Optionally adds [calorieRequirement] on the final recipe.
     *  Returns the perfect [CookieRecipe] or `null` if it's impossible to bake one to the given specifications.
     */
    private fun findPerfectRecipe(
        ingredients: List<Ingredient>,
        capacity: Int = 100,
        calorieRequirement: Int? = null,
    ): CookieRecipe? =
        bruteForceMeasurements(ingredients.size, capacity)
            .map { ingredients.zip(it).toMap() }
            .map(::CookieRecipe)
            .filter { calorieRequirement == null || it.calories == calorieRequirement }
            .maxByOrNull { it.score }

    override fun partOne(input: String): Any {
        val ingredients = input.lineSequence().map(this::parseInput).toList()
        return findPerfectRecipe(ingredients)!!.score
    }

    override fun partTwo(input: String): Any {
        val ingredients = input.lineSequence().map(this::parseInput).toList()
        return findPerfectRecipe(ingredients, calorieRequirement = 500)!!.score
    }
}
