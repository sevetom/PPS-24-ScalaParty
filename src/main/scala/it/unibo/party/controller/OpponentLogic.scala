package it.unibo.party.controller

import it.unibo.party.common.{PartyPhase, PartyState}
import it.unibo.party.controller.Moves.{PartyMove, PartyMoveType}
import it.unibo.party.controller.pubsub.Subscriber
import it.unibo.party.geometry.Direction

trait OpponentLogic extends Subscriber[PartyState]

object OpponentLogic:

  def apply(playingAgent: PlayingAgent): OpponentLogic = OpponentLogicImpl(playingAgent)

  private case class OpponentLogicImpl(playingAgent: PlayingAgent) extends OpponentLogic:
    override def notify(event: PartyState): Unit =
      if event.currentPlayer.id == playingAgent.id then
        event.phase match
          case PartyPhase.PlayerMoving =>
            event.possibleDirections.flatMap(_.headOption).foreach:
              dir => playingAgent.makeMove(PartyMove(playingAgent.id, PartyMoveType.Movement, Some(dir)))
          case PartyPhase.DiceRoll | PartyPhase.StartingRoll =>
            playingAgent.makeMove(PartyMove(playingAgent.id, PartyMoveType.DiceRoll, None))
          case _ => // Ignore other phases