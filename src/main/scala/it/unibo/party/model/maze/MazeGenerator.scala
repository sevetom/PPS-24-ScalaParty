package it.unibo.party.model.maze

import alice.tuprolog.{Struct, Term, Var, Int as PInt}
import it.unibo.party.model.maze.MazePosition.MazePosition
import it.unibo.party.{RichFile, extractTerm, mkPrologEngine, openTheoryFile}

import scala.util.Random
import scala.util.matching.Regex

object MazeGenerator:
  private def scalaListToProlog(seeds: Seq[Int]): Term =
    seeds.foldRight[Term](Struct("[]"))((elem, acc) =>
      Struct(".", PInt(elem), acc)
    )

  private def loadProlog(input: Struct): LazyList[Term] =
    var prologRules = ""
    openTheoryFile("src/main/resources/prolog/mazeGenerationRules.pl").read().foreach(line =>
      if !line.startsWith("%") || line.trim.nonEmpty then prologRules = prologRules.concat("\n" + line))
    val prologTheory = prologRules
    val engine: Term => LazyList[Term] = mkPrologEngine(prologTheory)
    engine(input)

  class NorthEastBinaryTree(width: Int, height: Int):
    require(width > 2 && height > 2, "Width and height must be greater than 2")
    require(width % 2 == 1 && height % 2 == 1, "Width and height must be odd numbers")
    private val pathWidth: Int = (width - 1) / 2
    private val pathHeight: Int = (height - 1) / 2

    def generate: Maze =
      val seedsLength = pathWidth * pathHeight * 2
      val seeds: Seq[Int] = for i <- 0 until seedsLength yield Random.nextInt(2)
      val input = Struct("generate_ne_maze", PInt(pathWidth), PInt(pathHeight), scalaListToProlog(seeds), Var("Walls"))
      val results = loadProlog(input)
      convertToMaze(results.map(extractTerm(_, 3)).headOption.get.toString)

    private def to_full_maze_cord(x: Int, y: Int): MazePosition =
      MazePosition(2 * x + 1, 2 * y + 1)

    private def convertToMaze(result: String): Maze =
      val pattern: Regex = """','\((\d+),','\((\d+),(\w+)\)\)""".r
      var maze: Map[MazePosition, MazeTile] =
        (for x <- 0 until width; y <- 0 until height yield
          MazePosition(x, y) -> MazeTile.Wall).toMap
      val findings = pattern.findAllMatchIn(result)    
      pattern.findAllMatchIn(result).foreach(m =>
        val pos = to_full_maze_cord(m.group(1).toInt, m.group(2).toInt)
        maze = maze.updated(pos, MazeTile.Passage)
        maze = maze.updated(m.group(3) match
          case "north" => MazePosition(pos.x, pos.y - 1)
          case "east" => MazePosition(pos.x + 1, pos.y)
          case _ => pos
          , MazeTile.Passage)
      )
      val entryPosXs = maze.filter((p, t) => p.y == 1 && t == MazeTile.Passage).keys.map(_.x).toSeq
      val exitPosXs = maze.filter((p, t) => p.y == height - 2 && t == MazeTile.Passage).keys.map(_.x).toSeq
      val entryX = if entryPosXs.nonEmpty then entryPosXs(Random.nextInt(entryPosXs.size))
      else width / 2
      val exitX = if exitPosXs.nonEmpty then exitPosXs(Random.nextInt(exitPosXs.size))
      else width / 2
      maze = maze.updated(MazePosition(entryX, 0), MazeTile.Entry)
      maze = maze.updated(MazePosition(exitX, height - 1), MazeTile.Exit)
      MazeBuilder.constructFromList(
        maze.toList.sortBy:
          case (pos, _) => (pos.x, pos.y)
      ).build()


