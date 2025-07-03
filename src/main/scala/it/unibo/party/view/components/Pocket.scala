package it.unibo.party.view.components

import it.unibo.party.model.items.Collectable
import it.unibo.party.model.items.Collectable.*
import scalafx.scene.control.Label
import scalafx.scene.layout.{FlowPane, HBox, Pane, VBox}
import scalafx.scene.paint.Color

object Pocket:

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