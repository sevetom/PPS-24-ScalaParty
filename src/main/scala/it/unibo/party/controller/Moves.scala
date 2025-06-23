package it.unibo.party.controller

object Moves:
  
  enum PartyMoveType:
    case DiceRoll, Movement

  case class PartyMove(
    playerId: Int,
    moveType: PartyMoveType,
    // direction: Option[Direction]
  )