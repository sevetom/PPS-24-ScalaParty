package it.unibo.party.model.player

import it.unibo.party.model.items.Collectable
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class PocketTest extends AnyFlatSpec:
  val pocket: Pocket = Pocket.empty
  val genericItem: Collectable = Collectable.Rung(1)
  val differentItemWithSameType: Collectable = Collectable.Rung(2)

  "A Pocket" should "be empty when it has no items" in:
    pocket.isEmpty shouldBe true

  it should "have the correct size" in:
    pocket.size shouldEqual 0

  it should "increase size when adding items" in :
    val updatedPocket = pocket.add(genericItem)
    updatedPocket.size shouldEqual 1

  it should "decrease size when removing items" in:
    val updatedPocket = pocket.add(genericItem).remove(genericItem)
    updatedPocket.size shouldEqual 0

  it should "allow retrieving all items" in:
    val updatedPocket = pocket.add(genericItem)
    updatedPocket.getAll shouldBe Seq(genericItem)

  it should "contain an item after it is added" in :
    val updatedPocket = pocket.add(genericItem)
    updatedPocket.contains(genericItem) shouldBe true

  it should "allow retrieving items by type" in :
    val updatedPocket = pocket.add(genericItem).add(differentItemWithSameType)
    val expectedItems = updatedPocket.getAll.filter(_.getClass == genericItem.getClass)
    updatedPocket.getByType(genericItem) should contain theSameElementsAs expectedItems

  it should "allow counting items by type" in:
    val updatedPocket = pocket.add(genericItem).add(differentItemWithSameType)
    updatedPocket.countByType(genericItem) shouldEqual updatedPocket.getByType(genericItem).size
