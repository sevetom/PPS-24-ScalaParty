package it.unibo.party.model.board

import it.unibo.party.geometry.Point2D
import it.unibo.party.model.board.BoardBox.BoardBox
import it.unibo.party.model.board.GameBoard.BoardPosition.BoardPosition
import it.unibo.party.model.player.Pawn

object GameBoard:
  object BoardPosition:
    opaque type BoardPosition = Point2D[Int]

    def apply(x: Int, y: Int): BoardPosition = Point2D(x, y)

    extension (bp: BoardPosition)
      def x: Int = bp.x
      def y: Int = bp.y
      def toPoint2D: Point2D[Int] = bp

  case class GameBoard(board: Map[BoardPosition, BoardBox], pawns: Map[Int, Pawn[BoardPosition]])

  extension(b: GameBoard)
    def movePawn(pawnId: Int, position: BoardPosition): GameBoard = b match
      case GameBoard(board, pawns) => GameBoard(
        board,
        pawns.map(entry => entry match
          case (id, pawn) if id == pawnId => (id, Pawn[BoardPosition](position, pawn.pocket))
        )
      )