package it.unibo.party.controller

import it.unibo.party.common.{GamePhase, GameState}
import it.unibo.party.controller.Moves.{PartyMove, PartyMoveType}
import it.unibo.party.controller.pubsub.Subscriber
import it.unibo.party.geometry.Direction

trait OpponentLogic extends Subscriber[GameState]

object OpponentLogic:

  def apply(id: Int, playingAgent: PlayingAgent): OpponentLogic = OpponentLogicImpl(id, playingAgent)

  private case class OpponentLogicImpl(id: Int, playingAgent: PlayingAgent) extends OpponentLogic:
    override def notify(event: GameState): Unit =
      (event.phase, event.currentPlayer) match
        case (GamePhase.PlayerMoving, 1) =>
          playingAgent.makeMove(PartyMove(id, PartyMoveType.Movement, Some(Direction.Right)))
        case _ =>
          () //placeholder
