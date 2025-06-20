package it.unibo.party.geometry

trait Point2D[N](using num: Numeric[N]):
  def x: N
  def y: N

  def +(other: Point2D[N]): Point2D[N]

  def -(other: Point2D[N]): Point2D[N]

  def isZero: Boolean

  def distanceFrom(other: Point2D[N]): Double

object Point2D:
  def apply[N](x: N, y: N)(using num: Numeric[N]): Point2D[N] = Point2DImpl(x, y)

  given [N](using num: Numeric[N]): Conversion[(N, N), Point2D[N]] with
    def apply(t: (N, N)): Point2D[N] =
      Point2D(t._1, t._2)

  private case class Point2DImpl[N](x: N, y: N)(using num: Numeric[N]) extends Point2D[N]:
    import num.*

    override def +(other: Point2D[N]): Point2D[N] = Point2D(plus(x, other.x), plus(y, other.y))

    override def -(other: Point2D[N]): Point2D[N] = Point2D(minus(x, other.x), minus(y, other.y))

    override def isZero: Boolean = num.equiv(x, num.zero) && num.equiv(y, num.zero)

    override def distanceFrom(other: Point2D[N]): Double =
      val dx = toDouble(minus(x, other.x))
      val dy = toDouble(minus(y, other.y))
      math.sqrt(dx * dx + dy * dy)

  val zero: Point2D[Double] = apply(0.0, 0.0)
