package it.unibo.party.controller.memory

import it.unibo.party.common.{MemoryState, MinigamePhase, Player, State}
import it.unibo.party.controller.{MiniController, Moves, TimedMiniController}
import it.unibo.party.model.memory.{Memory, MemoryBox}
import it.unibo.party.controller.Moves.MemoryMove
import it.unibo.party.geometry.Point2D

private val winTime = 30000L // 30 seconds

object MemoryController:
  def apply(game: Memory): MiniController = MemoryControllerImpl(game, 0L, winTime, MemoryState(MinigamePhase.Playing, None), 0)

  private case class MemoryControllerImpl(game: Memory,
                                          startTime: Long,
                                          winTime: Long,
                                          state: MemoryState,
                                          selected: Int) extends TimedMiniController:

    override def start(): MiniController =
      val generatedMemory: Memory = game.generate
      copy(
        game = generatedMemory,
        startTime = System.currentTimeMillis(),
        state = MemoryState(MinigamePhase.Playing, None),
        selected = 0
      )

    override def handleMove(move: Moves.Move): MiniController =
      var firstSelection = Point2D[Int](0, 0)
      move match
        case MemoryMove.FlipCard(pos) =>
          selected match
            case 0 =>
              firstSelection = pos
              copy(
                selected = 1
              )
            case 1 =>
              val (isMatch, updatedGame) = game.check(MemoryBox(firstSelection), MemoryBox(pos))
              if isMatch then
                if updatedGame.isOver && System.currentTimeMillis() - startTime < winTime then
                  copy(
                    game = updatedGame,
                    state = MemoryState(MinigamePhase.GameOver, Some(Player(0))),
                    selected = 0
                  )
                else
                  copy(
                    game = updatedGame,
                    state = MemoryState(MinigamePhase.Playing, None),
                    selected = 0
                  )
              else
                copy(
                  game = updatedGame,
                  selected = 0
                )
            case _ =>
              throw new IllegalStateException("Invalid selection state")


