package view

import controller.MainGameController
import core.Action
import core.PokerAction
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Slider
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.util.StringConverter
import java.util.ArrayList
import kotlin.math.floor

class Buttons(
        private val resX: Double,
        private val resY: Double,
        private val controller: MainGameController,
        private val view: MainView) {

    val textField: TextField = TextField()
    val slider: Slider = Slider()

    init {
        initSlider()
    }

    fun initSlider() {
        slider.min = controller.sBlind
        slider.max = view.player.chips
        slider.majorTickUnit = 80.0
        slider.isShowTickLabels = true
        slider.isShowTickMarks = true
        slider.blockIncrement = 1.0
        textField.textProperty().bindBidirectional(slider.valueProperty(),
                object : StringConverter<Number>() {
                    override fun toString(t: Number): String =
                            floor(t as Double).toString()

                    override fun fromString(str: String): Number =
                            if (str.isEmpty()) 0.0 else str.toDouble()
                })
    }

    fun createButtonsHBox(bet: Boolean, amount: Double): HBox {
        val bHBox = HBox(5.0)
        bHBox.padding = Insets(0.0, 10.0, 0.0, resX / 8)
        val buttons = createButtons(bet, amount)
        bHBox.children.addAll(buttons)
        bHBox.children.add(textField)
        bHBox.children.add(slider)
        bHBox.alignment = Pos.TOP_LEFT
        return bHBox
    }

    private fun createButtons(bet: Boolean, amount: Double): List<Button> {
        val buttons = ArrayList<Button>()
        if (bet) {
            val foldButton = Button("FOLD")
            val callButton = Button("CALL")
            val raiseButton = Button("RAISE")
            buttons.addAll(listOf(foldButton, callButton, raiseButton))
        } else {
            val foldButton = Button("FOLD")
            val checkButton = Button("CHECK")
            val betButton = Button("BET")
            buttons.addAll(listOf(foldButton, checkButton, betButton))
        }
        for (btn in buttons) {
            createListener(btn, PokerAction.valueOf(btn.text), amount)
            btn.disableProperty().bind(view.playable.not())
        }
        return buttons
    }

    private fun createListener(button: Button, pokerAction: PokerAction, amount: Double) {
        var action: Action
        when (pokerAction) {
            PokerAction.FOLD -> action = Action(view.player, pokerAction, 0.0, controller.handState)
            PokerAction.CHECK -> action = Action(view.player, pokerAction, 0.0, controller.handState)
            PokerAction.CALL -> action = Action(view.player, pokerAction, amount, controller.handState)
            PokerAction.BET -> action =
                    Action(view.player, pokerAction, slider.valueProperty().intValue().toDouble(), controller.handState)
            PokerAction.RAISE -> action =
                    Action(view.player, pokerAction, slider.valueProperty().intValue().toDouble(), controller.handState)
            PokerAction.ALLIN -> action =
                    Action(view.player, pokerAction, view.player.chips, controller.handState)
            else -> {
                action =
                        Action(view.player, pokerAction, view.player.chips, controller.handState)
                println("Error is we get default GUI listener")
            }
        }
        val finalAction = action
        button.setOnAction {
            if (finalAction.pokerAction == PokerAction.BET || finalAction.pokerAction == PokerAction.RAISE) {
                finalAction.amount = slider.valueProperty().intValue().toDouble()
            }
            controller.pushPlayerAction(finalAction)
        }
    }

    fun createNewHandButton(): Button {
        val startButton = Button("NEW HAND")
        startButton.setOnAction {
            view.clearTable()
            view.chatClear()
            controller.startNewHand()
        }
        startButton.disableProperty().bind(view.playable.not())
        return startButton
    }
}
