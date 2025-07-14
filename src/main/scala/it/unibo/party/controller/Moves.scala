package it.unibo.party.controller

import it.unibo.party.geometry.{Direction, Point2D}

object Moves:

  trait Move

  case class StartMove() extends Move

  enum PartyMove extends Move:
    case DiceRoll(playerId: Int)
    case Movement(playerId: Int, direction: Direction)
    case StartMinigame
    case Resume

  enum PuzzleMove extends Move:
    case SelectPiece(pieceId: Int)
    case PlacePiece(position: Point2D[Int])
    case RemovePiece
    

  enum MazeMove extends Move:
    case Movement(direction: Direction)

  enum MemoryMove extends Move:
    case FlipCard(pos: Point2D[Int])
    
    