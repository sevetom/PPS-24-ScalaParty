package it.unibo.party.common.state

import it.unibo.party.common.{MinigamePhase, MinigameState, Player}
import it.unibo.party.geometry.Point2D
import it.unibo.party.model.maze.MazeTile

/**
 * Represents the state of a maze minigame.
 * 
 * @param phase the current phase of the minigame
 * @param timeRequired the time required to complete the maze
 * @param maze the maze represented as a map of points to maze tiles
 * @param playerPosition the current position of the player in the maze
 * @param winner the player who has won the maze, if any
 * @param solution the solution path through the maze, represented as a list of points
 */
case class MazeState(
                      phase: MinigamePhase,
                      timeRequired: Long,
                      maze: Map[Point2D[Int], MazeTile],
                      playerPosition: Point2D[Int],
                      winner: Option[Player],
                      solution: List[Point2D[Int]]
                    ) extends MinigameState

object MazeState:
  val empty: MazeState = 
    MazeState(
      phase = MinigamePhase.Playing,
      timeRequired = 0L,
      maze = Map.empty,
      playerPosition = Point2D(0, 0),
      winner = None,
      solution = List.empty
    )
