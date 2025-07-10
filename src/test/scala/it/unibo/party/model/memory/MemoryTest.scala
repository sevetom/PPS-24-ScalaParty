package it.unibo.party.model.memory

import it.unibo.party.geometry.Point2D
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class MemoryTest extends AnyFlatSpec:
  val emptyMemory: Memory = Memory(Map.empty)
  val genericFigure: Figure = Figure.Circle
  val genericCouplePoints: (MemoryBox, MemoryBox) = (
    MemoryBox(Point2D(1, 1)),
    MemoryBox(Point2D(1, 2))
  )

  "The Memory" should "be empty when created" in:
    val memory = emptyMemory
    memory.isEmpty shouldBe true

  it should "allow adding figures" in:
    val memory = Memory(Map(genericFigure -> genericCouplePoints))
    memory.isEmpty shouldBe false

  it should "contain the added figure" in:
    val memory = Memory(Map(genericFigure -> genericCouplePoints))
    memory.layout should contain key genericFigure

  it should "return true for two equal memories" in:
    val memory = Memory(Map(genericFigure -> genericCouplePoints))
    memory.check(genericCouplePoints)._1 shouldBe true

  it should "return false for two different memories" in:
    val memory = Memory(Map(genericFigure -> genericCouplePoints))
    memory.check(MemoryBox(Point2D(2, 2)), MemoryBox(Point2D(3, 3)))._1 shouldBe false

  it should "be able to generate a full memory table" in:
    val memory = emptyMemory
    memory.generate.layout.keys should contain allElementsOf Figure.values.toSeq

  it should "not be able to check a pair of points already truly checked" in:
    val memory = Memory(Map(genericFigure -> genericCouplePoints))
    val newMemory = memory.check(genericCouplePoints)
    an [IllegalStateException] should be thrownBy
      newMemory._2.check(genericCouplePoints)