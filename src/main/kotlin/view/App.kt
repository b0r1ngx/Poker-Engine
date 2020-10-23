package view

import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.App

class App: App(Image("aces.png"),StartMenu::class, Styles::class) {
    override fun start(stage: Stage) {
        with(stage) {
            width = 300.0
            height = 300.0
            stage.isResizable = false
        }
        super.start(stage)
    }
}
