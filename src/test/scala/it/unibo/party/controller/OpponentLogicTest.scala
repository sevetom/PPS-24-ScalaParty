package it.unibo.party.controller

import it.unibo.party.model.board.GameBoard.GameBoard
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import it.unibo.party.model.partyGame.*

class OpponentLogicTest extends AnyFlatSpec:

  "Opponent" should "play a valid move based on the game phase" in:
    val gamePhase: GamePhase = GamePhase.INITIAL_ROLL
    val opponentLogic: OpponentLogic = OpponentLogic()
    opponentLogic.update(gamePhase) shouldBe true