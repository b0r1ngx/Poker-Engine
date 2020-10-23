package player

import core.*
import java.util.*

class EasyBot(override val name: String, chips: Double): Play {
    override val cards: MutableList<Card>
    override var chips: Double = 0.toDouble()
        private set
    override lateinit var action: Action
        private set
    var sitout: Boolean = false
    var present: Boolean = false
    var allin: Boolean = false
    override val type = "bot"

    private var handState: HandState? = null

    init {
        this.cards = ArrayList()
        this.chips = chips
        this.present = true
    }

    override fun prepareForAction(board: List<Card>, currentRaise: Double, handState: HandState?) {
        when {
            currentRaise == 0.0 ->
                this.action = Action(this, PokerAction.CHECK, 0.0, handState)
            currentRaise <= chips ->
                this.action = Action(this, PokerAction.CALL, currentRaise, handState)
            else -> {
                this.action = Action(this, PokerAction.ALLIN, this.chips, handState)
            }
        }
    }

    override fun post(amount: Double): Double {
        return if (chips - amount >= 0) {
            amount
        } else 0.0
    }

    override fun addChips(amount: Double) {
        if (amount > 0.0) {
            chips += amount
        }
    }

    override fun reduceChips(amount: Double): Double {
        if (chips >= amount) {
            chips -= amount
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

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o !is EasyBot) {
            return false
        }
        val player = o as EasyBot?
        return type === player!!.type && name == player!!.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name, type)
    }
}
