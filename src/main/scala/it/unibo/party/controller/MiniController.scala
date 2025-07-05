package it.unibo.party.controller

import it.unibo.party.common.State
import it.unibo.party.controller.Moves.Move

trait MiniController:
  def start(): MiniController
  def handleMove(move: Move): (MiniController, State)

trait TimedMiniController extends MiniController:
  def startTime: Long
  def winTime: Long

//object PuzzleController:
//
//  case class PuzzleControllerImpl(startTime: Long, winTime: Long) extends TimedMiniController(startTime, winTime):
//    override def start(): MiniController = ???
//    override def handleMove(move: Move): (MiniController, State) = ???

