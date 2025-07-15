package it.unibo.party.model.maze

import it.unibo.party.geometry.Direction
import it.unibo.party.model.maze.MazePosition.MazePosition

private def mazeWidth: Int = 17
private def mazeHeight: Int = 17

/**
 * Represents a game of maze where a player can navigate through the maze to reach the exit.
 */
trait MazeGame:
  /**
   * @return the current position of the player in the maze.
   */
  def player: MazePosition

  /**
   * @return the maze in which the player is navigating.
   */
  def maze: Maze

  /**
   * Generates a new maze and resets the player's position to the entry point.
   *
   * @return a new MazeGame instance with the newly generated maze.
   */
  def generateMaze(): MazeGame

  /**
   * Moves the player in the specified direction if the move is valid (i.e., not hitting a wall).
   *
   * @param direction the direction in which the player wants to move.
   * @return a new MazeGame instance with the updated player position.
   */
  def movePlayer(direction: Direction): MazeGame

  /**
   * Checks if the player has reached the exit of the maze.
   *
   * @return true if the player is at the exit, false otherwise.
   */
  def reachedEnd(): Boolean

object MazeGame:
  def apply(player: MazePosition, maze: Maze): MazeGame =
    MazeGameImpl(player, maze)

  private case class MazeGameImpl(player: MazePosition, maze: Maze) extends MazeGame:
    private def generator = MazeGenerator.NorthEastBinaryTree(mazeWidth, mazeHeight)

    def generateMaze(): MazeGame =
      val newMaze = generator.generate
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
