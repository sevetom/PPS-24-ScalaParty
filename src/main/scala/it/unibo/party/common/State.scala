package it.unibo.party.common

import it.unibo.party.geometry.{Direction, Point2D}
import it.unibo.party.model.items.Collectable
import it.unibo.party.model.maze.MazeTile
import it.unibo.party.model.partyGame.PartyGame
import it.unibo.party.model.player.Pocket

trait State:
  def phase: Phase


trait MinigameState extends State:
  override def phase: MinigamePhase
  def winner: Option[Player]

case class PuzzleState(phase: MinigamePhase, winner: Option[Player]) extends MinigameState
case class MemoryState(phase: MinigamePhase, winner: Option[Player]) extends MinigameState

case class PartyState(
                       phase: PartyPhase,
                       currentPlayer: Player,
                       diceResult: Option[(Player, Int)] = Option.empty,
                       possibleDirections: Option[Set[Direction]] = Option.empty,
                       itemsCollected: Map[Player, Pocket],
                       board: Set[Point2D[Int]],
                       playersPositions: Map[Player, Point2D[Int]],
                       itemsPositions: Map[Point2D[Int], Collectable],
                     ) extends State

object PartyState:
  def fromGame(
                game: PartyGame,
                gamePhase: PartyPhase,
                playerTurn: Player,
                diceResult: Option[(Player, Int)] = Option.empty,
                possibleDirections: Option[Set[Direction]] = Option.empty
              ):
  PartyState =
    PartyState(
      phase = gamePhase,
      currentPlayer = playerTurn,
      diceResult = diceResult,
      possibleDirections = possibleDirections,
      itemsCollected = game.getPockets.map((id, pocket) => Player(id) -> pocket),
      board = game.getBoardBoxes.map(_.toPoint2D),
      playersPositions = game.getPlayersPosition.map((id, pos) => Player(id) -> pos.toPoint2D),
      itemsPositions = game.getItems.map((pos, item) => pos.toPoint2D -> item)
    )
    
  val emptyPartyState: PartyState = 
    PartyState(
      phase = PartyPhase.StartingRoll,
      currentPlayer = Player(0),
      itemsCollected = Map.empty,
      board = Set.empty,
      playersPositions = Map.empty,
      itemsPositions = Map.empty
    )