package view

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight.BOLD
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        // Define css classes
        val heading by cssclass()

        // Define colors
        val mainColor = c("#bdbd22")
    }

    init {
        heading {
            textFill = mainColor
            fontSize = 20.px
            fontWeight = BOLD
        }

        button {
            padding = box(10.px, 20.px)
            fontWeight = BOLD
        }

        val flat = mixin {
            backgroundInsets += box(0.px)
            borderColor += box(Color.DARKGRAY)
        }

        s(button, textInput) {
            +flat
        }
    }
}

//class Styles: Stylesheet() {
//    companion object {
//        val heading by cssclass()
//    }
//
//    init {
//        label and heading{
//            padding = box(10.px)
//            fontSize = 20.px
//            fontWeight = FontWeight.BOLD
//        }
//        heading {
//
//        }
//        button {
//
//        }
//    }
//}