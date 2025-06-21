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

    "A BoardBox" should "not have items if empty" in:
    emptyBox.isEmpty should be (true)

  it should "have items if not empty" in:
    fullBox.isEmpty should be (false)

  it should "return the owned item when trying to acquire something if it's empty" in:
    emptyBox.tryAcquireItem(ownedItems) should be (ownedItems, EmptyBox)

  it should "empty itself and return the updated list of items when trying to acquire something if it's full" in:
    fullBox.tryAcquireItem(ownedItems) should be (ownedItems :+ (item), EmptyBox)

    





