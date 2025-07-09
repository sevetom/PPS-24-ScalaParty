package it.unibo.party.common.state

import it.unibo.party.common.{MinigamePhase, MinigameState, Player}
import it.unibo.party.geometry.Point2D
import it.unibo.party.model.maze.MazeTile

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
