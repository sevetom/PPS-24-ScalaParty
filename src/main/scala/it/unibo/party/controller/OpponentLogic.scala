package it.unibo.party.controller

import it.unibo.party.common.{PartyPhase, PartyState, State}
import it.unibo.party.controller.Moves.PartyMove
import it.unibo.party.controller.pubsub.Subscriber

trait OpponentLogic extends Subscriber[State]

object OpponentLogic:

  def apply(playingAgent: PlayingAgent): OpponentLogic = OpponentLogicImpl(playingAgent)

  private case class OpponentLogicImpl(playingAgent: PlayingAgent) extends OpponentLogic:
    override def notify(event: State): Unit = event match 
      case e: PartyState if e.currentPlayer.id == playingAgent.id =>
        e.phase match
          case PartyPhase.PlayerMoving =>
            e.possibleDirections.flatMap(_.headOption).foreach:
              dir => playingAgent.makeMove(PartyMove.Movement(playingAgent.id, dir))
          case PartyPhase.DiceRoll | PartyPhase.StartingRoll =>
            playingAgent.makeMove(PartyMove.DiceRoll(playingAgent.id))
          case _ => // Ignore other phases
      case _ => 
        
