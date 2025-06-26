package it.unibo.party.model.board

import it.unibo.party.geometry.{Direction, Point2D}
import it.unibo.party.model.board.BoardBox.BoardBox
import it.unibo.party.model.board.BoardBox.BoardBox.*
import it.unibo.party.model.board.GameBoard.BoardPosition.BoardPosition
import it.unibo.party.model.items.Collectable.*
import it.unibo.party.model.player.{Pawn, Pocket}

object GameBoard:
  object BoardPosition:
    opaque type BoardPosition = Point2D[Int]

    def apply(x: Int, y: Int): BoardPosition = Point2D(x, y)
    
    extension (bp: BoardPosition)
      def x: Int = bp.x
      def y: Int = bp.y
      def toPoint2D: Point2D[Int] = bp
      
    extension (bp: BoardPosition)
      def +(dir: Direction): BoardPosition =
        val (dx, dy) = dir.offset
        BoardPosition(bp.x + dx, bp.y + dy)

  case class GameBoard(board: Map[BoardPosition, BoardBox], pawns: Map[Int, Pawn[BoardPosition]])

  extension (b: GameBoard)
    def movePawn(pawnId: Int, position: BoardPosition): GameBoard = b match
      case GameBoard(board, pawns) if pawns.contains(pawnId) && board.contains(position) =>
        val (newOwned, newBoardBox) = board(position).tryAcquireItem(pawns(pawnId).pocket.getAll)
        GameBoard(
          board.updated(position, newBoardBox),
          pawns.updated(pawnId, pawns(pawnId).moveTo(position).withPocket(Pocket(newOwned))
          )
        )
      case _ => throw IllegalArgumentException()

    def availableDirections(from: BoardPosition): List[Direction] =
      Direction
        .values
        .map(dir => (dir, from + dir))
        .filter((dir, pos) => b.board.contains(pos))
        .map((dir, _) => dir)
        .toList


  def standardBoard() : GameBoard =
    GameBoard(
      Map(
          BoardPosition(0, 0) -> FullBox(Rung(3)), BoardPosition(1, 0) -> EmptyBox, BoardPosition(2, 0) -> EmptyBox, BoardPosition(3, 0) -> EmptyBox, BoardPosition(4, 0) -> EmptyBox, BoardPosition(5, 0) -> EmptyBox, BoardPosition(6, 0) -> EmptyBox, BoardPosition(7, 0) -> EmptyBox,
          BoardPosition(0, 1) -> EmptyBox, BoardPosition(4, 1) -> EmptyBox, BoardPosition(7, 1) -> EmptyBox,
          BoardPosition(0, 2) -> EmptyBox, BoardPosition(4, 2) -> FullBox(Monad()), BoardPosition(7, 2) -> EmptyBox,
          BoardPosition(0, 3) -> EmptyBox, BoardPosition(1, 3) -> EmptyBox, BoardPosition(2, 3) -> EmptyBox, BoardPosition(3, 3) -> EmptyBox, BoardPosition(4, 3) -> EmptyBox, BoardPosition(5, 3) -> EmptyBox, BoardPosition(6, 3) -> EmptyBox, BoardPosition(7, 3) -> EmptyBox, BoardPosition(8, 3) -> EmptyBox, BoardPosition(9, 3) -> EmptyBox,
          BoardPosition(0, 4) -> EmptyBox, BoardPosition(4, 4) -> EmptyBox, BoardPosition(7, 4) -> EmptyBox, BoardPosition(9, 4) -> EmptyBox,
          BoardPosition(0, 5) -> EmptyBox, BoardPosition(4, 5) -> EmptyBox, BoardPosition(7, 5) -> EmptyBox, BoardPosition(9, 5) -> EmptyBox,
          BoardPosition(0, 6) -> EmptyBox, BoardPosition(4, 6) -> EmptyBox, BoardPosition(7, 6) -> EmptyBox, BoardPosition(9, 6) -> EmptyBox,
          BoardPosition(0, 7) -> EmptyBox, BoardPosition(1, 7) -> FullBox(Monad()), BoardPosition(2, 7) -> EmptyBox, BoardPosition(3, 7) -> EmptyBox, BoardPosition(4, 7) -> EmptyBox, BoardPosition(5, 7) -> EmptyBox, BoardPosition(6, 7) -> EmptyBox, BoardPosition(7, 7) -> EmptyBox, BoardPosition(8, 7) -> EmptyBox, BoardPosition(9, 7) -> EmptyBox,
          BoardPosition(3, 8) -> EmptyBox, BoardPosition(9, 8) -> EmptyBox,
          BoardPosition(3, 9) -> EmptyBox, BoardPosition(4, 9) -> EmptyBox, BoardPosition(5, 9) -> EmptyBox, BoardPosition(6, 9) -> EmptyBox, BoardPosition(7, 9) -> EmptyBox, BoardPosition(8, 9) -> EmptyBox, BoardPosition(9, 9) -> EmptyBox
      ),
      Map(
        0 -> Pawn[BoardPosition](BoardPosition(9, 9), Pocket.empty),
        1 -> Pawn[BoardPosition](BoardPosition(9, 9), Pocket.empty),
      )
    )
