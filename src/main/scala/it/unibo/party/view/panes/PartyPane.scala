package it.unibo.party.view.panes

import it.unibo.party.common.PartyPhase.WaitingMinigame
import it.unibo.party.common.{PartyPhase, PartyState}
import it.unibo.party.controller.Moves.PartyMove
import it.unibo.party.controller.PlayingAgent
import it.unibo.party.model.items.CollectableOperations.*
import it.unibo.party.model.items.CollectableType.RungType
import it.unibo.party.view.components.{Board, Pocket, SideBoxes}
import it.unibo.party.view.input.InputHandler
import scalafx.geometry.Pos
import scalafx.scene.control.{Button, Label}
import scalafx.scene.input.InputIncludes.jfxKeyEvent2sfx
import scalafx.scene.layout.{BorderPane, HBox, Pane, VBox}

object PartyPane:

  /**
   * Creates a pane representing the party state, displaying the game board, player pockets, and other game elements.
   * @param state the current state of the party
   * @param playingAgent the agent controlling the player's actions
   * @return a Pane containing the game representation
   */
  def apply(state: PartyState, playingAgent: PlayingAgent): Pane = {
    val userId: Int = playingAgent.id
    val pockets: Seq[Pane] = state.itemsCollected.map((player, pocket) =>
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
        alignment = Pos.TopCenter
        children += SideBoxes.rungPrice(state.itemsPositions.find((k, v) => v.getType == RungType).get._2.getPrice)
        children += SideBoxes.diceBox(
          userId,
          state.diceResult,
          () => playingAgent.makeMove(PartyMove.DiceRoll(state.currentPlayer.id)),
          state.currentPlayer.id == userId &&
            (state.phase == PartyPhase.DiceRoll ||
              state.phase == PartyPhase.StartingRoll)
        )
        if state.phase == WaitingMinigame then
          children += new Button("Ready!"):
            onAction = _ => playingAgent.makeMove(PartyMove.StartMinigame)

      onKeyPressed = event => InputHandler(event, state, playingAgent)
      focusTraversable = true
  }

