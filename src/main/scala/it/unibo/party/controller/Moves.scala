package it.unibo.party.controller

import it.unibo.party.geometry.Point2D

object Moves:
  
  enum PartyMoveType:
    case DICE_ROLL, MOVEMENT

  case class PartyMove(
    playerId: Int,
    moveType: PartyMoveType,
    diceResult: Option[Int] = None,
    position: Option[Point2D[Int]] = None,
    // direction: Option[Direction]
  )