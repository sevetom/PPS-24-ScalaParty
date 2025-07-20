package it.unibo.party.controller

import it.unibo.party.common.Player
import it.unibo.party.controller.managers.DiceChallengeManager
import it.unibo.party.model.partyGame.Dice
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class DiceChallengeManagerTest extends AnyFlatSpec:
  val challenge: DiceChallengeManager = DiceChallengeManager(Dice())

  "A DiceChallengeManager" should "allow rolling the dice for a player" in:
    val player = Player(0)
    val updatedChallenge = challenge.roll(player)
    updatedChallenge.diceResults.contains(player) shouldBe true

  it should "recognise if there is a tie" in:
    val tieChallenge = DiceChallengeManager(Dice(), Map(
      Player(0) -> 3,
      Player(1) -> 3
    ))
    tieChallenge.tie shouldBe true
