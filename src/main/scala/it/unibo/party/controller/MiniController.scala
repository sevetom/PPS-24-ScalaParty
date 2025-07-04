package it.unibo.party.controller

import it.unibo.party.common.State
import it.unibo.party.controller.Moves.Move

trait MiniController:
  def start(): MiniController
  def handleMove(move: Move): (MiniController, State)

case class MaxTime(duration: Long):
  def isTimeUp(startTime: Long, currentTime: Long): Boolean =
    currentTime - startTime >= duration

trait TimedMiniController(startTime: Long, winTime: Long) extends MiniController

//object PuzzleController:
//
//  case class PuzzleControllerImpl(startTime: Long, winTime: Long) extends TimedMiniController(startTime, winTime):
//    override def start(): MiniController = ???
//    override def handleMove(move: Move): (MiniController, State) = ???

