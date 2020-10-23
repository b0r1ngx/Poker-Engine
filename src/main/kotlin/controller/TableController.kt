package controller

import javafx.geometry.Pos
import javafx.scene.layout.*
import javafx.scene.paint.Color
import tornadofx.*


enum class BoxType(p :Pane) {
    vBox(VBox()), hBox(HBox())
}

class TableController: Controller() {


    fun initTable(): StackPane {
        return StackPane().apply {
            ellipse {
                radiusX = 250.0
                radiusY = 125.0
                fill = Color.CORNFLOWERBLUE
            }
        }
    }

    fun initCircleUnit(boxType: BoxType,position: Pos ,color: Color, distanceBetweenChilds: Int = 0): StackPane {
        return StackPane().apply {
            if (boxType == BoxType.hBox) {
                hbox(distanceBetweenChilds) {
                    alignment = position
                    circle {
                        radius = 25.0
                        fill = color
                    }
                }
            } else {
                vbox {
                    alignment = position
                }
            }
        }
    }

    fun twoPlayers(): BorderPane {
        return BorderPane().apply {
            circle {
                centerX = 100.0
                centerY = 400.0
                radius = 25.0
                fill = Color.CORAL
            }
            circle {
                centerX = 400.0
                centerY = 400.0
                radius = 25.0
                fill = Color.RED
            }
        }
    }

    fun threePlayers(): StackPane {
        return StackPane().apply {

        }
    }
}