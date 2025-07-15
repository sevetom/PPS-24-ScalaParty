package it.unibo.party.controller

import it.unibo.party.common.{PartyPhase, PartyState, Player}
import it.unibo.party.controller.Moves.PartyMove.*
import it.unibo.party.controller.Moves.{Move, PartyMove}
import it.unibo.party.controller.managers.{DiceChallengeManager, PartyTurnManager}
import it.unibo.party.geometry.Direction
import it.unibo.party.model.items.CollectableOperations.getType
import it.unibo.party.model.items.CollectableType.{MonadType, RungType}
import it.unibo.party.model.partyGame.{MovementResult, PartyGame}

private val stepsPerPlayer: Int = 1
private val winRungs: Int = 5

/**
 * Controller for managing the game state and player interactions in the Party game.
 */
trait PartyController extends MiniController:
  override def state: PartyState

  override def handleMove(move: Move): MiniController

  /**
   * Sets the next player to roll two dice in the next turn.
   * Valid only for the next turn of the specified player.
   *
   * @param player the player who will roll two dice next
   * @return a new PartyController instance with the updated doubleRoller
   */
  def doubleRollNextTurn(player: Player): PartyController

object PartyController:
  /**
   * Creates a new instance of PartyController with the initial game state.
   *
   * @param game the PartyGame instance representing the game
   * @param players the list of players participating in the game
   * @return
   */
  def apply(game: PartyGame, players: List[Player]): PartyController =
    PartyControllerImpl(
      PartyState.empty,
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
                                          remainingStepsAfterRoll: Int
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
          diceResult = Some((turnManager.currentPlayer, remainingStepsAfterRoll)),
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
        case Resume | StartMinigame => ctx.copy(turnManager = ctx.turnManager.nextTurn())
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
        remainingStepsAfterRoll = regeneratedCtx.remainingStepsAfterRoll
      )

    /**
     * Handles the movement of the current player in the game.
     *
     * @param ctx       the current party controller context
     * @param direction the direction in which the player wants to move
     * @return a new PartyControllerImpl instance with the updated game state
     */
    private def handleMovement(ctx: PartyControllerImpl, direction: Direction): PartyControllerImpl =
      val result = ctx.game.movePlayer(turnManager.currentPlayer.id, direction, stepsPerPlayer)
      result match
        case MovementResult.Moved(updatedGame) if ctx.remainingStepsAfterRoll > 0 =>
          val remaining = ctx.remainingStepsAfterRoll - stepsPerPlayer
          val nextTurnManager =
            if remaining <= 0 then ctx.turnManager.nextTurn() else ctx.turnManager
          ctx.copy(
            state = ctx.state.copy(
              possibleDirections = Some(updatedGame.getPossibleDirections(turnManager.currentPlayer.id)),
              diceResult = Some((turnManager.currentPlayer, remaining))
            ),
            game = updatedGame,
            remainingStepsAfterRoll = remaining,
            turnManager = nextTurnManager
          )
        case _ => ctx

    /**
     * Handles the starting challenge roll for the current player.
     *
     * @param ctx the current party controller context
     * @return a new PartyControllerImpl instance with the updated game state
     */
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

    /**
     * Handles the dice roll for the current player.
     *
     * @param ctx the current party controller context
     * @return a new PartyControllerImpl instance with the updated game state
     */
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
        remainingStepsAfterRoll = rolls.sum,
        turnManager = ctx.turnManager.nextTurn(),
        doubleRoller = checkedDoubleRoller
      )

    /**
     * Checks if any player has won the game by collecting enough rungs.
     *
     * @param ctx the current party controller context
     * @return a new PartyControllerImpl instance with the game ended if a player has won
     */
    private def checkWinCondition(ctx: PartyControllerImpl): PartyControllerImpl =
      ctx.game.getPockets.find((_, v) => v.countByType(RungType) >= winRungs) match
        case Some((playerId, _)) =>
          ctx.copy(turnManager = ctx.turnManager.end(Player(playerId)))
        case _ => ctx

    /**
     * Regenerates the game board if empty of monads or rungs.
     *
     * @param ctx the current party controller context
     * @return a new PartyControllerImpl instance with the regenerated board if needed
     */
    private def regenerateBoard(ctx: PartyControllerImpl): PartyControllerImpl =
      val items = ctx.game.getItems.values.map(_.getType)
      if !items.exists(_ == RungType) then
        ctx.copy(game = ctx.game.regenerateBoard)
      else if !items.exists(_ == MonadType) then
        ctx.copy(game = ctx.game.regenerateMonads)
      else ctx

