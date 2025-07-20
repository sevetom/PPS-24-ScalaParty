package it.unibo.party.model.puzzle

import alice.tuprolog.{Prolog, Theory}
import it.unibo.party.model.puzzle.PuzzlePosition.PuzzlePosition
import it.unibo.party.{RichFile, openTheoryFile}
import scala.util.Random

object PuzzleUtils:

  /**
   * Generates a random solution for a puzzle with the given dimensions and maximum piece size.
   *
   * @param width The width of the puzzle.
   * @param height The height of the puzzle.
   * @param maxPieceSize The maximum size of each piece in the puzzle.
   * @return A set of sets, where each inner set represents a piece of the puzzle.
   */
  def generateRandomSolution(width: Int, height: Int, maxPieceSize: Int): Set[Set[PuzzlePosition]] =
    def getAdjacentPositions(pos: PuzzlePosition): Set[PuzzlePosition] =
      val (x, y) = (pos.x, pos.y)
      Set[PuzzlePosition](
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
        val adjacentPositions = getAdjacentPositions(startingPosition)
        val positions = availablePositions.intersect(adjacentPositions)
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

  /**
   * Checks if the given solution is valid for a puzzle of the specified dimensions.
   * @param solution A set of sets representing the pieces of the puzzle, where each inner set is a piece.
   * @param width The width of the puzzle.
   * @param height The height of the puzzle.
   * @return True if the solution is valid, false otherwise.
   */
  def isValidSolution(solution: Set[Set[PuzzlePosition]], width: Int, height: Int): Boolean =
    val mappedSolution = solution.map(p => p.map(pos => pos.y * width + pos.x))
    var prologRules = ""
    openTheoryFile("/prolog/puzzleSolutionValidation.pl").read().foreach(line =>
      if !line.startsWith("%") || line.trim.nonEmpty then
        prologRules = prologRules.concat("\n" + line)
    )
    val engine = Prolog()
    engine.setTheory(Theory(prologRules))
    engine.solve(s"is_valid_solution(${mappedSolution.map(_.mkString("[", ",", "]")).mkString("[", ",", "]")}, $width, $height).").isSuccess

  /**
   * Normalizes a piece of the puzzle by shifting its positions to have the most upper-left coordinate at (0, 0).
   * @param piece A set of PuzzlePosition representing a piece of the puzzle.
   * @return A normalized set of PuzzlePosition.
   */  
  def normalizePiece(piece: Set[PuzzlePosition]): Set[PuzzlePosition] =
    if piece.isEmpty then piece
    else {
      val minPos = piece.filter(pos => pos.y == piece.map(_.y).min).minBy(_.x)
      piece.map(pos => PuzzlePosition(pos.x - minPos.x, pos.y - minPos.y))
    }

