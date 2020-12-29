package aoc.solutions.y2015

import aoc.core.AdventDay
import aoc.solutions.y2015.Y2015D21.Item.*

/**
 *  Before you say anything, yes, I know this is way too over-engineered for what it needs to do, but I took at is an
 *  opportunity to play with Kotlin's excellent type system, and see what I would come up with if developing my own
 *  RPG.
 */
class Y2015D21 : AdventDay(2015, 21, "RPG Simulator 20XX") {

    /** Describes something of value, that has a gold [cost]. */
    private interface Valuable {
        val cost: Int
    }

    /** Describes something relevant for combat. */
    private interface CombatStat {
        val damage: Int
        val armor: Int
    }

    /** Any entity, like a player or hostile mob that can do battle. */
    private interface CombatParticipant : CombatStat {
        val health: Int
    }

    /** All types of items in this RPG and their base stats. */
    private sealed class Item(
        val name: String,
        override val cost: Int,
        override val damage: Int,
        override val armor: Int
    ) : Valuable, CombatStat {
        open class Weapon(name: String, cost: Int, damage: Int) : Item(name, cost, damage, armor = 0)
        open class Armor(name: String, cost: Int, armor: Int) : Item(name, cost, damage = 0, armor)
        open class Ring(name: String, cost: Int, damage: Int = 0, armor: Int = 0) : Item(name, cost, damage, armor)

        object NoWeapon : Weapon("No Weapon", 0, 0)
        object NoArmor : Armor("No Armor", 0, 0)
        object NoRing : Ring("No Ring", 0)
    }

    /**
     * Container for a type of item, consider it the box in the inventory UI.
     * Calls [onUpdate] whenever a new item has been equipped, useful for updating stats.
     */
    private class EquipmentSlot<T : Item>(initialItem: T, private val onUpdate: (T) -> Unit = {}) {

        var item: T = initialItem
            private set(value) {
                onUpdate(value)
                field = value
            }

        fun equip(item: T): T {
            val oldItem = this.item
            this.item = item
            return oldItem
        }
    }

    /** Defines the slots available for a player to equip items in, and calculates combined statistics. */
    private class LoadOut : Valuable, CombatStat {
        val weaponSlot: EquipmentSlot<Weapon> = EquipmentSlot(NoWeapon) { updateStats() }
        val armorSlot: EquipmentSlot<Armor> = EquipmentSlot(NoArmor) { updateStats() }
        val leftRingSlot: EquipmentSlot<Ring> = EquipmentSlot(NoRing) { updateStats() }
        val rightRingSlot: EquipmentSlot<Ring> = EquipmentSlot(NoRing) { updateStats() }

        override var cost: Int = 0
            private set
        override var damage: Int = 0
            private set
        override var armor: Int = 0
            private set

        fun allItems() = listOf(weaponSlot, armorSlot, leftRingSlot, rightRingSlot).map { it.item }

        private fun updateStats() {
            val items = allItems()
            cost = items.sumBy { it.cost }
            damage = items.sumBy { it.damage }
            armor = items.sumBy { it.armor }
        }
    }

    /** Player controlled character, stats depend on equipped items. */
    private class Character(
        override val health: Int,
        val loadOut: LoadOut = LoadOut()
    ) : CombatParticipant, CombatStat by loadOut

    /** Computer controlled character, stats depend on the will of the developers. */
    private class Enemy(override val health: Int, override val damage: Int, override val armor: Int) : CombatParticipant

    /** Simulates a duel and returns whether this participant would win against their [opponent]. */
    private fun CombatParticipant.wouldWinAgainst(opponent: CombatParticipant): Boolean {
        val thisWinsIn = opponent.health.toDouble() / maxOf(1, damage - opponent.armor)
        val opponentWinsIn = health.toDouble() / maxOf(1, opponent.damage - armor)
        return thisWinsIn <= opponentWinsIn
    }

    // Setup complete! Actual advent solution below this line ---------------------------------------------------------

    /** The shopkeeper's inventory, defined in the problem description. */
    private val shopItems = setOf(
        Weapon("Dagger", cost = 8, damage = 4),
        Weapon("Shortsword", cost = 10, damage = 5),
        Weapon("Warhammer", cost = 25, damage = 6),
        Weapon("Longsword", cost = 40, damage = 7),
        Weapon("Greataxe", cost = 74, damage = 8),

        Armor("Leather", cost = 13, armor = 1),
        Armor("Chainmail", cost = 31, armor = 2),
        Armor("Splintmail", cost = 53, armor = 3),
        Armor("Bandedmail", cost = 75, armor = 4),
        Armor("Platemail", cost = 102, armor = 5),

        Ring("Damage +1", cost = 25, damage = 1),
        Ring("Damage +2", cost = 50, damage = 2),
        Ring("Damage +3", cost = 100, damage = 3),
        Ring("Defense +1", cost = 20, armor = 1),
        Ring("Defense +2", cost = 40, armor = 2),
        Ring("Defense +3", cost = 80, armor = 3),
    )

    /** Parses the input and returns the boss [Enemy] to fight against. */
    private fun parseInput(input: List<String>) = Enemy(
        health = input[0].substringAfter(": ").toInt(),
        damage = input[1].substringAfter(": ").toInt(),
        armor = input[2].substringAfter(": ").toInt(),
    )

    /**
     * Given the available items to select from, finds the optimal cost for the items to purchase such that the desired
     * requirements are met.
     */
    private fun optimizeStrategy(
        shopItems: Set<Item>,
        enemy: Enemy,
        minimizeCost: Boolean = true,
        playerShouldWin: Boolean = true,
    ): Int {
        val player = Character(health = 100, LoadOut())
        val weapons = shopItems.filterIsInstance<Weapon>()
        val armors = shopItems.filterIsInstance<Armor>().plus(NoArmor)
        val rings = shopItems.filterIsInstance<Ring>().plus(NoRing)

        val optimalCostStrategy: (Int, Int) -> Int = if (minimizeCost) ::minOf else ::maxOf
        var optimalCost = if (minimizeCost) Int.MAX_VALUE else Int.MIN_VALUE

        for (weapon in weapons) {
            player.loadOut.weaponSlot.equip(weapon)

            for (armor in armors) {
                player.loadOut.armorSlot.equip(armor)

                for (leftRing in rings) {
                    player.loadOut.leftRingSlot.equip(leftRing)

                    for (rightRing in rings.minus(leftRing).plus(NoRing)) {
                        player.loadOut.rightRingSlot.equip(rightRing)

                        if (player.wouldWinAgainst(enemy) == playerShouldWin) {
                            optimalCost = optimalCostStrategy(optimalCost, player.loadOut.cost)
                        }
                    }
                }
            }
        }

        return optimalCost
    }

    override fun partOne(input: String) =
        optimizeStrategy(shopItems, enemy = parseInput(input.lines()))

    override fun partTwo(input: String) =
        optimizeStrategy(shopItems, enemy = parseInput(input.lines()), minimizeCost = false, playerShouldWin = false)
}
