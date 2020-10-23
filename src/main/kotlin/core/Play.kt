package core

interface Play {
    val action: Action

    val chips: Double

    val name: String

    val cards: List<Card>

    val type: String

    fun prepareForAction(board: List<Card>, currentRaise: Double, handState: HandState?) //double

    fun post(amount: Double): Double //double

    fun addChips(amount: Double) //double

    fun reduceChips(amount: Double): Double //double

    fun addCard(card: Card)

    fun clearCards()

//    override fun equals(o: Any?): Boolean
}
