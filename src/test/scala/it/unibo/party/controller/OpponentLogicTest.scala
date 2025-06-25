package it.unibo.party.controller

import it.unibo.party.common.GameState.GamePhase
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import it.unibo.party.controller.Moves.*
import it.unibo.party.geometry.Point2D

class OpponentLogicTest extends AnyFlatSpec:

  val moves: PartyMove = PartyMove(
    playerId = 1,
    moveType = PartyMoveType.DICE_ROLL,
    diceResult = Some(3),
    position = Some(Point2D(0, 0))
  )

  "Opponent" should "return an number to determine the first player when the game starts" in :
    val gamePhase: GamePhase = GamePhase.GAME_START
    val opponentLogic: OpponentLogic = OpponentLogic()
    opponentLogic.update(gamePhase, moves) match
      case OpponentActionResult.Rolled(values) => values.head should (be >= 1 and be <= 6)
      case _ => fail("Expected Rolled action result")

  "Opponent" should "return a valid roll result when rolling the dice" in :
    val gamePhase: GamePhase = GamePhase.DICE_ROLL
    val opponentLogic: OpponentLogic = OpponentLogic()
    opponentLogic.update(gamePhase, moves) match
      case OpponentActionResult.Rolled(values) => values.length shouldEqual 1
      case _ => fail("Expected Rolled action result")

  "Opponent" should "return a valid move when the player is moving" in :
    val gamePhase: GamePhase = GamePhase.PLAYER_MOVING
    val opponentLogic: OpponentLogic = OpponentLogic()
    opponentLogic.update(gamePhase, moves) match
      case OpponentActionResult.Moved(position, items) =>
        position shouldEqual Point2D(3, 0) // Assuming a rightward movement of 3 units
      case _ => fail("Expected Moved action result")

  "Opponent" should "not play minigames" in:
    val gamePhase: GamePhase = GamePhase.PLAYING_MINIGAME
    val opponentLogic: OpponentLogic = OpponentLogic()
    opponentLogic.update(gamePhase, moves) match
      case OpponentActionResult.Default() => succeed
      case _ => fail("Expected Default action result")