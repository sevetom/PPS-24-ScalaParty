package it.unibo.party.common

import it.unibo.party.geometry.{Direction, Point2D}
import it.unibo.party.model.items.Collectable
import it.unibo.party.model.memory.Memory
import it.unibo.party.model.partyGame.PartyGame
import it.unibo.party.model.player.Pocket

trait State:
  def phase: Phase

trait MinigameState extends State:
  override def phase: MinigamePhase
  def winner: Option[Player]

case class PuzzleState(
                        phase: MinigamePhase,
                        winner: Option[Player],
                        currentSolution: Map[Int, Set[Point2D[Int]]],
                        currentPiece: Option[Int],
                        nonPlacedPieces: Map[Int, Set[Point2D[Int]]],
                        width: Int,
                        height: Int,
                        maxTime: Long
                      ) extends MinigameState

object PuzzleState:
  def empty: PuzzleState =
    PuzzleState(MinigamePhase.Playing, None, Map.empty, None, Map.empty, 0, 0, 0L)

case class MemoryState(
                        game: Memory,
                        phase: MinigamePhase,
                        winner: Option[Player],
                        firstSelection: Option[Point2D[Int]],
                        mismatchedPair: Option[(Point2D[Int], Point2D[Int])] = None,
                        winTime: Long
                      ) extends MinigameState

/**
 * Represents the state of the party game.
 * 
 * @param phase the current phase of the party game
 * @param currentPlayer the player whose turn it is
 * @param diceResult the result of the dice roll, if needed
 * @param possibleDirections the possible directions the current player can move, if needed
 * @param itemsCollected a map of players and their collected items
 * @param board the set of points representing the game board
 * @param playersPositions a map of players and their positions on the board
 * @param itemsPositions a map of item positions on the board
 */
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
  /**
   * Utility for creating a PartyState from a PartyGame instance.
   * 
   * @param game the PartyGame instance
   * @param gamePhase the current phase of the game
   * @param playerTurn the player whose turn it is
   * @param diceResult the result of the dice roll, if needed
   * @param possibleDirections the possible directions the current player can move, if needed
   * @return
   */
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
    
  val empty: PartyState = 
    PartyState(
      phase = PartyPhase.StartingRoll,
      currentPlayer = Player(0),
      itemsCollected = Map.empty,
      board = Set.empty,
      playersPositions = Map.empty,
      itemsPositions = Map.empty
    )