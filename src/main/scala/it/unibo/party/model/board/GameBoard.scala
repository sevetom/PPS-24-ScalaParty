package it.unibo.party.model.board

import it.unibo.party.geometry.{Direction, Point2D}
import it.unibo.party.model.board.BoardBox.BoardBox
import it.unibo.party.model.board.BoardBox.BoardBox.*
import it.unibo.party.model.board.BoardDSL.*
import it.unibo.party.model.board.BoardDSL.Cell.*
import it.unibo.party.model.board.GameBoard.BoardPosition.BoardPosition
import it.unibo.party.model.items.Collectable
import it.unibo.party.model.items.Collectable.*
import it.unibo.party.model.player.{Pawn, Pocket}

import scala.language.postfixOps

object GameBoard:
  
  object BoardPosition:
    opaque type BoardPosition = Point2D[Int]

    def apply(x: Int, y: Int): BoardPosition = Point2D(x, y)
    
    extension (bp: BoardPosition)
      def x: Int = bp.x
      def y: Int = bp.y
      def toPoint2D: Point2D[Int] = bp
      def +(dir: Direction): BoardPosition =
        val (dx, dy) = dir.offset
        BoardPosition(bp.x + dx, bp.y + dy)

  case class GameBoard(board: Map[BoardPosition, BoardBox], pawns: Map[Int, Pawn[BoardPosition]]):
    def movePawn(pawnId: Int, position: BoardPosition): GameBoard = this match
      case GameBoard(board, pawns) if pawns.contains(pawnId) && board.contains(position) =>
        val (newOwned, newBoardBox) = board(position).tryAcquireItem(pawns(pawnId).pocket.getAll)
        GameBoard(
          board.updated(position, newBoardBox),
          pawns.updated(pawnId, pawns(pawnId).moveTo(position).withPocket(Pocket(newOwned))
          )
        )
      case _ => throw IllegalArgumentException()

    def availableDirections(from: BoardPosition): Set[Direction] =
      ( for dir <- Direction.values
            if this.board.contains(from + dir)
        yield dir
      ).toSet
      
    def clearItems: GameBoard = 
      GameBoard(
        this.board.map((_, _) match
          case (pos, _) => (pos, EmptyBox)
        ),
        this.pawns
      )
    
    def addRandomItems(item: Collectable, count: Int): GameBoard =
      val randomPositions = scala.util.Random.shuffle(this.board.filter((_, box) => box.isEmpty).keys.toList).take(count)
      val newBoard = randomPositions.foldLeft(this.board)((acc, pos) => acc.updated(pos, FullBox(item)))
      GameBoard(newBoard, this.pawns)


      

  def standardBoard() : GameBoard =
     \| | O | O | O | O | O | O | 5 | O |
     -> | O | * | * | * | O | * | * | M |
     -> | O | * | * | * | M | * | * | O |
     -> | O | M | O | O | O | O | M | O | O | O |
     -> | O | * | * | * | O | * | * | O | * | O |
     -> | O | * | * | * | O | * | * | O | * | O |
     -> | O | * | * | * | O | * | * | O | * | O |
     -> | O | M | O | O | O | O | M | O | O | O |
     -> | * | * | * | O | * | * | * | * | * | O |
     -> | * | * | * | O | O | M | O | O | O | O |+
      (0, BoardPosition(9, 9)) |+
      (1, BoardPosition(9, 9)) |/

