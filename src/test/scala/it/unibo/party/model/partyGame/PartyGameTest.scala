package it.unibo.party.model.partyGame

import it.unibo.party.model.board.GameBoard.GameBoard
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import it.unibo.party.model.partyGame.*

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
