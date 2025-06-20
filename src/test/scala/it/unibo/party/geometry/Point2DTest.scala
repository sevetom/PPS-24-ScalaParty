package it.unibo.party.geometry

import it.unibo.party.geometry.Point2D
import org.scalatest.flatspec.AnyFlatSpec

class Point2DTest extends AnyFlatSpec:
  val p0: Point2D = Point2D.zero

  "A Point2D" should "be equal to itself" in {
    assert(p0 == p0)
  }

  it should "be equal to another Point2D with the same coordinates" in {
    val p1: Point2D = Point2D(0, 0)
    assert(p0 == p1)
  }

  it should "not be equal to another Point2D with different coordinates" in {
    val p1: Point2D = Point2D(1, 0)
    assert(p0 != p1)
  }

  it should "know if it is greater or lower than another Point2D" in {
    val p1: Point2D = Point2D(1, 0)
    assert(p0 < p1)
    assert(p1 > p0)
    assert(p0 <= p1)
    assert(p1 >= p0)
  }

  it should "know the distance from another Point2D" in {
    val p1: Point2D = Point2D(3, 4)
    assert((p1 distanceFrom p0) == 5)
  }
