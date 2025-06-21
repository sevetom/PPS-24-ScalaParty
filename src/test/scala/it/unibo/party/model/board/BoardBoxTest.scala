package it.unibo.party.model.board

import BoardBox.BoardBox.*
import BoardBox.BoardBox
import it.unibo.party.model.items.Collectable
import it.unibo.party.model.items.Collectable.{Monad, Rung}
import it.unibo.party.model.player.Pocket
import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*

class BoardBoxTest extends AnyFlatSpec with should.Matchers:
  val emptyBox: BoardBox = EmptyBox
  val item: Collectable = Collectable.Monad()
  val fullBox: BoardBox = FullBox(item)
  val ownedItems = Seq(Monad(), Monad(), Rung(4))

    "A BoardBox" should "be empty if it doesn't have items" in:
    emptyBox.isEmpty should be (true)

  it should "not be empty if it has items" in:
    fullBox.isEmpty should be (false)

  it should "return the owned items when trying to acquire an item but it's empty" in:
    emptyBox.tryAcquireItem(ownedItems) should be (ownedItems, EmptyBox)

  it should "empty itself and return the updated sequence of items when acquiring an item" in:
    fullBox.tryAcquireItem(ownedItems) should be (ownedItems :+ (item), EmptyBox)

    





