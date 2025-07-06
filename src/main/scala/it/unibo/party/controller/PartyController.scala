package it.unibo.party.controller

import it.unibo.party.common.PartyPhase
import it.unibo.party.common.{PartyState, Player}
import it.unibo.party.controller.Moves.PartyMove
import it.unibo.party.controller.Moves.PartyMove.*
import it.unibo.party.controller.managers.{DiceChallengeManager, PartyTurnManager}
import it.unibo.party.controller.pubsub.{Publisher, Subscriber}
import it.unibo.party.geometry.Direction
import it.unibo.party.model.items.CollectableType.{MonadType, RungType}
import it.unibo.party.model.partyGame.{MovementResult, PartyGame}
import it.unibo.party.model.items.CollectableOperations.getType

private val stepsPerPlayer: Int = 1
private val winRungs: Int = 5

trait PartyController extends MiniController:
  override def state: PartyState

  override def handleMove(move: PartyMove): PartyController

object PartyController:
  def apply(game: PartyGame, players: List[Player]): PartyController =
    PartyControllerImpl(
      PartyState.emptyPartyState,
      game,
      players,
      PartyTurnManager.fromTheStart(players),
      DiceChallengeManager(),
      Option.empty,
      0
    )

  private case class PartyControllerImpl(
                                          state: PartyState,
                                          game: PartyGame,
                                          players: List[Player],
                                          turnManager: PartyTurnManager,
                                          startingChallenge: DiceChallengeManager,
                                          doubleRoller: Option[Player],
                                          remainingSteps: Int
                                        ) extends PartyController:

    override def start(): MiniController =
      val startingState = PartyState.fromGame(
        game,
        turnManager.currentPhase,
        turnManager.currentPlayer
      )
      copy(state = startingState)

    override def handleMove(move: PartyMove): PartyController =
      var directions: Option[Set[Direction]] = Option.empty
      var diceResult: Option[(Player, Int)] = Some((turnManager.currentPlayer, remainingSteps - 1))
      var updatedGame = game
      var updatedRemainingSteps = remainingSteps
      var updatedTurnManager = turnManager
      var updatedStartingChallenge = startingChallenge
      move match
        case Movement(playerId, direction) if playerId == turnManager.currentPlayer.id =>
          // Movement logic
          val result = game.movePlayer(turnManager.currentPlayer.id, direction, stepsPerPlayer)
          result match
            case MovementResult.Moved(game) =>
              updatedGame = game
              updatedRemainingSteps -= stepsPerPlayer
              if updatedRemainingSteps <= 0 then
                updatedTurnManager = turnManager.nextTurn()
          directions = Some(updatedGame.getPossibleDirections(turnManager.currentPlayer.id))
        case DiceRoll(playerId) if playerId == turnManager.currentPlayer.id =>
          turnManager.currentPhase match
            case PartyPhase.StartingRoll =>
              // Starting roll logic
              val dicePlayer = turnManager.currentPlayer
              updatedStartingChallenge = startingChallenge.newRoll(dicePlayer)
              val result = updatedStartingChallenge.diceResults(dicePlayer)
              updatedTurnManager = turnManager.nextTurn()
              if updatedTurnManager.currentPhase != PartyPhase.StartingRoll then
                updatedTurnManager = updatedTurnManager.changePlayerOrder(
                  startingChallenge.diceResults.map((p, r) => (p, -r)))
              diceResult = Some((dicePlayer, result))
            case PartyPhase.DiceRoll =>
              // Dice rolling logic
              val result = game.roll(1)
              updatedGame = result._1
              updatedRemainingSteps = result._2.sum
              diceResult = Some((turnManager.currentPlayer, remainingSteps))
              updatedTurnManager = turnManager.nextTurn()
              directions = Some(game.getPossibleDirections(turnManager.currentPlayer.id))
            case _ =>
        case Resume =>
          updatedTurnManager = turnManager.nextTurn()
        case _ =>
      
      // Check win condition    
      val winner = updatedGame.getPockets.find((k, v) => v.countByType(RungType) >= winRungs)
      if winner.isDefined then
        updatedTurnManager = updatedTurnManager.end(Player(winner.get._1))
      
      // Board regeneration
      if !updatedGame.getItems.values.exists(_.getType == RungType) then
        updatedGame = updatedGame.regenerateBoard
      else if !updatedGame.getItems.values.exists(_.getType == MonadType) then
        updatedGame = updatedGame.regenerateMonads
      
      copy(
        state = PartyState.fromGame(
          game,
          turnManager.currentPhase,
          turnManager.currentPlayer,
          diceResult,
          directions
        ),
        game = updatedGame,
        turnManager = updatedTurnManager,
        startingChallenge = updatedStartingChallenge,
        remainingSteps = updatedRemainingSteps
      )

