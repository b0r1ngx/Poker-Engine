package player

import core.*
import kotlin.random.Random


class MediumRandomBot(override val name: String,
                      chips: Double): Play {
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
        val r = Random.nextInt(1, 8)
        // Default start chips = 1000
        val smallBet = chips * 0.02
        val mediumBet = chips * 0.05
        val highBet = chips * 0.15
        val valueBet = chips * 0.33
        println("RandomBot is take number $r")
        when (r) {
            1 -> if (currentRaise == 0.0)
                this.action = Action(this, PokerAction.CHECK, 0.0, handState)
            else
                this.action = Action(this, PokerAction.FOLD, 0.0, handState)
            2, 3 -> if (currentRaise == 0.0 && chips >= smallBet)
                this.action = Action(this, PokerAction.BET, smallBet, handState)
            else if (chips >= currentRaise)
                this.action = Action(this, PokerAction.CALL, currentRaise, handState)
            else
                this.action = Action(this, PokerAction.FOLD, 0.0, handState)
            4, 5 -> if (currentRaise == 0.0 && chips >= mediumBet)
                this.action = Action(this, PokerAction.BET, mediumBet, handState)
            else if (chips >= currentRaise)
                this.action = Action(this, PokerAction.CALL, currentRaise, handState)
            else
                this.action = Action(this, PokerAction.FOLD, 0.0, handState)
            6 -> when {
                currentRaise == 0.0 ->
                    when {
                        chips >= highBet -> this.action = Action(this, PokerAction.BET, highBet, handState)
                        chips >= mediumBet -> this.action = Action(this, PokerAction.BET, mediumBet, handState)
                        chips >= smallBet -> this.action = Action(this, PokerAction.BET, smallBet, handState)
                    }
                chips >= 2 * currentRaise -> this.action = Action(this, PokerAction.RAISE, 2 * currentRaise, handState)
                chips >= currentRaise -> this.action = Action(this, PokerAction.CALL, currentRaise, handState)
                else -> this.action = Action(this, PokerAction.ALLIN, chips, HandState.ALLIN)
            }
            7 -> when {
                currentRaise == 0.0 ->
                    when {
                        chips >= valueBet -> this.action = Action(this, PokerAction.BET, valueBet, handState)
                        chips >= highBet -> this.action = Action(this, PokerAction.BET, highBet, handState)
                        chips >= mediumBet -> this.action = Action(this, PokerAction.BET, mediumBet, handState)
                        chips >= smallBet -> this.action = Action(this, PokerAction.BET, smallBet, handState)
                    }
                chips >= 5 * currentRaise -> this.action = Action(this, PokerAction.RAISE, 5 * currentRaise, handState)
                chips >= 3 * currentRaise -> this.action = Action(this, PokerAction.RAISE, 3 * currentRaise, handState)
                chips >= currentRaise -> this.action = Action(this, PokerAction.CALL, currentRaise, handState)
                else -> this.action = Action(this, PokerAction.ALLIN, chips, HandState.ALLIN)
            }
        }
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
}