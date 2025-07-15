package it.unibo.party.view.panes

import scalafx.geometry.Pos
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{Pane, StackPane, VBox}
import scalafx.scene.text.Font

object MinigamePane:

  /**
   * Creates a standard minigame UI pane with a title and a button.
   * @param onClick the action to perform when the button is clicked
   * @return a StackPane containing the minigame UI elements
   */
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
  