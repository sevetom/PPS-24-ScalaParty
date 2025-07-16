package it.unibo.party.geometry

/**
 * Generic Point2D trait that represents a point in 2D space.
 * It is parameterized by a numeric type N, allowing for
 * flexibility in the numeric representation of coordinates.
 *
 * @tparam N the numeric type for the coordinates, which must support basic arithmetic operations
 *           and comparisons.
 *           This is typically a type like Int, Float, Double, etc.
 */
trait Point2D[N](using num: Numeric[N]):
  /**
   * @return the x coordinate of the point
   */
  def x: N

  /**
   * @return the y coordinate of the point
   */
  def y: N

  /**
   * Adds another Point2D to this point.
   *
   * @param other the point to add
   * @return a new Point2D representing the sum of this point and the other point
   */
  def +(other: Point2D[N]): Point2D[N]

  /**
   * Subtracts another Point2D from this point.
   *
   * @param other the point to subtract
   * @return a new Point2D representing the difference between this point and the other point
   */
  def -(other: Point2D[N]): Point2D[N]

  /**
   * Checks if this point is the zero point.
   *
   * @return true if both x and y coordinates are zero, false otherwise
   */
  def isZero: Boolean

  /**
   * Calculates the distance from this point to another Point2D.
   *
   * @param other the point to which the distance is calculated
   * @return the Euclidean distance between this point and the other point
   */
  def distanceFrom(other: Point2D[N]): Double

object Point2D:
  /**
   * Creates a new instance of a Point2D.
   *
   * @param x the x coordinate of the point
   * @param y the y coordinate of the point
   * @tparam N the numeric type for the coordinates
   * @return a new Point2D instance with the specified coordinates
   */
  def apply[N](x: N, y: N)(using num: Numeric[N]): Point2D[N] = Point2DImpl(x, y)

  /**
   * Extractor for Point2D, allowing pattern matching on Point2D instances.
   *
   * @param p the Point2D instance to extract coordinates from
   * @tparam N the numeric type of the coordinates
   * @return a tuple containing the x and y coordinates of the point
   */
  def unapply[N](p: Point2D[N])(using num: Numeric[N]): (N, N) =
    (p.x, p.y)

  given [N](using num: Numeric[N]): Conversion[(N, N), Point2D[N]] with
    def apply(t: (N, N)): Point2D[N] =
      Point2D(t._1, t._2)

  given [N](using Numeric[N]): Conversion[Point2D[N], (N, N)] with
    def apply(p: Point2D[N]): (N, N) =
      (p.x, p.y)

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
