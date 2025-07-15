package it.unibo.party.controller

import it.unibo.party.common.*
import it.unibo.party.controller.Moves.Move

trait MiniController:
  /**
   * The current state of the mini-game.
   * @return The current state of the mini-game.
   */
  def state: State

  /**
   * Starts the mini-game by initializing the state and any necessary resources.
   * @return A new instance of the MiniController with the initial state.
   */
  def start(): MiniController

  /**
   * Handles a move in the mini-game, updating the state accordingly.
   * @param move The move to be handled.
   * @return A new instance of the MiniController with the updated state.
   */
  def handleMove(move: Move): MiniController

trait TimedMiniController extends MiniController:
  /**
   * The time when the mini-game started.
   * @return The start time in milliseconds.
   */
  def startTime: Long
  /**
   * The time limit for winning the mini-game.
   * @return The win time in milliseconds.
   */
  def winTime: Long

  /**
   * Checks if the time limit for winning the mini-game has been reached.
   * @return True if the time limit has been reached, false otherwise.
   */
  def isTimeUp: Boolean = System.currentTimeMillis() - startTime >= winTime
