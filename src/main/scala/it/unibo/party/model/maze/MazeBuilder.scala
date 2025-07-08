package it.unibo.party.model.maze

import it.unibo.party.model.maze.MazePosition.MazePosition

class MazeBuilder(val width: Int, val height: Int):
  private var maze: Maze = Maze.empty
  private var currentIndex: Int = 0

  private def indexToPosition(idx: Int): MazePosition =
    val x = idx % width
    val y = idx / width
    MazePosition(x, y)

  def +(tile: MazeTile): MazeBuilder =
    require(currentIndex < width * height)
    val pos = indexToPosition(currentIndex)
    maze = maze + (pos, tile)
    currentIndex += 1
    this

  def build(): Maze =
    require(currentIndex == width * height)
    maze

object MazeBuilder:
  export MazeBuilder.DSL.*

  def configure(configuration: MazeBuilder ?=> MazeBuilder): MazeBuilder =
    given MazeBuilder(10, 10)
    configuration

  object DSL:
    def W(using b: MazeBuilder): MazeBuilder = b + MazeTile.Wall
    def *(using b: MazeBuilder): MazeBuilder = b + MazeTile.Passage
    def E(using b: MazeBuilder): MazeBuilder = b + MazeTile.Entry
    def X(using b: MazeBuilder): MazeBuilder = b + MazeTile.Exit

    extension (builder: MazeBuilder)
      def |(b: MazeBuilder): MazeBuilder = b


