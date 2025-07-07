package it.unibo.party.controller

import it.unibo.party.common.PartyPhase
import it.unibo.party.common.{PartyState, Player}
import it.unibo.party.controller.Moves.{Move, PartyMove}
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

  override def handleMove(move: Move): MiniController

  def doubleRollNextTurn(player: Player): PartyController

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

    override def doubleRollNextTurn(player: Player): PartyController =
      copy(doubleRoller = Some(player))

    override def handleMove(move: Move): MiniController =
      val ctx = copy(
        state = state.copy(
          diceResult = Some((turnManager.currentPlayer, remainingSteps)),
          possibleDirections = Option.empty
        )
      )
      val updatedCtx = move match
        case Movement(playerId, direction) if playerId == turnManager.currentPlayer.id =>
          handleMovement(ctx, direction)
        case DiceRoll(playerId) if playerId == turnManager.currentPlayer.id =>
          ctx.turnManager.currentPhase match
            case PartyPhase.StartingRoll => handleStartingRoll(ctx)
            case PartyPhase.DiceRoll => handleDiceRoll(ctx)
            case _ => ctx
        case Resume => ctx.copy(turnManager = ctx.turnManager.nextTurn())
        case _ => ctx
      val postWinCtx = checkWinCondition(updatedCtx)
      val regeneratedCtx = regenerateBoard(postWinCtx)
      copy(
        state = PartyState.fromGame(
          regeneratedCtx.game,
          regeneratedCtx.turnManager.currentPhase,
          regeneratedCtx.turnManager.currentPlayer,
          regeneratedCtx.state.diceResult,
          regeneratedCtx.state.possibleDirections
        ),
        game = regeneratedCtx.game,
        turnManager = regeneratedCtx.turnManager,
        startingChallenge = regeneratedCtx.startingChallenge,
        remainingSteps = regeneratedCtx.remainingSteps
      )

    private def handleMovement(ctx: PartyControllerImpl, direction: Direction): PartyControllerImpl =
      val result = ctx.game.movePlayer(turnManager.currentPlayer.id, direction, stepsPerPlayer)
      result match
        case MovementResult.Moved(updatedGame) if ctx.remainingSteps > 0 =>
          val remaining = ctx.remainingSteps - stepsPerPlayer
          val nextTurnManager =
            if remaining <= 0 then ctx.turnManager.nextTurn() else ctx.turnManager
          ctx.copy(
            state = ctx.state.copy(
              possibleDirections = Some(updatedGame.getPossibleDirections(turnManager.currentPlayer.id))
            ),
            game = updatedGame,
            remainingSteps = remaining,
            turnManager = nextTurnManager
          )
        case _ => ctx

    private def handleStartingRoll(ctx: PartyControllerImpl): PartyControllerImpl =
      val dicePlayer = ctx.turnManager.currentPlayer
      val newChallenge = ctx.startingChallenge.newRoll(dicePlayer)
      val diceRes = newChallenge.diceResults(dicePlayer)
      val nextTurnManager = ctx.turnManager.nextTurn()
      val orderedTurnManager =
        if nextTurnManager.currentPhase != PartyPhase.StartingRoll then
          nextTurnManager.changePlayerOrder(newChallenge.diceResults.map((p, r) => (p, -r)))
        else nextTurnManager
      ctx.copy(
        state = ctx.state.copy(
          diceResult = Some((dicePlayer, diceRes))
        ),
        startingChallenge = newChallenge,
        turnManager = orderedTurnManager,
      )

    private def handleDiceRoll(ctx: PartyControllerImpl): PartyControllerImpl =
      val (dices, checkedDoubleRoller) =
        if ctx.doubleRoller.contains(ctx.turnManager.currentPlayer) then (2, Option.empty)
        else (1, ctx.doubleRoller)
      val (rolledGame, rolls) = ctx.game.roll(dices)
      ctx.copy(
        state = ctx.state.copy(
          possibleDirections = Some(rolledGame.getPossibleDirections(turnManager.currentPlayer.id)),
          diceResult = Some((ctx.turnManager.currentPlayer, rolls.sum))
        ),
        game = rolledGame,
        remainingSteps = rolls.sum,
        turnManager = ctx.turnManager.nextTurn(),
        doubleRoller = checkedDoubleRoller
      )

    private def checkWinCondition(ctx: PartyControllerImpl): PartyControllerImpl =
      ctx.game.getPockets.find((_, v) => v.countByType(RungType) >= winRungs) match
        case Some((playerId, _)) =>
          ctx.copy(turnManager = ctx.turnManager.end(Player(playerId)))
        case _ => ctx

    private def regenerateBoard(ctx: PartyControllerImpl): PartyControllerImpl =
      val items = ctx.game.getItems.values.map(_.getType)
      if !items.exists(_ == RungType) then
        ctx.copy(game = ctx.game.regenerateBoard)
      else if !items.exists(_ == MonadType) then
        ctx.copy(game = ctx.game.regenerateMonads)
      else ctx

