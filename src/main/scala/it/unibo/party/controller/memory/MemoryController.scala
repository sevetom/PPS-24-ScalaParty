package it.unibo.party.controller.memory

import it.unibo.party.common.state.MemoryState
import it.unibo.party.common.{MinigamePhase, Player}
import it.unibo.party.controller.Moves.MemoryMove
import it.unibo.party.controller.{MiniController, Moves, TimedMiniController}
import it.unibo.party.geometry.Point2D
import it.unibo.party.model.memory.{Memory, MemoryBox}

private val winTime = 30000L // 30 seconds

object MemoryController:
  /**
   * Creates a new MemoryController instance.
   *
   * @param game the Memory game to be played
   * @param challenger the player who challenges
   * @param challenged the player who is challenged
   * @return a new instance of MemoryController
   */
  def apply(game: Memory, challenger: Player, challenged: Player): MiniController = MemoryControllerImpl(MemoryState(game, MinigamePhase.Playing, None, None, winTime = winTime), challenger, challenged, 0L, winTime)

  private case class MemoryControllerImpl(state: MemoryState,
                                          challenger: Player,
                                          challenged: Player,
                                          startTime: Long,
                                          winTime: Long) extends TimedMiniController:

    override def start(): MiniController =
      val generatedMemory: Memory = state.game.generate
      copy(
        state = MemoryState(
          game = generatedMemory,
          phase = MinigamePhase.Playing,
          winner = None,
          firstSelection = None,
          winTime = winTime),
        startTime = System.currentTimeMillis()
      )

    override def handleMove(move: Moves.Move): MiniController =
      move match
        case MemoryMove.FlipCard(pos) =>
          val boxIsAlreadyShown = state.game.layout.values
            .flatMap(p => List(p._1, p._2))
            .find(_.pos == pos)
            .exists(_.isShow)

          if boxIsAlreadyShown || state.firstSelection.contains(pos) then
            throw new IllegalArgumentException("This box is already shown or selected.")

          state.firstSelection match
            case None =>
              copy(state = state.copy(
                firstSelection = Some(pos),
                mismatchedPair = None))

            case Some(firstPos) =>
              val coupleToCheck = (MemoryBox(firstPos), MemoryBox(pos))
              val (isMatch, updatedGame) = state.game.check(coupleToCheck)

              if isMatch then
                val hasWonInTime = !isTimeUp && updatedGame.isOver
                val winner = (hasWonInTime, isTimeUp) match
                  case (true, _) => Some(challenger)
                  case (_, true) => Some(challenged)
                  case _ => None
                val phase = if updatedGame.isOver || isTimeUp then MinigamePhase.GameOver else MinigamePhase.Playing
                copy(state = state.copy(
                  game = updatedGame,
                  phase = phase,
                  winner = winner,
                  firstSelection = None))
              else
                copy(state = state.copy(
                  game = updatedGame,
                  firstSelection = None,
                  mismatchedPair = Some((firstPos, pos))
                ))
        case _ =>
          copy(state = state.copy(mismatchedPair = None))