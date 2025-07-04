package it.unibo.party.controller

import it.unibo.party.common.State
import it.unibo.party.controller.Moves.Move

trait MiniController:
  def start(): MiniController
  def handleMove(move: Move): (MiniController, State)

case class MaxTime(duration: Long):
  def isTimeUp(startTime: Long, currentTime: Long): Boolean =
    currentTime - startTime >= duration

trait TimedMiniController(maxTime: MaxTime) extends MiniController

object PuzzleController:

  case class PuzzleControllerImpl(startTime: Long, maxTime: MaxTime) extends TimedMiniController(maxTime):
    override def start(): MiniController = PuzzleControllerImpl(System.currentTimeMillis(), maxTime)

    override def handleMove(move: Move): (MiniController, State) = ???

