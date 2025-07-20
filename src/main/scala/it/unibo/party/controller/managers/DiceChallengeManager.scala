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
  def roll(player: Player): DiceChallengeManager

  /**
   * Checks if there is a tie in the dice challenge.
   *
   * @return true if there is a tie, false otherwise.
   */
  def tie: Boolean

object DiceChallengeManager:
  /**
   * Creates a new instance of DiceChallengeManager with the specified dice and initial results.
   *
   * @param dice        the dice to be used for rolling
   * @param diceResults a map containing the initial results of the dice challenge for each player
   * @return a new instance of DiceChallengeManager
   */
  def apply(
             dice: Dice = Dice(),
             diceResults: Map[Player, Int] = Map.empty
           ): DiceChallengeManager =
    DiceChallengeManagerImpl(dice, diceResults)

  private case class DiceChallengeManagerImpl(dice: Dice, diceResults: Map[Player, Int]) 
    extends DiceChallengeManager:

    override def roll(player: Player): DiceChallengeManager =
      val diceResult = dice.roll()._2.head
      this.copy(diceResults = diceResults + (player -> diceResult))

    override def tie: Boolean =
      diceResults.nonEmpty && diceResults.values.toSet.size < diceResults.keys.size
