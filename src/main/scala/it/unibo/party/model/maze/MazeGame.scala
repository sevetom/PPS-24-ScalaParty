package it.unibo.party.model.maze

import it.unibo.party.model.maze.MazePosition.MazePosition

private def width: Int = 10
private def height: Int = 10

trait MazeGame:
  def player: MazePosition
  def maze: Map[MazePosition, MazeTile]

  def generateMaze(): MazeGame
  def movePlayer(direction: String): MazeGame
  def reachedEnd(): Boolean

object MazeGame:
  def apply(player: MazePosition, maze: Map[MazePosition, MazeTile]): MazeGame = 
    MazeGameImpl(player, maze)
    
  private case class MazeGameImpl(player: MazePosition, maze: Map[MazePosition, MazeTile]) extends MazeGame:
    
    def generateMaze(): MazeGame = ???

    def movePlayer(direction: String): MazeGame = ???

    def reachedEnd(): Boolean = ???
      
  val emptyMaze: MazeGame = MazeGame(MazePosition(0, 0), Map.empty)
