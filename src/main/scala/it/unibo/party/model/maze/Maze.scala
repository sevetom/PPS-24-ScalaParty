package it.unibo.party.model.maze

import it.unibo.party.model.maze.MazePosition.MazePosition

trait Maze:
  def layout: Map[MazePosition, MazeTile]
  def solution: Option[List[MazePosition]]

  def +(tile: (MazePosition, MazeTile)): Maze

  def get(pos: MazePosition): Option[MazeTile] = layout.get(pos)

  def contains(pos: MazePosition): Boolean = layout.contains(pos)

  def isEmpty: Boolean = layout.isEmpty
  
  def size: Int = layout.size
  
object Maze:
  def apply(path: Map[MazePosition, MazeTile]): Maze = MazeImpl(path)

  def apply(width: Int, height: Int)(architecture: MazeBuilder ?=> MazeBuilder): Maze =
    MazeBuilder.construct(width, height)(architecture).build()

  val empty: Maze = Maze(Map.empty)

  private case class MazeImpl(layout: Map[MazePosition, MazeTile]) extends Maze:
    def +(tile: (MazePosition, MazeTile)): Maze = 
      copy(layout = layout.updated(tile._1, tile._2))

    lazy val solution: Option[List[MazePosition]] = computeSolution()

    private def computeSolution(): Option[List[MazePosition]] = ???