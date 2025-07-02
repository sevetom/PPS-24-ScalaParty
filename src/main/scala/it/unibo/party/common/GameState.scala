package it.unibo.party.common

import it.unibo.party.geometry.{Direction, Point2D}
import it.unibo.party.model.items.Collectable
import it.unibo.party.model.partyGame.PartyGame
import it.unibo.party.model.player.Pocket

enum GamePhase:
  case StartingRoll, DiceRoll, PlayerMoving, PlayingMinigame, GameOver

case class GameState(
                      phase: GamePhase,
                      currentPlayer: Int,
                      diceResult: Option[(Int, Int)] = Option.empty,
                      possibleDirections: Option[Set[Direction]] = Option.empty,
                      itemsCollected: Map[Int, Pocket],
                      board: Set[Point2D[Int]],
                      playersPositions: Map[Int, Point2D[Int]],
                      itemsPositions: Map[Point2D[Int], Collectable],
                    )

object GameState:
  def fromGame(
                game: PartyGame,
                gamePhase: GamePhase,
                playerTurn: Int,
                diceResult: Option[(Int, Int)] = Option.empty,
                possibleDirections: Option[Set[Direction]] = Option.empty
              ):
  GameState =
    GameState(
      phase = gamePhase,
      currentPlayer = playerTurn,
      diceResult = diceResult,
      possibleDirections = possibleDirections,
      itemsCollected = game.getPockets,
      board = game.getBoardBoxes.map(_.toPoint2D),
      playersPositions = game.getPlayersPosition.map((id, pos) => id -> pos.toPoint2D),
      itemsPositions = game.getItems.map((pos, item) => pos.toPoint2D -> item)
    )