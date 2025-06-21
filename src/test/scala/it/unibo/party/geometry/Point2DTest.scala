package it.unibo.party.geometry

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class Point2DTest extends AnyFlatSpec:
  val p0: Point2D[Double] = Point2D.zero

  "A Point2D" should "be equal to itself" in:
    p0 shouldEqual p0

  it should "know if it is zero" in :
    p0.isZero shouldBe true

  it should "be equal to another Point2D with the same coordinates" in:
    val p1: Point2D[Double] = Point2D.zero
    p0 shouldEqual p1

  it should "not be equal to another Point2D with different coordinates" in:
    val p1: Point2D[Double] = Point2D(1.0, 0.0)
    p0 should not equal p1

  it should "add another Point2D correctly" in:
    val p1: Point2D[Double] = Point2D(1.0, 2.0)
    val result: Point2D[Double] = p0 + p1
    result shouldEqual Point2D(1.0, 2.0)

  it should "subtract another Point2D correctly" in:
    val p1: Point2D[Double] = Point2D(1.0, 2.0)
    val result: Point2D[Double] = p1 - p0
    result shouldEqual Point2D(1.0, 2.0)

  it should "know the distance from another Point2D" in:
    val p1: Point2D[Double] = Point2D(3, 4)
    p1 distanceFrom p0 shouldEqual 5

  "A Pair of numbers" should "be convertible to Point2D" in:
    val p: Point2D[Int] = (1, 2)
    (p.x, p.y) shouldEqual (1, 2)
