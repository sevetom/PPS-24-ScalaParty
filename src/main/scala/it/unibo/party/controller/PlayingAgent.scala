package it.unibo.party.controller

import it.unibo.party.controller.Moves.Move

trait PlayingAgent:
  /**
   * The unique identifier of the agent.
   * @return The identifier.
   */
  def id: Int
  /**
   * The controller that the agent interacts with.
   * @return The controller.
   */
  def controller: Controller
  
  /**
   * Makes a move in the game by notifying the controller.
   * @param move The move to be made.
   */
  def makeMove(move: Move): Unit = controller.handleMove(move)

object PlayingAgent:
  
  def apply(id: Int, controller: Controller): PlayingAgent = PlayingAgentImpl(id, controller)

  private case class PlayingAgentImpl(id: Int, controller: Controller) extends PlayingAgent