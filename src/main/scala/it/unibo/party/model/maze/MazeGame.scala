package it.unibo.party.model.maze

import it.unibo.party.model.maze.MazePosition.MazePosition

private def width: Int = 10
private def height: Int = 10

trait MazeGame:
  def player: MazePosition
  def maze: Maze

  def generateMaze(): MazeGame
  def movePlayer(direction: String): MazeGame
  def reachedEnd(): Boolean

object MazeGame:
  def apply(player: MazePosition, maze: Maze): MazeGame = 
    MazeGameImpl(player, maze)
    
  private case class MazeGameImpl(player: MazePosition, maze: Maze) extends MazeGame:
    
    def generateMaze(): MazeGame = ???

    def movePlayer(direction: String): MazeGame = ???

    def reachedEnd(): Boolean = ???
      
  val emptyMaze: MazeGame = MazeGame(MazePosition(0, 0), Maze.empty)
