package it.unibo.party.controller

import it.unibo.party.controller.Moves.PartyMove

trait PlayingAgent:
  def id: Int
  def controller: PartyController
  
  def makeMove(move: PartyMove): Unit =
    controller.handleMove(move)

object PlayingAgent:
  
  def apply(id: Int, controller: PartyController): PlayingAgent = PlayingAgentImpl(id, controller)

  private case class PlayingAgentImpl(id: Int, controller: PartyController) extends PlayingAgent