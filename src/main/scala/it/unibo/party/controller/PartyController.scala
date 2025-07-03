package it.unibo.party.controller

import it.unibo.party.common.PartyPhase.StartingRoll
import it.unibo.party.common.{PartyPhase, PartyState, Player}
import it.unibo.party.controller.Moves.{PartyMove, PartyMoveType}
import it.unibo.party.controller.managers.PartyTurnManager
import it.unibo.party.controller.pubsub.{Publisher, Subscriber}
import it.unibo.party.geometry.Direction
import it.unibo.party.model.partyGame.{Dice, MovementResult, PartyGame}
import it.unibo.party.model.items.CollectableType.RungType

private val stepsPerPlayer: Int = 1
private val winRungs: Int = 1

trait PartyController:
  def addViewListener(listener: Subscriber[PartyState]): Unit

  def addMoveListener(listener: Subscriber[PartyState]): Unit

  def start(): Unit

  def handleMove(move: PartyMove): Unit

object PartyController:
  def apply(game: PartyGame, players: List[Player]) = PartyControllerImpl(game, players)

  class PartyControllerImpl(private var game: PartyGame, private var players: List[Player]) extends PartyController:
    private var statePublisher: Publisher[PartyState] = Publisher(List.empty)
    private var turnManager: PartyTurnManager = PartyTurnManager.fromTheStart(players)
    private var diceResults: Map[Player, Int] = Map.empty

    override def addViewListener(listener: Subscriber[PartyState]): Unit =
      statePublisher = statePublisher.subscribeFirst(listener)

    override def addMoveListener(listener: Subscriber[PartyState]): Unit =
      statePublisher = statePublisher.subscribeLast(listener)

    override def start(): Unit =
      statePublisher.publish(
        PartyState.fromGame(
          game,
          turnManager.currentPhase,
          turnManager.currentPlayer
        )
      )

    override def handleMove(move: PartyMove): Unit =
      var directions: Option[Set[Direction]] = Option.empty
      var diceResult: Option[(Player, Int)] = Option.empty
      if move.playerId == turnManager.currentPlayer.id then
        move.moveType match
          case PartyMoveType.Movement =>
            directions = Some(handleMovement(move.direction.getOrElse(Direction.Up)))
          case PartyMoveType.DiceRoll =>
            diceResult = Some(handleStartRoll())
        handleWinCondition()
        statePublisher.publish(
          PartyState.fromGame(
            game,
            turnManager.currentPhase,
            turnManager.currentPlayer,
            diceResult,
            directions,
          )
        )

    private def handleMovement(direction: Direction): Set[Direction] =
      val result = game.movePlayer(turnManager.currentPlayer.id, direction, stepsPerPlayer)
      result match
        case MovementResult.Moved(updatedGame) =>
          game = updatedGame
          turnManager = turnManager.nextTurn()
      game.getPossibleDirections(turnManager.currentPlayer.id)

    private def handleStartRoll(): (Player, Int) =
      val dicePlayer = turnManager.currentPlayer
      val diceResult = Dice().roll()._2.head
      diceResults = diceResults + (dicePlayer -> -diceResult)
      turnManager = turnManager.nextTurn()
      if turnManager.currentPhase != StartingRoll then
        turnManager = turnManager.changePlayerOrder(diceResults)
      (turnManager.currentPlayer, diceResult)

    private def handleWinCondition(): Unit =
      val winner = game.getPockets.find((k, v) => v.countByType(RungType) >= winRungs)
      if winner.isDefined then
        turnManager.end(Player(winner.get._1))
