package it.unibo.party.common

import it.unibo.party.geometry.{Direction, Point2D}
import it.unibo.party.model.items.Collectable
import it.unibo.party.model.partyGame.PartyGame
import it.unibo.party.model.player.Pocket

trait Phase
trait State

enum PartyPhase extends Phase:
  case GameStart, DiceRoll, PlayerMoving, GameOver

enum PuzzlePhase extends Phase:
  case Playing, GameOver

case class PartyState(
                      phase: PartyPhase,
                      currentPlayer: Int,
                      diceResult: Option[Int] = Option.empty,
                      possibleDirections: Option[Set[Direction]] = Option.empty,
                      itemsCollected: Map[Int, Pocket],
                      board: Set[Point2D[Int]],
                      playersPositions: Map[Int, Point2D[Int]],
                      itemsPositions: Map[Point2D[Int], Collectable],
                    ) extends State

case class PuzzleState(
                      phase: PuzzlePhase,
                      win: Boolean,
                      gridSize: Int
                      ) extends State


object PartyState:
  def fromGame(
                game: PartyGame,
                gamePhase: PartyPhase,
                playerTurn: Int,
                diceResult: Option[Int] = Option.empty,
                possibleDirections: Option[Set[Direction]] = Option.empty
              ):
  PartyState =
    PartyState(
      phase = gamePhase,
      currentPlayer = playerTurn,
      diceResult = diceResult,
      possibleDirections = possibleDirections,
      itemsCollected = game.getPockets,
      board = game.getBoardBoxes.map(_.toPoint2D),
      playersPositions = game.getPlayersPosition.map((id, pos) => id -> pos.toPoint2D),
      itemsPositions = game.getItems.map((pos, item) => pos.toPoint2D -> item)
    )