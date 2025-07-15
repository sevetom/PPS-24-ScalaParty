package it.unibo.party.model.maze

import alice.tuprolog.{Struct, Term, Var}
import it.unibo.party.model.maze.MazeBuilder.DSL.*
import it.unibo.party.model.maze.MazePosition.MazePosition
import it.unibo.party.{RichFile, executeProlog, extractTerm, mkPrologEngine, openTheoryFile}

/**
 * Represents a maze with a layout defined by a map of positions to tiles.
 */
trait Maze:
  /**
   * @return the layout of the maze as a map where keys are positions and values are tiles.
   */
  def layout: Map[MazePosition, MazeTile]

  /**
   * Computes the solution of the maze, if it exists.
   *
   * @return an optional list of positions representing the solution path.
   */
  def solution: Option[List[MazePosition]]

  /**
   * Adds a tile to the maze at the specified position.
   *
   * @param tile a tuple containing the position and the tile to be added.
   * @return a new Maze instance with the updated layout.
   */
  def +(tile: (MazePosition, MazeTile)): Maze

  /**
   * Retrieves the tile at the specified position.
   *
   * @param pos the position in the maze.
   * @return an optional tile at the specified position, if it exists.
   */
  def get(pos: MazePosition): Option[MazeTile] = layout.get(pos)

  /**
   * Checks if the maze contains a tile at the specified position.
   *
   * @param pos the position to check.
   * @return true if the maze contains a tile at the specified position, false otherwise.
   */
  def contains(pos: MazePosition): Boolean = layout.contains(pos)

  /**
   * Checks if the maze is empty.
   *
   * @return true if the maze has no tiles, false otherwise.
   */
  def isEmpty: Boolean = layout.isEmpty

  /**
   * @return the number of tiles in the maze.
   */
  def size: Int = layout.size

object Maze:
  def apply(path: Map[MazePosition, MazeTile]): Maze = MazeImpl(path)

  def apply(width: Int, height: Int)(architecture: MazeBuilder ?=> MazeBuilder): Maze =
    MazeBuilder.construct(width, height)(architecture).build()

  val empty: Maze = Maze(Map.empty)

  val standard: Maze = Maze(10, 11):
    W | W | W | W | E | W | W | W | W | W
    W | * | * | * | * | * | * | * | * | W
    W | * | W | W | W | W | W | W | * | W
    W | * | W | * | * | * | * | W | * | W
    W | * | W | W | * | W | * | W | * | W
    W | * | * | W | * | W | * | * | * | W
    W | W | * | W | * | W | * | W | W | W
    W | * | * | W | * | W | * | W | * | W
    W | * | W | W | W | W | * | W | * | W
    W | * | * | * | * | W | * | * | * | W
    W | W | W | W | X | W | W | W | W | W

  private case class MazeImpl(layout: Map[MazePosition, MazeTile]) extends Maze:
    def +(tile: (MazePosition, MazeTile)): Maze =
      copy(layout = layout.updated(tile._1, tile._2))

    lazy val solution: Option[List[MazePosition]] = computeSolution()

    private def computeSolution(): Option[List[MazePosition]] =
      val prologFacts = layout.map((p, t) =>
        t match
          case MazeTile.Passage => s"passage(${p.x}, ${p.y})."
          case MazeTile.Entry => s"entry(${p.x}, ${p.y})."
          case MazeTile.Exit => s"exit(${p.x}, ${p.y})."
          case _ => ""
      ).mkString("\n")
      val input = Struct("solve_maze", Var("Path"))
      val results = executeProlog("src/main/resources/prolog/mazeSolutionRules.pl", prologFacts, input)
      val strOutput = results.map(extractTerm(_, 0)).headOption
      if strOutput.isEmpty then
        Option.empty
      else
        val pattern = """\((\d+),(\d+)\)""".r
        val result: List[MazePosition] = pattern.findAllMatchIn(strOutput.get.toString).map(m =>
          MazePosition(m.group(1).toInt, m.group(2).toInt)
        ).toList
        Some(result)
      