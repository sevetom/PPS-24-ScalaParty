package it.unibo.party.controller.managers

import it.unibo.party.common.PartyPhase.*
import it.unibo.party.common.{PartyPhase, Player}

trait PartyTurnManager:
  def currentPlayer: Player

  def currentPhase: PartyPhase

  def changePlayerOrder(order: Map[Player, Int]): PartyTurnManager

  def nextTurn(): PartyTurnManager

  def end(winner: Player): PartyTurnManager

object PartyTurnManager:
  def apply(players: List[Player], currentIndex: Int, currentPhase: PartyPhase): PartyTurnManager =
    PartyTurnManagerImpl(players, currentIndex, currentPhase)

  private case class PartyTurnManagerImpl(players: List[Player], currentIndex: Int, currentPhase: PartyPhase) extends PartyTurnManager:

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
          if currentIndex + 1 >= players.size then (0, PlayingMinigame)
          else (currentIndex + 1, DiceRoll)
        case PlayingMinigame => (currentIndex, DiceRoll)
        case GameOver => (currentIndex, GameOver)
      this.copy(currentIndex = index, currentPhase = phase)

    override def end(winner: Player): PartyTurnManager =
      this.copy(currentIndex = players.indexOf(winner), currentPhase = GameOver)

  def fromTheStart(players: List[Player]): PartyTurnManager = apply(players, 0, StartingRoll)
