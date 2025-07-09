package it.unibo.party.controller.maze

import it.unibo.party.common.{MinigamePhase, Player}
import it.unibo.party.common.state.MazeState
import it.unibo.party.controller.Moves.{MazeMove, Move}
import it.unibo.party.controller.{MiniController, TimedMiniController}
import it.unibo.party.model.maze.MazeGame

private val startingWinTime = 60000L // 60 seconds

object MazeController:

  def apply(game: MazeGame, challenger: Player): MiniController =
    MazeControllerImpl(game, challenger, 0L, startingWinTime, MazeState.empty)

  private case class MazeControllerImpl(
                                         game: MazeGame,
                                         player: Player,
                                         startTime: Long,
                                         winTime: Long,
                                         state: MazeState) extends TimedMiniController:

    override def start(): MiniController =
      copy(
        game = game.generateMaze(),
        startTime = System.currentTimeMillis(),
        state = MazeState(
          phase = MinigamePhase.Playing,
          timeRequired = winTime,
          maze = game.maze.layout.map((point, tile) => (point.toPoint2D, tile)),
          playerPosition = game.player.toPoint2D,
          winner = Option.empty
        )
      )

    override def handleMove(move: Move): MiniController =
      move match
        case MazeMove.Movement(direction) =>
          val movedGame = game.movePlayer(direction)
          copy(
            game = movedGame,
            state = state.copy(
              phase = if movedGame.reachedEnd() || isTimeUp then MinigamePhase.GameOver else state.phase,
              maze = movedGame.maze.layout.map((point, tile) => (point.toPoint2D, tile)),
              playerPosition = movedGame.player.toPoint2D,
              winner = if movedGame.reachedEnd() then Some(player) else if isTimeUp then Option.empty else state.winner
            )
          )
        case _ => copy()  
