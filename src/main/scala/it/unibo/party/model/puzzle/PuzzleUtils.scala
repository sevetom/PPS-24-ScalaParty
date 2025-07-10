package it.unibo.party.model.puzzle

import it.unibo.party.model.puzzle.PuzzlePosition.PuzzlePosition

import scala.collection.immutable.HashSet
import scala.util.Random

object PuzzleUtils:

  def generateRandomSolution(width: Int, height: Int, maxPieceSize: Int): Set[Set[PuzzlePosition]] =
    def getAdiacentPositions(pos: PuzzlePosition): Set[PuzzlePosition] =
      val (x, y) = (pos.x, pos.y)
      HashSet[PuzzlePosition](
        PuzzlePosition(x - 1, y),
        PuzzlePosition(x + 1, y),
        PuzzlePosition(x, y - 1),
        PuzzlePosition(x, y + 1)
      ).filter(p => p.x >= 0 && p.x < width && p.y >= 0 && p.y < height)

    def generateRandomPieceRec(
                                startingPosition: PuzzlePosition,
                                availablePositions: Set[PuzzlePosition],
                                pieceSize: Int
                              ): Set[PuzzlePosition] = pieceSize match
      case 0 => Set.empty
      case _ =>
        val adiacentPositions = getAdiacentPositions(startingPosition)
        val positions = availablePositions.intersect(adiacentPositions)
        positions match
          case s if s.isEmpty => Set(startingPosition)
          case _ =>
            val randomPosition = Random.shuffle(positions.toList).head
            val newAvailablePositions = availablePositions - randomPosition
            val otherPiece = generateRandomPieceRec(randomPosition, newAvailablePositions, pieceSize - 1)
            Set(randomPosition) ++ generateRandomPieceRec(randomPosition, newAvailablePositions, pieceSize - 1)

    def generateRandomSolutionRec(availablePositions: Set[PuzzlePosition]): Set[Set[PuzzlePosition]] =
      availablePositions match
        case s if s.isEmpty => Set(Set.empty)
        case _ =>
          val randomPosition = Random.shuffle(availablePositions.toList).head
          val piece = generateRandomPieceRec(randomPosition, availablePositions, maxPieceSize)
          val newAvailablePositions = availablePositions -- piece
          val remainingPieces = generateRandomSolutionRec(newAvailablePositions)
          remainingPieces match
            case s if s.size == 1 && s.head.isEmpty => Set(piece)
            case _ => remainingPieces + piece

    val initialPositions: Set[PuzzlePosition] =
      (
        for x <- 0 until width
            y <- 0 until height
        yield PuzzlePosition(x, y)
      ).toSet
    generateRandomSolutionRec(initialPositions)



  def isValidSolution(solution: Set[Set[PuzzlePosition]], width: Int, height: Int, pieceSize: Int): Boolean =
    ???
    
