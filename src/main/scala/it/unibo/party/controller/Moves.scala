package it.unibo.party.controller

import it.unibo.party.geometry.Direction

object Moves:

  trait MoveType
  trait Move:
    def playerId: Int
    def moveType: MoveType
  
  enum PartyMoveType extends MoveType:
    case DiceRoll, Movement

  enum PuzzleMoveType extends MoveType:
    case CheckSolution, Exit

  enum MazeMoveType extends MoveType:
    case Movement, Exit
    
  enum MemoryMoveType extends MoveType:
    case FlipCard, Exit

  case class PartyMove(playerId: Int, moveType: PartyMoveType, direction: Option[Direction]) extends Move
    
  case class PuzzleMove(playerId: Int, moveType: PuzzleMoveType, solution: Set[Set[Int]] ) extends Move
  
  case class MazeMove(playerId: Int, moveType: MazeMoveType, direction: Option[Direction]) extends Move
    
    