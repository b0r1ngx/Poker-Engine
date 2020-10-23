package player

import core.*

class Human(override val name: String,
            chips: Double): Play {
    override val cards: MutableList<Card>
    override var chips = 0.0
    override lateinit var action: Action
    private val sitout: Boolean = false
    private val present: Boolean = false
    private val allin: Boolean = false
    override val type = "human"

    init {
        println("PLAYER NAME: $name")
        this.chips = chips
        this.cards = mutableListOf()
    }

    override fun prepareForAction(board: List<Card>, currentRaise: Double, handState: HandState?) {}

    override fun post(amount: Double): Double {
        return amount
    }

    override fun addChips(amount: Double) {
        chips += amount
    }

    override fun reduceChips(amount: Double): Double {
        if (chips >= amount) {
            chips -= amount
            println("chips was taken from human hand = " + this.chips)
            return amount
        }
        val allChips = chips
        chips = 0.0
        return allChips
    }

    override fun addCard(card: Card) {
        this.cards.add(card)
    }

    override fun clearCards() {
        this.cards.clear()
    }
}
