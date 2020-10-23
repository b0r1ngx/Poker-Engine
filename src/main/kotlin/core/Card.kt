package core

import javafx.scene.Parent
import javafx.scene.image.Image
import javafx.scene.image.ImageView

enum class Suit {
    Clubs, Diamonds, Spades, Hearts;

    val imageName = this.name.substring(0, 1)
}

enum class Rank(val value: Int) {
    Deuce(2), Three(3), Four(4), Five(5),
    Six(6), Seven(7), Eight(8), Nine(9),
    Ten(10), Jack(11), Queen(12), King(13), Ace(14)
}
class Card(val rank: Rank, val suit: Suit):  Parent(), Comparable<Card> {

    private val imagePath: String
        get() = "${rank.value}${suit.imageName}.png"
    //simplify later for hide/show cards on table
    private val cardBackPath = "blue_back.png"

    fun hide() {
        children.addAll(cardBackImg())
    }

    fun show() {
        children.addAll(cardImg())
    }

    private fun realiseImg(i: String): ImageView {
        val iv = ImageView()
        iv.image = Image(i)
        iv.fitWidth = 100.0
        iv.isPreserveRatio = true
        iv.isSmooth = true
        iv.isCache = true
        return iv
    }

    private fun cardImg(): ImageView =
        realiseImg(imagePath)

    private fun cardBackImg(): ImageView =
        realiseImg(cardBackPath)


    override fun compareTo(other: Card): Int = when {
        this.rank == other.rank -> 0
        this.rank < other.rank -> -1
        else -> 1
    }

    override fun equals(o: Any?): Boolean = o is Card &&
            suit == o.suit && rank == o.rank

    override fun toString(): String = "$rank of $suit"
}