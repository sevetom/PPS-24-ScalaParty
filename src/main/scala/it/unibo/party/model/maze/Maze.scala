package it.unibo.party.model.maze

import it.unibo.party.model.maze.MazePosition.MazePosition

trait Maze:
  def path: Map[MazePosition, MazeTile]

  def +(tile: (MazePosition, MazeTile)): Maze

  def get(pos: MazePosition): Option[MazeTile] = path.get(pos)

  def contains(pos: MazePosition): Boolean = path.contains(pos)

  def isEmpty: Boolean = path.isEmpty
  
  def size: Int = path.size
  
object Maze:
  def apply(path: Map[MazePosition, MazeTile]): Maze = MazeImpl(path)

  def apply(width: Int, height: Int)(architecture: MazeBuilder ?=> MazeBuilder): Maze =
    MazeBuilder.construct(width, height)(architecture).build()

  val empty: Maze = Maze(Map.empty)

  private case class MazeImpl(path: Map[MazePosition, MazeTile]) extends Maze:
    def +(tile: (MazePosition, MazeTile)): Maze = 
      copy(path = path.updated(tile._1, tile._2))
