package it.unibo.party.controller

import it.unibo.party.common.{GamePhase, GameState}
import it.unibo.party.controller.Moves.{PartyMove, PartyMoveType}
import it.unibo.party.controller.pubsub.{Publisher, Subscriber}
import it.unibo.party.geometry.Direction
import it.unibo.party.model.partyGame.{MovementResult, PartyGame}

private val stepsPerPlayer: Int = 1

trait PartyController:
  def addMoveListener(listener: Subscriber[GameState]): Unit

  def start(): Unit

  def handleMove(move: PartyMove): Unit

object PartyController:
  def apply(game: PartyGame, players: Seq[Int]) = new PartyControllerImpl(game, players)

  class PartyControllerImpl(private var game: PartyGame, private val players: Seq[Int]) extends PartyController:
    private val statePublisher: Publisher[GameState] = Publisher.emptyPublisher
    private val gamePhase: GamePhase = GamePhase.PlayerMoving
    private var currentPlayerIndex: Int = 0

    override def addMoveListener(listener: Subscriber[GameState]): Unit = statePublisher.subscribe(listener)

    override def start(): Unit =
      val result: MovementResult = game.movePlayer(currentPlayerIndex, Direction.Up, 0)
      var directions: Option[Set[Direction]] = Option.empty
      result match
        case MovementResult.InvalidMove =>
          throw new IllegalStateException("No directions at starting point")
        case MovementResult.Moved(updatedGame, availableDirections) =>
          game = updatedGame
          directions = availableDirections
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
      if move.playerId == players(currentPlayerIndex) then
        move.moveType match
          case PartyMoveType.Movement =>
            val result: MovementResult = game.movePlayer(currentPlayerIndex, move.direction.get, stepsPerPlayer)
            result match
              case MovementResult.InvalidMove =>
                throw new IllegalStateException("Invalid direction")
              case MovementResult.Moved(updatedGame, availableDirections) =>
                game = updatedGame
                directions = availableDirections
            if directions.isEmpty then
              currentPlayerIndex += 1
              currentPlayerIndex = if currentPlayerIndex < players.length then currentPlayerIndex else 0
          case PartyMoveType.DiceRoll => // not yet implemented
        statePublisher.publish(
          GameState.fromGame(
            game,
            gamePhase,
            players(currentPlayerIndex),
            Some(stepsPerPlayer),
            directions
          )
        )