package it.unibo.party.controller

import it.unibo.party.geometry.Direction

object Moves:
  
  enum PartyMoveType:
    case DiceRoll, Movement

  case class PartyMove(
    playerId: Int,
    moveType: PartyMoveType,
    direction: Option[Direction]
  )