package core

class Deck(private val size: Int = 52) {
    private var deck = mutableListOf<Card>()

    // playing with full deck or a just 6+
    init {
        if (size == 52) {
            for (s in Suit.values()) {
                for (r in Rank.values())
                    deck.add(Card(r, s))
            }
            deck.shuffle()
        }
        if (size == 36) {
            for (s in Suit.values()) {
                for (r in Rank.values().toList().subList(4, 13))
                    deck.add(Card(r, s))
            }
            deck.shuffle()
        }
    }

    fun draw(): Card {
        val draw = deck.first()
        deck.remove(draw)
        return draw
    }

    fun burnOneCard() {
        deck.remove(deck.first())
    }

    fun size() = deck.size
}

fun main() {
    val size = 36
    val deck = Deck(size)
    for (i in 0 until size) {
        println(deck.draw())
    }
}