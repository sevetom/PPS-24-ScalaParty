package it.unibo.party.controller

import it.unibo.party.geometry.{Direction, Point2D}

object Moves:

  trait Move
  
  trait StartMove extends Move
  
  enum PartyMove extends Move:
    case DiceRoll(playerId: Int)
    case Movement(playerId: Int, direction: Direction)
    case Resume

  enum PuzzleMove extends Move:
    case CheckSolution(solution: Set[Set[Int]])

  enum MazeMove extends Move:
    case Movement(direction: Direction)

  enum MemoryMove extends Move:
    case FlipCard(pos: Point2D[Int])
    
    