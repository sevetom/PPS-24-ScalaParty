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
