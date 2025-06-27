package it.unibo.party.controller

import it.unibo.party.common.{GamePhase, GameState}
import it.unibo.party.controller.Moves.{PartyMove, PartyMoveType}
import it.unibo.party.controller.pubsub.Subscriber
import it.unibo.party.geometry.Direction

trait OpponentLogic extends Subscriber[GameState]

object OpponentLogic:

  def apply(playingAgent: PlayingAgent): OpponentLogic = OpponentLogicImpl(playingAgent)

  private case class OpponentLogicImpl(playingAgent: PlayingAgent) extends OpponentLogic:
    override def notify(event: GameState): Unit =
      if event.currentPlayer == playingAgent.id then {
        event.phase match
          case GamePhase.PlayerMoving =>
            playingAgent.makeMove(PartyMove(playingAgent.id, PartyMoveType.Movement, Some(event.possibleDirections.get.head)))
          case _ =>
      }
