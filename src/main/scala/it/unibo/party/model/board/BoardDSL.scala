package it.unibo.party.model.board

import it.unibo.party.model.board
import it.unibo.party.model.board.BoardBox.BoardBox
import it.unibo.party.model.board.BoardBox.BoardBox.*
import it.unibo.party.model.board.GameBoard.{BoardPosition, GameBoard}
import it.unibo.party.model.items.Collectable.{Monad, Rung}
import it.unibo.party.model.board.BoardDSL.*
import it.unibo.party.model.board.BoardDSL.Cell.*

import scala.language.postfixOps

object BoardDSL:

  enum Cell:
    case O
    case M
    case *
    case >

  case class BuildingBoard(gameBoard: GameBoard, row: Int, col: Int)

  private def addBox(b: BuildingBoard, box: BoardBox): GameBoard =
    b.gameBoard.copy(
      board = b.gameBoard.board.updated(
        BoardPosition(b.row, b.col),
        box
      )
    )

  private def extend(b: BuildingBoard, c: Cell | Int): BuildingBoard = c match
    case Cell.O => b.copy(gameBoard = addBox(b, EmptyBox), row = b.row + 1)
    case Cell.* => b.copy(row = b.row + 1)
    case Cell.M => b.copy(gameBoard = addBox(b, FullBox(Monad())), row = b.row + 1)
    case num: Int => b.copy(gameBoard = addBox(b, FullBox(Rung(num))), row = b.row + 1)
    case Cell.> => b.copy(row = 0, col = b.col + 1)

  def -> : BuildingBoard = BuildingBoard(GameBoard(Map.empty, Map.empty), 0, 0)

  extension (b: BuildingBoard)
    def |(c: Cell | Int) :BuildingBoard = extend(b, c)
    def |/ :GameBoard = b.gameBoard

object main extends App:
  println(
    -> | O | O | O | O | O | O | 5 | O |
     > | O | * | * | * | O | * | * | M |
     > | O | * | * | * | M | * | * | O |
     > | O | M | O | O | O | O | M | O | O | O |
     > | O | * | * | * | O | * | * | O | * | O |
     > | O | * | * | * | O | * | * | O | * | O |
     > | O | * | * | * | O | * | * | O | * | O |
     > | O | M | O | O | O | O | M | O | O | O |
     > | * | * | * | O | * | * | * | * | * | O |
     > | * | * | * | O | O | M | O | O | O | O |/
  )
