package it.unibo.party.model.player

import it.unibo.party.model.items.Collectable
import it.unibo.party.model.player.Pocket
import org.scalatest.flatspec.AnyFlatSpec

class PocketTest extends AnyFlatSpec:
  val pocket: Pocket = Pocket.empty
  val genericItem: Collectable = Collectable.Rung(1)
  val differentItemWithSameType: Collectable = Collectable.Rung(2)

  "A Pocket" should "be empty when it has no items" in:
    assert(pocket.isEmpty)
    assert(pocket.size == 0)

  it should "allow adding items" in:
    val updatedPocket = pocket.add(genericItem)
    assert(!updatedPocket.isEmpty)
    assert(updatedPocket.size == 1)
    assert(updatedPocket.contains(genericItem))

  it should "allow removing items" in:
    val updatedPocket = pocket.add(genericItem).remove(genericItem)
    assert(updatedPocket.isEmpty)
    assert(updatedPocket.size == 0)

  it should "allow checking for item existence" in:
    val updatedPocket = pocket.add(genericItem)
    assert(updatedPocket.contains(genericItem))
    assert(!updatedPocket.contains(differentItemWithSameType))

  it should "allow counting items by type" in:
    val updatedPocket = pocket.add(genericItem).add(differentItemWithSameType)
    assert(updatedPocket.countByType(genericItem) == 2)
    assert(updatedPocket.countByType(differentItemWithSameType) == 2)

  it should "allow retrieving all items" in:
    val updatedPocket = pocket.add(genericItem)
    assert(updatedPocket.getAll.contains(genericItem))
    assert(updatedPocket.getAll.size == 1)

  it should "allow retrieving items by type" in:
    val updatedPocket = pocket.add(genericItem).add(differentItemWithSameType)
    assert(updatedPocket.getByType(genericItem).size == 2)
    assert(updatedPocket.getByType(differentItemWithSameType).size == 2)
