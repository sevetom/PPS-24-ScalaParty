package it.unibo.party.controller

import it.unibo.party.controller.Moves.{Move, PartyMove}

trait PlayingAgent:
  def id: Int
  def controller: Controller
  
  def makeMove(move: Move): Unit = controller.handleMove(move)

object PlayingAgent:
  
  def apply(id: Int, controller: PartyController): PlayingAgent = PlayingAgentImpl(id, controller)

  private case class PlayingAgentImpl(id: Int, controller: PartyController) extends PlayingAgent