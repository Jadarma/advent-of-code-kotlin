package aoc.solutions.y2015

import aoc.core.AdventDay
import aoc.solutions.y2015.Y2015D22.Spell.*

class Y2015D22 : AdventDay(2015, 22, "Wizard Simulator 20XX") {

    /** Parses the input and returns the initial [GameState] to simulate. */
    private fun parseInput(input: List<String>) = GameState(
        playerHealth = 50,
        playerMana = 500,
        enemyHealth = input[0].substringAfter(": ").toInt(),
        enemyDamage = input[1].substringAfter(": ").toInt(),
    )

    /** Describes the properties (but not the effects) of a spell. */
    private enum class Spell(val cost: Int, val turns: Int = 0) {
        MagicMissile(cost = 53),
        Drain(cost = 73),
        Shield(cost = 113, turns = 6),
        Poison(cost = 173, turns = 6),
        Recharge(cost = 229, turns = 5);
    }

    /** Describes the state of the game in any point during the battle simulation. */
    private data class GameState(
        val playerHealth: Int,
        val playerMana: Int,
        val enemyHealth: Int,
        val enemyDamage: Int,

        private val spellList: List<Spell> = emptyList(),
        private val shieldTimer: Int = 0,
        private val poisonTimer: Int = 0,
        private val rechargeTimer: Int = 0,
        private val hardMode: Boolean = false,
    ) {
        /** Returns the total mana cost of spells cast until this point. */
        val totalCost by lazy { spellList.sumBy { it.cost } }

        /** Filters the spells that can be cast by the player in the current state. */
        val availableSpells: Set<Spell> by lazy {
            mutableListOf(MagicMissile, Drain).apply {
                if (shieldTimer <= 1) add(Shield)
                if (poisonTimer <= 1) add(Poison)
                if (rechargeTimer <= 1) add(Recharge)
            }.filter { it.cost <= playerMana }.toSet()
        }

        /** Cast the selected [spell] and adjust the cooldown of passive ones. */
        private fun playerTurn(spell: Spell) =
            when (spell) {
                MagicMissile -> copy(enemyHealth = enemyHealth - 4)
                Drain -> copy(playerHealth = playerHealth + 2, enemyHealth = enemyHealth - 2)
                Shield -> copy(shieldTimer = spell.turns)
                Poison -> copy(poisonTimer = spell.turns)
                Recharge -> copy(rechargeTimer = spell.turns)
            }.copy(playerMana = playerMana - spell.cost, spellList = spellList + spell)

        /** If the enemy is alive, attack the player. */
        private fun enemyTurn() = when {
            enemyHealth <= 0 -> this
            else -> copy(playerHealth = playerHealth - maxOf(1, enemyDamage - if (shieldTimer > 0) 7 else 0))
        }

        /** Apply the effects of all passive spells. */
        private fun applyEffects() = copy(
            playerMana = if (rechargeTimer > 0) playerMana + 101 else playerMana,
            enemyHealth = if (poisonTimer > 0) enemyHealth - 3 else enemyHealth,
            shieldTimer = maxOf(0, shieldTimer - 1),
            poisonTimer = maxOf(0, poisonTimer - 1),
            rechargeTimer = maxOf(0, rechargeTimer - 1),
        )

        /** Calculates the next [GameState] after the player and enemy turns if the player selected this [spell]. */
        fun playRound(spell: Spell): GameState {
            require(spell in availableSpells) { "You cannot cast this!" }
            if (hardMode && playerHealth == 1) return copy(playerHealth = 0)
            val hardModeAdjusted = if (hardMode) copy(playerHealth = playerHealth - 1) else this
            return hardModeAdjusted
                .playerTurn(spell)
                .applyEffects()
                .enemyTurn()
                .applyEffects()
        }
    }

    /** Checks for winning conditions: enemy is dead. */
    private fun GameState.playerWon() = enemyHealth <= 0

    /** Checks for losing conditions: either dead or out of spells to cast. */
    private fun GameState.playerLost() = playerHealth <= 0 || availableSpells.isEmpty()

    /** Picks the state with the lowest mana cost. */
    private fun minByManaCost(a: GameState?, b: GameState?) = minOf(a, b, nullsLast(compareBy { it.totalCost }))

    /**
     * Recursively simulates the battle by picking spells searching for the optimal strategy.
     *
     * @param state The state to search from.
     * @param currentBest If another winning state is already known, optimises the search from this [state] by
     * pruning out all paths that would have a greater total cost, even if winning.
     * @return The most optimal [GameState] that wins the match, or `null` if impossible to win from this [state].
     */
    private fun trySpells(state: GameState, currentBest: GameState? = null): GameState? =
        when {
            state.playerLost() -> null
            state.playerWon() -> minByManaCost(state, currentBest)
            else -> state.availableSpells.fold(currentBest) { best, spell ->
                when {
                    best != null && state.totalCost + spell.cost > best.totalCost -> best
                    else -> minByManaCost(best, trySpells(state.playRound(spell), best))
                }
            }
        }

    override fun partOne(input: String) =
        parseInput(input.lines())
            .let(this::trySpells)!!
            .totalCost

    override fun partTwo(input: String) =
        parseInput(input.lines())
            .copy(hardMode = true)
            .let(this::trySpells)!!
            .totalCost
}
