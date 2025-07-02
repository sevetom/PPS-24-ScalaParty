package it.unibo.party.controller

import it.unibo.party.common.GamePhase.StartingRoll
import it.unibo.party.common.{GamePhase, GameState}
import it.unibo.party.controller.Moves.{PartyMove, PartyMoveType}
import it.unibo.party.controller.pubsub.{Publisher, Subscriber}
import it.unibo.party.geometry.Direction
import it.unibo.party.model.partyGame.{Dice, MovementResult, PartyGame}
import it.unibo.party.model.items.CollectableType.RungType

private val stepsPerPlayer: Int = 1
private val winRungs: Int = 1

trait PartyController:
  def addViewListener(listener: Subscriber[GameState]): Unit

  def addMoveListener(listener: Subscriber[GameState]): Unit

  def start(): Unit

  def handleMove(move: PartyMove): Unit

object PartyController:
  def apply(game: PartyGame, players: List[Int]) = new PartyControllerImpl(game, players)

  class PartyControllerImpl(private var game: PartyGame, private var players: List[Int]) extends PartyController:
    private var statePublisher: Publisher[GameState] = Publisher(List.empty)
    private var gamePhase: GamePhase = GamePhase.StartingRoll
    private var currentPlayerIndex: Int = 0
    private var turnPlayer = players(currentPlayerIndex)
    private var diceResults: Map[Int, Int] = Map.empty

    override def addViewListener(listener: Subscriber[GameState]): Unit =
      statePublisher = statePublisher.subscribeFirst(listener)

    override def addMoveListener(listener: Subscriber[GameState]): Unit =
      statePublisher = statePublisher.subscribeLast(listener)

    override def start(): Unit =
      statePublisher.publish(
        GameState.fromGame(
          game,
          gamePhase,
          turnPlayer
        )
      )

    override def handleMove(move: PartyMove): Unit =
      var directions: Option[Set[Direction]] = Option.empty
      var diceResult: Option[(Int, Int)] = Option.empty
      if move.playerId == turnPlayer then
        move.moveType match
          case PartyMoveType.Movement =>
            directions = Some(handleMovement(move.direction.getOrElse(Direction.Up)))
          case PartyMoveType.DiceRoll =>
            diceResult = Some(handleStartRoll())
        handleWinCondition()
        statePublisher.publish(
          GameState.fromGame(
            game,
            gamePhase,
            turnPlayer,
            diceResult,
            directions,
          )
        )

    private def handleMovement(direction: Direction): Set[Direction] =
      val result = game.movePlayer(currentPlayerIndex, direction, stepsPerPlayer)
      result match
        case MovementResult.Moved(updatedGame) =>
          game = updatedGame
          currentPlayerIndex += 1
          currentPlayerIndex = if currentPlayerIndex < players.length then currentPlayerIndex else 0
          turnPlayer = players(currentPlayerIndex)
      game.getPossibleDirections(turnPlayer)

    private def handleStartRoll(): (Int, Int) =
      val dicePlayer = turnPlayer
      val diceResult = Dice().roll()._2.head
      diceResults = diceResults + (turnPlayer -> diceResult)
      currentPlayerIndex += 1
      if currentPlayerIndex >= players.size then
        gamePhase = GamePhase.DiceRoll
        currentPlayerIndex = 0
        players = diceResults.toList.sortWith(_._2 > _._2).map(_._1)
      turnPlayer = players(currentPlayerIndex)
      (turnPlayer, diceResult)

    private def handleWinCondition(): Unit =
      val winner = game.getPockets.find((k, v) => v.countByType(RungType) >= winRungs)
      if winner.isDefined then
        gamePhase = GamePhase.GameOver
        turnPlayer = winner.get._1
