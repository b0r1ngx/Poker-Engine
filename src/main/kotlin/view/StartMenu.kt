package view

import javafx.beans.value.ObservableValue
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.geometry.Side
import javafx.scene.control.Label
import javafx.scene.paint.Color
import javafx.stage.Modality
import tornadofx.*

enum class TypesOfPoker {
}

class StartMenu: View("Poker") {
    override val root = vbox(5) {
        alignment = Pos.CENTER
        label("What type of poker you prefer to play:")
        listmenu(orientation = Orientation.HORIZONTAL,
                iconPosition = Side.LEFT) {
            item(text = "Texas Hold'em") {
                activeItem = this
                whenSelected { typeOfPoker = "Texas Hold'em" }
            }

            item(text = "Omaha") {
                whenSelected { typeOfPoker = "Omaha" }
            }
            item(text = "7-Card Stud") {
                whenSelected { typeOfPoker = "7-Card Stud" }
            }
        }

        label("How much seats around the table:")
        vbox {
            slider(
                    min = 2.0,
                    max = 9.0, ) {
                isSnapToTicks = true
                isShowTickLabels = true
                isShowTickMarks = true
                majorTickUnit = 1.0
                minorTickCount = 0
                blockIncrement = 1.0
                this.valueProperty().addListener {
                    observable, oldValue, newValue -> tableSeats = newValue.toInt() }
            } }

        label("What level of players do u wanna see around the table:")
        listmenu(orientation = Orientation.HORIZONTAL,
                iconPosition = Side.TOP) {

            item(text = "Low") {
                activeItem = this
                whenSelected { typeAI = "Low" }
            }
            item(text = "Medium") {
                whenSelected { typeAI = "Medium" }
            }
            item(text = "Advanced") {
                whenSelected { typeAI = "Advanced" }
            }
            item(text = "Mixed") {
                whenSelected { typeAI = "Mixed" }
            }
        }

        vbox {
            alignment = Pos.CENTER
            button("Play") {
                alignment = Pos.CENTER
                action {
                    find<MainView>().openModal(
                            modality = Modality.NONE,
                            escapeClosesWindow = false,
                            owner = null,
                            resizable = true)//false
                    close()
                }
            }
        }
    }

    companion object {
        var typeOfPoker = "Texas Hold'em"
        var tableSeats = 2
        var typeAI = "Low"
    }
}