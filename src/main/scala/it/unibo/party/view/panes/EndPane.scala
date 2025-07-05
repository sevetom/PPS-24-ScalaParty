package it.unibo.party.view.panes

import it.unibo.party.common.Player
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{Pane, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight, Text}


object EndPane:
  def apply(playerId: Int, winnerId: Int, onClose: () => Unit): Pane = new StackPane {
    children = Seq(
      new VBox {
        spacing = 20
        alignment = Pos.Center
        children = Seq(
          new Text("GAME OVER") {
            font = Font("Poppins", FontWeight.Bold, 50)
            fill = Color.White
          },
          new Text(if winnerId == playerId then "You won!" else s"You lose :(") {
            font = Font("Poppins", 30)
            fill = Color.LightGray
          },
          new Button("CLOSE") {
            font = Font("Poppins", 20)
            onAction = _ => {
              onClose()
            }
          }
        )
      }
    )
  }
  
