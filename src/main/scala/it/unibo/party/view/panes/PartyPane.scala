package it.unibo.party.view.panes

import it.unibo.party.common.{PartyPhase, PartyState, Player}
import it.unibo.party.controller.Moves.PartyMove
import it.unibo.party.controller.Moves.PartyMoveType.DiceRoll
import it.unibo.party.model.items.CollectableOperations.*
import it.unibo.party.controller.PlayingAgent
import it.unibo.party.model.items.CollectableType.RungType
import it.unibo.party.view.components.{Board, Pocket, SideBoxes}
import it.unibo.party.view.input.InputHandler
import scalafx.scene.control.Label
import scalafx.scene.layout.{BorderPane, HBox, Pane, VBox}
import scalafx.scene.input.InputIncludes.jfxKeyEvent2sfx

object PartyPane:

  def apply(state: PartyState, playingAgent: PlayingAgent): Pane = {
    val userId: Int = playingAgent.id
    var pockets: Seq[Pane] = state.itemsCollected.map((player, pocket) =>
      Pocket(
        if userId == player.id then "Your pocket" else "Enemy's pocket",
        pocket.getAll,
        if userId == player.id then "user-pocket" else "enemy-pocket"
      )
    ).toSeq

    new BorderPane:

      top = new HBox:
        styleClass += "turn-label-container"
        children += new Label:
          text = if userId == playingAgent.id then
            "It's your turn!" else
            "It's the enemy's turn!"


      center = Board(
        state.board,
        state.playersPositions,
        state.itemsPositions
      )

      left = new VBox:
        styleClass += "side-container"
        children = pockets

      right = new VBox:
        styleClass += "side-container"
        children += SideBoxes.rungPrice(state.itemsPositions.find((k, v) => v.getType == RungType).get._2.getPrice)
        children += SideBoxes.diceBox(
          userId,
          state.diceResult,
          () => playingAgent.makeMove(PartyMove(state.currentPlayer.id, DiceRoll, None)),
          state.currentPlayer.id == userId &&
            (state.phase == PartyPhase.DiceRoll ||
              state.phase == PartyPhase.StartingRoll)
        )

      onKeyPressed = event => InputHandler(event, state, playingAgent)
      focusTraversable = true
  }

