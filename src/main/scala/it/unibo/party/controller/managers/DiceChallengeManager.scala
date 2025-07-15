package it.unibo.party.controller.managers

import it.unibo.party.common.Player
import it.unibo.party.model.partyGame.Dice

/**
 * Manages the results of the dice challenge in the party game.
 */
trait DiceChallengeManager:
  /**
   * @return a map containing the results of the dice challenge for each player.
   */
  def diceResults: Map[Player, Int]

  /**
   * Rolls the dice for the specified player and updates the results.
   *
   * @param player the player who is rolling the dice.
   * @return a new instance of DiceChallengeManager with updated results.
   */
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
