package it.unibo.party.controller

import it.unibo.party.controller.Moves.{Move, PartyMove}

trait PlayingAgent:
  def id: Int
  def controller: PartyController
  
  def makeMove(move: Move): Unit = move match 
    case PartyMove(_, _, _) => controller.handleMove(move.asInstanceOf[PartyMove])

object PlayingAgent:
  
  def apply(id: Int, controller: PartyController): PlayingAgent = PlayingAgentImpl(id, controller)

  private case class PlayingAgentImpl(id: Int, controller: PartyController) extends PlayingAgent