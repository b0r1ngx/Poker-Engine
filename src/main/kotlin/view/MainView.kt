package view

import controller.MainGameController
import core.Action
import core.Card
import core.Play
import core.UInterface
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.Slider
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import player.Human
import player.RandomBot
import tornadofx.*
import view.StartMenu.Companion.tableSeats
import view.StartMenu.Companion.typeOfPoker

class MainView: View("Play $typeOfPoker $tableSeats-max seats"), UInterface {
    private var butonnller: Buttons
    private var controller: MainGameController

    val playable: SimpleBooleanProperty = SimpleBooleanProperty(true)

    private lateinit var playerGraphics: HandUI
    private lateinit var botsGraphics: HandUI
    private lateinit var tableGraphics: HandUI

    private val botsCards: HBox = HBox()
    private val playerCards: HBox = HBox()
    private val tableCards: HBox = HBox()

    val player = Human("Human", 100.0)
    val bot = RandomBot("RandomBot", 100.0)
    private var uiPlayers = listOf(player, bot)

    private var dealerBtn = 1
    private var handNumber = 1

    private val playerStrChips = SimpleStringProperty("1000 $")
    private val botsStrChips = SimpleStringProperty("1000 $")

    private val handNumberProperty = SimpleStringProperty("HAND #0")
    private val potSize = SimpleStringProperty("POT: 0")

    private var chatStr = "CHAT"
    private val chat = SimpleStringProperty("CHAT")

    private var slider: Slider

    private var playerButtons: HBox
    private var endHand: HBox? = null
    private var bottomStackPane: StackPane? = null

    init {
        controller = MainGameController(uiPlayers, this)
        slider = Slider()

        butonnller = Buttons(WIDTH, HEIGHT, controller, this)
        playerButtons = butonnller.createButtonsHBox(true, controller.sBlind)
    }

    override val root = when (tableSeats) {
         2 -> pane {
            setPrefSize(WIDTH, HEIGHT)
            createPlayersAndTable()
            //initialize background
            region {
                setPrefSize(WIDTH, HEIGHT)
                style = "-fx-background-color: rgba(0, 0, 0, 1)"
            }
            //root
            vbox(5) {
                //root for table and chat
                hbox {
                    //table
                    stackpane {
                        //background for table
                        rectangle(width = WIDTH * .8, height = HEIGHT * .8) {
                            fill = Color.CORNFLOWERBLUE
                        }
                        // cards, bank, pot
                        hbox {
                            //card box
                            vbox {
                                prefWidth = WIDTH*.7
                                prefHeight = HEIGHT*.8
                                alignment = Pos.TOP_LEFT
                                paddingAll = 35
                                children.addAll(botsCards, tableCards, playerCards)
                            }
                            //bank and pot box
                            vbox {
                                label {
                                    prefHeight = HEIGHT*.1
                                    textProperty().bind(botsStrChips)
                                }
                                label {
                                    prefHeight = HEIGHT*.55
                                    textProperty().bind(potSize)
                                }
                                label {
                                    textProperty().bind(playerStrChips)
                                }
                            }
                        }
                    }
                    //chat
                    stackpane {
                        //chat bg
                        rectangle(width = WIDTH * .2, height = HEIGHT * .8) {
                            fill = Color.DEEPSKYBLUE
                        }
                        //chat form
                        vbox(5) {
                            // just for spacing
                            label("")
                            //hand #
                            label {
                                textProperty().bind(handNumberProperty)
                            }
                            //pot size
                            label {
                                textProperty().bind(potSize)
                            }
                            //chat text
                            label {
                                textProperty().bind(chat)
                            }
                        }
                    }
                }
                createChangeHandBoxes()
                children.add(bottomStackPane)
            }
            controller.launchGame()
            controller.startNewHand()
        }
        else -> pane {
            alert(Alert.AlertType.ERROR, "На данный момент можно играть только Texas Hold'em 2-seats")
        }
    }

    private fun createChangeHandBoxes() {
        this.endHand = createEndHand()
        this.bottomStackPane = StackPane(createBottomBackGround(), playerButtons)
    }

    private fun createBottomBackGround(): Rectangle {
        val bG = Rectangle(WIDTH * .9, HEIGHT * .1)
        bG.fill = Color.LIGHTCORAL
        return bG
    }

    private fun createEndHand(): HBox = hbox {
        paddingRight = 10
        paddingLeft = WIDTH/8
        children.add(butonnller.createNewHandButton())
    }

    private fun createPlayersAndTable() {
        botsGraphics = HandUI(botsCards.children)
        tableGraphics = HandUI(tableCards.children)
        playerGraphics = HandUI(playerCards.children)

        botsCards.prefHeight = 0.0
        botsCards.alignment = Pos.TOP_CENTER

        tableCards.prefHeight = HEIGHT * 0.3
        tableCards.alignment = Pos.CENTER_LEFT

        playerCards.prefHeight = HEIGHT * 0.6
        playerCards.alignment = Pos.BOTTOM_CENTER
    }

    fun clearTable() {
        botsGraphics.reset()
        tableGraphics.reset()
        playerGraphics.reset()
    }

    private fun setStrChips(button: Int) {
        val buttonStr = "(B) "
        if (button == 0) {
            playerStrChips.value = buttonStr + "Human: " + player.chips + " $"
            botsStrChips.value = "Bot: " + bot.chips + " $"
        } else {
            playerStrChips.value = "Human: " + player.chips + " $"
            botsStrChips.value = buttonStr + "Bot: " + bot.chips + " $"
        }
    }

    fun chatClear() {
        chatStr = "Chat: "
        chat.value = chatStr
    }

    override fun setPlayable(b: Boolean) {
        playable.set(b)
    }

    override fun refreshUi(sbPost: Action, pot: Double, amountForCall: Double) {
        this.bottomStackPane!!.children.removeAt(1)
        this.bottomStackPane!!.children.add(butonnller.createButtonsHBox(controller.currentStreetBet(), amountForCall))
        chatInfo(sbPost.player.name + " " + sbPost.pokerAction.toString() + " " + sbPost.amount)

        potSize.value = "Pot: $pot $"
        setStrChips(dealerBtn)
    }

    override fun dealCard(player: Play, card: Card) {
        if (player.type == "human") {
            card.show()
            playerGraphics.takeCard(card)
        } else {
            card.hide()
            botsGraphics.takeCard(card)
        }
    }

    override fun resetPot() {
        potSize.value = "Pot: " + 0.0
    }

    override fun dealTableCard(card: Card) {
        tableGraphics.takeCard(card)
        card.show()

    }

    override fun chatInfo(info: String) {
        chatStr = chatStr + "\n" + info
        chat.value = chatStr
    }

    override fun showDown() {
        for (card in bot.cards) {
            card.show()
        }
    }

    override fun refreshUINewDeal(button: Int) {
        dealerBtn = button
        handNumberProperty.value = "Hand #" + handNumber++
        potSize.value = "Pot: " + 0.0 + " $"
        setStrChips(button)
    }

    override fun endHand() {
        this.bottomStackPane!!.children.clear()
        this.bottomStackPane!!.children.addAll(createBottomBackGround() ,endHand)
    }

    override fun endGameAlert(winner: Play): Alert =
            alert(Alert.AlertType.INFORMATION, "Игра окончена, победидель ${winner.name}")

    companion object {
        const val WIDTH = 1024.0
        const val HEIGHT = 800.0
    }
}