package it.unibo.party.controller

import it.unibo.party.common.{GamePhase, GameState}
import it.unibo.party.controller.Moves.{PartyMove, PartyMoveType}
import it.unibo.party.controller.pubsub.{Publisher, Subscriber}
import it.unibo.party.geometry.Direction
import it.unibo.party.model.partyGame.{MovementResult, PartyGame}
import it.unibo.party.model.items.CollectableOperations.*
import it.unibo.party.model.items.CollectableType.RungType

private val stepsPerPlayer: Int = 1
private val winRungs: Int = 1

trait PartyController:
  def addMoveListener(listener: Subscriber[GameState]): Unit

  def start(): Unit

  def handleMove(move: PartyMove): Unit

object PartyController:
  def apply(game: PartyGame, players: Seq[Int]) = new PartyControllerImpl(game, players)

  class PartyControllerImpl(private var game: PartyGame, private val players: Seq[Int]) extends PartyController:
    private var statePublisher: Publisher[GameState] = Publisher.emptyPublisher
    private var gamePhase: GamePhase = GamePhase.PlayerMoving
    private var currentPlayerIndex: Int = 0

    override def addMoveListener(listener: Subscriber[GameState]): Unit = statePublisher = statePublisher.subscribe(listener)

    override def start(): Unit =
      val result: MovementResult = game.movePlayer(currentPlayerIndex, Direction.Up, 0)
      var directions: Option[Set[Direction]] = Option.empty
      result match
        case MovementResult.Moved(updatedGame, availableDirections) =>
          game = updatedGame
          directions = availableDirections
        case _ =>
      statePublisher.publish(
        GameState.fromGame(
          game,
          gamePhase,
          players(currentPlayerIndex),
          Some(stepsPerPlayer),
          directions
        )
      )

    override def handleMove(move: PartyMove): Unit =
      var directions: Option[Set[Direction]] = Option.empty
      var turnPlayer = players(currentPlayerIndex)
      if move.playerId == turnPlayer then
        move.moveType match
          case PartyMoveType.Movement =>
            val result: MovementResult = game.movePlayer(currentPlayerIndex, move.direction.get, stepsPerPlayer)
            result match
              case MovementResult.Moved(updatedGame, availableDirections) =>
                game = updatedGame
                directions = availableDirections
                currentPlayerIndex += 1
                currentPlayerIndex = if currentPlayerIndex < players.length then currentPlayerIndex else 0
                turnPlayer = players(currentPlayerIndex)
              case _ =>
          case PartyMoveType.DiceRoll => // not yet implemented
        val winner = game.getPocketsContents.find((k, v) => v.count(_.getType == RungType) >= winRungs)
        if winner.isDefined then
          gamePhase = GamePhase.GameOver
          turnPlayer = winner.get._1
        statePublisher.publish(
          GameState.fromGame(
            game,
            gamePhase,
            turnPlayer,
            Some(stepsPerPlayer),
            directions
          )
        )