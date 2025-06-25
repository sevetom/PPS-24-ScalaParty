package it.unibo.party.controller

import it.unibo.party.common.{GamePhase, GameState}
import it.unibo.party.controller.Moves.PartyMove
import it.unibo.party.controller.pubsub.{Publisher, Subscriber}
import it.unibo.party.geometry.{Direction, Point2D}
import it.unibo.party.model.items.Collectable
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
    private var gamePhase: GamePhase = GamePhase.PlayerMoving
    private var currentPlayerIndex: Int = 0

    override def addMoveListener(listener: Subscriber[GameState]): Unit = statePublisher.subscribe(listener)

    override def start(): Unit = ???
    //      val result: MovementResult = game.movePlayer(currentPlayerIndex, Direction.Up, 0)
    //      val directions = result.availableDirections
    //      statePublisher.publish(
    //          GameState.fromGame(
    //            result.updatedGame,
    //            gamePhase,
    //            players(currentPlayerIndex),
    //            Some(stepsPerPlayer)
    //          )
    //        )

    override def handleMove(move: PartyMove): Unit = ???
//      var directions: Option[Set[Direction]] = Option.empty
//      if move.playerId == players(currentPlayerIndex) then
//        move.moveType match
//          case Moves.PartyMoveType.Movement =>
//            val result: MovementResult = game.movePlayer(currentPlayerIndex, move.direction.get, stepsPerPlayer)
//            directions = result.availableDirections
//            if directions.isEmpty then
//              currentPlayerIndex = (currentPlayerIndex + 1) % players.size
//        statePublisher.publish(
//            GameState.fromGame(
//                result.updatedGame,
//                gamePhase,
//                players(currentPlayerIndex),
//                Some(stepsPerPlayer),
//                directions
//              )
//          )