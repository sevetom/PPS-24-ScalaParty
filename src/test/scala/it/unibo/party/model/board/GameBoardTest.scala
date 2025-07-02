package it.unibo.party.model.board

import BoardBox.BoardBox
import it.unibo.party.geometry.Direction
import it.unibo.party.model.board.BoardBox.BoardBox.*
import it.unibo.party.model.board.GameBoard.BoardPosition
import it.unibo.party.model.board.GameBoard.BoardPosition.BoardPosition
import it.unibo.party.model.board.GameBoard.GameBoard
import it.unibo.party.model.items.Collectable
import it.unibo.party.model.items.Collectable.Monad
import it.unibo.party.model.player.{Pawn, Pocket}
import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*
import it.unibo.party.model.board.BoardDSL.*
import it.unibo.party.model.board.BoardDSL.Cell.*

import scala.language.postfixOps

class GameBoardTest extends AnyFlatSpec with should.Matchers:
  val pawnId = 1
  val pawnStartPosition: BoardPosition = BoardPosition(0, 0)
  val existentPosition: BoardPosition = BoardPosition(1, 0)
  val simpleBoard: GameBoard = \| | O | O |+ (pawnId, pawnStartPosition) |/
  val directionBoard: GameBoard =
    \| | O | O |
    -> | O | O |+
      (pawnId, pawnStartPosition) |/
  val itemBoard: GameBoard =
    \| | 4 | O |
    -> | O | M |+
      (pawnId, pawnStartPosition) |/

  "A GameBoard" should "allow to move a pawn in an existent position" in:
    val newBoard = simpleBoard.movePawn(pawnId, existentPosition)
    newBoard.pawns(pawnId).position should be (existentPosition)
    
  it should "Throw IllegalArgumentException when trying to move a pawn to a non-existent position" in:
    val nonExistentPosition: BoardPosition = BoardPosition(2, 2)
    assertThrows[IllegalArgumentException]:
      simpleBoard.movePawn(pawnId, nonExistentPosition)
      
  it should "Throw IllegalArgumentException when trying to move a non-existing pown" in:
    val nonExistentPawnId = 5
    assertThrows[IllegalArgumentException]:
      simpleBoard.movePawn(nonExistentPawnId, existentPosition)
      
  it should "Make a pawn acquire an item when moving to a box with an item on it" in:
    val boardWithMonad = GameBoard(
      Map(BoardPosition(0, 0) -> EmptyBox, BoardPosition(1, 0) -> FullBox(Monad())),
      Map(pawnId -> Pawn[BoardPosition](pawnStartPosition, Pocket.empty))
    )
    val positionWithMonad: BoardPosition = BoardPosition(1, 0)
    val newBoard = boardWithMonad.movePawn(pawnId, positionWithMonad)
    newBoard.pawns(pawnId).pocket.contains(Monad()) should be (true)
    
  it should "Return the correct possible directions from a position" in:
    val expectedDirections = List(Direction.Right, Direction.Down)
    val availableDirections = directionBoard.availableDirections(pawnStartPosition)
    availableDirections should contain theSameElementsAs expectedDirections

  it should "Return the board without items when calling clearItems" in:
    val clearedBoard = itemBoard.clearItems
    clearedBoard.board.values.forall(_.isEmpty) should be (true)

  it should "Return the board with items in random positions when calling addRandomItems" in:
    val item = Monad()
    val count = 2
    val boardWithItems = itemBoard.clearItems.addRandomItems(item, count)
    boardWithItems.board.values.count(_ match
      case FullBox(_) => true
      case EmptyBox => false
    ) should be (count)
    
    
    
    

