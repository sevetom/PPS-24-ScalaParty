package it.unibo.party.model

import org.scalatest.*
import flatspec.*
import it.unibo.party.model.GameBoard.BoardBox
import it.unibo.party.model.GameBoard.BoardBox.*

import matchers.*

class GameBoardTest extends AnyFlatSpec with should.Matchers:

  "A BoardBox" should "not have items if empty" in:
    val box: BoardBox = EmptyBox
    box.isEmpty should be (true)



