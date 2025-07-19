package it.unibo.party.model.maze

import it.unibo.party.model.maze.MazePosition.MazePosition

/**
 * Builder for constructing a maze with a specified width and height.
 * Inspired by https://github.com/jahrim/PPS-22-chess/blob/master/chess/src/main/scala/io/github/chess/engine/model/board/ChessBoardBuilder.scala
 *
 * @param width  the width of the maze
 * @param height the height of the maze
 */
class MazeBuilder(val width: Int, val height: Int):
  private var maze: Maze = Maze.empty
  private var currentIndex: Int = 0

  private def indexToPosition(idx: Int): MazePosition =
    val x = idx % width
    val y = idx / width
    MazePosition(x, y)

  /**
   * Adds a tile to the maze at the current index position.
   *
   * @param tile the tile to be added to the maze
   * @return the updated MazeBuilder instance
   */
  def +(tile: MazeTile): MazeBuilder =
    val pos = indexToPosition(currentIndex)
    maze = maze + (pos, tile)
    currentIndex += 1
    this

  /**
   * Builds the maze, ensuring it meets the required conditions:
   *  - The maze must have the correct number of tiles.
   *  - There must be exactly one entry and one exit.
   *  - The entry and exit must be on the maze borders.
   *  - The maze must be solvable.
   *
   * @return the constructed Maze instance
   * @throws IllegalArgumentException if any of the conditions are not met
   */
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

  /**
   * Constructs a MazeBuilder from a list of (MazePosition, MazeTile) pairs.
   *
   * @param layout the list of (MazePosition, MazeTile) ordered pairs representing the maze layout
   * @return a MazeBuilder instance initialized with the specified layout
   */
  def constructFromList(layout: List[(MazePosition, MazeTile)]): MazeBuilder =
    val width = layout.map(_._1.x).max + 1
    val height = layout.map(_._1.y).max + 1
    val builder = new MazeBuilder(width, height)
    layout.foreach:
      case (pos, tile) => builder + tile
    builder

  /**
   * Constructs a MazeBuilder with the specified width and height,
   *
   * @param width     the width of the maze
   * @param height    the height of the maze
   * @param structure the structure of the maze to be built, provided as a function that uses the MazeBuilder context
   * @return a MazeBuilder instance initialized with the specified structure
   */
  def construct(width: Int, height: Int)(structure: MazeBuilder ?=> MazeBuilder): MazeBuilder =
    given MazeBuilder(width, height)
    structure

  /**
   * Provides a DSL for building mazes using a more readable syntax.
   */
  object DSL:
    /**
     * Adds a wall tile to the maze using the provided MazeBuilder context.
     *
     * @param b the implicit MazeBuilder context
     * @return the updated MazeBuilder instance with the wall tile added
     */
    def W(using b: MazeBuilder): MazeBuilder = b + MazeTile.Wall

    /**
     * Adds a passage tile to the maze using the provided MazeBuilder context.
     *
     * @param b the implicit MazeBuilder context
     * @return the updated MazeBuilder instance with the passage tile added
     */
    def *(using b: MazeBuilder): MazeBuilder = b + MazeTile.Passage

    /**
     * Adds an entry tile to the maze using the provided MazeBuilder context.
     *
     * @param b the implicit MazeBuilder context
     * @return the updated MazeBuilder instance with the entry tile added
     */
    def E(using b: MazeBuilder): MazeBuilder = b + MazeTile.Entry

    /**
     * Adds an exit tile to the maze using the provided MazeBuilder context.
     *
     * @param b the implicit MazeBuilder context
     * @return the updated MazeBuilder instance with the exit tile added
     */
    def X(using b: MazeBuilder): MazeBuilder = b + MazeTile.Exit

    extension (builder: MazeBuilder)
      /**
       * Combines the current MazeBuilder with another one using the `|` operator.
       * This allows for chaining multiple builders together.
       *
       * @param b the MazeBuilder to combine with
       * @return a new MazeBuilder that combines both
       */
      def |(b: MazeBuilder): MazeBuilder = b


