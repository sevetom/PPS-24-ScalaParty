package it.unibo.party.view.components

import com.sun.javafx.geometry.BoundsUtils
import it.unibo.party.common.Player
import scalafx.geometry.Pos
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{HBox, Pane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, Text}

object SideBoxes:

  def rungPrice(price: Int): Pane = new VBox:
    styleClass += "side-box"
    styleClass += "rung-price-box"
    children += new HBox:
      styleClass += "side-box-title"
      children += Label("Rung Price")
    children += new HBox:
      styleClass += "rung-price-content"
      children += Label(price.toString)
      children += Items.monad(22)

  def diceBox(userId: Int, result: Option[(Player, Int)], onRoll: () => Unit, isEnabled: Boolean): Pane = new VBox:
    styleClass += "side-box"
    styleClass += "dice-box"
    children += new HBox:
      styleClass += "dice-box-header"
      val rollButton = Button("Roll")
      rollButton.onAction = _ => onRoll()
      children += rollButton
      rollButton.disable = !isEnabled
    children += new HBox:
      styleClass += "dice-content"
      children ++= Seq(
        new Text(result
          .map((player, value) => if player.id == userId then "You " else "Enemy ")
          .getOrElse("")) {
          font = Font("Poppins", 20)
          fill = Color.White
        },
        new Label(result.map(_._2.toString).getOrElse(""))
      )