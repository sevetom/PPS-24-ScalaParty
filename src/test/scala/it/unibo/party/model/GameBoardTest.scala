package it.unibo.party.model

import org.scalatest.*
import flatspec.*
import it.unibo.party.model.GameBoard.BoardBox
import it.unibo.party.model.GameBoard.BoardBox.*
import matchers.*

class GameBoardTest extends AnyFlatSpec with should.Matchers:
  val emptyBox: BoardBox = EmptyBox

  "A BoardBox" should "not have items if empty" in:
    emptyBox.isEmpty should be (true)

  it should "return an empty optional if an item is requested but not present" in:
    emptyBox.tryAcquireItem should be (Option.empty)



