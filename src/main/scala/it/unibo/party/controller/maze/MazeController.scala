package it.unibo.party.controller.maze

import it.unibo.party.common.{MinigamePhase, Player}
import it.unibo.party.common.state.MazeState
import it.unibo.party.controller.Moves.{MazeMove, Move}
import it.unibo.party.controller.{MiniController, TimedMiniController}
import it.unibo.party.model.maze.MazeGame

private val winTime = 15000L // 15 seconds

/**
 * Controller for the Maze minigame.
 */
object MazeController:

  /**
   * Creates a new instance of the MazeController.
   *
   * @param game The MazeGame instance to be used.
   * @param challenger The player who is challenging.
   * @param challenged The player who is being challenged.
   * @return A new instance of MiniController for the Maze minigame.
   */
  def apply(game: MazeGame, challenger: Player, challenged: Player): MiniController =
    MazeControllerImpl(game, challenger, challenged, 0L, winTime, MazeState.empty)

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
        game = generatedGame,
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
