package core

enum class PokerAction(val value: Int) {
    POST(0), FOLD(1), CHECK(2), CALL(3), BET(4), RAISE(5), ALLIN(6)

}

enum class HandState {
    NEWHAND, PREFLOP, FLOP, TURN, RIVER, SHOWDOWN, ALLIN;

    operator fun next(): HandState {
        return values()[(this.ordinal + 1) % values().size]
    }
}

class Action(
        val player: Play,
        var pokerAction: PokerAction?,
        var amount: Double,
        val handState: HandState?) {

    override fun toString(): String =
            player.name + " " + pokerAction + " " + amount

}