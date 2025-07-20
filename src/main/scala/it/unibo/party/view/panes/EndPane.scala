package it.unibo.party.view.panes

import it.unibo.party.view.utils.Svg.loadSvgPath
import scalafx.geometry.Pos
import scalafx.scene.control.Button
import scalafx.scene.layout.{Pane, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.SVGPath
import scalafx.scene.text.{Font, FontWeight, Text}

object EndPane:
  /**
   * Creates a pane to display the end of the game.
   *
   * @param playerId the ID of the player that the pane is displayed to
   * @param winnerId the ID of the player that won the game
   * @param onClose  a callback function to be executed when the close button is pressed
   * @return a Pane containing the end game message and a close button
   */
  def apply(playerId: Int, winnerId: Int, onClose: () => Unit): Pane = new StackPane:
    children = Seq(
      new VBox:
        spacing = 20
        alignment = Pos.Center
        children = Seq(
          new Text("GAME OVER"):
            font = Font("Poppins", FontWeight.Bold, 50)
            fill = Color.White
          ,
          new Text(if winnerId == playerId then "You won!" else s"You lose :("):
            font = Font("Poppins", 30)
            fill = Color.LightGray
          ,
          new Button("CLOSE"):
            font = Font("Poppins", 20)
            onAction = _ => onClose()
        )
        if winnerId == playerId then
          children += new SVGPath:
            content = loadSvgPath("/svg/scala.svg", "scala")
            fill = Color.Gold
    )
  
