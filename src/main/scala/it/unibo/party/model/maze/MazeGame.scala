package it.unibo.party.model.maze

import it.unibo.party.geometry.Direction
import it.unibo.party.model.maze.MazePosition.MazePosition

private def width: Int = 10
private def height: Int = 10

trait MazeGame:
  def player: MazePosition

  def maze: Maze

  def generateMaze(): MazeGame

  def movePlayer(direction: Direction): MazeGame

  def reachedEnd(): Boolean

object MazeGame:
  def apply(player: MazePosition, maze: Maze): MazeGame =
    MazeGameImpl(player, maze)

  private case class MazeGameImpl(player: MazePosition, maze: Maze) extends MazeGame:

    def generateMaze(): MazeGame = 
      val newMaze = Maze.standard
      val newPlayerPos = newMaze.layout.find(_._2 == MazeTile.Entry).get
      MazeGame(newPlayerPos._1, newMaze)

    def movePlayer(direction: Direction): MazeGame =
      val newPosition = player + direction
      copy(
        player = if maze.contains(newPosition) && maze.get(newPosition).exists(_ != MazeTile.Wall)
        then newPosition else player
      )

    def reachedEnd(): Boolean =
      maze.get(player) match
        case Some(MazeTile.Exit) => true
        case _ => false

  val empty: MazeGame = MazeGame(MazePosition(0, 0), Maze.empty)
