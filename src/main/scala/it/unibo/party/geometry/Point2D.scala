package it.unibo.party.geometry

trait Point2D:
  def x: Int
  def y: Int

  def +(other: Point2D): Point2D

  def -(other: Point2D): Point2D

  def >=(other: Point2D): Boolean

  def <=(other: Point2D): Boolean

  def <(other: Point2D): Boolean

  def >(other: Point2D): Boolean
  
  def distanceFrom(other: Point2D): Double

object Point2D:
  def apply(x: Int, y: Int): Point2D = Point2DImpl(x, y)

  private case class Point2DImpl(override val x: Int, override val y: Int) extends Point2D:
    override def +(other: Point2D): Point2D = Point2D(x + other.x, y + other.y)

    override def -(other: Point2D): Point2D = Point2D(x - other.x, y - other.y)

    override def >=(other: Point2D): Boolean = x >= other.x && y >= other.y

    override def <=(other: Point2D): Boolean = x <= other.x && y <= other.y

    override def <(other: Point2D): Boolean = !(this >= other)

    override def >(other: Point2D): Boolean = !(this <= other)

    override def distanceFrom(other: Point2D): Double =
      Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2))

  val zero: Point2D = apply(0, 0)
