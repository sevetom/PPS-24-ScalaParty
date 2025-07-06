package it.unibo.party.view.panes

import scalafx.geometry.Pos
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{Pane, StackPane, VBox}
import scalafx.scene.text.Font

object MinigamePane:

  def apply(onClick: () => Unit): Pane = new StackPane {
    children = Seq(
      new VBox {
        spacing = 20
        alignment = Pos.Center
        children = Seq(
          new Label("MINIGAME") {
            font = Font("Poppins", 50)
            textFill = scalafx.scene.paint.Color.White
          },
          new Button("WIN") {
            font = Font("Poppins", 30)
            onAction = _ => {
              onClick()
            }
          }
        )
      }
    )
  }
  