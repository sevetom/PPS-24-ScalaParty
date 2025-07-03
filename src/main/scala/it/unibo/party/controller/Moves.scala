package it.unibo.party.controller

import it.unibo.party.geometry.Direction

object Moves:

  trait MoveType
  trait Move
  
  enum PartyMoveType extends MoveType:
    case DiceRoll, Movement

  enum PuzzleMoveType extends MoveType:
    case CheckSolution

  enum MazeMoveType extends MoveType:
    case Movement

  case class PartyMove(
    playerId: Int,
    moveType: PartyMoveType,
    direction: Option[Direction]
  ) extends Move

  case class PuzzleMove(
    solution: Set[Set[Int]]
  ) extends Move