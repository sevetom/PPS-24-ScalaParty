package it.unibo.party.view.panes

import scalafx.geometry.Pos
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{Pane, StackPane, VBox}
import scalafx.scene.text.Font

object StartPane:

  /**
   * Creates a pane representing the start screen of the game.
   * @param onClick callback function to be executed when the start button is clicked
   * @return a Pane containing the start screen representation
   */
  def apply(onClick: () => Unit): Pane = new StackPane {
    children = Seq(
      new VBox {
        spacing = 20
        alignment = Pos.Center
        children = Seq(
          new Label("SCALA PARTY") {
            font = Font("Poppins", 50)
            textFill = scalafx.scene.paint.Color.White
          },
          new Button("START") {
            font = Font("Poppins", 30)
            onAction = _ => {
              onClick()
            }
          }
        )
      }
    )
  }