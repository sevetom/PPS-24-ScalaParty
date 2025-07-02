package it.unibo.party.controller

import it.unibo.party.common.{GamePhase, GameState}
import it.unibo.party.controller.Moves.{PartyMove, PartyMoveType}
import it.unibo.party.controller.pubsub.{Publisher, Subscriber}
import it.unibo.party.geometry.Direction
import it.unibo.party.model.items.CollectableType.RungType
import it.unibo.party.model.partyGame.{MovementResult, PartyGame}

private val stepsPerPlayer: Int = 1
private val winRungs: Int = 1

trait PartyController:
  def addViewListener(listener: Subscriber[GameState]): Unit

  def addMoveListener(listener: Subscriber[GameState]): Unit

  def start(): Unit

  def handleMove(move: PartyMove): Unit

object PartyController:
  def apply(game: PartyGame, players: Seq[Int]) = new PartyControllerImpl(game, players)

  class PartyControllerImpl(private var game: PartyGame, private val players: Seq[Int]) extends PartyController:
    private var statePublisher: Publisher[GameState] = Publisher(List.empty)
    private var gamePhase: GamePhase = GamePhase.PlayerMoving
    private var currentPlayerIndex: Int = 0

    override def addViewListener(listener: Subscriber[GameState]): Unit =
      statePublisher = statePublisher.subscribeFirst(listener)

    override def addMoveListener(listener: Subscriber[GameState]): Unit =
      statePublisher = statePublisher.subscribeLast(listener)

    override def start(): Unit =
      val directions: Set[Direction] = game.getPossibleDirections(players(currentPlayerIndex))
      statePublisher.publish(
        GameState.fromGame(
          game,
          gamePhase,
          players(currentPlayerIndex),
          Some(stepsPerPlayer),
          Some(directions)
        )
      )

    override def handleMove(move: PartyMove): Unit =
      var directions: Option[Set[Direction]] = Option.empty
      var turnPlayer = players(currentPlayerIndex)
      if move.playerId == turnPlayer then
        move.moveType match
          case PartyMoveType.Movement =>
            val result = game.movePlayer(currentPlayerIndex, move.direction.get, game.dice.lastRolled.sum)
            result match
              case MovementResult.Moved(updatedGame) =>
                game = updatedGame
                currentPlayerIndex += 1
                currentPlayerIndex = if currentPlayerIndex < players.length then currentPlayerIndex else 0
                turnPlayer = players(currentPlayerIndex)
                directions = Some(game.getPossibleDirections(turnPlayer))
              case _ =>
          case PartyMoveType.DiceRoll =>
            val (newDice, result) = game.dice.roll()
            game = PartyGame(game.board)(using newDice)
            directions = Some(game.getPossibleDirections(turnPlayer))
        val winner = game.getPockets.find((k, v) => v.countByType(RungType) >= winRungs)
        if winner.isDefined then
          gamePhase = GamePhase.GameOver
          turnPlayer = winner.get._1
        statePublisher.publish(
          GameState.fromGame(
            game,
            gamePhase,
            turnPlayer,
            Some(game.dice.lastRolled.sum),
            directions
          )
        )