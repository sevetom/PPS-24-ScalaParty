package it.unibo.party.controller.maze

import it.unibo.party.common.{MinigamePhase, Player}
import it.unibo.party.common.state.MazeState
import it.unibo.party.controller.Moves.{MazeMove, Move}
import it.unibo.party.controller.{MiniController, TimedMiniController}
import it.unibo.party.model.maze.MazeGame

private val startingWinTime = 15000L // 15 seconds

object MazeController:

  def apply(game: MazeGame, challenger: Player, challenged: Player): MiniController =
    MazeControllerImpl(game, challenger, challenged, 0L, startingWinTime, MazeState.empty)

  private case class MazeControllerImpl(
                                         game: MazeGame,
                                         challenger: Player,
                                         challenged: Player,
                                         startTime: Long,
                                         winTime: Long,
                                         state: MazeState) extends TimedMiniController:

    override def start(): MiniController =
      val generatedGame = game.generateMaze()
      copy(
        game = generatedGame.generateMaze(),
        startTime = System.currentTimeMillis(),
        state = MazeState(
          phase = MinigamePhase.Playing,
          timeRequired = winTime,
          maze = generatedGame.maze.layout.map((point, tile) => (point.toPoint2D, tile)),
          playerPosition = generatedGame.player.toPoint2D,
          winner = Option.empty,
          solution = generatedGame.maze.solution.getOrElse(List()).map(_.toPoint2D)
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
              winner = if movedGame.reachedEnd() then Some(challenger) else if isTimeUp then Some(challenged) else state.winner
            )
          )
        case _ => copy()  
