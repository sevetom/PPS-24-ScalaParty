package it.unibo.party.model.maze

import alice.tuprolog.{Struct, Term, Var, Int as PInt}
import it.unibo.party.model.maze.MazePosition.MazePosition
import it.unibo.party.Scala2P.given_Conversion_Seq_Term
import it.unibo.party.{RichFile, executeProlog, extractTerm, mkPrologEngine, openTheoryFile}

import scala.util.Random
import scala.util.matching.Regex

/**
 * MazeGenerator is an object that provides functionality to generate mazes
 */
object MazeGenerator:

  /**
   * Generates a maze using the North-East Binary Tree algorithm.
   *
   * @param width  the width of the maze, must be an odd number greater than 2
   * @param height the height of the maze, must be an odd number greater than 2
   * @throws IllegalArgumentException if width or height are not valid
   */
  class NorthEastBinaryTree(width: Int, height: Int):
    require(width > 2 && height > 2, "Width and height must be greater than 2")
    require(width % 2 == 1 && height % 2 == 1, "Width and height must be odd numbers")
    private val pathWidth: Int = (width - 1) / 2
    private val pathHeight: Int = (height - 1) / 2

    extension (p: (Int, Int))
      private def toFullMazePosition: MazePosition = MazePosition(2 * p._1 + 1, 2 * p._2 + 1)

    /**
     * Generates a maze using the North-East Binary Tree algorithm.
     *
     * @return a Maze object representing the generated maze
     */
    def generate: Maze =
      val seedsLength = pathWidth * pathHeight * 2
      val seeds: Seq[Int] = for i <- 0 until seedsLength yield Random.nextInt(2)
      val input = Struct("generate_ne_maze", PInt(pathWidth), PInt(pathHeight), seeds, Var("Walls"))
      val results = executeProlog("/prolog/mazeGenerationRules.pl", "", input)
      convertToMaze(results.map(extractTerm(_, 3)).headOption.get.toString)

    private def convertToMaze(result: String): Maze =
      val pattern: Regex = """','\((\d+),','\((\d+),(\w+)\)\)""".r
      var maze: Map[MazePosition, MazeTile] =
        (for x <- 0 until width; y <- 0 until height yield
          MazePosition(x, y) -> MazeTile.Wall).toMap
      val findings = pattern.findAllMatchIn(result)
      pattern.findAllMatchIn(result).foreach(m =>
        val pos: MazePosition = (m.group(1).toInt, m.group(2).toInt).toFullMazePosition
        maze = maze.updated(pos, MazeTile.Passage)
        maze = maze.updated(m.group(3) match
          case "north" => MazePosition(pos.x, pos.y - 1)
          case "east" => MazePosition(pos.x + 1, pos.y)
          case _ => pos
          , MazeTile.Passage)
      )
      val (entryX, exitX) = findEntryAndExit(maze)
      maze = maze.updated(MazePosition(entryX, 0), MazeTile.Entry)
      maze = maze.updated(MazePosition(exitX, height - 1), MazeTile.Exit)
      MazeBuilder.constructFromList(
        maze.toList.sortBy:
          case (pos, _) => (pos.y, pos.x)
      ).build()

    private def findEntryAndExit(maze: Map[MazePosition, MazeTile]): (Int, Int) =
      val entryPosXs = maze.filter((p, t) => p.y == 1 && t == MazeTile.Passage).keys.map(_.x).toSeq
      val exitPosXs = maze.filter((p, t) => p.y == height - 2 && p.x <= width / 2 && t == MazeTile.Passage)
        .keys.map(_.x).toSeq
      val entryX = if entryPosXs.nonEmpty then entryPosXs(Random.nextInt(entryPosXs.size))
      else width / 2
      val exitX = if exitPosXs.nonEmpty then exitPosXs(Random.nextInt(exitPosXs.size))
      else width / 2
      (entryX, exitX)
