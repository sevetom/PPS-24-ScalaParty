package it.unibo.party.model.player

import it.unibo.party.model.items.{Collectable, CollectableType}
import it.unibo.party.model.items.CollectableOperations.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class PocketTest extends AnyFlatSpec:
  val emptyPocket: Pocket = Pocket.empty
  val genericItem: Collectable = Collectable.Rung(1)
  val differentItemWithSameType: Collectable = Collectable.Rung(2)

  "A Pocket" should "be empty when it has no items" in :
    emptyPocket.isEmpty shouldBe true

  it should "have the correct size" in :
    emptyPocket.size shouldEqual 0

  it should "increase size when adding items" in :
    val updatedPocket = emptyPocket.add(genericItem)
    updatedPocket.size shouldEqual 1

  it should "decrease size when removing items" in :
    val updatedPocket = emptyPocket.add(genericItem).remove(genericItem)
    updatedPocket.size shouldEqual 0

  it should "allow retrieving all items" in :
    val updatedPocket = emptyPocket.add(genericItem)
    updatedPocket.getAll shouldBe Seq(genericItem)

  it should "contain an item after it is added" in :
    val updatedPocket = emptyPocket.add(genericItem)
    updatedPocket.contains(genericItem) shouldBe true

  it should "allow retrieving items by type" in :
    val updatedPocket = emptyPocket.add(genericItem).add(differentItemWithSameType)
    val expectedItems = updatedPocket.getAll.filter(_.getType == genericItem.getType)
    updatedPocket.getByType(genericItem.getType) should contain theSameElementsAs expectedItems

  it should "allow counting items by type" in :
    val updatedPocket = emptyPocket.add(genericItem).add(differentItemWithSameType)
    updatedPocket.countByType(genericItem.getType) shouldEqual updatedPocket.getByType(genericItem.getType).size

  it should "allow adding multiple items" in :
    val addCount = 3
    val updatedPocket = emptyPocket.addMultiple(genericItem, addCount)
    updatedPocket.size shouldEqual addCount

  it should "allow removing multiple items by type" in :
    val removeCount = 2
    val totalCount = 5
    val updatedPocket = emptyPocket.addMultiple(genericItem, totalCount)
        .removeMultipleByType(genericItem.getType, removeCount)
    updatedPocket.countByType(genericItem.getType) shouldEqual totalCount - removeCount
    
  
