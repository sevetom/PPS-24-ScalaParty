package it.unibo.party.view.panes

import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{Pane, StackPane}
import scalafx.scene.paint.Color
import scalafx.scene.text.Font


object EndPane:
  def apply(): Pane = {
    new Pane {
      style = "-fx-background-color: rgba(0, 0, 0, 0.5);"
      children = Seq(
        new StackPane {
          style = "-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 10;"
          padding = Insets(20)
          children = Seq(
            new Button("Game Over!") {
              onMouseClicked = _ => System.exit(0)
            }
          )
        }
      )
    }
  }
  
