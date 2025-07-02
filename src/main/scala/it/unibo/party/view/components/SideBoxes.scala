package it.unibo.party.view.components

import scalafx.scene.control.Label
import scalafx.scene.layout.{HBox, Pane, VBox}

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

  def diceBox(result: Int): Pane = new VBox:
    styleClass += "side-box"
    styleClass += "dice-box"
    children += new HBox:
      styleClass += "dice-box-header"
    children += new HBox:
      styleClass += "dice-content"
      children += Label(result.toString)