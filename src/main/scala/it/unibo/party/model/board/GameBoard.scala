package it.unibo.party.model.board

import it.unibo.party.geometry.{Direction, Point2D}
import it.unibo.party.model.board.BoardBox.BoardBox
import it.unibo.party.model.board.BoardBox.BoardBox.*
import it.unibo.party.model.board.BoardDSL.*
import it.unibo.party.model.board.BoardDSL.Cell.*
import it.unibo.party.model.board.GameBoard.BoardPosition.BoardPosition
import it.unibo.party.model.items.CollectableOperations.toCollectable
import it.unibo.party.model.items.{Collectable, CollectableType}
import it.unibo.party.model.player.{Pawn, Pocket}

import scala.language.postfixOps
import scala.util.Random

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
    /**
     * Moves a pawn to a new position on the board.
     * @param pawnId the ID of the pawn to move.
     * @param position the new position to move the pawn to.
     * @throws IllegalArgumentException if the pawn ID is not found or the position is not valid.
     * @return a new GameBoard with the pawn moved to the specified position.
     */
    def movePawn(pawnId: Int, position: BoardPosition): GameBoard = this match
      case GameBoard(board, pawns) if pawns.contains(pawnId) && board.contains(position) =>
        val (newOwned, newBoardBox) = board(position).tryAcquireItem(pawns(pawnId).pocket.getAll)
        GameBoard(
          board.updated(position, newBoardBox),
          pawns.updated(pawnId, pawns(pawnId).moveTo(position).withPocket(Pocket(newOwned))
          )
        )
      case _ => throw IllegalArgumentException()

    /**
     * Returns the possible directions a pawn can move from a given position.
     * @param from the position from which to check available directions.
     * @return a set of directions that are available from the given position.
     */
    def availableDirections(from: BoardPosition): Set[Direction] =
      ( for dir <- Direction.values
            if this.board.contains(from + dir)
        yield dir
      ).toSet

    /**
     * Clears all items from the board, setting all boxes to EmptyBox.
     * @return a new GameBoard with all boxes cleared.
     */
    def clearItems: GameBoard = copy(board = board.view.mapValues(_ => EmptyBox).toMap)

    /**
     * Adds items in random empty positions on the board.
     * @param itemType the type of item to add.
     * @param count the number of items to add.
     * @return a new GameBoard with the specified number of items added at random empty positions.
     */
    def addRandomItems(itemType: CollectableType, count: Int): GameBoard =
      val randomPositions =
        Random.shuffle(board.filter(_._2.isEmpty).keys.toList.diff(pawns.values.map(_.position).toList)).take(count)
      val newBoard = randomPositions.foldLeft(board)((acc, pos) =>
        acc.updated(pos, FullBox(itemType.toCollectable)))
      GameBoard(newBoard, pawns)

    /**
     * Adds a single item at a random empty position on the board.
     * @param item the item to add to the board.
     * @return a new GameBoard with the item added at a random empty position.
     */
    def addRandomItem(item: Collectable): GameBoard =
      val emptyPositions = this.board.filter((_, box) => box.isEmpty).keys.toList.diff(pawns.values.map(_.position).toList)
      if emptyPositions.isEmpty then this
      else
        val randomPosition = Random.shuffle(emptyPositions).head
        GameBoard(this.board.updated(randomPosition, FullBox(item)), this.pawns)

  def standardBoard() : GameBoard =
     \| | O | O | M | O | O | O | 5 | O |
     -> | O | * | * | * | O | * | * | M |
     -> | O | * | * | * | M | * | * | O |
     -> | O | M | O | O | O | O | M | O | O | O |
     -> | O | * | * | * | O | * | * | O | * | O |
     -> | O | * | * | * | M | * | * | O | * | M |
     -> | O | * | * | * | O | * | * | O | * | O |
     -> | O | M | O | O | O | O | M | O | O | O |
     -> | * | * | * | O | * | * | * | * | * | O |
     -> | * | * | * | O | O | M | O | O | O | O |+
      (0, BoardPosition(9, 9)) |+
      (1, BoardPosition(9, 9)) |/

