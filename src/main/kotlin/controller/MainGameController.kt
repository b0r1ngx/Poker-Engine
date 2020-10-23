package controller

import core.*
import core.HandState.*
import core.PokerAction.*
import java.util.*

class MainGameController(private var players: List<Play>, var view: UInterface) {

    var combinations = Combinations()
    private var table = mutableListOf<Card>()
    private var deck = Deck()
    private var dealerBtn = 0
    private var turn = 0
    private var turns = 0
    private var pot = 0.0
    private var playablePlayers = 0
    private var seats = arrayOfNulls<Play>(2)
    var handState: HandState? = null
    private lateinit var potTrack: Array<DoubleArray>
    private var ai: Play
    private lateinit var handActions: Stack<Action>

    private var bBlind = 2.0
    var sBlind = bBlind / 2

    init {
        this.table = mutableListOf<Card>()
        var i = 0
        for (player in players) {
            this.seats[i++] = player
        }
        this.ai = players[1]
    }

    fun launchGame() {
        this.handState = NEWHAND
        this.dealerBtn = 0
        this.deck = Deck()
        this.handActions = Stack()
    }

    fun startNewHand() {
        this.handActions.clear()
        table.clear()
        deck = Deck()
        resetPotTrack()
        this.dealerBtn = if (dealerBtn + 1 == players.size) 0 else dealerBtn + 1
        this.playablePlayers = players.size
        this.turns = players.size
        dealBlinds()
        dealPocketCards()
        this.turn = dealerBtn

        if (seats[turn]!!.type == "bot") {
            prepareAiAction(chipsToCall(true))
        } else this.view.setPlayable(true)
    }

    private fun dealBlinds() {
        this.handState = PREFLOP
        val sb = dealerBtn
        val bb = if (dealerBtn + 1 == playablePlayers) 0 else dealerBtn + 1

        seats[bb]!!.post(this.bBlind)
        seats[sb]!!.reduceChips(this.sBlind)
        seats[bb]!!.reduceChips(this.bBlind)

        val sbPost = Action(seats[sb]!!, POST, sBlind, this.handState)
        val bbPost = Action(seats[bb]!!, POST, bBlind, this.handState)
        updatePotLog(sbPost)
        updatePotLog(bbPost)

        this.view.refreshUi(sbPost, pot, 1.0)
        this.view.refreshUi(bbPost, pot, 1.0)
    }

    private fun chipsToCall(playersMove: Boolean): Double {
        printPotTrack()
        println("HandState number = ${handState!!.ordinal}")
        var max = 0.0
        val street = handState!!.ordinal
        for (i in players.indices) {
            if (potTrack[i][street] > max) max = potTrack[i][street]
        }
        if (!playersMove) {
            val p = if (turn == 0) 1 else 0
            return max - potTrack[p][street]
        }
        return max - potTrack[turn][street]
    }

    private fun dealPocketCards() {
        deck.burnOneCard()
        for (i in 0..1) {
            for (j in players.indices) {
                val card = deck.draw()
                players[i].addCard(card)
            }
        }
        for (player in players) {
            for (card in player.cards) {
                this.view.dealCard(player, card)
            }
        }
    }

    fun pushPlayerAction(action: Action) {
        view.setPlayable(false)
        if (seats[turn]!!.type != "human") {
            println("Now is not your turn!")
            return
        }

        if (controllerAction(action) == 1)
            showDown()
        else {
            if (seats[turn]!!.type == "human") {
                view.setPlayable(true)
                return
            }
            prepareAiAction(chipsToCall(true))
        }
    }

    private fun prepareAiAction(amount: Double) {
        ai.prepareForAction(table, amount, this.handState)
        val aiAction = ai.action
        pushAiAction(aiAction)
    }

    private fun pushAiAction(action: Action) =
            when {
                controllerAction(action) == 1 -> showDown()
                seats[turn]!!.type == "bot" -> {
                    view.setPlayable(false)
                    prepareAiAction(0.0)
                }
                else -> view.setPlayable(true)
            }

    //Controller makes game
    private fun controllerAction(action: Action): Int {
        this.handActions.add(action)
        if (this.handState!!.ordinal > RIVER.ordinal) {
            println("We dont be need here, should not update after river")
            return 2
        }
        val chipsToPot = action.player.reduceChips(action.amount)
        this.pot += chipsToPot
        if (action.player.chips == 0.0) {
            action.pokerAction = PokerAction.ALLIN
            action.amount = chipsToPot
        }
        updatePotLog(action)
        println("amountForCall = " + chipsToCall(false))
        this.view.refreshUi(action, pot, chipsToCall(false))

        when (action.pokerAction) {
            FOLD -> {
                this.playablePlayers -= 1
                this.turns -= 1
            }
            CHECK, CALL -> turns -= 1
            BET, RAISE -> {
                this.turns = playablePlayers - 1
            }
            else -> println("we dont wanna see this, we at updatelogic in logic")
        }
        if (allIn(action)) {
            while (this.handState!!.ordinal < RIVER.ordinal) {
                this.handState = this.handState!!.next()
                dealStreet()
            }
            return 1
        } else if (playablePlayers > 1 && turns == 0) {
            if (handState == RIVER) return 1
            turns = playablePlayers
            this.handState = this.handState!!.next()
            dealStreet()
            this.turn = if (this.dealerBtn + 1 == playablePlayers) 0 else this.dealerBtn + 1
        } else if (playablePlayers > 1) {
            this.turn = if (this.turn + 1 == playablePlayers) 0 else this.turn + 1
        } else if (playablePlayers == 1) {
            this.turn = if (this.turn + 1 >= players.size) 0 else this.turn + 1
            return 1
        } else
            return 1
        return 0
    }

    fun currentStreetBet(): Boolean {
        if (handActions.isEmpty()) {
            println("handActions is Empty!")
            return true
        }
        val lastAction = handActions.peek()
        if (lastAction.pokerAction == POST || lastAction.pokerAction == BET ||
                lastAction.pokerAction == RAISE || lastAction.pokerAction == PokerAction.ALLIN) {
            println("Someone is Bet!")
            return true
        }
        println("We don't get bet")
        return false
    }

    private fun allIn(action: Action): Boolean {
        if ((action.pokerAction == CALL && tableAllIns() >= players.size - 1) ||
                (action.pokerAction == PokerAction.ALLIN && tableAllIns() == players.size)) {
            return true
        } else if (action.pokerAction == PokerAction.ALLIN) {
            val current = this.handActions.pop()
            if (this.handActions.isNotEmpty()) {
                val prev = this.handActions.peek()
                if ((prev.pokerAction == BET || prev.pokerAction == RAISE) && prev.amount > action.amount) {
                    handActions.add(current)
                    return true
                }
            } else {
                handActions.add(current)
                return true
            }
        }
        return false
    }

    private fun tableAllIns(): Int {
        var count = 0
        for (player in players) {
            if (player.chips == 0.0) count++
        }
        return count
    }

    private fun checkAllIns(): Boolean {
        for (player in players) {
            if (player.chips < bBlind) return true
        }
        return false
    }

    private fun dealStreet() {
        var card: Card
        when (handState) {
            FLOP -> {
                deck.burnOneCard()
                var i = 0
                while (i < 3) {
                    card = deck.draw()
                    table.add(card)
                    view.dealTableCard(card)
                    i++
                }
            }
            TURN, RIVER -> {
                deck.burnOneCard()
                card = deck.draw()
                table.add(card)
                view.dealTableCard(card)
            }
            SHOWDOWN -> {}
            else -> println("Error is handstate default in logic")
        }
    }

//     whos the winner of the end
//    fun winner(): Play {
//        var highScore = 0
//        for (p in players) {
//            var currentScore: Int = Combinations.scoreHand(p, board)
//            if (p.action!!.pokerAction == FOLD) {
//                currentScore = 0
//            }
//            if (p.action!!.pokerAction != FOLD && winner == null) {
//                winner = p
//                highScore = currentScore
//            } else if (currentScore > highScore && p.action!!.pokerAction != FOLD) {
//                highScore = currentScore
//                winner = p
//            }
//        }
//        return winner
//    }

    //list/map of values and what the hand is
    private fun showDown() {
        view.setPlayable(false)
        view.chatInfo("End of hand!")
        if (playablePlayers > 1) {
            view.showDown()
            val winZ = mutableListOf<Pair<Play, Int>>()
            for (p in players)
                winZ.add(Pair(p, combinations.scoreHand(p, table)))
            when {
                winZ[0].second > winZ[1].second -> {
                    view.chatInfo("")
                    view.chatInfo("WINNER: ")
                    view.chatInfo(winZ[0].first.name + "(B: " + winZ[0].first.chips.toString() + ") win's pot " + this.pot)
                    winZ[0].first.addChips(pot)
                }
                winZ[1].second > winZ[0].second -> {
                    view.chatInfo("")
                    view.chatInfo("WINNER: ")
                    view.chatInfo(winZ[1].first.name + "(B: " + winZ[1].first.chips.toString() + ") win's pot " + this.pot)
                    winZ[1].first.addChips(pot)
                }
                else -> {
                    view.chatInfo("")
                    view.chatInfo("SPLIT: ")
                    view.chatInfo(winZ[0].first.name + "(B: " + winZ[0].first.chips.toString() + ") win's pot " + this.pot / 2)
                    view.chatInfo(winZ[1].first.name + "(B: " + winZ[1].first.chips.toString() + ") win's pot " + this.pot / 2)
                    winZ[0].first.addChips(pot / 2)
                    winZ[1].first.addChips(pot / 2)
                }
            }
            view.chatInfo("")
            view.chatInfo("End of showdown")
        } else {
            view.chatInfo("WINNER: ")
            view.chatInfo(seats[turn]!!.name + "(B: " + seats[turn]!!.chips + ") win's pot " + " " + this.pot)
            view.chatInfo("NO SHOW")
            seats[turn]!!.addChips(pot)
        }
        endRound()
    }

    private fun endRound() {
        for (player in players) {
            if (player.chips == 0.0)
                view.endGameAlert(player)
            player.clearCards()
        }
        table.clear()
        view.setPlayable(!checkAllIns())
        view.refreshUINewDeal(dealerBtn)
        view.endHand()
    }

    private fun updatePotLog(action: Action) {
        val street = this.handState!!.ordinal
        this.potTrack[findSeat(action.player)][street] += action.amount
        var sum = 0.0
        for (y in players.indices) {
            for (x in 0..3) {
                sum += potTrack[y][x]
            }
        }
        this.pot = sum
    }

    private fun resetPotTrack() {
        view.resetPot()
        this.potTrack = Array(players.size) { DoubleArray(8) }
        this.pot = 0.0
    }

    private fun printPotTrack() {
        for (i in players.indices)
            for (j in 0..3) {
                print(potTrack[i][j].toString() + " ")
            }
        println()
    }

    private fun findSeat(player: Play): Int {
        for (i in players.indices)
            if (players[i] == player) return i
        return -1
    }
}