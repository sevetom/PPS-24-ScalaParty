package it.unibo.party.view.panes

import it.unibo.party.common.PartyState
import it.unibo.party.model.items.CollectableOperations.*
import it.unibo.party.controller.PlayingAgent
import it.unibo.party.model.items.CollectableType.RungType
import it.unibo.party.view.components.{Board, Pocket, SideBoxes}
import it.unibo.party.view.input.InputHandler
import scalafx.scene.control.Label
import scalafx.scene.layout.{BorderPane, HBox, Pane, VBox}
import scalafx.scene.input.InputIncludes.jfxKeyEvent2sfx

object PartyPane:

  def apply(state: PartyState, playingAgent: PlayingAgent): Pane =
    new BorderPane:
      
      top = new HBox:
        styleClass += "turn-label-container"
        children += new Label:
          text = state.currentPlayer match
            case 0 => "It's your turn!"
            case 1 => "It's the enemy's turn!"


      center = Board(
        state.board,
        state.playersPositions,
        state.itemsPositions
      )

      left = new VBox:
        styleClass += "side-container"
        children = Seq(
          Pocket(
            "Your pocket",
            state.itemsCollected.get(0).orElse(Option(it.unibo.party.model.player.Pocket.empty)).get.getAll,
            "user-pocket"
          ),
          Pocket(
            "Enemy's pocket",
            state.itemsCollected.get(1).orElse(Option(it.unibo.party.model.player.Pocket.empty)).get.getAll,
            "enemy-pocket"
          )
        )

      right = new VBox:
        styleClass += "side-container"
        children += SideBoxes.rungPrice(state.itemsPositions.find((k, v) => v.getType == RungType).get._2.getPrice)
        children += SideBoxes.diceBox(state.diceResult.getOrElse(0))

      onKeyPressed = event => InputHandler(event, state, playingAgent)
      focusTraversable = true

