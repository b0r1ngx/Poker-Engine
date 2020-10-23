package player

import core.*

class RandomBot(override val name: String,
                chips: Double) : Play {
    override val cards: MutableList<Card> = mutableListOf()
    override var chips: Double = 0.0
        private set
    override lateinit var action: Action
    private var sitOut: Boolean = false
    private var present: Boolean = false
    var allin: Boolean = false
    override val type = "bot"
    private val handState: HandState? = null

    private val value = Array(13) { BooleanArray(13) }

    init {
        this.chips = chips
        this.present = true
    }

    override fun prepareForAction(board: List<Card>, currentRaise: Double, handState: HandState?) {
        val r = (Math.random() * 7 + 1).toInt()
        println("r = $r")
        when (r) {
            1 -> if (currentRaise == 0.0) {
                this.action = Action(this, PokerAction.CHECK, 0.0, handState)
            } else {
                this.action = Action(this, PokerAction.FOLD, 0.0, handState)
            }
            2, 3, 4 -> if (currentRaise == 0.0) {
                this.action = Action(this, PokerAction.BET, 2.0, handState)
            } else {
                this.action = Action(this, PokerAction.CALL, currentRaise, handState)
            }
            else -> if (currentRaise == 0.0) {
                this.action = Action(this, PokerAction.BET, 6.0, handState)
            } else if (this.chips > currentRaise * 2) {
                this.action = Action(this, PokerAction.RAISE, currentRaise * 2, handState)
            } else {
                this.action = Action(this, PokerAction.ALLIN, this.chips, handState)
            }
        }
    }

    private fun testPrint() {
        println("---RANDOMBOT ACTION--------")
        println(cards[0].toString() + " " + cards[1])
        println(action)
        println("------------------------")
    }

    override fun post(amount: Double): Double {
        if (chips - amount >= 0) {
            return amount
        }
        println("BLIND POST ERROR")
        return 0.0
    }

    override fun addChips(amount: Double) {
        if (amount > 0.0) {
            chips += amount
        }
    }

    override fun reduceChips(amount: Double): Double {
        if (chips >= amount) {
            chips -= amount
            println("chips was taken from bot hand = " + this.chips)
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

//    override fun equals(other: Any?): Boolean {
//        if (this === other) {
//            return true
//        }
//        if (other == null) {
//            return false
//        }
//        if (javaClass != other.javaClass) {
//            return false
//        }
//        val other = other as RandomBot?
//        return this.type == other!!.type
//
//    }
//
//    override fun hashCode(): Int {
//        var result = name.hashCode()
//        result = 31 * result + cards.hashCode()
//        result = 31 * result + chips.hashCode()
//        result = 31 * result + sitOut.hashCode()
//        result = 31 * result + present.hashCode()
//        result = 31 * result + allin.hashCode()
//        result = 31 * result + type.hashCode()
//        result = 31 * result + (handState?.hashCode() ?: 0)
//        result = 31 * result + value.contentDeepHashCode()
//        return result
//    }
}
