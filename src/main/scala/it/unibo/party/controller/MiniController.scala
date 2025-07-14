package it.unibo.party.controller

import it.unibo.party.common.*
import it.unibo.party.controller.Moves.Move

trait MiniController:
  def state: State
  
  def start(): MiniController
  def handleMove(move: Move): MiniController

trait TimedMiniController extends MiniController:
  def startTime: Long
  def winTime: Long

  def isTimeUp: Boolean = System.currentTimeMillis() - startTime >= winTime

object PuzzleController:

  def apply(startTime: Long, winTime: Long, state: PuzzleState): MiniController = PuzzleControllerImpl(startTime, winTime, state)
  
  private case class PuzzleControllerImpl(startTime: Long, winTime: Long, state: PuzzleState) extends TimedMiniController:
    override def start(): MiniController = this
    override def handleMove(move: Move): MiniController = this


