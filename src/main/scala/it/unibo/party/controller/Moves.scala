package it.unibo.party.controller

import it.unibo.party.geometry.Point2D
import it.unibo.party.model.board.GameBoard.GameBoard

object Moves:
  
  enum PartyMoveType:
    case DICE_ROLL, MOVEMENT

  case class PartyMove(
    playerId: Int,
    moveType: PartyMoveType,
    diceResult: Option[Int] = None,
    position: Option[Point2D[Int]] = None,
    board: Option[GameBoard] = None,
    // direction: Option[Direction]
  )