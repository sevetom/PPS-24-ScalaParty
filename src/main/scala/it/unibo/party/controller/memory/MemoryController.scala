package it.unibo.party.controller.memory

import it.unibo.party.common.{MemoryState, MinigamePhase, Player, State}
import it.unibo.party.controller.{MiniController, Moves, TimedMiniController}
import it.unibo.party.model.memory.{Memory, MemoryBox}
import it.unibo.party.controller.Moves.MemoryMove
import it.unibo.party.geometry.Point2D

private val winTime = 30000L // 30 seconds

object MemoryController:
  def apply(game: Memory): MiniController = MemoryControllerImpl(MemoryState(game, MinigamePhase.Playing, None, None), 0L, winTime)

  private case class MemoryControllerImpl(state: MemoryState,
                                          startTime: Long,
                                          winTime: Long) extends TimedMiniController:

    override def start(): MiniController =
      val generatedMemory: Memory = state.game.generate
      copy(
        state = MemoryState(generatedMemory, MinigamePhase.Playing, None, None),
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
              val newState = state.copy(firstSelection = Some(pos), mismatchedPair = None)
              copy(state = newState)

            case Some(firstPos) =>
              val coupleToCheck = (MemoryBox(firstPos), MemoryBox(pos))
              val (isMatch, updatedGame) = state.game.check(coupleToCheck)

              if isMatch then
                val winner = if updatedGame.isOver then Some(Player(0)) else None
                val phase = if updatedGame.isOver then MinigamePhase.GameOver else MinigamePhase.Playing
                val newState = state.copy(game = updatedGame, phase = phase, winner = winner, firstSelection = None)
                copy(state = newState)
              else
                val newState = state.copy(
                  game = updatedGame,
                  firstSelection = None,
                  mismatchedPair = Some((firstPos, pos))
                )
                copy(state = newState)
        case _ =>
          val newState = state.copy(mismatchedPair = None)
          copy(state = newState)