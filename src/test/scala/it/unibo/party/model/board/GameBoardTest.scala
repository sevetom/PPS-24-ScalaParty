package it.unibo.party.model.board

import BoardBox.BoardBox
import it.unibo.party.model.board.BoardBox.BoardBox.*
import it.unibo.party.model.board.GameBoard.BoardPosition
import it.unibo.party.model.board.GameBoard.BoardPosition.BoardPosition
import it.unibo.party.model.board.GameBoard.GameBoard
import it.unibo.party.model.player.{Pawn, Pocket}
import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*

class GameBoardTest extends AnyFlatSpec with should.Matchers:
  val pawnId = 1
  val pawnStratPosition: BoardPosition = BoardPosition(0, 0)
  val simpleBoard: GameBoard = GameBoard(
    Map(BoardPosition(0, 0) -> EmptyBox, BoardPosition(1, 0) -> EmptyBox),
    Map(pawnId -> Pawn[BoardPosition](pawnStratPosition, Pocket.empty))
  )

  "A GameBoard" should "allow to move a pawn in an existent position" in:
    val pawnEndPosition: BoardPosition = BoardPosition(1, 0)
    val newBoard = simpleBoard.movePawn(pawnId, pawnEndPosition)
    newBoard.pawns(pawnId).position should be (pawnEndPosition)
    
    

