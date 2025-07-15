package it.unibo.party.view.components

import it.unibo.party.model.items.Collectable
import it.unibo.party.model.items.Collectable.*
import scalafx.scene.control.Label
import scalafx.scene.layout.{FlowPane, HBox, Pane, VBox}
import scalafx.scene.paint.Color

object Pocket:
  /**
   * Creates a pane representing a player's pocket, displaying collectable items.
   * @param title the text to be displayed as the title of the pocket
   * @param items the sequence of collectable items to be displayed in the pocket
   * @param styleClassValue the style class to be applied to the pocket pane
   * @return a Pane containing the pocket representation
   */
  def apply(title: String, items: Seq[Collectable], styleClassValue: String): Pane =
    new VBox:
      styleClass += styleClassValue
      styleClass += "side-box"
      children += new HBox:
        styleClass += "side-box-title"
        children += new Label(title)
      children += new FlowPane:
        styleClass += "pocket-content"
        items.foreach(_ match
          case Monad() => children += Items.monad(9)
          case Rung(_) => children += Items.rung(1.5)
        )