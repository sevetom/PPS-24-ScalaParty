package it.unibo.party.controller

import it.unibo.party.common.GameState.GamePhase
import it.unibo.party.controller.Moves.PartyMove
import it.unibo.party.geometry.Point2D
import it.unibo.party.model.partyGame.Dice

sealed trait OpponentActionResult

object OpponentActionResult:
  case class Rolled(values: List[Int]) extends OpponentActionResult

  case class Turn() extends OpponentActionResult

  case class Moved(position: Point2D[Int]) extends OpponentActionResult

  case class Default() extends OpponentActionResult

trait OpponentLogic:
  def update(gamePhase: GamePhase, move: PartyMove): OpponentActionResult

object OpponentLogic:
  def apply(): OpponentLogic = OpponentLogicImpl()

  private case class OpponentLogicImpl() extends OpponentLogic:
    override def update(gamePhase: GamePhase, move: PartyMove): OpponentActionResult =
      gamePhase match
        case GamePhase.GAME_START =>
          val dice = Dice()
          val (_, result) = dice.roll()
          OpponentActionResult.Rolled(result)
        case GamePhase.DICE_ROLL =>
          val dice = Dice()
          val (_, result) = dice.roll()
          OpponentActionResult.Rolled(result)
        case GamePhase.PLAYER_MOVING =>
          OpponentActionResult.Moved(move.position.getOrElse(Point2D(0, 0)) +
            Point2D(move.diceResult.getOrElse(0), 0)) // Assuming a simple rightward movement for demonstration
        case _ =>
          OpponentActionResult.Default()
