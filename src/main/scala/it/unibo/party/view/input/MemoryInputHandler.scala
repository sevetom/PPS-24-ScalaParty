package it.unibo.party.view.input

import it.unibo.party.common.MinigamePhase
import it.unibo.party.controller.Moves.MemoryMove
import it.unibo.party.controller.PlayingAgent
import it.unibo.party.geometry.Point2D

object MemoryInputHandler:

  /**
   * Handles a card click in the memory game.
   *
   * @param pos the position of the card that was clicked.
   * @param phase the current phase of the minigame.
   * @param agent the playing agent that will handle the move.
   */
  def handleCardClick(pos: Point2D[Int], phase: MinigamePhase, agent: PlayingAgent): Unit =
    phase match
      case MinigamePhase.Playing =>
        agent.makeMove(MemoryMove.FlipCard(pos))
      case _ => ()
