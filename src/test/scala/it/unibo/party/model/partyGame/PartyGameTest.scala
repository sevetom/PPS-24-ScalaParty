package it.unibo.party.model.partyGame

import it.unibo.party.model.board.GameBoard.GameBoard
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import it.unibo.party.model.partyGame.*

class PartyGameTest extends AnyFlatSpec:

  "A PartyGame" should "have a board" in:
    val board: GameBoard = GameBoard(Map.empty, Map.empty)
    val partyGame: PartyGame = PartyGame(board)
    partyGame.board shouldEqual board
