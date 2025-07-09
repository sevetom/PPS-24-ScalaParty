package it.unibo.party.model.memory

import it.unibo.party.geometry.Point2D
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class MemoryTest extends AnyFlatSpec:
  val emptyMemory: Memory = Memory(Map.empty)
  val genericFigure: Figure = Figure.Circle
  val genericCouplePoints: (Point2D[Int], Point2D[Int]) = (Point2D(0, 0), Point2D(1, 1))

  "The Memory" should "be empty when created" in:
    val memory = emptyMemory
    memory.isEmpty shouldBe true

  it should "allow adding figures" in:
    val memory = Memory(Map(genericFigure -> genericCouplePoints))
    memory.isEmpty shouldBe false

  it should "contain the added figure" in:
    val memory = Memory(Map(genericFigure -> genericCouplePoints))
    memory.layout should contain key genericFigure

