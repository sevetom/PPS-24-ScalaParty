package it.unibo.party.model.board

import GameBoard.BoardBox.*
import GameBoard.BoardBox
import it.unibo.party.model.items.Collectable
import it.unibo.party.model.player.Pocket
import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*

class GameBoardTest extends AnyFlatSpec with should.Matchers:
  val emptyBox: BoardBox = EmptyBox
  val item: Collectable = Collectable.Monad()
  val fullBox: BoardBox = FullBox(item)

  "A BoardBox" should "not have items if empty" in:
    emptyBox.isEmpty should be (true)

  it should "have items if not empty" in:
    fullBox.isEmpty should be (false)

  it should "return an empty optional if an item is requested but not present" in:
    emptyBox.tryAcquireItem(Pocket.empty) should be (Option.empty)

  it should "return the an optional of the monad if requested and present" in:
    fullBox.tryAcquireItem(Pocket.empty) should be (Option(item))





