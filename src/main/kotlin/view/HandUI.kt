package view

import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.ObservableList
import javafx.scene.Node
import java.util.*
import core.Card


class HandUI(private val observalCards: ObservableList<Node>) {
    private val coreCards: MutableList<Card>
    private val value = SimpleIntegerProperty(0)

    init {
        coreCards = ArrayList<Card>()
    }

    fun takeCard(card: Card) {
        coreCards.add(card)
        observalCards.add(card)
    }

    fun reset() {
        observalCards.clear()
        coreCards.clear()
        value.set(0)
    }

    val cards: List<Any>
        get() = coreCards

    fun size(): Int {
        return observalCards.size
    }
}
