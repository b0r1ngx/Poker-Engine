package core

import javafx.scene.control.Alert

interface UInterface {
    fun setPlayable(b: Boolean)

    fun refreshUi(sbPost: Action, pot: Double, amountForCall: Double)

    fun dealCard(player: Play, card: Card)

    fun dealTableCard(card: Card)

    fun refreshUINewDeal(button: Int)

    fun resetPot()

    fun chatInfo(info: String)

    fun showDown()

    fun endHand()

    fun endGameAlert(winner: Play): Alert
}