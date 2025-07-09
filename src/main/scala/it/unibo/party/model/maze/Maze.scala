package it.unibo.party.model.maze

import alice.tuprolog.{Struct, Term, Var}
import it.unibo.party.model.maze.MazePosition.MazePosition
import it.unibo.party.{RichFile, extractTerm, mkPrologEngine, openTheoryFile}

trait Maze:
  def layout: Map[MazePosition, MazeTile]

  def solution: Option[List[MazePosition]]

  def +(tile: (MazePosition, MazeTile)): Maze

  def get(pos: MazePosition): Option[MazeTile] = layout.get(pos)

  def contains(pos: MazePosition): Boolean = layout.contains(pos)

  def isEmpty: Boolean = layout.isEmpty

  def size: Int = layout.size

object Maze:
  def apply(path: Map[MazePosition, MazeTile]): Maze = MazeImpl(path)

  def apply(width: Int, height: Int)(architecture: MazeBuilder ?=> MazeBuilder): Maze =
    MazeBuilder.construct(width, height)(architecture).build()

  val empty: Maze = Maze(Map.empty)

  private case class MazeImpl(layout: Map[MazePosition, MazeTile]) extends Maze:
    def +(tile: (MazePosition, MazeTile)): Maze =
      copy(layout = layout.updated(tile._1, tile._2))

    lazy val solution: Option[List[MazePosition]] = computeSolution()

    private def computeSolution(): Option[List[MazePosition]] =
      val prologFacts = layout.map((p, t) =>
        t match
          case MazeTile.Passage => s"walkable(${p.x}, ${p.y})."
          case MazeTile.Entry => s"entry(${p.x}, ${p.y})." + s" walkable(${p.x}, ${p.y})."
          case MazeTile.Exit => s"exit(${p.x}, ${p.y})." + s" walkable(${p.x}, ${p.y})."
          case _ => ""
      ).mkString("\n")
      var prologRules = ""
      openTheoryFile("src/main/resources/prolog/mazeSolutionRules.pl").read().foreach(line =>
        if !line.startsWith("%") || line.trim.nonEmpty then prologRules = prologRules.concat("\n" + line))
      val prologTheory = prologFacts + prologRules
      val engine: Term => LazyList[Term] = mkPrologEngine(prologTheory)
      val input = Struct("solve_maze", Var("Path"))
      val results = engine(input)
      val strOutput = results.map(extractTerm(_, 0)).headOption.get.toString
      val pattern = """\((\d+),(\d+)\)""".r
      val result: List[MazePosition] = pattern.findAllMatchIn(strOutput).map(m =>
        MazePosition(m.group(1).toInt, m.group(2).toInt)
      ).toList
      if result.isEmpty then Option.empty
      else Some(result)
      