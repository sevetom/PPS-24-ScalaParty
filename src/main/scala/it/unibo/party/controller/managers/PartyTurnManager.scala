package it.unibo.party.controller.managers

import it.unibo.party.common.PartyPhase.*
import it.unibo.party.common.{PartyPhase, Player}

/**
 * Manages the turn order and phases of the party game.
 */
trait PartyTurnManager:
  /**
   * @return the current player whose turn it is.
   */
  def currentPlayer: Player

  /**
   * @return the current phase of the party game.
   */
  def currentPhase: PartyPhase

  /**
   * Changes the order of players based on the provided map.
   *
   * @param order a map where keys are players and values are their new order.
   * @return a new instance of PartyTurnManager with updated player order.
   */
  def changePlayerOrder(order: Map[Player, Int]): PartyTurnManager

  /**
   * Advances to the next turn in the party game.
   *
   * @return a new instance of PartyTurnManager with updated current player and phase.
   */
  def nextTurn(): PartyTurnManager

  /**
   * Ends the game with the specified winner.
   *
   * @param winner the player who won the game.
   * @return a new instance of PartyTurnManager with the game ended and the winner set.
   */
  def end(winner: Player): PartyTurnManager

object PartyTurnManager:
  /**
   * Creates a new instance of PartyTurnManager with the specified players, current index, and phase.
   *
   * @param players      the list of players in the party game
   * @param currentIndex the index of the current player in the players list
   * @param currentPhase the current phase of the party game
   * @return a new instance of PartyTurnManager
   */
  def apply(players: List[Player], currentIndex: Int, currentPhase: PartyPhase): PartyTurnManager =
    PartyTurnManagerImpl(players, currentIndex, currentPhase)

  private case class PartyTurnManagerImpl(players: List[Player], currentIndex: Int, currentPhase: PartyPhase) 
    extends PartyTurnManager:

    override def currentPlayer: Player = players(currentIndex)

    override def changePlayerOrder(order: Map[Player, Int]): PartyTurnManager =
      this.copy(players = order.toList.sortBy(_._2).map(_._1), currentIndex = 0)

    override def nextTurn(): PartyTurnManager =
      val (index, phase) = currentPhase match
        case StartingRoll =>
          if currentIndex + 1 >= players.size then (0, DiceRoll)
          else (currentIndex + 1, StartingRoll)
        case DiceRoll => (currentIndex, PlayerMoving)
        case PlayerMoving =>
          if currentIndex + 1 >= players.size then (0, WaitingMinigame)
          else (currentIndex + 1, DiceRoll)
        case WaitingMinigame => (0, PlayingMinigame)
        case PlayingMinigame => (currentIndex, DiceRoll)
        case GameOver => (currentIndex, GameOver)
      this.copy(currentIndex = index, currentPhase = phase)

    override def end(winner: Player): PartyTurnManager =
      this.copy(currentIndex = players.indexOf(winner), currentPhase = GameOver)

  def fromTheStart(players: List[Player]): PartyTurnManager = apply(players, 0, StartingRoll)
