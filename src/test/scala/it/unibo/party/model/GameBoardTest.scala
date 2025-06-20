package it.unibo.party.model

import org.scalatest.*
import flatspec.*
import it.unibo.party.model.GameBoard.{BoardBox, Collectable}
import it.unibo.party.model.GameBoard.BoardBox.*
import matchers.*

class GameBoardTest extends AnyFlatSpec with should.Matchers:
  val emptyBox: BoardBox = EmptyBox
  val item: Collectable = 1
  val fullBox: BoardBox = FullBox(item)

  "A BoardBox" should "not have items if empty" in:
    emptyBox.isEmpty should be (true)

  it should "have items if not empty" in:
    fullBox.isEmpty should be (false)

  it should "return an empty optional if an item is requested but not present" in:
    emptyBox.tryAcquireItem should be (Option.empty)

  it should "return the an optional of the item if requested and present" in:
    fullBox.tryAcquireItem should be (Option(item))





