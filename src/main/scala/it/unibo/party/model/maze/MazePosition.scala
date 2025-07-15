package it.unibo.party.model.maze

import it.unibo.party.geometry.{Direction, Point2D}

object MazePosition:
  /**
   * Represents a position in the maze using 2D coordinates.
   */
  opaque type MazePosition = Point2D[Int]

  def apply(x: Int, y: Int): MazePosition = Point2D(x, y)

  extension (bp: MazePosition)
    def x: Int = bp.x
    def y: Int = bp.y
    def toPoint2D: Point2D[Int] = bp
    def +(dir: Direction): MazePosition =
      val (dx, dy) = dir.offset
      MazePosition(bp.x + dx, bp.y + dy)
