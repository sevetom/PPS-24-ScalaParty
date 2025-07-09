package it.unibo.party.model.memory

import it.unibo.party.geometry.Point2D
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class MemoryTest extends AnyFlatSpec:
  "The Memory" should "be empty when created" in:
    val memory = Memory(Map.empty)
    memory.isEmpty shouldBe true

  it should "allow adding figures" in:
    val point1 = Point2D(1, 2)
    val point2 = Point2D(3, 4)
    val memory = Memory(Map(Figure.Circle -> (point1, point2)))
    memory.isEmpty shouldBe false
