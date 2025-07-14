package it.unibo.party.model.puzzle

import it.unibo.party.geometry.Point2D

object PuzzlePosition:

  opaque type PuzzlePosition = Point2D[Int]

  def apply(x: Int, y: Int): PuzzlePosition = Point2D(x, y)

  extension (bp: PuzzlePosition)
    def x: Int = bp.x
    def y: Int = bp.y
    def toPoint2D: Point2D[Int] = bp

  
