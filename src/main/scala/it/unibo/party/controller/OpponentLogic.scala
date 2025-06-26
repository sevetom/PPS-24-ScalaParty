package it.unibo.party.controller

import it.unibo.party.common.{GamePhase, GameState}
import it.unibo.party.controller.Moves.{PartyMove, PartyMoveType}
import it.unibo.party.geometry.Direction

trait OpponentLogic:
  def update(state: GameState): Unit

object OpponentLogic:

  def apply(id: Int, playingAgent: PlayingAgent): OpponentLogic = OpponentLogicImpl(id, playingAgent)

  private case class OpponentLogicImpl(id: Int, playingAgent: PlayingAgent) extends OpponentLogic:
    override def update(state: GameState): Unit =
      (state.phase, state.currentPlayer) match
        case (GamePhase.PlayerMoving, 1) =>
          playingAgent.makeMove(PartyMove(id, PartyMoveType.Movement, Some(Direction.Right)))
        case _ =>
          () //placeholder
