package it.unibo.party.controller.managers

import it.unibo.party.common.Player
import it.unibo.party.model.partyGame.Dice

trait DiceChallengeManager:
  def diceResults: Map[Player, Int]

  def newRoll(player: Player): DiceChallengeManager

object DiceChallengeManager:
  def apply(
             dice: Dice = Dice(),
             diceResults: Map[Player, Int] = Map.empty
           ): DiceChallengeManager =
    DiceChallengeManagerImpl(dice, diceResults)

  private case class DiceChallengeManagerImpl(dice: Dice, diceResults: Map[Player, Int]) extends DiceChallengeManager:

    override def newRoll(player: Player): DiceChallengeManager =
      val diceResult = dice.roll()._2.head
      this.copy(diceResults = diceResults + (player -> diceResult))
