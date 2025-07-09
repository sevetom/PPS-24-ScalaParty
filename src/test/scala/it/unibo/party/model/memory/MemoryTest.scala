package it.unibo.party.model.memory

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class MemoryTest extends AnyFlatSpec:
  "The Memory" should "be empty when created" in:
    val memory = Memory(Map.empty)
    memory.isEmpty shouldBe true
