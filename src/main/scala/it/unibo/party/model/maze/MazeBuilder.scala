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
    val pos = indexToPosition(currentIndex)
    maze = maze + (pos, tile)
    currentIndex += 1
    this

  def build(): Maze =
    require(currentIndex == width * height, "Maze must have the correct number of tiles")
    require(maze.layout.values.count(_ == MazeTile.Entry) == 1, "Maze must have exactly one entry")
    require(maze.layout.values.count(_ == MazeTile.Exit) == 1, "Maze must have exactly one exit")
    require(maze.layout.find(_._2 == MazeTile.Entry).exists(
      (p, _) => p.x == 0 || p.x == width - 1 || p.y == 0 || p.y == height - 1
    ), "Entry must be on the maze borders")
    require(maze.layout.find(_._2 == MazeTile.Exit).exists(
      (p, _) => p.x == 0 || p.x == width - 1 || p.y == 0 || p.y == height - 1
    ), "Exit must be on the maze borders")
    require(maze.solution.isDefined, "Maze must be solvable")
    maze

object MazeBuilder:
  export MazeBuilder.DSL.*

  def construct(width: Int, height: Int)(structure: MazeBuilder ?=> MazeBuilder): MazeBuilder =
    given MazeBuilder(width, height)
    structure

  object DSL:
    def W(using b: MazeBuilder): MazeBuilder = b + MazeTile.Wall

    def *(using b: MazeBuilder): MazeBuilder = b + MazeTile.Passage

    def E(using b: MazeBuilder): MazeBuilder = b + MazeTile.Entry

    def X(using b: MazeBuilder): MazeBuilder = b + MazeTile.Exit

    extension (builder: MazeBuilder)
      def |(b: MazeBuilder): MazeBuilder = b


