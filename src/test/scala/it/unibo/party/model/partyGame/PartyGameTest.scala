package it.unibo.party.model.partyGame

import it.unibo.party.geometry.Direction
import it.unibo.party.model.board.BoardBox.BoardBox
import it.unibo.party.model.board.GameBoard.{BoardPosition, GameBoard}
import it.unibo.party.model.partyGame.*
import it.unibo.party.model.player.{Pawn, Pocket}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class PartyGameTest extends AnyFlatSpec:

  val initialPartyGame: PartyGame = PartyGame.empty

  "A PartyGame" should "have a board" in:
    val board: GameBoard = GameBoard(Map.empty, Map.empty)
    initialPartyGame.board shouldEqual board

  "A PartyGame" should "have a dice" in:
    initialPartyGame.dice should not be null

  "A PartyGame" should "allow rolling the dice" in:
    val (updatedGame, result) = initialPartyGame.roll(1)
    result.length shouldEqual 1

  "A PartyGame" should "not allow rolling the dice with a negative number of times" in:
    an[IllegalArgumentException] should be thrownBy initialPartyGame.roll(-1)

  "A Dice roll" should "return as many values as requested" in:
    val (updatedGame, result) = initialPartyGame.roll(3)
    result.length shouldEqual 3

  "A PartyGame" should "keep track of the last rolled values" in :
    val (updatedGame, result) = initialPartyGame.roll(2)
    updatedGame.dice.lastRolled should contain theSameElementsAs result

  "A PartyGame" should "allow moving a pawn to a valid position" in :
    val pawnId = 1
    val initialPosition = BoardPosition(0, 0)
    val newPosition = BoardPosition(1, 0)
    val board = GameBoard(
      board = Map(
        initialPosition -> BoardBox.EmptyBox,
        newPosition -> BoardBox.EmptyBox
      ),
      pawns = Map(pawnId -> Pawn(initialPosition, Pocket.empty))
    )
    val game = PartyGame(board)(using Dice())
    val result = game.movePlayer(pawnId, Direction.Right, 1)
    result shouldBe a[MovementResult.Moved]
    val updatedGame = result.asInstanceOf[MovementResult.Moved].updatedGame

    val playerPosition = updatedGame.getPlayersPosition
    playerPosition should contain (pawnId -> newPosition)
