package it.unibo.party.controller

import it.unibo.party.common.Direction

object Moves:
  
  enum PartyMoveType:
    case DICE_ROLL, MOVEMENT

  case class PartyMove(
    playerId: Int,
    moveType: PartyMoveType, 
    direction: Option[Direction]
  )